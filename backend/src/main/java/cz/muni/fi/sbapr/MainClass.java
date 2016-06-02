/**
 *  Copyright 2016 Viktoriia Bakalova
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and limitations under the License.
 */
package cz.muni.fi.sbapr;

import cz.muni.fi.sbapr.analyzer.CompactProfileAnalyzerImpl;
import cz.muni.fi.sbapr.enums.CompactProfile;
import cz.muni.fi.sbapr.enums.ResolutionType;
import cz.muni.fi.sbapr.exceptions.AnalyzerException;
import java.io.File;


public class MainClass {

    /**
     * Sample client console application
     * @param args arg0 (required) - path to jar archive,
     *             arg1 (optional, 'maven' or 'classpath') - desired resolution type, defaults to 'maven'
     *             arg2 (optional, 'compact1', 'compact2', 'compact3' or 'fulljre') - desired compact profile, defaults to 'compact1'
     */
    public static void main(String[] args) {

        if(args.length < 1 || args.length > 3) {
            printUsage();
            System.exit(1);
        }

        ResolutionType desiredResolution = ResolutionType.MAVEN;
        CompactProfile desiredProfile = CompactProfile.COMPACT1;
        if(args.length > 1) {
            if(args[1].substring(0, args[1].indexOf('=')).equals("-resolution")) {
                switch (args[1].substring(args[1].indexOf('=') + 1, args[1].length())) {
                    case "maven":
                        desiredResolution = ResolutionType.MAVEN;
                        break;
                    case "classpath":
                        desiredResolution = ResolutionType.CLASSPATH;
                        break;
                    default: printUsage(); System.exit(1);
                }
                if(args.length == 3) {
                    if (!args[2].substring(0, args[2].indexOf('=')).equals("-profile")) {
                        printUsage();
                        System.exit(1);
                    }
                    switch (args[2].substring(args[2].indexOf('=') + 1, args[2].length())) {
                        case "compact1": desiredProfile = CompactProfile.COMPACT1; break;
                        case "compact2": desiredProfile = CompactProfile.COMPACT2; break;
                        case "compact3": desiredProfile = CompactProfile.COMPACT3; break;
                        case "fulljre": desiredProfile = CompactProfile.FULL_JRE; break;
                        default: printUsage(); System.exit(1);
                    }
                }
            }
            else if(args[1].substring(0, args[1].indexOf('=')).equals("-profile")) {
                switch (args[1].substring(args[1].indexOf('=') + 1, args[1].length())) {
                    case "compact1": desiredProfile = CompactProfile.COMPACT1; break;
                    case "compact2": desiredProfile = CompactProfile.COMPACT2; break;
                    case "compact3": desiredProfile = CompactProfile.COMPACT3; break;
                    case "fulljre": desiredProfile = CompactProfile.FULL_JRE; break;
                    default: printUsage(); System.exit(1);
                }
                if(args.length == 3) {
                    if (!args[2].substring(0, args[2].indexOf('=')).equals("-resolution")) {
                        printUsage();
                        System.exit(1);
                    }
                    switch (args[2].substring(args[2].indexOf('=') + 1, args[2].length())) {
                        case "maven":
                            desiredResolution = ResolutionType.MAVEN;
                            break;
                        case "classpath":
                            desiredResolution = ResolutionType.CLASSPATH;
                            break;
                        default: printUsage(); System.exit(1);
                    }
                }
            }
            else {
                printUsage();
                System.exit(1);
            }
        }
        try {
            String result = CompactProfileAnalyzerImpl.ANALYZER.analyze(new File(args[0]), desiredResolution, desiredProfile).toString();
            System.out.println(result);
        } catch(AnalyzerException ex) {
            System.out.println("Application failed with exception: " + ex);
        }
    }

    private static void printUsage() {
        System.out.println("Usage: <path_to_jar> -resolution=<resolution_type> -profile=<required_profile>");
        System.out.println("<path_to_jar>: required, path to the analyzed jar archive");
        System.out.println("<resolution_type>: 'maven' or 'classpath', optional, default is maven");
        System.out.println("<required_profile>: 'compact1', 'compact2', 'compact3', 'fulljre', optional, default is compact1");
    }
}
