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

package org.wso2.maven.p2.feature.uninstall;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.eclipse.sisu.equinox.launching.internal.P2ApplicationLauncher;

import java.util.List;

/**
 * Mojo which will uninstall a given set of carbon features from a product.
 *
 * @since 2.0.0
 */
@Mojo(name = "uninstall", defaultPhase = LifecyclePhase.PACKAGE)
public class FeatureUnInstallMojo extends AbstractMojo {

    /**
     * Destination to which the features should be installed
     */
    @Parameter(required = true)
    private String destination;
    /**
     * target profile
     */
    @Parameter(required = true)
    private String profile;

    /**
     * List of features
     */
    @Parameter(required = true)
    private List<Feature> features;

    @Parameter(defaultValue = "${project}")
    private MavenProject project;

    @Component
    private P2ApplicationLauncher launcher;

    /**
     * Kill the forked test process after a certain number of seconds. If set to 0, wait forever for
     * the process, never timing out.
     *
     * @parameter expression="${p2.timeout}"
     */
    @Parameter(defaultValue = "${p2.timeout}")
    private int forkedProcessTimeoutInSeconds;


    /**
     * Overridden method of AbstractMojo class. This is picked up by the maven runtime for execution.
     *
     * @throws MojoExecutionException throws when any runtime exception occurs. i.e: fail to read write file, fail to
     *                                parse a configuration xml
     * @throws MojoFailureException   throws when the tool breaks for any configuration issues
     */
    public void execute() throws MojoExecutionException, MojoFailureException {
        FeatureUnInstaller unInstaller = new FeatureUnInstaller();
        unInstaller.setDestination(this.destination);
        unInstaller.setFeatures(this.features);
        unInstaller.setLauncher(this.launcher);
        unInstaller.setProfile(this.profile);
        unInstaller.setProject(this.project);
        unInstaller.setForkedProcessTimeoutInSeconds(this.forkedProcessTimeoutInSeconds);

        this.getLog().info("Running Equinox P2 Director Application");
        unInstaller.uninstallFeatures();
    }

}
