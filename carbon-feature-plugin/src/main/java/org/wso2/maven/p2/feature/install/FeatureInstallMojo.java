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

import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.eclipse.sisu.equinox.launching.EquinoxLauncher;
import org.eclipse.tycho.BuildOutputDirectory;
import org.eclipse.tycho.p2.tools.director.shared.DirectorRuntime;
import org.eclipse.tycho.plugins.p2.director.DirectorMojo.DirectorRuntimeType;
import org.eclipse.tycho.plugins.p2.director.runtime.StandaloneDirectorRuntimeFactory;
import org.wso2.maven.p2.utils.P2Constants;

import java.io.File;
import java.net.URL;
import java.util.List;

/**
 * Install a given set of carbon features on a product. The artifacts and metadata of the features to be installed
 * should reside in the repository given under {@code repositoryURL}.
 *
 * @since 2.0.0
 */
@Mojo(name = "install", defaultPhase = LifecyclePhase.PACKAGE)
public class FeatureInstallMojo extends AbstractMojo {


    /**
     * Destination to which the features should be installed.
     */
    @Parameter(required = true)
    private String destination;

    /**
     * Target runtime.
     */
    @Parameter(required = true)
    private String runtime;


    /**
     * URL of the Metadata Repository.
     */
    @Parameter
    private URL repositoryURL;

    /**
     * List of features.
     */
    @Parameter(required = true)
    private List<Feature> features;

    /**
     * Flag to indicate whether to delete old profile files.
     */
    @Parameter(defaultValue = "true")
    private boolean deleteOldRuntimeFiles = true;


    @Parameter(defaultValue = "${project}")
    private MavenProject project;

    @Component
    private EquinoxLauncher launcher;

    @Component
    private MavenSession session;

    @Parameter(defaultValue = "standalone")
    private DirectorRuntimeType directorRuntime;

    @Component
    private StandaloneDirectorRuntimeFactory standaloneDirectorFactory;

    /**
     * Kill the forked test process after a certain number of seconds. If set to 0, wait forever for
     * the process, never timing out.
     */
    @Parameter(defaultValue = "${p2.timeout}")
    private int forkedProcessTimeoutInSeconds;

    private File runtimeLocation;

    /**
     * Overridden method of AbstractMojo class. This is picked up by the maven runtime for execution.
     *
     * @throws MojoExecutionException throws when any runtime exception occurs. i.e: fail to read write file, fail to
     *                                parse a configuration xml
     * @throws MojoFailureException   throws when the tool breaks for any configuration issues
     */
    public void execute() throws MojoExecutionException, MojoFailureException {
        this.runtimeLocation = new BuildOutputDirectory(this.project.getBuild().getDirectory()).getChild("director");
        getDirectorRuntime();
        FeatureInstaller installer = constructFeatureInstaller();
        installer.install();
    }

    /**
     * Constructs the FeatureInstaller object.
     * @return FeatureInstaller
     */
    private FeatureInstaller constructFeatureInstaller() {
        FeatureInstallResourceBundle resourceBundle = new FeatureInstallResourceBundle();
        resourceBundle.setDestination(this.destination);
        resourceBundle.setProfile(this.runtime == null ? P2Constants.DEFAULT_PROFILE_ID : this.runtime);
        resourceBundle.setRepository(this.repositoryURL);
        resourceBundle.setFeatures(this.features);
        resourceBundle.setDeleteOldProfileFiles(this.deleteOldRuntimeFiles);
        resourceBundle.setProject(this.project);
        resourceBundle.setLauncher(this.launcher);
        resourceBundle.setForkedProcessTimeoutInSeconds(this.forkedProcessTimeoutInSeconds);
        resourceBundle.setLog(getLog());
        resourceBundle.setRuntimeLocation(this.runtimeLocation);
        return new FeatureInstaller(resourceBundle);
    }

    private DirectorRuntime getDirectorRuntime() throws MojoFailureException, MojoExecutionException {
        switch (directorRuntime) {
            case standalone:
                return standaloneDirectorFactory.createStandaloneDirector(this.runtimeLocation,
                        this.session.getLocalRepository(), this.forkedProcessTimeoutInSeconds);
            default:
                throw new MojoFailureException("Unsupported value for attribute 'directorRuntime': \"" +
                        directorRuntime + "\"");
        }
    }
}
