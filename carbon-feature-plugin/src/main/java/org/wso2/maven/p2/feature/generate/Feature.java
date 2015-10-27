/*
 *  Copyright (c) 2015, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.wso2.maven.p2.feature.generate;

import org.wso2.maven.p2.beans.CarbonArtifact;
import org.wso2.maven.p2.utils.BundleUtils;

/**
 * Bean class representing a Feature object provided as an input param to FeatureGenMojo.
 *
 * @since 2.0.0
 */
public class Feature extends CarbonArtifact {
    private String id;
    private boolean optional;

    /**
     * Returns the feature id represented by this Feature.
     * <p>
     * If the name of the feature id ends with '.feature', then '.feature' word is removed and the result is
     * returned.
     *
     * @return String feature id.
     */
    public String getId() {
        if (id != null && id.endsWith(".feature")) {
            return id.substring(0, id.lastIndexOf(".feature"));
        }
        return id;
    }

    /**
     * Sets the feature version for the given feature.
     *
     * @param id String
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Returns whether this particular feature is optional or not. This is considered when generating the feature.xml
     *
     * @return true if this feature is optional
     */
    public boolean isOptional() {
        return this.optional;
    }

    /**
     * Sets whether this feature is optional or not. This is applicable only to included features.
     * <p>
     * Note: There is no usage of this method in the plugin code. However, this is used by plexus container to inject
     * the value.
     *
     * @param optional boolean
     */
    public void setOptional(boolean optional) {
        this.optional = optional;
    }

    /**
     * Returns the Feature version in OSGI format.
     * <p>
     * i.e: If the maven version for this artifact is 4.2.0-SNAPSHOT, this will return 4.2.0.SNAPSHOT
     *
     * @return String
     */
    public String getFeatureVersion() {
        return BundleUtils.getOSGIVersion(super.getVersion());
    }
}
