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

package org.wso2.maven.p2.feature.install;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.wso2.maven.p2.utils.FileManagementUtil;
import org.wso2.maven.p2.utils.P2Constants;
import org.wso2.maven.p2.utils.StandaloneManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.nio.file.Paths;

/**
 * FeatureInstaller takes parameters from the pom.xml and generates the profile.
 *
 * @since 2.0.0
 */
public class FeatureInstaller {

    private final FeatureInstallResourceBundle resourceBundle;
    private final String destination;

    private Log log;

    /**
     * ProfileGenerator constructor taking ProfileResourceBundle as a parameter.
     *
     * @param resourceBundle ProfileResourceBundle
     */
    public FeatureInstaller(FeatureInstallResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        this.log = resourceBundle.getLog();
        this.destination = resourceBundle.getDestination();
    }

    /**
     * Performs the installation operation.
     *
     * @throws MojoExecutionException throws when any runtime exception occurs. i.e: fail to read write file, fail to
     *                                install any given feature.
     * @throws MojoFailureException   throws when the tool breaks for any configuration issues
     */
    public void install() throws MojoExecutionException, MojoFailureException {
        try {
            writeEclipseIni();
            installFeatures();
            updateProfileConfigIni();
            deleteOldProfiles();
        } catch (IOException e) {
            throw new MojoExecutionException(e.getMessage(), e);
        }
    }

    /**
     * Updating profile's config.ini p2.data.area property using relative path.
     */
    private void updateProfileConfigIni() {
        File profileConfigIni = FileManagementUtil.getProfileConfigIniFile(destination, resourceBundle.getProfile());
        FileManagementUtil.changeConfigIniProperty(profileConfigIni, "eclipse.p2.data.area", P2Constants.P2_DIRECTORY,
                resourceBundle.getLog());
    }

    /**
     * Calls the P2ApplicationLauncher and install the features.
     *
     * @throws MojoFailureException throws when the director application fail to install any given feature.
     */
    private void installFeatures() throws MojoFailureException {
        this.log.info("Running Equinox P2 Director Application");
        StandaloneManager launcher = new StandaloneManager(resourceBundle.getLauncher());
        launcher.setRuntimeLocation(resourceBundle.getRuntimeLocation());
        launcher.addArgumentsToInstallFeatures(resourceBundle.getRepository().toExternalForm(), destination,
                resourceBundle.getProfile());
        launcher.performAction(extractIUsToInstall(), resourceBundle.getForkedProcessTimeoutInSeconds());
    }

    /**
     * Generate the formatted string representation of features from the features passed in through the pom.xml. This
     * formatted string is passed into P2ApplicationLauncher to generate the profile.
     *
     * @return formatted string to pass into P2ApplicationLauncher
     */
    private String extractIUsToInstall() {
        StringBuilder installIUs = new StringBuilder();
        resourceBundle.getFeatures().forEach(feature ->
                installIUs.append(feature.getId().trim()).append("/").append(feature.getVersion().trim()).append(","));

        return installIUs.toString();
    }

    /**
     * Delete old profile files located at ${destination}/p2/org.eclipse.equinox.p2.engine/profileRegistry.
     *
     * @throws IOException throws when fail to delete old profile files
     */
    private void deleteOldProfiles() throws IOException {
        //In not specified to delete, then return the method.
        if (!resourceBundle.isDeleteOldProfileFiles()) {
            return;
        }
        String destination = resourceBundle.getDestination();
        if (!destination.endsWith("/")) {
            destination = destination + "/";
            resourceBundle.setDestination(destination);
        }
        String profileFolderName = resourceBundle.getDestination() +
                "p2/org.eclipse.equinox.p2.engine/profileRegistry/" + resourceBundle.getProfile() + ".profile";

        File profileFolder = new File(profileFolderName);
        if (profileFolder.isDirectory()) {
            String[] profileFileList = profileFolder.list((dir, name) -> name.endsWith(".profile"));

            //deleting old profile files
            if (profileFileList != null) {
                for (int i = 0; i < (profileFileList.length - 1); i++) {
                    File profileFile = new File(profileFolderName, profileFileList[i]);
                    if (profileFile.exists() && !profileFile.delete()) {
                        throw new IOException("Failed to delete old profile file: " +
                                profileFile.getAbsolutePath());
                    }
                }
            }

        }
    }

    /**
     * Truncate and write the eclipse.ini file with the new profile location.
     *
     * @throws IOException throws when fail to eclipse.ini file/fail to delete old files.
     */
    private void writeEclipseIni() throws IOException {
        String profileLocation = resourceBundle.getDestination() + File.separator + resourceBundle.getProfile();

        File eclipseIni = Paths.get(profileLocation, "eclipse.ini").toFile();
        if (eclipseIni.exists()) {
            updateFile(eclipseIni, profileLocation);
        }
    }

    /**
     * Update eclipse.ini.
     *
     * @param file            File object
     * @param profileLocation location of the profile directory
     * @throws IOException throws if fail to update the file
     */
    private void updateFile(File file, String profileLocation) throws IOException {
        if (file.exists()) {
            if (!file.delete()) {
                throw new IOException("Failed to delete " + file.getAbsolutePath());
            }
        }
        this.log.info("Updating " + file.getName());
        try (Writer writer = new OutputStreamWriter(new FileOutputStream(file), P2Constants.DEFAULT_ENCODING);
             PrintWriter pw = new PrintWriter(writer)) {
            pw.write("-install\n");
            pw.write(profileLocation);
            pw.flush();
        } catch (IOException e) {
            this.log.debug("Error while writing to file " + file.getName(), e);
        }
    }
}
