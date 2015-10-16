/*
 * Copyright 2004,2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.wso2.maven.p2.feature.install;

import org.wso2.maven.p2.utils.BundleUtils;

/**
 * Bean class representing a feature.
 */
public class Feature {

    private String id;
    private String version;

    /**
     * Returns the feature id representing this feature. If the given id ends with feature, <i>.group</i> is appended.
     * If the given does not ends with <i>.feature.group</i>, then <i>.feature.group</i> is appended. This is
     * necessary for the director application to work.
     *
     * @return String
     */
    public String getId() {
        if (id != null && id.endsWith(".feature")) {
            return id + ".group";
        }
        if (id != null && !id.endsWith(".feature.group")) {
            return id + ".feature.group";
        }
        return id;
    }

    /**
     * Sets the feature id.
     *
     * @param id String
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Returns the version of this feature in OSGI format.
     * i.e: If the maven version for this feature is 4.2.0-SNAPSHOT, the OSGI representation is 4.2.0.SNAPSHOT.
     *
     * @return String
     */
    public String getVersion() {
        return BundleUtils.getOSGIVersion(version);
    }

    /**
     * Sets the version of this feature.
     *
     * @param version String
     */
    public void setVersion(String version) {
        this.version = version;
    }

}
