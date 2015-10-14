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

import org.apache.maven.model.Resource;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.FileUtils;
import org.wso2.maven.p2.beans.CarbonArtifact;
import org.wso2.maven.p2.exceptions.CarbonArtifactNotFoundException;
import org.wso2.maven.p2.exceptions.OSGIInformationExtractionException;
import org.wso2.maven.p2.utils.DependencyResolver;
import org.wso2.maven.p2.utils.FileManagementUtil;
import org.wso2.maven.p2.utils.P2ApplicationLaunchManager;
import org.wso2.maven.p2.utils.P2Utils;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

/**
 * RepositoryGenerator takes parameters from the pom.xml and generates the repository.
 *
 * @since 2.0.0
 */
public class RepositoryGenerator {

    private final RepositoryResourceBundle resourceBundle;
    private final MavenProject project;

    private File tempDir;
    private File sourceDir;

    private File repoGenerationLocation;
    private File archiveFile;
    private File categoryDefinitionFile;

    private Log log;

    /**
     * The features and bundles publisher application (org.eclipse.equinox.p2.publisher.FeaturesAndBundlesPublisher) is
     * a command line application that is capable of generating metadata (p2 repositories) from pre-built Eclipse
     * bundles and features.
     */
    private static final String PUBLISHER_APPLICATION = "org.eclipse.equinox.p2.publisher.FeaturesAndBundlesPublisher";

    /**
     * The category publisher application (org.eclipse.equinox.p2.publisher.CategoryPublisher) is a command line
     * application that is capable of categorizing a set of Installable Units in a given repository.
     */
    private static final String CATEGORY_PUBLISHER_APPLICATION = "org.eclipse.equinox.p2.publisher.CategoryPublisher";

    private P2ApplicationLaunchManager p2LaunchManager;
    private HashMap<String, CarbonArtifact> dependentBundles;
    private HashMap<String, CarbonArtifact> dependentFeatures;

    public RepositoryGenerator(RepositoryResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        this.log = resourceBundle.getLog();
        this.project = this.resourceBundle.getProject();
        p2LaunchManager = new P2ApplicationLaunchManager(resourceBundle.getLauncher());
    }

    public void generate() throws MojoExecutionException, MojoFailureException {
        try {
            resolveDependencies();
            populateRequiredArtifactData();
            setupTempOutputFolderStructure();
            unzipFeaturesToOutputFolder();
            copyBundleArtifactsToOutputFolder();
            copyProjectResourcesToOutputFolder();
            generateRepository();
            updateRepositoryWithCategories();
            archiveGeneratedRepo();
            performMopUp();
        } catch (IOException | TransformerException | ParserConfigurationException e) {
            throw new MojoFailureException(e.getMessage(), e);
        } catch (OSGIInformationExtractionException | CarbonArtifactNotFoundException e) {
            throw new MojoExecutionException(e.getMessage(), e);
        }
    }

    private void resolveDependencies() throws IOException, OSGIInformationExtractionException {
        this.log.info("Inspecting maven dependencies.");
        List<HashMap<String, CarbonArtifact>> artifacts = DependencyResolver.getDependenciesForProject(project,
                resourceBundle.getRepositorySystem(), resourceBundle.getRemoteRepositories(),
                resourceBundle.getLocalRepository());
        dependentBundles = artifacts.get(0);
        dependentFeatures = artifacts.get(1);
    }

    private void populateRequiredArtifactData() throws CarbonArtifactNotFoundException {
        populateBundleDataFromCache();
        populateFeatureDataFromCache();
    }

    private void populateBundleDataFromCache() throws CarbonArtifactNotFoundException {
        for (Bundle bundle : resourceBundle.getBundleArtifacts()) {
            String key = bundle.getSymbolicName() + "_" + bundle.getVersion();
            CarbonArtifact artifact = dependentBundles.get(key);
            if (artifact == null) {
                throw new CarbonArtifactNotFoundException("Bundle not found");
            }
            artifact.copyTo(bundle);
        }
    }

    private void populateFeatureDataFromCache() throws CarbonArtifactNotFoundException {
        for (Feature feature : resourceBundle.getFeatureArtifacts()) {
            String key = feature.getId() + "_" + feature.getVersion();
            CarbonArtifact artifact = dependentFeatures.get(key);
            if (artifact == null) {
                throw new CarbonArtifactNotFoundException("Feature " + key + " not found");
            }
            artifact.copyTo(feature);
            feature.setId(artifact.getArtifactId());
        }
    }

    /**
     * Copy maven project resources located in the resources folder into mata repository.
     *
     * @throws IOException
     */
    private void copyProjectResourcesToOutputFolder() throws IOException {
        List<Resource> resources = project.getResources();
        if (resources != null) {
            this.log.info("Copying resources");
            for (Resource resource : resources) {
                try {
                    File resourceFolder = new File(resource.getDirectory());
                    if (resourceFolder.exists()) {
                        this.log.info("   " + resource.getDirectory());
                        FileManagementUtil.copyDirectory(resourceFolder, repoGenerationLocation);
                    }
                } catch (IOException e) {
                    throw new IOException("Unable copy resources: " + resource.getDirectory(), e);
                }
            }
        }
    }

    /**
     * Generate the repository by calling P2ApplicationLauncher.
     *
     * @throws MojoFailureException
     */
    private void generateRepository() throws MojoFailureException {
        this.log.info("Running Equinox P2 Publisher Application for Repository Generation");
        p2LaunchManager.setWorkingDirectory(project.getBasedir());
        p2LaunchManager.setApplicationName(PUBLISHER_APPLICATION);
        p2LaunchManager.addRepoGenerationArguments(sourceDir.getAbsolutePath(), resourceBundle.getRepository().
                toString(), getRepositoryName());
        p2LaunchManager.performAction(resourceBundle.getForkedProcessTimeoutInSeconds());
        this.log.info("Completed running Equinox P2 Publisher Application for Repository Generation");
    }

    /**
     * Unzip the given feature zip files into the output folder which will ultimately converted into P2 repo.
     *
     * @throws IOException
     */
    private void unzipFeaturesToOutputFolder() throws IOException {
        List<Feature> artifacts = resourceBundle.getFeatureArtifacts();
        for (Feature feature : artifacts) {
            try {
                this.log.info("Extracting feature " + feature.getGroupId() + ":" +
                        feature.getArtifactId());
                FileManagementUtil.unzip(feature.getArtifact().getFile(), sourceDir);
            } catch (IOException e) {
                throw new IOException("Error occurred when extracting the Feature Artifact: " +
                        feature.toString(), e);
            }
        }
    }

    /**
     * Copy artifacts into the repository folder.
     *
     * @throws IOException
     */
    private void copyBundleArtifactsToOutputFolder() throws IOException {
        List<Bundle> bundles = resourceBundle.getBundleArtifacts();
        if (bundles.size() > 0) {
            this.log.info("Copying bundle artifacts.");
        }
        File pluginsDir = new File(sourceDir, "plugins");
        for (Bundle bundleArtifact : bundles) {
            try {
                this.log.info("Copying bundle artifact:" + bundleArtifact.getSymbolicName());
                File file = bundleArtifact.getArtifact().getFile();
                FileManagementUtil.copy(file, new File(pluginsDir, file.getName()));
            } catch (IOException e) {
                throw new IOException("Error occurred when extracting the Feature Artifact: " +
                        bundleArtifact.toString(), e);
            }
        }
    }

    /**
     * Creates a zip archive from the generated repository and delete the repo.
     *
     * @throws MojoExecutionException
     */
    private void archiveGeneratedRepo() throws MojoExecutionException {
        if (resourceBundle.isArchive()) {
            this.log.info("Generating repository archive...");
            FileManagementUtil.zipFolder(repoGenerationLocation.toString(), archiveFile.toString(),
                    resourceBundle.getLog());
            this.log.info("Repository Archive: " + archiveFile.toString());
            try {
                FileManagementUtil.deleteDirectories(repoGenerationLocation);
            } catch (IOException e) {
                throw new MojoExecutionException("Failed to delete " + repoGenerationLocation.getAbsolutePath(), e);
            }
        }
    }

    private void setupTempOutputFolderStructure() throws IOException {
        try {
            File targetDir = new File(project.getBasedir(), "target");
            String timestampVal = String.valueOf((new Date()).getTime());
            tempDir = new File(targetDir, "tmp." + timestampVal);
            sourceDir = new File(tempDir, "featureExtract");
            if (!sourceDir.mkdirs()) {
                throw new IOException("Error occurred while creating output folder structure");
            }

            if (resourceBundle.getRepository() == null) {
                File repo = new File(targetDir, project.getArtifactId() + "_" + project.getVersion());
                resourceBundle.setRepository(repo.toURI().toURL());
            }

            repoGenerationLocation = new File(resourceBundle.getRepository().getFile().replace("/",
                    File.separator));
            archiveFile = new File(targetDir, project.getArtifactId() + "_" + project.getVersion() + ".zip");
            categoryDefinitionFile = File.createTempFile("equinox-p2", "category");
        } catch (IOException e) {
            throw new IOException("Error occurred while creating output folder structure", e);
        }
    }

    /**
     * Update the generated repository with categories.
     *
     * @throws MojoExecutionException
     */
    private void updateRepositoryWithCategories() throws TransformerException, ParserConfigurationException,
            MojoExecutionException, MojoFailureException {
        boolean isCategoriesAvailable = resourceBundle.getCategories() != null &&
                resourceBundle.getCategories().size() != 0;

        if (isCategoriesAvailable) {
            this.log.info("Running Equinox P2 Category Publisher Application for the Generated Repository");
            P2Utils.createCategoryFile(project, resourceBundle.getCategories(), categoryDefinitionFile);

            p2LaunchManager.setWorkingDirectory(project.getBasedir());
            p2LaunchManager.setApplicationName(CATEGORY_PUBLISHER_APPLICATION);
            p2LaunchManager.addUpdateRepoWithCategoryArguments(resourceBundle.getRepository().toString(),
                    categoryDefinitionFile.toURI().toString());

            p2LaunchManager.performAction(resourceBundle.getForkedProcessTimeoutInSeconds());
            this.log.info("Completed running Equinox P2 Category Publisher Application for the Generated Repository");
        }
    }

    /**
     * Delete the temporary folder.
     */
    private void performMopUp() {
        try {
            FileUtils.deleteDirectory(tempDir);
        } catch (Exception e) {
            this.log.warn(new MojoExecutionException("Unable complete mop up operation", e));
        }
    }

    public String getRepositoryName() {
        if (resourceBundle.getName() == null) {
            return project.getArtifactId();
        } else {
            return resourceBundle.getName();
        }
    }

}
