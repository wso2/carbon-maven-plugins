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

package org.wso2.maven.p2.utils;

import org.apache.maven.plugin.MojoFailureException;
import org.eclipse.sisu.equinox.launching.internal.P2ApplicationLauncher;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;

/**
 * Wrapper class containing P2ApplicationLauncher which makes configuring the P2ApplicationLauncher easier.
 *
 * @since 2.0.0
 */
public class P2ApplicationLaunchManager {

    private final P2ApplicationLauncher launcher;

    /**
     * The constructor which initializes this class. This class wraps the P2ApplicationLauncher and expose a set
     * of simple, easy to use methods to configure the P2ApplicationLauncher.
     *
     * @param launcher P2ApplicationLauncher which will be wrapped by this wrapper class
     */
    public P2ApplicationLaunchManager(P2ApplicationLauncher launcher) {
        this.launcher = launcher;
    }

    /**
     * Sets the working directory of P2Applilcation launcher instance.
     *
     * @param workingDir File object pointing the directory
     */
    public void setWorkingDirectory(File workingDir) {
        this.launcher.setWorkingDirectory(workingDir);
    }

    /**
     * Sets the application name.
     *
     * @param applicationName name of the application
     */
    public void setApplicationName(String applicationName) {
        this.launcher.setApplicationName(applicationName);
    }

    /**
     * Sets the P2ApplicationLauncher's arguments to install features.
     *
     * @param repositoryLocation a comma separated list of metadata repository(or artifact repository as in p2 both
     *                           metadata and artifacts resides in the same repo) URLs where the software to be
     *                           installed can be found.
     * @param installIUs         a comma separated list of IUs to install. Each entry in the list is in the form
     *                           {@code <id> [ '/' <version> ]}. If you are looking to install a feature, the identifier
     *                           of the feature has to be suffixed with ".feature.group".
     * @param destination        the path of a folder in which the targeted product is located.
     * @param profile            the profile id containing the description of the targeted product. This ID is
     *                           defined by the eclipse.p2.profile property contained in the config.ini of the
     *                           targeted product.
     */
    public void addArgumentsToInstallFeatures(String repositoryLocation,
                                              String installIUs, String destination, String profile) {

        launcher.addArguments(
                "-metadataRepository", repositoryLocation,
                "-artifactRepository", repositoryLocation,
                "-profileProperties", "org.eclipse.update.install.features=true",
                "-installIU", installIUs,
                "-bundlepool", Paths.get(destination, "lib").toString(),
                //to support shared installation in carbon
                "-shared", Paths.get(destination, "lib", "p2").toString(),
                //target is set to a separate directory per Profile
                "-destination", Paths.get(destination, profile).toString(),
                "-profile", profile,
                "-roaming");
    }

    /**
     * Sets the P2ApplicationLauncher's arguments to uninstall features.
     *
     * @param uninstallIUs a comma separated list of IUs to uninstall. Each entry in the list is in the form
     *                     {@code <id> [ '/' <version> ]}. If you are looking to uninstall a feature, the identifier
     *                     of the feature has to be suffixed with ".feature.group".
     * @param destination  the path of a folder in which the targeted product is located
     * @param profile      the profile id containing the description of the targeted product
     */
    public void addArgumentsToUnInstallFeatures(String uninstallIUs, String destination,
                                                String profile) {
        launcher.addArguments(
                "-profileProperties", "org.eclipse.update.install.features=false",
                // a comma separated list of IUs to uninstall. Each entry in the list is in the form
                // <id> [ '/' <version> ]
                "-uninstallIU", uninstallIUs,
                // the location of where the plug-ins and features will be stored. This value is only taken into account
                // when a new profile is created. For an application where all the bundles are located into the
                // plugins/ folder of the destination, set it to <destination>
                // to support shared installation in carbon
                "-shared", Paths.get(destination, "lib", "p2").toString(),
                "-destination", Paths.get(destination, profile).toString(),
                "-profile", profile
        );
    }

    /**
     * Sets the P2ApplicationLauncher's arguments to generate P2 repository. For this scenario both metadata repository
     * and artifact repository are same.
     *
     * @param sourceDir            the location of the update site
     * @param metadataRepoLocation the URI to the metadata repository where the installable units should be published
     * @param repositoryName       name of the artifact repository where the artifacts should be published
     */
    public void addRepoGenerationArguments(String sourceDir, String metadataRepoLocation,
                                           String repositoryName) {
        launcher.addArguments("-source", sourceDir,
                "-metadataRepository", metadataRepoLocation,
                "-metadataRepositoryName", repositoryName,
                "-artifactRepository", metadataRepoLocation,
                "-artifactRepositoryName", repositoryName,
                "-publishArtifacts",
                "-publishArtifactRepository",
                "-compress",
                "-append");
    }

    /**
     * Sets the P2ApplicationLauncher's arguments and configure it to categorizing a set of Installable Units in a given
     * repository.
     *
     * @param metadataRepositoryLocation a comma separated list of metadata repository URLs where the software to be
     *                                   installed can be found.
     * @param categoryDefinitionFile     The category file which drives the categorization of installable units in the
     *                                   repository
     */
    public void addUpdateRepoWithCategoryArguments(String metadataRepositoryLocation, String categoryDefinitionFile) {
        launcher.addArguments("-metadataRepository", metadataRepositoryLocation,
                "-categoryDefinition", categoryDefinitionFile,
                "-categoryQualifier",
                "-compress",
                "-append");

    }

    /**
     * Sets the P2ApplicationLauncher's arguments and configure it to publish a give product.
     *
     * @param repositoryURL            the URL to the metadata repository where the product should be published
     * @param productConfigurationFile File object pointing the .product file
     * @param executable               location of the equinox executable jar
     * @throws IOException throws when unable to get the canonical path from the product configuration file
     */
    public void addPublishProductArguments(URL repositoryURL, File productConfigurationFile, String executable)
            throws IOException {
        launcher.addArguments(
                "-metadataRepository", repositoryURL.toString(),
                "-artifactRepository", repositoryURL.toString(),
                "-productFile", productConfigurationFile.getCanonicalPath(),
                "-executables", executable,
                "-publishArtifacts",
                "-configs", "gtk.linux.x86",
                "-flavor", "tooling",
                "-append");

    }

    /**
     * Sets the P2ApplicationLauncher's arguments and configure it to generate a profile.
     *
     * @param repositoryURL the URL to the metadata repository where the product should be published
     * @param id            product id taken from .product file
     * @param profile       name of the new profile
     * @param targetPath    location of the components directory of the carbon distribution
     */
    public void addGenerateProfileArguments(URL repositoryURL, String id, String profile, URL targetPath) {
        launcher.addArguments(
                "-metadataRepository", repositoryURL.toExternalForm(),
                "-artifactRepository", repositoryURL.toExternalForm(),
                "-installIU", id,
                "-profileProperties", "org.eclipse.update.install.features=true",
                "-profile", profile,
                "-bundlepool", targetPath.toExternalForm() + File.separator + "lib",
                //to support shared installation in carbon
                "-shared", targetPath.toExternalForm() + File.separator + "lib" + File.separator + "p2",
                //target is set to a separate directory per Profile
                "-destination", targetPath.toExternalForm() + File.separator + profile,
                "-p2.os", "linux",
                "-p2.ws", "gtk",
                "-p2.arch", "x86",
                "-roaming"
        );
    }

    /**
     * Generate/update the repository.
     *
     * @param forkedProcessTimeoutInSeconds int
     * @throws MojoFailureException throws when unable to perform the p2 activity
     */
    public void performAction(int forkedProcessTimeoutInSeconds) throws MojoFailureException {
        int result = launcher.execute(forkedProcessTimeoutInSeconds);
        if (result != 0) {
            throw new MojoFailureException("P2 publisher return code was " + result);
        }
    }

}
