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

public class ClasspathSimpleTest {

    @Test
    public void testHelper1Profile1NoClassPathEntryDesiredNoneOrHigher() {

        Set<PackageProfilePair> inProfilePackages = new HashSet<>();
        inProfilePackages.add(new PackageProfilePair("java.lang", CompactProfile.COMPACT1));

        Set<PackageProfilePair> outsideProfilePackages = new HashSet<>();

        AnalysisResult requiredResult = new AnalysisResult(CompactProfile.COMPACT1, inProfilePackages, outsideProfilePackages);

        try {
            File jarArchive = new File("src/main/resources/classpath/helper1Compact1NoDepsInPom.jar");
            AnalysisResult result = CompactProfileAnalyzerImpl.ANALYZER.analyze(jarArchive, ResolutionType.CLASSPATH, null);
            Assert.assertEquals(requiredResult, result);

            result = CompactProfileAnalyzerImpl.ANALYZER.analyze(jarArchive, ResolutionType.CLASSPATH, CompactProfile.COMPACT1);
            Assert.assertEquals(requiredResult, result);

            result = CompactProfileAnalyzerImpl.ANALYZER.analyze(jarArchive, ResolutionType.CLASSPATH, CompactProfile.COMPACT2);
            Assert.assertEquals(requiredResult, result);

            result = CompactProfileAnalyzerImpl.ANALYZER.analyze(jarArchive, ResolutionType.CLASSPATH, CompactProfile.COMPACT3);
            Assert.assertEquals(requiredResult, result);

            result = CompactProfileAnalyzerImpl.ANALYZER.analyze(jarArchive, ResolutionType.CLASSPATH, CompactProfile.FULL_JRE);
            Assert.assertEquals(requiredResult, result);
        }
        catch(AnalyzerException ex) {
            Assert.fail("Successful analysis expected");
        }
    }

    @Test
    public void testHelper2Profile1NoClassPathEntryDesiredNoneOrHigher() {

        Set<PackageProfilePair> inProfilePackages = new HashSet<>();
        inProfilePackages.add(new PackageProfilePair("java.lang", CompactProfile.COMPACT1));
        inProfilePackages.add(new PackageProfilePair("java.math", CompactProfile.COMPACT1));


        Set<PackageProfilePair> outsideProfilePackages = new HashSet<>();

        AnalysisResult requiredResult = new AnalysisResult(CompactProfile.COMPACT1, inProfilePackages, outsideProfilePackages);

        try {
            File jarArchive = new File("src/main/resources/classpath/helper2Compact1NoDepsInPom.jar");
            AnalysisResult result = CompactProfileAnalyzerImpl.ANALYZER.analyze(jarArchive, ResolutionType.CLASSPATH, null);
            Assert.assertEquals(requiredResult, result);

            result = CompactProfileAnalyzerImpl.ANALYZER.analyze(jarArchive, ResolutionType.CLASSPATH, CompactProfile.COMPACT1);
            Assert.assertEquals(requiredResult, result);

            result = CompactProfileAnalyzerImpl.ANALYZER.analyze(jarArchive, ResolutionType.CLASSPATH, CompactProfile.COMPACT2);
            Assert.assertEquals(requiredResult, result);

            result = CompactProfileAnalyzerImpl.ANALYZER.analyze(jarArchive, ResolutionType.CLASSPATH, CompactProfile.COMPACT3);
            Assert.assertEquals(requiredResult, result);

            result = CompactProfileAnalyzerImpl.ANALYZER.analyze(jarArchive, ResolutionType.CLASSPATH, CompactProfile.FULL_JRE);
            Assert.assertEquals(requiredResult, result);
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

            File jarArchive = new File("src/main/resources/classpath/compact1TwoCompact1DepsInPom/compact1TwoCompact1DepsInPom.jar");
            AnalysisResult result = CompactProfileAnalyzerImpl.ANALYZER.analyze(jarArchive, ResolutionType.CLASSPATH, null);
            Assert.assertEquals(requiredResult, result);

            result = CompactProfileAnalyzerImpl.ANALYZER.analyze(jarArchive, ResolutionType.CLASSPATH, CompactProfile.COMPACT1);
            Assert.assertEquals(requiredResult, result);

            result = CompactProfileAnalyzerImpl.ANALYZER.analyze(jarArchive, ResolutionType.CLASSPATH, CompactProfile.COMPACT2);
            Assert.assertEquals(requiredResult, result);

            result = CompactProfileAnalyzerImpl.ANALYZER.analyze(jarArchive, ResolutionType.CLASSPATH, CompactProfile.COMPACT3);
            Assert.assertEquals(requiredResult, result);

            result = CompactProfileAnalyzerImpl.ANALYZER.analyze(jarArchive, ResolutionType.CLASSPATH, CompactProfile.FULL_JRE);
            Assert.assertEquals(requiredResult, result);
        }
        catch(AnalyzerException ex) {
            Assert.fail("Successful analysis expected");
        }
    }

    @Test
    public void testHelperProfile2NoClassPathEntryDesiredNoneOr1() {
        try {

            Set<PackageProfilePair> inProfilePackages = new HashSet<>();
            inProfilePackages.add(new PackageProfilePair("java.io", CompactProfile.COMPACT1));
            inProfilePackages.add(new PackageProfilePair("java.lang", CompactProfile.COMPACT1));

            Set<PackageProfilePair> outsideProfilePackages = new HashSet<>();
            outsideProfilePackages.add(new PackageProfilePair("java.sql", CompactProfile.COMPACT2));

            AnalysisResult requiredResult = new AnalysisResult(CompactProfile.COMPACT2, inProfilePackages, outsideProfilePackages);

            File jarArchive = new File("src/main/resources/classpath/helperCompact2NoDepsInPom.jar");
            AnalysisResult result = CompactProfileAnalyzerImpl.ANALYZER.analyze(jarArchive, ResolutionType.CLASSPATH, null);
            Assert.assertEquals(requiredResult, result);

            result = CompactProfileAnalyzerImpl.ANALYZER.analyze(jarArchive, ResolutionType.CLASSPATH, CompactProfile.COMPACT1);
            Assert.assertEquals(requiredResult, result);
        }
        catch(AnalyzerException ex) {
            Assert.fail("Successful analysis expected");
        }
    }

    @Test
    public void testHelperProfile2NoClassPathEntryDesired2OrHigher() {
        try {
            Set<PackageProfilePair> inProfilePackages = new HashSet<>();
            inProfilePackages.add(new PackageProfilePair("java.io", CompactProfile.COMPACT1));
            inProfilePackages.add(new PackageProfilePair("java.lang", CompactProfile.COMPACT1));
            inProfilePackages.add(new PackageProfilePair("java.sql", CompactProfile.COMPACT2));

            Set<PackageProfilePair> outsideProfilePackages = new HashSet<>();

            AnalysisResult requiredResult = new AnalysisResult(CompactProfile.COMPACT2, inProfilePackages, outsideProfilePackages);

            File jarArchive = new File("src/main/resources/classpath/helperCompact2NoDepsInPom.jar");
            AnalysisResult result = CompactProfileAnalyzerImpl.ANALYZER.analyze(jarArchive, ResolutionType.CLASSPATH, CompactProfile.COMPACT2);
            Assert.assertEquals(requiredResult, result);

            result = CompactProfileAnalyzerImpl.ANALYZER.analyze(jarArchive, ResolutionType.CLASSPATH, CompactProfile.COMPACT3);
            Assert.assertEquals(requiredResult, result);

            result = CompactProfileAnalyzerImpl.ANALYZER.analyze(jarArchive, ResolutionType.CLASSPATH, CompactProfile.FULL_JRE);
            Assert.assertEquals(requiredResult, result);
        }
        catch(AnalyzerException ex) {
            Assert.fail("Successful analysis expected");
        }
    }

    @Test
    public void testTransitiveProfile2DesiredNoneOr1() {
        try {

            Set<PackageProfilePair> inProfilePackages = new HashSet<>();
            inProfilePackages.add(new PackageProfilePair("java.io", CompactProfile.COMPACT1));
            inProfilePackages.add(new PackageProfilePair("java.lang", CompactProfile.COMPACT1));
            inProfilePackages.add(new PackageProfilePair("java.math", CompactProfile.COMPACT1));

            Set<PackageProfilePair> outsideProfilePackages = new HashSet<>();
            outsideProfilePackages.add(new PackageProfilePair("java.sql", CompactProfile.COMPACT2));

            AnalysisResult requiredResult = new AnalysisResult(CompactProfile.COMPACT2, inProfilePackages, outsideProfilePackages);

            File jarArchive = new File("src/main/resources/classpath/Compact2Compact2DepInPom_jar/Compact2Compact2DepInPom.jar");
            AnalysisResult result = CompactProfileAnalyzerImpl.ANALYZER.analyze(jarArchive, ResolutionType.CLASSPATH, null);
            Assert.assertEquals(requiredResult, result);

            jarArchive = new File("src/main/resources/classpath/Compact2Compact2DepInPom_jar/Compact2Compact2DepInPom.jar");
            result = CompactProfileAnalyzerImpl.ANALYZER.analyze(jarArchive, ResolutionType.CLASSPATH, CompactProfile.COMPACT1);
            Assert.assertEquals(requiredResult, result);
        }
        catch(AnalyzerException ex) {
            Assert.fail("Successful analysis expected");
        }
    }


    @Test
    public void testTransitiveProfile2Desired2OrHigher() {
        try {

            Set<PackageProfilePair> inProfilePackages = new HashSet<>();
            inProfilePackages.add(new PackageProfilePair("java.io", CompactProfile.COMPACT1));
            inProfilePackages.add(new PackageProfilePair("java.lang", CompactProfile.COMPACT1));
            inProfilePackages.add(new PackageProfilePair("java.math", CompactProfile.COMPACT1));
            inProfilePackages.add(new PackageProfilePair("java.sql", CompactProfile.COMPACT2));

            Set<PackageProfilePair> outsideProfilePackages = new HashSet<>();

            AnalysisResult requiredResult = new AnalysisResult(CompactProfile.COMPACT2, inProfilePackages, outsideProfilePackages);

            File jarArchive = new File("src/main/resources/classpath/Compact2Compact2DepInPom_jar/Compact2Compact2DepInPom.jar");
            AnalysisResult result = CompactProfileAnalyzerImpl.ANALYZER.analyze(jarArchive, ResolutionType.CLASSPATH, CompactProfile.COMPACT2);
            Assert.assertEquals(requiredResult, result);

            result = CompactProfileAnalyzerImpl.ANALYZER.analyze(jarArchive, ResolutionType.CLASSPATH, CompactProfile.COMPACT2);
            Assert.assertEquals(requiredResult, result);

            result = CompactProfileAnalyzerImpl.ANALYZER.analyze(jarArchive, ResolutionType.CLASSPATH, CompactProfile.COMPACT3);
            Assert.assertEquals(requiredResult, result);

            result = CompactProfileAnalyzerImpl.ANALYZER.analyze(jarArchive, ResolutionType.CLASSPATH, CompactProfile.FULL_JRE);
            Assert.assertEquals(requiredResult, result);
        }
        catch(AnalyzerException ex) {
            Assert.fail("Successful analysis expected");
        }
    }

    @Test
    public void testHelperProfile3NoClassPathEntryDesiredNone1Or2() {
        try {

            Set<PackageProfilePair> inProfilePackages = new HashSet<>();
            inProfilePackages.add(new PackageProfilePair("java.lang", CompactProfile.COMPACT1));

            Set<PackageProfilePair> outsideProfilePackages = new HashSet<>();
            outsideProfilePackages.add(new PackageProfilePair("javax.tools", CompactProfile.COMPACT3));

            AnalysisResult requiredResult = new AnalysisResult(CompactProfile.COMPACT3, inProfilePackages, outsideProfilePackages);

            File jarArchive = new File("src/main/resources/classpath/helperCompact3NoDepsInPom.jar");
            AnalysisResult result = CompactProfileAnalyzerImpl.ANALYZER.analyze(jarArchive, ResolutionType.CLASSPATH, null);
            Assert.assertEquals(requiredResult, result);

            result = CompactProfileAnalyzerImpl.ANALYZER.analyze(jarArchive, ResolutionType.CLASSPATH, CompactProfile.COMPACT1);
            Assert.assertEquals(requiredResult, result);

            result = CompactProfileAnalyzerImpl.ANALYZER.analyze(jarArchive, ResolutionType.CLASSPATH, CompactProfile.COMPACT2);
            Assert.assertEquals(requiredResult, result);
        }
        catch(AnalyzerException ex) {
            Assert.fail("Successful analysis expected");
        }
    }

    @Test
    public void testHelperProfile3NoClassPathEntryDesired3OrFull() {
        try {

            Set<PackageProfilePair> inProfilePackages = new HashSet<>();
            inProfilePackages.add(new PackageProfilePair("java.lang", CompactProfile.COMPACT1));
            inProfilePackages.add(new PackageProfilePair("javax.tools", CompactProfile.COMPACT3));

            Set<PackageProfilePair> outsideProfilePackages = new HashSet<>();

            AnalysisResult requiredResult = new AnalysisResult(CompactProfile.COMPACT3, inProfilePackages, outsideProfilePackages);

            File jarArchive = new File("src/main/resources/classpath/helperCompact3NoDepsInPom.jar");
            AnalysisResult result = CompactProfileAnalyzerImpl.ANALYZER.analyze(jarArchive, ResolutionType.CLASSPATH, CompactProfile.COMPACT3);
            Assert.assertEquals(requiredResult, result);

            jarArchive = new File("src/main/resources/classpath/helperCompact3NoDepsInPom.jar");
            result = CompactProfileAnalyzerImpl.ANALYZER.analyze(jarArchive, ResolutionType.CLASSPATH, CompactProfile.FULL_JRE);
            Assert.assertEquals(requiredResult, result);
        }
        catch(AnalyzerException ex) {
            Assert.fail("Successful analysis expected");
        }
    }

    @Test
    public void testTransitiveProfile3DesiredNoneOr1() {
        try {

            Set<PackageProfilePair> inProfilePackages = new HashSet<>();
            inProfilePackages.add(new PackageProfilePair("java.io", CompactProfile.COMPACT1));
            inProfilePackages.add(new PackageProfilePair("java.lang", CompactProfile.COMPACT1));
            inProfilePackages.add(new PackageProfilePair("java.math", CompactProfile.COMPACT1));

            Set<PackageProfilePair> outsideProfilePackages = new HashSet<>();
            outsideProfilePackages.add(new PackageProfilePair("java.sql", CompactProfile.COMPACT2));
            outsideProfilePackages.add(new PackageProfilePair("javax.tools", CompactProfile.COMPACT3));

            AnalysisResult requiredResult = new AnalysisResult(CompactProfile.COMPACT3, inProfilePackages, outsideProfilePackages);

            File jarArchive = new File("src/main/resources/classpath/compact3compact3DepInPom_jar/compact3compact3DepInPom.jar");
            AnalysisResult result = CompactProfileAnalyzerImpl.ANALYZER.analyze(jarArchive, ResolutionType.CLASSPATH, null);
            Assert.assertEquals(requiredResult, result);

            result = CompactProfileAnalyzerImpl.ANALYZER.analyze(jarArchive, ResolutionType.CLASSPATH, CompactProfile.COMPACT1);
            Assert.assertEquals(requiredResult, result);
        }
        catch(AnalyzerException ex) {
            Assert.fail("Successful analysis expected");
        }
    }

    @Test
    public void testTransitiveProfile3Desired2() {
        try {
            Set<PackageProfilePair> inProfilePackages = new HashSet<>();
            inProfilePackages.add(new PackageProfilePair("java.io", CompactProfile.COMPACT1));
            inProfilePackages.add(new PackageProfilePair("java.lang", CompactProfile.COMPACT1));
            inProfilePackages.add(new PackageProfilePair("java.math", CompactProfile.COMPACT1));
            inProfilePackages.add(new PackageProfilePair("java.sql", CompactProfile.COMPACT2));

            Set<PackageProfilePair> outsideProfilePackages = new HashSet<>();
            outsideProfilePackages.add(new PackageProfilePair("javax.tools", CompactProfile.COMPACT3));

            AnalysisResult requiredResult = new AnalysisResult(CompactProfile.COMPACT3, inProfilePackages, outsideProfilePackages);

            File jarArchive = new File("src/main/resources/classpath/compact3compact3DepInPom_jar/compact3compact3DepInPom.jar");
            AnalysisResult result = CompactProfileAnalyzerImpl.ANALYZER.analyze(jarArchive, ResolutionType.CLASSPATH, CompactProfile.COMPACT2);
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
            inProfilePackages.add(new PackageProfilePair("java.io", CompactProfile.COMPACT1));
            inProfilePackages.add(new PackageProfilePair("java.lang", CompactProfile.COMPACT1));
            inProfilePackages.add(new PackageProfilePair("java.math", CompactProfile.COMPACT1));
            inProfilePackages.add(new PackageProfilePair("java.sql", CompactProfile.COMPACT2));
            inProfilePackages.add(new PackageProfilePair("javax.tools", CompactProfile.COMPACT3));

            Set<PackageProfilePair> outsideProfilePackages = new HashSet<>();

            AnalysisResult requiredResult = new AnalysisResult(CompactProfile.COMPACT3, inProfilePackages, outsideProfilePackages);

            File jarArchive = new File("src/main/resources/classpath/compact3compact3DepInPom_jar/compact3compact3DepInPom.jar");
            AnalysisResult result = CompactProfileAnalyzerImpl.ANALYZER.analyze(jarArchive, ResolutionType.CLASSPATH, CompactProfile.COMPACT3);
            Assert.assertEquals(requiredResult, result);

            result = CompactProfileAnalyzerImpl.ANALYZER.analyze(jarArchive, ResolutionType.CLASSPATH, CompactProfile.FULL_JRE);
            Assert.assertEquals(requiredResult, result);
        }
        catch(AnalyzerException ex) {
            Assert.fail("Successful analysis expected");
        }
    }

    @Test
    public void testHelperFullJRENoClassPathEntryDesiredNone12Or3() {
        try {

            Set<PackageProfilePair> inProfilePackages = new HashSet<>();
            inProfilePackages.add(new PackageProfilePair("java.lang", CompactProfile.COMPACT1));

            Set<PackageProfilePair> outsideProfilePackages = new HashSet<>();
            outsideProfilePackages.add(new PackageProfilePair("java.awt", CompactProfile.FULL_JRE));
            outsideProfilePackages.add(new PackageProfilePair("javax.swing", CompactProfile.FULL_JRE));

            AnalysisResult requiredResult = new AnalysisResult(CompactProfile.FULL_JRE, inProfilePackages, outsideProfilePackages);

            File jarArchive = new File("src/main/resources/classpath/helperFullJRENoDepsInPom.jar");
            AnalysisResult result = CompactProfileAnalyzerImpl.ANALYZER.analyze(jarArchive, ResolutionType.CLASSPATH, null);
            Assert.assertEquals(requiredResult, result);

            result = CompactProfileAnalyzerImpl.ANALYZER.analyze(jarArchive, ResolutionType.CLASSPATH, CompactProfile.COMPACT1);
            Assert.assertEquals(requiredResult, result);

            result = CompactProfileAnalyzerImpl.ANALYZER.analyze(jarArchive, ResolutionType.CLASSPATH, CompactProfile.COMPACT2);
            Assert.assertEquals(requiredResult, result);

            result = CompactProfileAnalyzerImpl.ANALYZER.analyze(jarArchive, ResolutionType.CLASSPATH, CompactProfile.COMPACT3);
            Assert.assertEquals(requiredResult, result);
        }
        catch(AnalyzerException ex) {
            Assert.fail("Successful analysis expected");
        }
    }

    @Test
    public void testTransitiveFullJREDesiredNoneOr1() {
        try {

            Set<PackageProfilePair> inProfilePackages = new HashSet<>();
            inProfilePackages.add(new PackageProfilePair("java.io", CompactProfile.COMPACT1));
            inProfilePackages.add(new PackageProfilePair("java.lang", CompactProfile.COMPACT1));
            inProfilePackages.add(new PackageProfilePair("java.math", CompactProfile.COMPACT1));

            Set<PackageProfilePair> outsideProfilePackages = new HashSet<>();
            outsideProfilePackages.add(new PackageProfilePair("java.sql", CompactProfile.COMPACT2));
            outsideProfilePackages.add(new PackageProfilePair("javax.tools", CompactProfile.COMPACT3));
            outsideProfilePackages.add(new PackageProfilePair("java.awt", CompactProfile.FULL_JRE));
            outsideProfilePackages.add(new PackageProfilePair("javax.swing", CompactProfile.FULL_JRE));

            AnalysisResult requiredResult = new AnalysisResult(CompactProfile.FULL_JRE, inProfilePackages, outsideProfilePackages);

            File jarArchive = new File("src/main/resources/classpath/fullJREFullJREDepInPom_jar/fullJREFullJREDepInPom.jar");
            AnalysisResult result = CompactProfileAnalyzerImpl.ANALYZER.analyze(jarArchive, ResolutionType.CLASSPATH, null);
            Assert.assertEquals(requiredResult, result);

            jarArchive = new File("src/main/resources/classpath/fullJREFullJREDepInPom_jar/fullJREFullJREDepInPom.jar");
            result = CompactProfileAnalyzerImpl.ANALYZER.analyze(jarArchive, ResolutionType.CLASSPATH, CompactProfile.COMPACT1);
            Assert.assertEquals(requiredResult, result);
        }
        catch(AnalyzerException ex) {
            Assert.fail("Successful analysis expected");
        }
    }

    @Test
    public void testTransitiveFullJREDesired2() {
        try {

            Set<PackageProfilePair> inProfilePackages = new HashSet<>();
            inProfilePackages.add(new PackageProfilePair("java.io", CompactProfile.COMPACT1));
            inProfilePackages.add(new PackageProfilePair("java.lang", CompactProfile.COMPACT1));
            inProfilePackages.add(new PackageProfilePair("java.math", CompactProfile.COMPACT1));
            inProfilePackages.add(new PackageProfilePair("java.sql", CompactProfile.COMPACT2));

            Set<PackageProfilePair> outsideProfilePackages = new HashSet<>();
            outsideProfilePackages.add(new PackageProfilePair("javax.tools", CompactProfile.COMPACT3));
            outsideProfilePackages.add(new PackageProfilePair("java.awt", CompactProfile.FULL_JRE));
            outsideProfilePackages.add(new PackageProfilePair("javax.swing", CompactProfile.FULL_JRE));

            AnalysisResult requiredResult = new AnalysisResult(CompactProfile.FULL_JRE, inProfilePackages, outsideProfilePackages);

            File jarArchive = new File("src/main/resources/classpath/fullJREFullJREDepInPom_jar/fullJREFullJREDepInPom.jar");
            AnalysisResult result = CompactProfileAnalyzerImpl.ANALYZER.analyze(jarArchive, ResolutionType.CLASSPATH, CompactProfile.COMPACT2);
            Assert.assertEquals(requiredResult, result);
        }
        catch(AnalyzerException ex) {
            Assert.fail("Successful analysis expected");
        }
    }

    @Test
    public void testTransitiveFullJREDesired3() {
        try {

            Set<PackageProfilePair> inProfilePackages = new HashSet<>();
            inProfilePackages.add(new PackageProfilePair("java.io", CompactProfile.COMPACT1));
            inProfilePackages.add(new PackageProfilePair("java.lang", CompactProfile.COMPACT1));
            inProfilePackages.add(new PackageProfilePair("java.math", CompactProfile.COMPACT1));
            inProfilePackages.add(new PackageProfilePair("java.sql", CompactProfile.COMPACT2));
            inProfilePackages.add(new PackageProfilePair("javax.tools", CompactProfile.COMPACT3));

            Set<PackageProfilePair> outsideProfilePackages = new HashSet<>();
            outsideProfilePackages.add(new PackageProfilePair("java.awt", CompactProfile.FULL_JRE));
            outsideProfilePackages.add(new PackageProfilePair("javax.swing", CompactProfile.FULL_JRE));

            AnalysisResult requiredResult = new AnalysisResult(CompactProfile.FULL_JRE, inProfilePackages, outsideProfilePackages);

            File jarArchive = new File("src/main/resources/classpath/fullJREFullJREDepInPom_jar/fullJREFullJREDepInPom.jar");
            AnalysisResult result = CompactProfileAnalyzerImpl.ANALYZER.analyze(jarArchive, ResolutionType.CLASSPATH, CompactProfile.COMPACT3);
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

            AnalysisResult requiredResult = new AnalysisResult(CompactProfile.FULL_JRE, inProfilePackages, outsideProfilePackages);

            File jarArchive = new File("src/main/resources/classpath/fullJREFullJREDepInPom_jar/fullJREFullJREDepInPom.jar");
            AnalysisResult result = CompactProfileAnalyzerImpl.ANALYZER.analyze(jarArchive, ResolutionType.CLASSPATH, CompactProfile.FULL_JRE);
            Assert.assertEquals(requiredResult, result);
        }
        catch(AnalyzerException ex) {
            Assert.fail("Successful analysis expected");
        }
    }




}
