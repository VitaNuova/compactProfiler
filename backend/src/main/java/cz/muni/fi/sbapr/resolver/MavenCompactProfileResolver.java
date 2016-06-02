/**
 *  Copyright 2016 Viktoriia Bakalova
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and limitations under the License.
 */
package cz.muni.fi.sbapr.resolver;

import cz.muni.fi.sbapr.analyzer.util.IOUtils;
import cz.muni.fi.sbapr.exceptions.ResolverException;
import cz.muni.fi.sbapr.resolver.strategy.NonFilteringTransitiveStrategy;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.jboss.shrinkwrap.resolver.api.maven.strategy.MavenResolutionStrategy;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarInputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;

public enum MavenCompactProfileResolver implements CompactProfileResolver {

    RESOLVER;

    public static final String NO_POM_FOUND_IN_JAR = "No pom file found in project jar";

    private MavenResolutionStrategy strategy;

    /**
     * Resolves transitive dependencies of jar archives built with Maven using non-filtering strategy
     * @param inputJar path to provided jar archive
     * @return list of resolved transitive dependencies
     * @throws ResolverException if there is no pom.xml file in provided jar archive, pom.xml file is invalid,
     * or error has occurred while retrieving transitive dependencies from repository
     */
    @Override
    public List<File> resolve(File inputJar) throws ResolverException {

        strategy = NonFilteringTransitiveStrategy.INSTANCE;

        List<File> result = new ArrayList<>();

        String pomFileName;
        try {
            pomFileName = getPomFile(inputJar);
            if(pomFileName == NO_POM_FOUND_IN_JAR) {
                throw new ResolverException("Pom file was not found in the provided jar archive");
            }
        } catch(IOException ex) {
            throw new ResolverException("I/O exception has occurred when reading pom file from the jar archive", ex);
        }

        Model model;
        try {
            Reader reader = new FileReader(pomFileName);
            MavenXpp3Reader xpp3Reader = new MavenXpp3Reader();
            model = xpp3Reader.read(reader);
        } catch(IOException ex) {
            throw new ResolverException("I/O exception has occurred when reading the pom file", ex);
        } catch(XmlPullParserException ex) {
            throw new ResolverException("Exception has occurred when parsing the pom file", ex);
        }

        String groupId = model.getGroupId();
        String artifactId = model.getArtifactId();
        String version = model.getVersion();
        if(groupId == null) {
            groupId = model.getParent().getGroupId();
        }
        if(version == null) {
            version = model.getParent().getVersion();
        }

        InputStream[] archives = Maven.resolver().resolve(groupId + ":" + artifactId + ":" + version).using(strategy).as(InputStream.class);
        try {
            for (InputStream archive : archives) {
                File path = IOUtils.readFromJarStream(archive);
                result.add(path);
            }
        } catch(IOException ex) {
            throw new ResolverException("I/O exception has occurred when reading dependency jars", ex);
        } finally {
            for(InputStream archive : archives) {
                if(archive != null) {
                    try {
                        archive.close();
                    }
                    catch(IOException ex) {}
                }
            }
        }

        return result;
    }

    /**
     * Resolves transitive dependencies of jar archives built with Maven using the provided resolution strategy
     * @param inputJar path to provided jar archive
     * @return list of resolved transitive dependencies
     * @throws ResolverException if there is no pom.xml file in provided jar archive, pom.xml file is invalid,
     * or error has occurred while retrieving transitive dependencies from repository
     */
    public List<File> resolve(File inputJar, MavenResolutionStrategy strategy) throws ResolverException {
        this.strategy = strategy;
        return resolve(inputJar);
    }

    private String getPomFile(File inputJar) throws IOException {

        Pattern pomFilePattern = Pattern.compile("pom[.]xml");
        ZipEntry zipEntry;
        try(JarInputStream jarInputStream = new JarInputStream(new FileInputStream(inputJar))) {
            while ((zipEntry = jarInputStream.getNextEntry()) != null) {
                Matcher pomFileMatcher = pomFilePattern.matcher(zipEntry.getName());
                if (pomFileMatcher.find()) {
                    break;
                }
            }
            if (zipEntry == null) {
                return NO_POM_FOUND_IN_JAR;
            }

            File pomTempFile = File.createTempFile("pom", ".xml");
            pomTempFile.deleteOnExit();

            byte[] buffer = new byte[1024];
            try (FileOutputStream pomOutputStream = new FileOutputStream(pomTempFile)) {
                int len;
                while ((len = jarInputStream.read(buffer)) > 0) {
                    pomOutputStream.write(buffer, 0, len);
                }
            }
            return pomTempFile.getAbsolutePath();
        }
    }


}

