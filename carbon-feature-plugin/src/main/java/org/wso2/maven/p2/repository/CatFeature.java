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
package org.wso2.maven.p2.repository;


import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.wso2.maven.p2.utils.BundleUtils;

import java.util.Properties;
import java.util.regex.Pattern;

/**
 * Bean class representing a CatFeature object provided as an input param to RepositoryGeneratorMojo.
 *
 * @since 2.0.0
 */
public class CatFeature {

    /**
     * Id of the feature.
     *
     * @parameter
     * @required
     */
    private String id;

    /**
     * Version of the feature.
     *
     * @parameter
     * @required
     */
    private String version;

    /**
     * @parameter default-value="${project}"
     */
    private MavenProject project;

    private boolean versionReplaced = false;

    public CatFeature() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVersion() throws MojoExecutionException {
        if (!versionReplaced) {
            replaceProjectKeysInVersion(project);
        }
        return BundleUtils.getOSGIVersion(version);
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setProject(MavenProject project) {
        this.project = project;
    }

    public void replaceProjectKeysInVersion(MavenProject project) throws MojoExecutionException {
        if (version == null) {
            throw new MojoExecutionException("Could not find the version for featureId: " + getId());
        }
        Properties properties = project.getProperties();
        properties.forEach((key, value) -> {
            version = version.replaceAll(Pattern.quote("${" + key + "}"), value.toString());
        });
        versionReplaced = true;
    }

}
