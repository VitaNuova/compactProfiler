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

public class MavenSimpleTest {
    @Test
    public void testTransitiveProfile1() {
        try {
            File jarArchive = new File("src/main/resources/maven/compact1TwoCompact1DepsInPom-1.jar");
            AnalysisResult result = CompactProfileAnalyzerImpl.ANALYZER.analyze(jarArchive, ResolutionType.MAVEN, CompactProfile.COMPACT1);
            Assert.assertEquals(CompactProfile.COMPACT1, result.getMinimalProfile());
        }
        catch(AnalyzerException ex) {
            Assert.fail("Successful analysis expected");
        }

    }

    @Test
    public void testTransitiveProfile2() {
        try {
            File jarArchive = new File("src/main/resources/maven/compact2Compact2DepInPom-1.0.jar");
            AnalysisResult result = CompactProfileAnalyzerImpl.ANALYZER.analyze(jarArchive, ResolutionType.MAVEN, CompactProfile.COMPACT2);
            Assert.assertEquals(CompactProfile.COMPACT2, result.getMinimalProfile());
        }
        catch(AnalyzerException ex) {
            Assert.fail("Successful analysis expected");
        }
    }

    @Test
    public void testTransitiveProfile3() {
        try {
            File jarArchive = new File("src/main/resources/maven/compact3Compact3DepInPom-1.jar");
            AnalysisResult result = CompactProfileAnalyzerImpl.ANALYZER.analyze(jarArchive, ResolutionType.MAVEN, CompactProfile.COMPACT3);
            Assert.assertEquals(CompactProfile.COMPACT3, result.getMinimalProfile());
        }
        catch(AnalyzerException ex) {
            Assert.fail("Successful analysis expected");
        }
    }

    @Test
    public void testTransitiveFullJRE() {
        try {
            File jarArchive = new File("src/main/resources/maven/fullJREFullJREDepInPom-1.jar");
            AnalysisResult result = CompactProfileAnalyzerImpl.ANALYZER.analyze(jarArchive, ResolutionType.MAVEN, CompactProfile.FULL_JRE);
            Assert.assertEquals(CompactProfile.FULL_JRE, result.getMinimalProfile());
        }
        catch(AnalyzerException ex) {
            Assert.fail("Successful analysis expected");
        }
    }

    @Test
    public void testTransitiveProfile1DesiredNone1OrHigher() {
        try {

            Set<PackageProfilePair> inProfilePackages = new HashSet<>();
            inProfilePackages.add(new PackageProfilePair("java.io", CompactProfile.COMPACT1));
            inProfilePackages.add(new PackageProfilePair("java.lang", CompactProfile.COMPACT1));
            inProfilePackages.add(new PackageProfilePair("java.math", CompactProfile.COMPACT1));

            Set<PackageProfilePair> outsideProfilePackages = new HashSet<>();

            AnalysisResult requiredResult = new AnalysisResult(CompactProfile.COMPACT1, inProfilePackages, outsideProfilePackages);

            File jarArchive = new File("src/main/resources/maven/compact1TwoCompact1DepsInPom-1.jar");
            AnalysisResult result = CompactProfileAnalyzerImpl.ANALYZER.analyze(jarArchive, ResolutionType.MAVEN, null);
            Assert.assertEquals(requiredResult, result);

            result = CompactProfileAnalyzerImpl.ANALYZER.analyze(jarArchive, ResolutionType.MAVEN, CompactProfile.COMPACT1);
            Assert.assertEquals(requiredResult, result);

            result = CompactProfileAnalyzerImpl.ANALYZER.analyze(jarArchive, ResolutionType.MAVEN, CompactProfile.COMPACT2);
            Assert.assertEquals(requiredResult, result);

            result = CompactProfileAnalyzerImpl.ANALYZER.analyze(jarArchive, ResolutionType.MAVEN, CompactProfile.COMPACT3);
            Assert.assertEquals(requiredResult, result);

            result = CompactProfileAnalyzerImpl.ANALYZER.analyze(jarArchive, ResolutionType.MAVEN, CompactProfile.FULL_JRE);
            Assert.assertEquals(requiredResult, result);
        }
        catch(AnalyzerException ex) {
            Assert.fail("Successful analysis expected");
        }
    }

    @Test
    public void testTransitiveProfile2Desired1() {
        try {
            File jarArchive = new File("src/main/resources/maven/compact2Compact2DepInPom-1.0.jar");
            AnalysisResult result = CompactProfileAnalyzerImpl.ANALYZER.analyze(jarArchive, ResolutionType.MAVEN, CompactProfile.COMPACT1);

            Set<PackageProfilePair> outsideProfilePackages = new HashSet<>();
            outsideProfilePackages.add(new PackageProfilePair("java.sql", CompactProfile.COMPACT2));

            Set<PackageProfilePair> inProfilePackages = new HashSet<>();
            inProfilePackages.add(new PackageProfilePair("java.io", CompactProfile.COMPACT1));
            inProfilePackages.add(new PackageProfilePair("java.lang", CompactProfile.COMPACT1));
            inProfilePackages.add(new PackageProfilePair("java.math", CompactProfile.COMPACT1));

            AnalysisResult requiredResult = new AnalysisResult(CompactProfile.COMPACT2, inProfilePackages, outsideProfilePackages);

            Assert.assertEquals(requiredResult, result);
        }
        catch(AnalyzerException ex) {
            Assert.fail("Successful analysis expected");
        }
    }

    @Test
    public void testTransitiveProfile2Desired2AndHigher() {
        try {
            File jarArchive = new File("src/main/resources/maven/compact2Compact2DepInPom-1.0.jar");
            AnalysisResult result = CompactProfileAnalyzerImpl.ANALYZER.analyze(jarArchive, ResolutionType.MAVEN, CompactProfile.COMPACT2);

            Set<PackageProfilePair> inProfilePackages = new HashSet<>();
            inProfilePackages.add(new PackageProfilePair("java.sql", CompactProfile.COMPACT2));
            inProfilePackages.add(new PackageProfilePair("java.io", CompactProfile.COMPACT1));
            inProfilePackages.add(new PackageProfilePair("java.lang", CompactProfile.COMPACT1));
            inProfilePackages.add(new PackageProfilePair("java.math", CompactProfile.COMPACT1));

            Set<PackageProfilePair> outsideProfilePackages = new HashSet<>();

            AnalysisResult requiredResult = new AnalysisResult(CompactProfile.COMPACT2, inProfilePackages, outsideProfilePackages);
            Assert.assertEquals(requiredResult, result);

            result = CompactProfileAnalyzerImpl.ANALYZER.analyze(jarArchive, ResolutionType.MAVEN, CompactProfile.COMPACT3);
            Assert.assertEquals(requiredResult, result);

            result = CompactProfileAnalyzerImpl.ANALYZER.analyze(jarArchive, ResolutionType.MAVEN, CompactProfile.FULL_JRE);
            Assert.assertEquals(requiredResult, result);
        }
        catch(AnalyzerException ex) {
            Assert.fail("Successful analysis expected");
        }
    }

    @Test
    public void testTransitiveProfile3Desired1() {
        try {
            File jarArchive = new File("src/main/resources/maven/compact3Compact3DepInPom-1.jar");
            AnalysisResult result = CompactProfileAnalyzerImpl.ANALYZER.analyze(jarArchive, ResolutionType.MAVEN, CompactProfile.COMPACT1);

            Set<PackageProfilePair> outsideProfilePackages = new HashSet<>();
            outsideProfilePackages.add(new PackageProfilePair("java.sql", CompactProfile.COMPACT2));
            outsideProfilePackages.add(new PackageProfilePair("javax.tools", CompactProfile.COMPACT3));

            Set<PackageProfilePair> inProfilePackages = new HashSet<>();
            inProfilePackages.add(new PackageProfilePair("java.io", CompactProfile.COMPACT1));
            inProfilePackages.add(new PackageProfilePair("java.lang", CompactProfile.COMPACT1));
            inProfilePackages.add(new PackageProfilePair("java.math", CompactProfile.COMPACT1));

            AnalysisResult requiredResult = new AnalysisResult(CompactProfile.COMPACT3, inProfilePackages, outsideProfilePackages);
            Assert.assertEquals(requiredResult, result);
        }
        catch(AnalyzerException ex) {
            Assert.fail("Successful analysis expected");
        }
    }

    @Test
    public void testTransitiveProfile3Desired2() {
        try {
            File jarArchive = new File("src/main/resources/maven/compact3Compact3DepInPom-1.jar");
            AnalysisResult result = CompactProfileAnalyzerImpl.ANALYZER.analyze(jarArchive, ResolutionType.MAVEN, CompactProfile.COMPACT2);

            Set<PackageProfilePair> outsideProfilePackages = new HashSet<>();
            outsideProfilePackages.add(new PackageProfilePair("javax.tools", CompactProfile.COMPACT3));

            Set<PackageProfilePair> inProfilePackages = new HashSet<>();
            inProfilePackages.add(new PackageProfilePair("java.sql", CompactProfile.COMPACT2));
            inProfilePackages.add(new PackageProfilePair("java.io", CompactProfile.COMPACT1));
            inProfilePackages.add(new PackageProfilePair("java.lang", CompactProfile.COMPACT1));
            inProfilePackages.add(new PackageProfilePair("java.math", CompactProfile.COMPACT1));

            AnalysisResult requiredResult = new AnalysisResult(CompactProfile.COMPACT3, inProfilePackages, outsideProfilePackages);
            Assert.assertEquals(requiredResult, result);
        }
        catch(AnalyzerException ex) {
            Assert.fail("Successful analysis expected");
        }
    }

    @Test
    public void testTransitiveProfile3Desired3OrFull() {
        try {

            Set<PackageProfilePair> inProfilePackages = new HashSet<>();
            inProfilePackages.add(new PackageProfilePair("javax.tools", CompactProfile.COMPACT3));
            inProfilePackages.add(new PackageProfilePair("java.sql", CompactProfile.COMPACT2));
            inProfilePackages.add(new PackageProfilePair("java.io", CompactProfile.COMPACT1));
            inProfilePackages.add(new PackageProfilePair("java.lang", CompactProfile.COMPACT1));
            inProfilePackages.add(new PackageProfilePair("java.math", CompactProfile.COMPACT1));

            Set<PackageProfilePair> outsideProfilePackages = new HashSet<>();

            AnalysisResult requiredResult = new AnalysisResult(CompactProfile.COMPACT3, inProfilePackages, outsideProfilePackages);

            File jarArchive = new File("src/main/resources/maven/compact3Compact3DepInPom-1.jar");
            AnalysisResult result = CompactProfileAnalyzerImpl.ANALYZER.analyze(jarArchive, ResolutionType.MAVEN, CompactProfile.COMPACT3);
            Assert.assertEquals(requiredResult, result);

            result = CompactProfileAnalyzerImpl.ANALYZER.analyze(jarArchive, ResolutionType.MAVEN, CompactProfile.FULL_JRE);
            Assert.assertEquals(requiredResult, result);

        }
        catch(AnalyzerException ex) {
            Assert.fail("Successful analysis expected");
        }
    }

    @Test
    public void testTransitiveFullJREDesired1() {
        try {
            File jarArchive = new File("src/main/resources/maven/fullJREFullJREDepInPom-1.jar");
            AnalysisResult result = CompactProfileAnalyzerImpl.ANALYZER.analyze(jarArchive, ResolutionType.MAVEN, CompactProfile.COMPACT1);

            Set<PackageProfilePair> outsideProfilePackages = new HashSet<>();
            outsideProfilePackages.add(new PackageProfilePair("java.awt", CompactProfile.FULL_JRE));
            outsideProfilePackages.add(new PackageProfilePair("javax.swing", CompactProfile.FULL_JRE));
            outsideProfilePackages.add(new PackageProfilePair("javax.tools", CompactProfile.COMPACT3));
            outsideProfilePackages.add(new PackageProfilePair("java.sql", CompactProfile.COMPACT2));

            Set<PackageProfilePair> inProfilePackages = new HashSet<>();
            inProfilePackages.add(new PackageProfilePair("java.io", CompactProfile.COMPACT1));
            inProfilePackages.add(new PackageProfilePair("java.lang", CompactProfile.COMPACT1));
            inProfilePackages.add(new PackageProfilePair("java.math", CompactProfile.COMPACT1));

            AnalysisResult requiredResult = new AnalysisResult(CompactProfile.FULL_JRE, inProfilePackages, outsideProfilePackages);
            Assert.assertEquals(requiredResult, result);
        }
        catch(AnalyzerException ex) {
            Assert.fail("Successful analysis expected");
        }
    }

    @Test
    public void testTransitiveFullJREDesired2() {
        try {
            File jarArchive = new File("src/main/resources/maven/fullJREFullJREDepInPom-1.jar");
            AnalysisResult result = CompactProfileAnalyzerImpl.ANALYZER.analyze(jarArchive, ResolutionType.MAVEN, CompactProfile.COMPACT2);

            Set<PackageProfilePair> outsideProfilePackages = new HashSet<>();
            outsideProfilePackages.add(new PackageProfilePair("java.awt", CompactProfile.FULL_JRE));
            outsideProfilePackages.add(new PackageProfilePair("javax.swing", CompactProfile.FULL_JRE));
            outsideProfilePackages.add(new PackageProfilePair("javax.tools", CompactProfile.COMPACT3));


            Set<PackageProfilePair> inProfilePackages = new HashSet<>();
            inProfilePackages.add(new PackageProfilePair("java.io", CompactProfile.COMPACT1));
            inProfilePackages.add(new PackageProfilePair("java.lang", CompactProfile.COMPACT1));
            inProfilePackages.add(new PackageProfilePair("java.math", CompactProfile.COMPACT1));
            inProfilePackages.add(new PackageProfilePair("java.sql", CompactProfile.COMPACT2));

            AnalysisResult requiredResult = new AnalysisResult(CompactProfile.FULL_JRE, inProfilePackages, outsideProfilePackages);
            Assert.assertEquals(requiredResult, result);
        }
        catch(AnalyzerException ex) {
            Assert.fail("Successful analysis expected");
        }
    }

    @Test
    public void testTransitiveFullJREDesired3() {
        try {
            File jarArchive = new File("src/main/resources/maven/fullJREFullJREDepInPom-1.jar");
            AnalysisResult result = CompactProfileAnalyzerImpl.ANALYZER.analyze(jarArchive, ResolutionType.MAVEN, CompactProfile.COMPACT3);

            Set<PackageProfilePair> outsideProfilePackages = new HashSet<>();
            outsideProfilePackages.add(new PackageProfilePair("java.awt", CompactProfile.FULL_JRE));
            outsideProfilePackages.add(new PackageProfilePair("javax.swing", CompactProfile.FULL_JRE));

            Set<PackageProfilePair> inProfilePackages = new HashSet<>();
            inProfilePackages.add(new PackageProfilePair("java.io", CompactProfile.COMPACT1));
            inProfilePackages.add(new PackageProfilePair("java.lang", CompactProfile.COMPACT1));
            inProfilePackages.add(new PackageProfilePair("java.math", CompactProfile.COMPACT1));
            inProfilePackages.add(new PackageProfilePair("java.sql", CompactProfile.COMPACT2));
            inProfilePackages.add(new PackageProfilePair("javax.tools", CompactProfile.COMPACT3));

            AnalysisResult requiredResult = new AnalysisResult(CompactProfile.FULL_JRE, inProfilePackages, outsideProfilePackages);
            Assert.assertEquals(requiredResult, result);
        }
        catch(AnalyzerException ex) {
            Assert.fail("Successful analysis expected");
        }
    }

    @Test
    public void testTransitiveFullJREDesiredFull() {
        try {

            Set<PackageProfilePair> inProfilePackages = new HashSet<>();
            inProfilePackages.add(new PackageProfilePair("java.io", CompactProfile.COMPACT1));
            inProfilePackages.add(new PackageProfilePair("java.lang", CompactProfile.COMPACT1));
            inProfilePackages.add(new PackageProfilePair("java.math", CompactProfile.COMPACT1));
            inProfilePackages.add(new PackageProfilePair("java.sql", CompactProfile.COMPACT2));
            inProfilePackages.add(new PackageProfilePair("javax.tools", CompactProfile.COMPACT3));
            inProfilePackages.add(new PackageProfilePair("java.awt", CompactProfile.FULL_JRE));
            inProfilePackages.add(new PackageProfilePair("javax.swing", CompactProfile.FULL_JRE));

            Set<PackageProfilePair> outsideProfilePackages = new HashSet<>();

            File jarArchive = new File("src/main/resources/maven/fullJREFullJREDepInPom-1.jar");
            AnalysisResult result = CompactProfileAnalyzerImpl.ANALYZER.analyze(jarArchive, ResolutionType.MAVEN, CompactProfile.FULL_JRE);

            AnalysisResult requiredResult = new AnalysisResult(CompactProfile.FULL_JRE, inProfilePackages, outsideProfilePackages);
            Assert.assertEquals(requiredResult, result);

        }
        catch(AnalyzerException ex) {
            Assert.fail("Successful analysis expected");
        }
    }


}
