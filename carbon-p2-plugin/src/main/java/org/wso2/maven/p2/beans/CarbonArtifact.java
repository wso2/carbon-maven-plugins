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

package org.wso2.maven.p2.beans;

import org.apache.maven.artifact.Artifact;

/**
 * Bean class representing CarbonArtifacts.
 * <p/>
 * This class is extended by all the Bean classes which represent any artifact created from the carbon feature plugin.
 *
 * @since 2.0.0
 */
public class CarbonArtifact {
    private String groupId;
    private String artifactId;
    private String version;
    private String symbolicName;
    private String type;
    private String bundleVersion;
    private Artifact artifact;
    private String compatibility = "equivalent";

    /**
     * Returns the maven group id for the carbon artifact.
     *
     * @return String
     */
    public String getGroupId() {
        return groupId;
    }

    /**
     * Sets the maven group id for the carbon artifact.
     *
     * @param groupId String
     */
    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    /**
     * Returns the maven artifact id for the carbon artifact.
     *
     * @return String
     */
    public String getArtifactId() {
        return artifactId;
    }

    /**
     * Sets the maven artifact id for the carbon artifact.
     *
     * @param artifactId String
     */
    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * Returns the actual maven artifact represented by this carbon artifact
     *
     * @return org.apache.maven.artifact object
     */
    public Artifact getArtifact() {
        return artifact;
    }

    /**
     * Sets the actual maven artifact represented by this carbon artifact
     *
     * @param artifact org.apache.maven.artifact object
     */
    public void setArtifact(Artifact artifact) {
        this.artifact = artifact;
    }

    /**
     * Returns the OSGI symbolic name which is in the manifest file of the artifact represented by this
     * carbon artifact. This is applicable only if the represented carbon artifact is an OSGI bundle.
     *
     * @return String symbolic name
     */
    public String getSymbolicName() {
        return symbolicName;
    }

    /**
     * Sets the OSGI symbolic name which is in the manifest file of the artifact represented by this
     * carbon artifact. This is applicable only if the represented carbon artifact is an OSGI bundle.
     *
     * @param symbolicName String
     */
    public void setSymbolicName(String symbolicName) {
        this.symbolicName = symbolicName;
    }

    /**
     * Returns the file type of the actual artifact represented by this carbon artifact.
     * i.e: Jar, Zip
     *
     * @return String file extension
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the file type(extension) of the actual artifact represented by this carbon artifact.
     *
     * @param type String
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Returns the OSGI bundle version of the actual artifact represented by this carbon artifact.
     * This is applicable only if the carbon artifact represent an OSGI bundle.
     *
     * @return String
     */
    public String getBundleVersion() {
        return bundleVersion;
    }

    /**
     * Sets the OSGI bundle version of the actual artifact represented by this carbon artifact.
     * This is applicable only if the carbon artifact represent an OSGI bundle.
     *
     * @param bundleVersion String
     */
    public void setBundleVersion(String bundleVersion) {
        this.bundleVersion = bundleVersion;
    }

    /**
     * Returns the compatibility type of this CarbonArtifact.
     * Permitted values are;
     * <ul>
     * <li>perfect</li>
     * <li>equivalent</li>
     * <li>compatible</li>
     * <li>greaterOrEqual</li>
     * <li>patch</li>
     * <li>optional</li>
     * </ul>
     *
     * @return String compatibility
     */
    public String getCompatibility() {
        return compatibility;
    }

    public void setCompatibility(String compatibility) {
        this.compatibility = compatibility;
    }

    /*
     * Though this is not usage for this method from the code, do not delete this as this is used by maven
     * context to inject match.
     *
     */
    public void setMatch(String match) {
        this.compatibility = match;
    }

    /**
     * Util method which will copy some of the properties of this parent class into it's sub classes.
     *
     * @param item target Subclass of CarbonArtifact object which needs to copy fields of this object.
     * @param <T>  SubClass of CarbonArtifact
     */
    public <T extends CarbonArtifact> void copyTo(T item) {
        item.setArtifact(this.artifact);
        item.setVersion(this.version);
        item.setArtifactId(this.artifactId);
        item.setBundleVersion(this.bundleVersion);
        item.setGroupId(this.groupId);
        item.setSymbolicName(this.symbolicName);
        item.setType(this.type);
    }
}
