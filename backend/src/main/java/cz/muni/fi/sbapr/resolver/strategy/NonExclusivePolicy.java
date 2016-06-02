/**
 *  Copyright 2016 Viktoriia Bakalova
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and limitations under the License.
 */
package cz.muni.fi.sbapr.resolver.strategy;

import org.jboss.shrinkwrap.resolver.api.maven.ScopeType;
import org.jboss.shrinkwrap.resolver.api.maven.strategy.TransitiveExclusionPolicy;

/**
 * Custom implementation of TransitiveExclusionPolicy from shrinkwrap resolver library
 * that allows optional dependencies and does not filter out dependencies with test and
 * provided scopes
 */
public enum NonExclusivePolicy implements TransitiveExclusionPolicy {

    INSTANCE;

    /**
     * Method that determines whether optional dependencies are allowed or skipped during dependency resolution
     * @return true to allow optional dependencies
     */
    public boolean allowOptional() {
        return true;
    }

    /**
     * Method that determines which dependencies are skipped during resolution based on scopes
     * @return empty array to allow dependencies with any scope, including test and provided
     */
    public ScopeType[] getFilteredScopes() {
        return new ScopeType[]{};
    }
}
