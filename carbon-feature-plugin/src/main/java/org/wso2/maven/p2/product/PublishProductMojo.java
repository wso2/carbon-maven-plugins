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
package org.wso2.maven.p2.product;


import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.eclipse.sisu.equinox.launching.internal.P2ApplicationLauncher;
import org.wso2.maven.p2.utils.P2ApplicationLaunchManager;

import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * Publish a given product using the .product file to the repository.
 *
 * @since 2.0.0
 */
@Mojo(name = "publish-product")
public class PublishProductMojo extends AbstractMojo {

    @Parameter(defaultValue = "${project}")
    protected MavenProject project;

    @Parameter
    private URL repositoryURL;

    @Parameter
    private String executable;

    /**
     * The product configuration, a .product file. This file manages all aspects
     * of a product definition from its constituent plug-ins to configuration
     * files to branding.
     */
    @Parameter
    private File productConfigurationFile;

    @Component
    private P2ApplicationLauncher launcher;

    /**
     * Kill the forked test process after a certain number of seconds. If set to 0, wait forever for
     * the process, never timing out.
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
        try {
            publishProduct();
        } catch (IOException e) {
            throw new MojoExecutionException("Cannot generate P2 metadata", e);
        }
    }

    /**
     * Perform the product publish action.
     *
     * @throws MojoFailureException throws when the tool breaks for any configuration issues
     * @throws IOException          throws if fail to read file canonical path.
     */
    private void publishProduct() throws MojoFailureException, IOException {
        P2ApplicationLaunchManager p2LaunchManager = new P2ApplicationLaunchManager(this.launcher);
        p2LaunchManager.setWorkingDirectory(project.getBasedir());
        p2LaunchManager.setApplicationName("org.eclipse.equinox.p2.publisher.ProductPublisher");
        p2LaunchManager.addPublishProductArguments(repositoryURL, productConfigurationFile, executable);
        p2LaunchManager.performAction(forkedProcessTimeoutInSeconds);
    }
}
