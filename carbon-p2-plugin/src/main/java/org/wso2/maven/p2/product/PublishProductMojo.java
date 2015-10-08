/*
*  Copyright (c) 2005-2011, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
*  WSO2 Inc. licenses this file to you under the Apache License,
*  Version 2.0 (the "License"); you may not use this file except
*  in compliance with the License.
*  You may obtain a copy of the License at
*
*    http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing,
* software distributed under the License is distributed on an
* "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
* KIND, either express or implied.  See the License for the
* specific language governing permissions and limitations
* under the License.
*/
package org.wso2.maven.p2.product;


import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.eclipse.tycho.p2.facade.internal.P2ApplicationLauncher;

import java.io.File;
import java.net.URL;

/**
 * Publish a given product using the .product file to the repository.
 */
@Mojo(name = "publish-product")
public class PublishProductMojo extends AbstractMojo {

    @Parameter(required = true, defaultValue = "${project}")
    protected MavenProject project;

    @Parameter
    private URL repositoryURL;

    /**
     * executable
     */
    @Parameter
    private String executable;

    /**
     * The product configuration, a .product file. This file manages all aspects
     * of a product definition from its constituent plug-ins to configuration
     * files to branding.
     *
     */
    @Parameter(defaultValue = "${productConfiguration}")
    private File productConfigurationFile;

    @Component
    private P2ApplicationLauncher launcher;

    /**
     * Kill the forked test process after a certain number of seconds. If set to 0, wait forever for
     * the process, never timing out.
     *
     */
    @Parameter(defaultValue = "${p2.timeout}")
    private int forkedProcessTimeoutInSeconds;

    public void execute() throws MojoExecutionException, MojoFailureException {
        try {
            publishProduct();
        }catch (Exception e) {
            throw new MojoExecutionException("Cannot generate P2 metadata", e);
        }
    }

    private void publishProduct()  throws Exception{
        P2ApplicationLauncher launcher = this.launcher;

        launcher.setWorkingDirectory(project.getBasedir());
        launcher.setApplicationName("org.eclipse.equinox.p2.publisher.ProductPublisher");

        launcher.addArguments(
                "-repositoryURL", repositoryURL.toString(),
                "-artifactRepository", repositoryURL.toString(),
                "-productFile", productConfigurationFile.getCanonicalPath(),
                "-executables", executable.toString(),
                "-publishArtifacts",
                "-configs", "gtk.linux.x86",
                "-flavor", "tooling",
                "-append");

        int result = launcher.execute(forkedProcessTimeoutInSeconds);
        if (result != 0) {
            throw new MojoFailureException("P2 publisher return code was " + result);
        }
    }
}