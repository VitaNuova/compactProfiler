/**
 *  Copyright 2016 Viktoriia Bakalova
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and limitations under the License.
 */

package cz.muni.fi.sbapr.analyzer.result;

import cz.muni.fi.sbapr.enums.CompactProfile;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class AnalysisResult {

    private CompactProfile minimalProfile;
    private Set<PackageProfilePair> inDesiredProfile = new HashSet<>();
    private Set<PackageProfilePair> outsideDesiredProfile = new HashSet<>();

    public AnalysisResult(CompactProfile minimalProfile, Set<PackageProfilePair> inDesiredProfile, Set<PackageProfilePair> outsideDesiredProfile) {
        if(minimalProfile == null) {
            throw new IllegalArgumentException("Minimal profile in the result cannot be null");
        }
        this.minimalProfile = minimalProfile;
        if(inDesiredProfile != null) {
            this.inDesiredProfile.addAll(inDesiredProfile);
        }
        if(outsideDesiredProfile != null) {
            this.outsideDesiredProfile.addAll(outsideDesiredProfile);
        }
    }

    public CompactProfile getMinimalProfile() {
        return minimalProfile;
    }

    public Set<PackageProfilePair> getInDesiredProfile() {
        return Collections.unmodifiableSet(inDesiredProfile);
    }

    public Set<PackageProfilePair> getOutsideDesiredProfile() {
        return Collections.unmodifiableSet(outsideDesiredProfile);
    }

    @Override
    public String toString() {
        return "AnalysisResult{" +
                "minimalProfile=" + minimalProfile +
                ", inDesiredProfile=" + inDesiredProfile +
                ", outsideDesiredProfile=" + outsideDesiredProfile +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AnalysisResult)) return false;

        AnalysisResult that = (AnalysisResult) o;

        if (minimalProfile != that.minimalProfile) return false;
        if (!inDesiredProfile.equals(that.inDesiredProfile)) return false;
        return outsideDesiredProfile.equals(that.outsideDesiredProfile);

    }

    @Override
    public int hashCode() {
        int result = minimalProfile.hashCode();
        result = 31 * result + inDesiredProfile.hashCode();
        result = 31 * result + outsideDesiredProfile.hashCode();
        return result;
    }
}
