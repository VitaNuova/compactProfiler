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

public class MavenTest {

    @Test
    public void testTransitiveFullDesired1() {
        try {

            Set<PackageProfilePair> inProfilePackages = new HashSet<>();
            //javax-servlet-api-3.1.0
            inProfilePackages.add(new PackageProfilePair("java.io", CompactProfile.COMPACT1));
            inProfilePackages.add(new PackageProfilePair("java.lang", CompactProfile.COMPACT1));
            inProfilePackages.add(new PackageProfilePair("java.net", CompactProfile.COMPACT1));
            inProfilePackages.add(new PackageProfilePair("java.text", CompactProfile.COMPACT1));
            inProfilePackages.add(new PackageProfilePair("java.util", CompactProfile.COMPACT1));
            inProfilePackages.add(new PackageProfilePair("java.lang.annotation", CompactProfile.COMPACT1));
            inProfilePackages.add(new PackageProfilePair("java.lang.reflect", CompactProfile.COMPACT1));
            inProfilePackages.add(new PackageProfilePair("java.security", CompactProfile.COMPACT1));
            //jetty-util-ajax-9.3.4.RC0
            inProfilePackages.add(new PackageProfilePair("java.util.concurrent", CompactProfile.COMPACT1));
            //jetty-test-helper-3.0
            inProfilePackages.add(new PackageProfilePair("java.nio.file", CompactProfile.COMPACT1));
            inProfilePackages.add(new PackageProfilePair("java.nio.file.attribute", CompactProfile.COMPACT1));
            inProfilePackages.add(new PackageProfilePair("java.util.concurrent.locks", CompactProfile.COMPACT1));
            inProfilePackages.add(new PackageProfilePair("java.util.jar", CompactProfile.COMPACT1));
            inProfilePackages.add(new PackageProfilePair("java.util.zip", CompactProfile.COMPACT1));
            inProfilePackages.add(new PackageProfilePair("java.util.regex", CompactProfile.COMPACT1));
            //hamcrest-library-1.3
            inProfilePackages.add(new PackageProfilePair("java.math", CompactProfile.COMPACT1));
            //junit-4.12
            inProfilePackages.add(new PackageProfilePair("java.util.concurrent.atomic", CompactProfile.COMPACT1));
            //jetty-util-9.3.4.RC0
            inProfilePackages.add(new PackageProfilePair("java.lang.invoke", CompactProfile.COMPACT1));
            inProfilePackages.add(new PackageProfilePair("java.lang.ref", CompactProfile.COMPACT1));
            inProfilePackages.add(new PackageProfilePair("java.nio", CompactProfile.COMPACT1));
            inProfilePackages.add(new PackageProfilePair("java.nio.channels", CompactProfile.COMPACT1));
            inProfilePackages.add(new PackageProfilePair("java.nio.charset", CompactProfile.COMPACT1));
            inProfilePackages.add(new PackageProfilePair("java.util.function", CompactProfile.COMPACT1));
            inProfilePackages.add(new PackageProfilePair("java.util.logging", CompactProfile.COMPACT1));
            inProfilePackages.add(new PackageProfilePair("java.security.cert", CompactProfile.COMPACT1));
            inProfilePackages.add(new PackageProfilePair("javax.net.ssl", CompactProfile.COMPACT1));
            inProfilePackages.add(new PackageProfilePair("javax.security.auth.x500", CompactProfile.COMPACT1));
            //jetty-security-9.3.4.RC0
            inProfilePackages.add(new PackageProfilePair("javax.security.auth", CompactProfile.COMPACT1));


            Set<PackageProfilePair> outsideProfilePackages = new HashSet<>();
            //hamcrest-library-1.3
            outsideProfilePackages.add(new PackageProfilePair("javax.xml.namespace", CompactProfile.COMPACT2));
            outsideProfilePackages.add(new PackageProfilePair("org.w3c.dom", CompactProfile.COMPACT2));
            outsideProfilePackages.add(new PackageProfilePair("java.beans", CompactProfile.FULL_JRE));
            outsideProfilePackages.add(new PackageProfilePair("javax.xml.xpath", CompactProfile.COMPACT2));
            //junit-4.12
            outsideProfilePackages.add(new PackageProfilePair("java.lang.management", CompactProfile.COMPACT3));
            //jetty-util-9.3.4.RC0
            outsideProfilePackages.add(new PackageProfilePair("java.awt", CompactProfile.FULL_JRE));
            outsideProfilePackages.add(new PackageProfilePair("java.sql", CompactProfile.COMPACT2));
            outsideProfilePackages.add(new PackageProfilePair("javax.imageio", CompactProfile.FULL_JRE));
            outsideProfilePackages.add(new PackageProfilePair("javax.xml.parsers", CompactProfile.COMPACT2));
            outsideProfilePackages.add(new PackageProfilePair("javax.naming", CompactProfile.COMPACT3));
            outsideProfilePackages.add(new PackageProfilePair("javax.naming.ldap", CompactProfile.COMPACT3));
            //jetty-perf-helper-1.0.5
            outsideProfilePackages.add(new PackageProfilePair("com.sun.management", CompactProfile.COMPACT3));
            //HdrHistogram-2.1.2
            outsideProfilePackages.add(new PackageProfilePair("javax.xml.bind", CompactProfile.FULL_JRE));
            //jetty-security-9.3.4.RC0
            outsideProfilePackages.add(new PackageProfilePair("org.ietf.jgss", CompactProfile.COMPACT3));
            //jetty-jmx-9.3.4.RC0
            outsideProfilePackages.add(new PackageProfilePair("java.rmi", CompactProfile.COMPACT2));
            outsideProfilePackages.add(new PackageProfilePair("java.rmi.registry", CompactProfile.COMPACT2));
            outsideProfilePackages.add(new PackageProfilePair("java.rmi.server", CompactProfile.COMPACT2));
            outsideProfilePackages.add(new PackageProfilePair("javax.management", CompactProfile.COMPACT3));
            outsideProfilePackages.add(new PackageProfilePair("javax.management.modelmbean", CompactProfile.COMPACT3));
            outsideProfilePackages.add(new PackageProfilePair("javax.management.remote", CompactProfile.COMPACT3));
            //jetty-xml-9.3.4.RC0
            outsideProfilePackages.add(new PackageProfilePair("org.xml.sax", CompactProfile.COMPACT2));
            outsideProfilePackages.add(new PackageProfilePair("org.xml.sax.helpers", CompactProfile.COMPACT2));
            //jetty-server-9.3.4.RC0
            outsideProfilePackages.add(new PackageProfilePair("javax.sql", CompactProfile.COMPACT2));

            File jarArchive = new File("src/main/resources/maven/example-async-rest-jar-9.3.4.RC0.jar");
            AnalysisResult result = CompactProfileAnalyzerImpl.ANALYZER.analyze(jarArchive, ResolutionType.MAVEN, CompactProfile.COMPACT1);

            AnalysisResult requiredResult = new AnalysisResult(CompactProfile.FULL_JRE, inProfilePackages, outsideProfilePackages);
            Assert.assertEquals(requiredResult, result);
        }
        catch(AnalyzerException ex) {
            Assert.fail("Successful analysis expected");
        }
    }
}
