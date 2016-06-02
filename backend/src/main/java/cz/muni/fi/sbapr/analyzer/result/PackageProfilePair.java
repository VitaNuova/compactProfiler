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

public class PackageProfilePair {

    private String packageName;
    private CompactProfile profile;

    public PackageProfilePair(String packageName, CompactProfile profileName) {
        if(packageName == null) {
            throw new IllegalArgumentException("Package name cannot be null");
        }
        if(profileName == null) {
            throw new IllegalArgumentException("Profile name cannot be null");
        }
        this.packageName = packageName;
        this.profile = profileName;
    }

    public String getPackageName() {
        return packageName;
    }

    public CompactProfile getProfileName() {
        return profile;
    }

    @Override
    public String toString() {
        return "cz.muni.fi.sbapr.analyzer.result.PackageProfilePair{" +
                "packageName='" + packageName + '\'' +
                ", profile=" + profile +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PackageProfilePair)) return false;

        PackageProfilePair that = (PackageProfilePair) o;

        if (!packageName.equals(that.packageName)) return false;
        return profile == that.profile;

    }

    @Override
    public int hashCode() {
        int result = packageName.hashCode();
        result = 31 * result + profile.hashCode();
        return result;
    }
}
