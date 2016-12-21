/*
 * Copyright (c) 2016, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.wso2.maven.p2.utils;

import org.apache.maven.plugin.MojoFailureException;
import org.eclipse.sisu.equinox.launching.EquinoxLauncher;
import org.eclipse.sisu.equinox.launching.internal.EquinoxInstallationLaunchConfiguration;
import org.eclipse.tycho.launching.LaunchConfiguration;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Eclipse installations using the p2 director application.
 * This director runtime can install products with meta-requirements,
 * e.g. for custom touchpoint actions
 *
 * @since 3.0.0
 */
public class StandaloneManager {

    private final EquinoxLauncher launcher;
    private File runtimeLocation;
    private List<String> programArguments;

    public StandaloneManager(EquinoxLauncher launcher) {
        this.launcher = launcher;
        programArguments = new ArrayList<>();
    }

    /**
     * Sets the Runtime Location.
     *
     * @param runtimeLocation {@link File}
     */
    public void setRuntimeLocation(File runtimeLocation) {
        this.runtimeLocation = runtimeLocation;
    }

    /**
     * Sets the StandaloneDirectorRuntime's arguments to install features.
     *
     * @param repositoryLocation a comma separated list of metadata repository (or artifact repository as
     *                           in p2 both
     *                           metadata and artifacts resides in the same repo) URLs where the software
     *                           to be
     *                           installed can be found.
     *                           of the feature has to be suffixed with ".feature.group".
     * @param destination        the path of a folder in which the targeted product is located.
     * @param profile            the profile id containing the description of the targeted product. This
     *                           ID is
     *                           defined by the eclipse.p2.profile property contained in the config.ini of the
     *                           targeted product.
     */
    public void addArgumentsToInstallFeatures(String repositoryLocation, String destination, String profile) {
        programArguments.add(P2Constants.Launcher.METADATA_REPOSITORY);
        programArguments.add(repositoryLocation);
        programArguments.add(P2Constants.Launcher.ARTIFACT_REPOSITORY);
        programArguments.add(repositoryLocation);
        programArguments.add(P2Constants.Launcher.DESTINATION);
        programArguments.add(Paths.get(destination, profile).toString());
        programArguments.add(P2Constants.Launcher.BUNDLEPOOL);
        programArguments.add(Paths.get(destination, P2Constants.LIB).toString());
        programArguments.add(P2Constants.Launcher.SHARED);
        programArguments.add(Paths.get(destination, P2Constants.LIB, P2Constants.P2).toString());
        programArguments.add(P2Constants.Launcher.PROFILE);
        programArguments.add(profile);
        programArguments.add(P2Constants.Launcher.PROFILE_PROPERTIES);
        programArguments.add(P2Constants.Launcher.ECLIPSE_UPDATE_FEATURE_TRUE);
        programArguments.add(P2Constants.Launcher.ROAMING);
    }

    /**
     * Calls the Eclipse Launcher to perform the action such as install the set of features.
     *
     * @param installIU     The feature (UI) that is to be install.
     * @param forkedProcessTimeoutInSeconds int
     * @throws MojoFailureException throws when unable to perform the p2 activity
     */
    public void performAction(String installIU, int forkedProcessTimeoutInSeconds) throws MojoFailureException {
        int index = programArguments.indexOf(P2Constants.Launcher.INSTALLIU);
        if (index >= 0) {
            programArguments.set(index + 1, installIU);
        } else {
            programArguments.add(P2Constants.Launcher.INSTALLIU);
            programArguments.add(installIU);
        }

        LaunchConfiguration launch = new EquinoxInstallationLaunchConfiguration(runtimeLocation, programArguments);
        int result = launcher.execute(launch, forkedProcessTimeoutInSeconds);
        if (result != 0) {
            throw new MojoFailureException("P2 publisher return code was " + result);
        }
    }
}
