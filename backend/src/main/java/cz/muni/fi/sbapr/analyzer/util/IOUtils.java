/**
 *  Copyright 2016 Viktoriia Bakalova
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and limitations under the License.
 */
package cz.muni.fi.sbapr.analyzer.util;

import java.io.*;

/**
 * Class containing static IO utility methods
 */
public final class IOUtils {

    private IOUtils() {}

    /**
     * Reads the contents of a specified file into a string
     * @param file file to be read into string
     * @return string with file contents
     * @throws IOException if file does not exist or cannot be opened
     */
    public static String readFileIntoString(File file) throws IOException {

        String output;
        try(BufferedReader br = new BufferedReader(new FileReader(file))){
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
            }
            output = sb.toString();
        }

        return output;
    }

    /**
     * Reads the contents of a jar stream into a temporary file
     * @param stream stream to read from
     * @return temporary file with the stream contents
     * @throws IOException if stream does not exist or cannot be opened
     */
    public static File readFromJarStream(InputStream stream) throws IOException {

        File tempJarFile = File.createTempFile("temp", ".jar");
        tempJarFile.deleteOnExit();
        BufferedInputStream reader = new BufferedInputStream(stream);
        try(BufferedOutputStream writer = new BufferedOutputStream(new FileOutputStream(tempJarFile))) {
            int read;
            byte[] buffer = new byte[1024];
            while ((read = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, read);
            }
        }

        return tempJarFile;
    }




}