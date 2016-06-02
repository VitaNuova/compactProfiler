/**
 *  Copyright 2016 Viktoriia Bakalova
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and limitations under the License.
 */
package cz.muni.fi.sbapr.resolver.strategy;

import org.jboss.shrinkwrap.resolver.api.maven.filter.MavenResolutionFilter;
import org.jboss.shrinkwrap.resolver.api.maven.strategy.MavenResolutionStrategy;
import org.jboss.shrinkwrap.resolver.api.maven.strategy.TransitiveExclusionPolicy;
import org.jboss.shrinkwrap.resolver.api.maven.strategy.TransitiveStrategy;

/**
 * Custom implementation of MavenResolutionStrategy from shrinkwrap resolver library.
 * Mimics the behavior of TransitiveStrategy but replaces exclusion policy to not exclude any
 * artifacts from dependency resolution
 */
public enum NonFilteringTransitiveStrategy implements MavenResolutionStrategy {

    INSTANCE;

    /**
     * Allows for transitive resolution of Maven artifact dependencies,
     * uses same resolution filters as TransitiveStrategy from shrinkwrap resolver
     * @return array of resolution filters
     */
    public MavenResolutionFilter[] getResolutionFilters() {
        return TransitiveStrategy.INSTANCE.getResolutionFilters();
    }


    /**
     * Allows for resolution of all dependencies, including optional and dependencies with test and provided scopes
     * @return instance of exclusion policy used
     */
    public TransitiveExclusionPolicy getTransitiveExclusionPolicy() {
        return NonExclusivePolicy.INSTANCE;
    }
}
