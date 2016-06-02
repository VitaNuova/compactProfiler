/**
 *  Copyright 2016 Viktoriia Bakalova
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and limitations under the License.
 */
package cz.muni.fi.sbapr.analyzer.validator;

import java.io.File;
import java.util.zip.ZipFile;

public enum BasicValidator implements JarValidator {

    VALIDATOR;

    /**
     * Checks if provided path is a valid path to a jar archive based on file extension and on trying to open it as a zip file for reading
     * @param path path to a jar archive
     * @return true if file extension is .jar, and the file can be opened as a zip file for reading
     */
    public boolean validate(File path) {

        if(path == null || !path.exists()) {
           return false;
        }

        int extensionIndex = path.getAbsolutePath().lastIndexOf('.');
        if(extensionIndex == -1) {
            return false;
        }

        String extension = path.getAbsolutePath().substring(extensionIndex);
        if(!extension.equals(".jar")) {
            return false;
        }

        try {
            new ZipFile(path);
        }
        catch(Exception ex) {
            return false;
        }

        return true;
    }

}
