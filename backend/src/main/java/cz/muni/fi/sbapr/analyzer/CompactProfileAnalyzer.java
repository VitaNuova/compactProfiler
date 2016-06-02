/**
 *  Copyright 2016 Viktoriia Bakalova
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and limitations under the License.
 */

package cz.muni.fi.sbapr.analyzer;

import cz.muni.fi.sbapr.analyzer.result.AnalysisResult;
import cz.muni.fi.sbapr.enums.CompactProfile;
import cz.muni.fi.sbapr.enums.ResolutionType;
import cz.muni.fi.sbapr.exceptions.AnalyzerException;

import java.io.File;

public interface CompactProfileAnalyzer {

    /**
     * Analyzes jar archive profile requirements based on the desired compact profile and desired type of resolution
     * @param inputJar jar archive to analyze
     * @param resolutionType desired type of resolution (defaults to classpath resolution)
     * @param desiredProfile desired compact profile (defaults to compact1 profile)
     * @return result of analysis (minimal compact profile required to run, lists of packages within and outside the desired profile)
     * @throws AnalyzerException in case invalid jar is provided
     */
    AnalysisResult analyze(File inputJar, ResolutionType resolutionType, CompactProfile desiredProfile) throws AnalyzerException;
}
