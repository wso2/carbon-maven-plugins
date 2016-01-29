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

import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectHelper;
import org.apache.maven.repository.RepositorySystem;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Bean class containing all the parameters entered to the mojo through plugin configuration. The purpose of this class
 * is to make any configuration property accessible from any class by simply passing this bean as a parameter.
 *
 * @since 2.0.0
 */
public class FeatureResourceBundle {

    private String id;
    private String version;
    private String label;
    private String description;
    private String providerName;
    private String copyright;
    private String licenceUrl;
    private String licence;
    private File manifest;
    private File propertiesFile;
    private Properties properties;
    private List<Bundle> bundles;
    private List<Feature> importFeatures;
    private List<Feature> includedFeatures;
    private List<Advice> adviceFileContent;

    private RepositorySystem repositorySystem;
    private MavenProject project;
    private ArtifactRepository localRepository;
    private MavenProjectHelper projectHelper;
    private List<ArtifactRepository> remoteRepositories;

    private Log log;

    /**
     * Returns the feature id being created. If the id ends with the text "feature" then that text is removed
     * and return the rest of the id.
     *
     * @return {@code String}
     */
    public String getId() {
        if (id != null && id.endsWith(".feature")) {
            return id.substring(0, id.indexOf(".feature"));
        }
        return id;
    }

    /**
     * Sets the id of the feature being created.
     *
     * @param id {@code String}
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Returns the version of the feature being created.
     *
     * @return {@code String}
     */
    public String getVersion() {
        return version;
    }

    /**
     * Sets the version of the feature being created.
     *
     * @param version {@code String}
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * Returns the label of the feature.
     *
     * @return {@code String}
     */
    public String getLabel() {
        return label;
    }

    /**
     * Sets the label of the feature.
     *
     * @param label {@code String}
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * Returns the description of the feature.
     *
     * @return {@code String}
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the feature.
     *
     * @param description {@code String}
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Returns the provider name for the feature.
     *
     * @return {@code String}
     */
    public String getProviderName() {
        return providerName;
    }

    /**
     * Sets the provider name of the feature.
     *
     * @param providerName {@code String}
     */
    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    /**
     * Returns the copyright of the feature.
     *
     * @return {@code String}
     */
    public String getCopyright() {
        return copyright;
    }

    /**
     * Sets the copyright of the feature.
     *
     * @param copyright {@code String}
     */
    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    /**
     * Returns the licensce url of the feature.
     *
     * @return {@code String}
     */
    public String getLicenceUrl() {
        return licenceUrl;
    }

    /**
     * Sets the license url of the feature.
     *
     * @param licenceUrl {@code String}
     */
    public void setLicenceUrl(String licenceUrl) {
        this.licenceUrl = licenceUrl;
    }

    /**
     * Returns the license text.
     *
     * @return {@code String}
     */
    public String getLicence() {
        return licence;
    }

    /**
     * Sets the license text.
     *
     * @param licence {@code String}
     */
    public void setLicence(String licence) {
        this.licence = licence;
    }

    /**
     * Returns the manifest file of the feature.
     *
     * @return {@link File}
     */
    public File getManifest() {
        return manifest;
    }

    /**
     * Sets the manifest file of the feature.
     *
     * @param manifest {@link File}
     */
    public void setManifest(File manifest) {
        this.manifest = manifest;
    }

    /**
     * Retnrs the property file of the feature.
     *
     * @return {@link File}
     */
    public File getPropertyFile() {
        return propertiesFile;
    }

    /**
     * Sets the property file of the feature.
     *
     * @param propertiesFile {@link File}
     */
    public void setPropertyFile(File propertiesFile) {
        this.propertiesFile = propertiesFile;
    }

    /**
     * Returns the properties specified for the feature.
     *
     * @return {@link Properties}
     */
    public Properties getProperties() {
        return properties;
    }

    /**
     * Sets the properties for the feature.
     *
     * @param properties {@link Properties}
     */
    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    /**
     * Sets the bundles list to be included in the feature.
     *
     * @param bundles {@code List<Bundle>}
     */
    public void setBundles(List<Bundle> bundles) {
        this.bundles = bundles;
    }

    /**
     * Returns the bundles list included in the feature.
     *
     * @return {@code List<Bundle>}
     */
    public List<Bundle> getBundles() {
        if (this.bundles == null) {
            return new ArrayList<>();
        }
        return this.bundles;
    }

    /**
     * Returns the list of features that this feature depends on.
     *
     * @return {@code List<Feature>}
     */
    public List<Feature> getImportFeatures() {
        return importFeatures;
    }

    /**
     * Sets the list of features that this feature depends on.
     *
     * @param importFeatures {@code List<Feature>}
     */
    public void setImportFeatures(List<Feature> importFeatures) {
        this.importFeatures = importFeatures;
    }

    /**
     * Returns the list of features that is included in this feature.
     *
     * @return {@code List<Feature>}
     */
    public List<Feature> getIncludeFeatures() {
        if (includedFeatures == null) {
            return new ArrayList<>();
        }
        return includedFeatures;
    }

    /**
     * Sets the list of features that is to be included in this feature.
     *
     * @param includedFeatures {@code List<Feature>}
     */
    public void setIncludeFeatures(List<Feature> includedFeatures) {
        this.includedFeatures = includedFeatures;
    }

    /**
     * Returns the list of advice files.
     *
     * @return {@code List<Advice>}
     */
    public List<Advice> getAdviceFileContent() {
        return adviceFileContent;
    }

    /**
     * Sets the list of advice files
     *
     * @param adviceFileContent {@code List<Advice>}
     */
    public void setAdviceFileContent(List<Advice> adviceFileContent) {
        this.adviceFileContent = adviceFileContent;
    }

    /**
     * Returns the RepositorySystem injected by the maven runtime.
     *
     * @return {@link RepositorySystem}
     */
    public RepositorySystem getRepositorySystem() {
        return repositorySystem;
    }

    /**
     * Sets the RepositorySystem injected by the maven runtime.
     *
     * @param repositorySystem {@link RepositorySystem}
     */
    public void setRepositorySystem(RepositorySystem repositorySystem) {
        this.repositorySystem = repositorySystem;
    }

    /**
     * Returns the LocalRepository injected by the maven runtime.
     *
     * @return {@link ArtifactRepository}
     */
    public ArtifactRepository getLocalRepository() {
        return localRepository;
    }

    /**
     * Sets the LocalRepository injected by the maven runtime.
     *
     * @param localRepository {@link ArtifactRepository}
     */
    public void setLocalRepository(ArtifactRepository localRepository) {
        this.localRepository = localRepository;
    }

    /**
     * Returns the remote repositories injected by the maven runtime.
     *
     * @return {@link ArtifactRepository}
     */
    public List<ArtifactRepository> getRemoteRepositories() {
        return remoteRepositories;
    }

    /**
     * Sets the remote repositories injected by the maven runtime.
     *
     * @param remoteRepositories {@link ArtifactRepository}
     */
    public void setRemoteRepositories(List<ArtifactRepository> remoteRepositories) {
        this.remoteRepositories = remoteRepositories;
    }

    /**
     * Returns the maven project injected by the maven runtime.
     *
     * @return {@link MavenProject}
     */
    public MavenProject getProject() {
        return project;
    }

    /**
     * Sets the maven project injected by the maven runtime.
     *
     * @param project {@link MavenProject}
     */
    public void setProject(MavenProject project) {
        this.project = project;
    }

    /**
     * Returns the maven project helper injected by the maven runtime.
     *
     * @return {@link MavenProjectHelper}
     */
    public MavenProjectHelper getProjectHelper() {
        return projectHelper;
    }

    /**
     * Sets the maven project helper injected by the maven runtime.
     *
     * @param projectHelper {@link MavenProjectHelper}
     */
    public void setProjectHelper(MavenProjectHelper projectHelper) {
        this.projectHelper = projectHelper;
    }

    /**
     * Sets the Log.
     *
     * @param logger {@link Log}
     */
    public void setLog(Log logger) {
        this.log = logger;
    }

    /**
     * Returns the Log.
     *
     * @return {@link Log}
     */
    public Log getLog() {
        return this.log;
    }

    /**
     * Returns the File object representing the feature.properties file expected to reside in src/main/resource
     * directory of the maven project.
     *
     * @return {@link File}
     */
    public File getPropertyFileInResourceDir() {
        Path baseDirPath = getProject().getBasedir().toPath();
        return baseDirPath.resolve(Paths.get("src", "main", "resources", "feature.properties")).toFile();
    }
}
