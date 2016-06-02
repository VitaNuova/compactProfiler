/**
 *  Copyright 2016 Viktoriia Bakalova
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and limitations under the License.
 */

package cz.muni.fi.sbapr.analyzer;

import com.sun.tools.jdeps.Main;
import cz.muni.fi.sbapr.analyzer.result.AnalysisResult;
import cz.muni.fi.sbapr.analyzer.result.PackageProfilePair;
import cz.muni.fi.sbapr.analyzer.util.IOUtils;
import cz.muni.fi.sbapr.analyzer.validator.BasicValidator;
import cz.muni.fi.sbapr.analyzer.validator.JarValidator;
import cz.muni.fi.sbapr.enums.CompactProfile;
import cz.muni.fi.sbapr.enums.ResolutionType;
import cz.muni.fi.sbapr.exceptions.AnalyzerException;
import cz.muni.fi.sbapr.exceptions.ResolverException;
import cz.muni.fi.sbapr.resolver.ClasspathCompactProfileResolver;
import cz.muni.fi.sbapr.resolver.MavenCompactProfileResolver;

import java.io.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum CompactProfileAnalyzerImpl implements CompactProfileAnalyzer {

    ANALYZER;

    private JarValidator validator = BasicValidator.VALIDATOR;

    private static class IntermediateResult {

        boolean isFinalResult;
        AnalysisResult result;

        IntermediateResult(boolean isFinalResult, AnalysisResult result) {
            this.isFinalResult = isFinalResult;
            this.result = result;
        }

        public AnalysisResult getResult() {
            return result;
        }

        public boolean isFinalResult() {
            return isFinalResult;
        }
    }

    /**
     * Implementation of CompactProfileAnalyzer interface
     * @param inputJar jar archive to analyze
     * @param resolutionType desired type of resolution (defaults to classpath resolution), recognizes classpath and maven resolution
     * @param desiredProfile desired compact profile (defaults to compact1)
     * @return result of analysis (minimal compact profile required to run, lists of packages within and outside the desired profile)
     * @throws AnalyzerException in case invalid jar is provided
     */
    @Override
    public AnalysisResult analyze(File inputJar, ResolutionType resolutionType, CompactProfile desiredProfile) throws AnalyzerException {

        boolean isValid = validator.validate(inputJar);
        if(!isValid) {
            throw new AnalyzerException("The submitted path is not a valid jar archive path.");
        }
        if(resolutionType == null) {
            resolutionType = ResolutionType.CLASSPATH;
        }
        if(desiredProfile == null) {
            desiredProfile = CompactProfile.COMPACT1;
        }

        IntermediateResult result;
        try {
            File resultFile = runJdeps(inputJar);
            result = parseJdepsResults(resultFile, desiredProfile);
        } catch(IOException ex) {
            throw new AnalyzerException("I/O exception has occurred when running jdeps on the input jar.", ex);
        }

        if (result.isFinalResult()) {
            return result.getResult();
        }

        List<File> dependencies = null;
        try {
            switch (resolutionType) {
                case MAVEN:
                    dependencies = MavenCompactProfileResolver.RESOLVER.resolve(inputJar);
                    break;
                case CLASSPATH:
                    dependencies = ClasspathCompactProfileResolver.RESOLVER.resolve(inputJar);
                    break;
            }
        } catch(ResolverException ex) {
            throw new AnalyzerException("Exception has occurred while resolving transitive dependencies.", ex);
        }

        CompactProfile minimalProfile = result.getResult().getMinimalProfile();
        Set<PackageProfilePair> inProfile = new HashSet<>(result.getResult().getInDesiredProfile());
        Set<PackageProfilePair> outProfile = new HashSet<>(result.getResult().getOutsideDesiredProfile());
        try {
            for (File dependency : dependencies) {
                File resultFile = runJdeps(dependency);
                IntermediateResult intermediateResult = parseJdepsResults(resultFile, desiredProfile);
                if(intermediateResult.getResult().getMinimalProfile().compareTo(minimalProfile) > 0) {
                    minimalProfile = intermediateResult.getResult().getMinimalProfile();
                }
                inProfile.addAll(intermediateResult.getResult().getInDesiredProfile());
                outProfile.addAll(intermediateResult.getResult().getOutsideDesiredProfile());
            }
        } catch(IOException ex) {
            throw new AnalyzerException("I/O error has occurred when running jdeps on a transitively dependent jar.");
        }

        return new AnalysisResult(minimalProfile, inProfile, outProfile);
    }

    public void setValidator(JarValidator validator) {
        if(validator == null) {
            throw new IllegalArgumentException("Jar validator cannot be null.");
        }
        this.validator = validator;
    }

    public void resetDefaultValidator() {
        validator = BasicValidator.VALIDATOR;
    }


    private File runJdeps(File inputJar) throws IOException {

        String jarFileName = inputJar.getName();
        File outputFile = File.createTempFile(jarFileName, ".jdeps");
        outputFile.deleteOnExit();

        String[] arguments = {"-P", inputJar.getAbsolutePath()};
        PrintWriter output = new PrintWriter(new BufferedWriter(new FileWriter(outputFile)));
        Main.run(arguments, output);

        return outputFile;
    }

    private IntermediateResult parseJdepsResults(File input, CompactProfile desiredProfile) throws IOException {

        String[] jdepsOutput = IOUtils.readFileIntoString(input).split(System.lineSeparator());

        CompactProfile minimalProfile = CompactProfile.COMPACT1;
        Set<PackageProfilePair> inProfilePairs = new HashSet<>();
        Set<PackageProfilePair> outsideProfilePairs = new HashSet<>();
        PackageProfilePair matchResult;
        boolean isFinalResult = true;

        for(String line : jdepsOutput) {
            matchResult = parseJdepsLine(line);
            if(matchResult != null) {
                CompactProfile profile = matchResult.getProfileName();
                if(profile.compareTo(desiredProfile) > 0 && profile != CompactProfile.NOT_FOUND) {
                    outsideProfilePairs.add(matchResult);
                }
                else if(profile != CompactProfile.NOT_FOUND) {
                    inProfilePairs.add(matchResult);
                }
                switch (profile) {
                    case FULL_JRE:
                        minimalProfile = CompactProfile.FULL_JRE;
                        break;
                    case COMPACT3:
                        if (minimalProfile.compareTo(CompactProfile.COMPACT3) < 0) {
                            minimalProfile = CompactProfile.COMPACT3;
                        }
                        break;
                    case COMPACT2:
                        if (minimalProfile.compareTo(CompactProfile.COMPACT2) < 0) {
                            minimalProfile = CompactProfile.COMPACT2;
                        }
                        break;
                    case COMPACT1: break;
                    default:
                        isFinalResult = false;
                }
            }
        }
        return new IntermediateResult(isFinalResult, new AnalysisResult(minimalProfile, inProfilePairs, outsideProfilePairs));
    }

    private PackageProfilePair parseJdepsLine(String line) {

        Pattern compact1Pattern = Pattern.compile("(?<=->)[^()]+(?=compact1)(?!.*not found)");
        Pattern compact2Pattern = Pattern.compile("(?<=->)[^()]+(?=compact2)(?!.*not found)");
        Pattern compact3Pattern = Pattern.compile("(?<=->)[^()]+(?=compact3)(?!.*not found)");
        Pattern fullJREPattern = Pattern.compile("(?<=->)[^()]+(?=Full JRE)(?!.*not found)");
        Pattern notFoundPattern = Pattern.compile("(?<=->)[^()]+(?=not found)");

        Matcher compact1Matcher = compact1Pattern.matcher(line);
        Matcher compact2Matcher = compact2Pattern.matcher(line);
        Matcher compact3Matcher = compact3Pattern.matcher(line);
        Matcher fullJREMatcher = fullJREPattern.matcher(line);
        Matcher notFoundMatcher = notFoundPattern.matcher(line);

        if (compact1Matcher.find() && !compact1Matcher.group().trim().isEmpty()) {
            return new PackageProfilePair(compact1Matcher.group().trim(), CompactProfile.COMPACT1);
        }
        else if (compact2Matcher.find() && !compact2Matcher.group().trim().isEmpty()) {
            return new PackageProfilePair(compact2Matcher.group().trim(), CompactProfile.COMPACT2);
        }
        else if(compact3Matcher.find() && !compact3Matcher.group().trim().isEmpty()) {
            return new PackageProfilePair(compact3Matcher.group().trim(), CompactProfile.COMPACT3);
        }
        else if(fullJREMatcher.find() && !fullJREMatcher.group().trim().isEmpty()) {
            return new PackageProfilePair(fullJREMatcher.group().trim(), CompactProfile.FULL_JRE);
        }
        else if(notFoundMatcher.find() && !notFoundMatcher.group().trim().isEmpty()) {
            return new PackageProfilePair(notFoundMatcher.group().trim(), CompactProfile.NOT_FOUND);
        }

        return null;
    }



}
