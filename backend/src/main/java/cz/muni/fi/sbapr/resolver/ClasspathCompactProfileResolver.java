/**
 *  Copyright 2016 Viktoriia Bakalova
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and limitations under the License.
 */
package cz.muni.fi.sbapr.resolver;

import cz.muni.fi.sbapr.exceptions.ResolverException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.JarInputStream;
import java.util.jar.Manifest;


public enum ClasspathCompactProfileResolver implements CompactProfileResolver {

    RESOLVER;

    public static final String NO_MANIFEST_FOUND_IN_JAR = "No manifest file in the given jar archive";
    public static final String NO_CLASSPATH_ENTRY = "No classpath specified in the jar manifest";

    /**
     * Resolves transitive dependencies based on Class-Path entry in the MANIFEST.MF file of provided jar archive
     * @param inputJar path to provided jar archive
     * @return list of transitively dependent jar archives
     * @throws ResolverException if there is no manifest file in jar archive, or jar archive is otherwise not valid
     */
    @Override
    public List<File> resolve(File inputJar) throws ResolverException {

        List<File> result = new ArrayList<>();
        try {
            String classpath = getClassPath(inputJar);
            if(classpath.equals(NO_MANIFEST_FOUND_IN_JAR)) {
                throw new ResolverException("No manifest file in jar archive");
            }
            if(classpath.equals(NO_CLASSPATH_ENTRY)) {
                return result;
            }
            String[] classpathEntries = classpath.split(" ");
            String inputPath = inputJar.getParentFile().getAbsolutePath();
            for(String dependency : classpathEntries) {
                String dependencyPath = inputPath + File.separator + dependency;
                result.add(new File(dependencyPath));
                result.addAll(resolve(new File(dependencyPath)));
            }
        } catch(IOException ex) {
            throw new ResolverException("I/O exception while processing jar archive", ex);
        }

        return result;
    }

    private String getClassPath(File inputJar) throws IOException {

        try (JarInputStream jarInputStream = new JarInputStream(new FileInputStream(inputJar))) {
            Manifest manifest = jarInputStream.getManifest();

            if (manifest == null) {
                return NO_MANIFEST_FOUND_IN_JAR;
            }

            Attributes attributes = manifest.getMainAttributes();

            String classpath = attributes.getValue("Class-Path");
            if(classpath == null) {
                return NO_CLASSPATH_ENTRY;
            }

            return classpath;
        }
    }





}
