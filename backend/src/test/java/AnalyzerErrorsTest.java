/**
 *  Copyright 2016 Viktoriia Bakalova
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and limitations under the License.
 */
import cz.muni.fi.sbapr.analyzer.CompactProfileAnalyzerImpl;
import cz.muni.fi.sbapr.analyzer.result.AnalysisResult;
import cz.muni.fi.sbapr.analyzer.result.PackageProfilePair;
import cz.muni.fi.sbapr.enums.CompactProfile;
import cz.muni.fi.sbapr.enums.ResolutionType;
import cz.muni.fi.sbapr.exceptions.AnalyzerException;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class AnalyzerErrorsTest {

    @Test
    public void invalidJarTest() {
        try {
            File jarArchive = new File("src/main/resources/invalidJars/MANIFEST.jar");
            CompactProfileAnalyzerImpl.ANALYZER.analyze(jarArchive, ResolutionType.MAVEN, CompactProfile.COMPACT1);
            Assert.fail();
        } catch(AnalyzerException ex) {}

        try {
            File jarArchive = new File("src/main/resources/invalidJars/MANIFEST.MF");
            CompactProfileAnalyzerImpl.ANALYZER.analyze(jarArchive, ResolutionType.MAVEN, CompactProfile.COMPACT1);
            Assert.fail();
        } catch(AnalyzerException ex) {}

        try {
            File jarArchive = new File("src/main/resources/invalidJars/META-INF");
            CompactProfileAnalyzerImpl.ANALYZER.analyze(jarArchive, ResolutionType.MAVEN, CompactProfile.COMPACT1);
            Assert.fail();
        } catch(AnalyzerException ex) {}
    }

    @Test
    public void defaultResolutionTypeTest() {

        Set<PackageProfilePair> inProfilePackages = new HashSet<>();
        inProfilePackages.add(new PackageProfilePair("java.lang", CompactProfile.COMPACT1));

        Set<PackageProfilePair> outsideProfilePackages = new HashSet<>();

        AnalysisResult requiredResult = new AnalysisResult(CompactProfile.COMPACT1, inProfilePackages, outsideProfilePackages);

        try {
            File jarArchive = new File("src/main/resources/classpath/helper1Compact1NoDepsInPom.jar");
            AnalysisResult result = CompactProfileAnalyzerImpl.ANALYZER.analyze(jarArchive, null, null);
            Assert.assertEquals(requiredResult, result);

            jarArchive = new File("src/main/resources/classpath/helper1Compact1NoDepsInPom.jar");
            result = CompactProfileAnalyzerImpl.ANALYZER.analyze(jarArchive, null, CompactProfile.COMPACT1);
            Assert.assertEquals(requiredResult, result);

            jarArchive = new File("src/main/resources/classpath/helper1Compact1NoDepsInPom.jar");
            result = CompactProfileAnalyzerImpl.ANALYZER.analyze(jarArchive, null, CompactProfile.COMPACT2);
            Assert.assertEquals(requiredResult, result);

            jarArchive = new File("src/main/resources/classpath/helper1Compact1NoDepsInPom.jar");
            result = CompactProfileAnalyzerImpl.ANALYZER.analyze(jarArchive, null, CompactProfile.COMPACT3);
            Assert.assertEquals(requiredResult, result);

            jarArchive = new File("src/main/resources/classpath/helper1Compact1NoDepsInPom.jar");
            result = CompactProfileAnalyzerImpl.ANALYZER.analyze(jarArchive, null, CompactProfile.FULL_JRE);
            Assert.assertEquals(requiredResult, result);
        }
        catch(AnalyzerException ex) {
            Assert.fail("Successful analysis expected");
        }
    }

    @Test
    public void noJarValidator() {
        try {
            CompactProfileAnalyzerImpl.ANALYZER.setValidator(null);
            Assert.fail();
        } catch(IllegalArgumentException ex) {}
    }
}
