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

import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.apache.maven.repository.RepositorySystem;
import org.eclipse.sisu.equinox.launching.internal.P2ApplicationLauncher;

import java.net.URL;
import java.util.List;

/**
 * Write environment information for the current build to file.
 *
 * @since 2.0.0
 */
@Mojo(name = "generate-repo", defaultPhase = LifecyclePhase.PACKAGE)
public class RepositoryGeneratorMojo extends AbstractMojo {

    @Parameter
    private String name;

    @Parameter
    private URL targetRepository;

    @Parameter(required = true)
    private List<Feature> features;

    @Parameter
    private List<Bundle> bundles;

    @Parameter
    private List categories;

    @Parameter(defaultValue = "${project}")
    private MavenProject project;

    @Parameter(defaultValue = "false")
    private boolean archive;

    @Component
    private RepositorySystem repositorySystem;

    @Parameter(defaultValue = "${localRepository}")
    private ArtifactRepository localRepository;


    @Parameter(defaultValue = "${project.remoteArtifactRepositories}")
    private List<ArtifactRepository> remoteRepositories;


    @Component
    private P2ApplicationLauncher launcher;

    /**
     * Kill the forked test process after a certain number of seconds. If set to 0, wait forever for
     * the process, never timing out.
     */
    @Parameter(defaultValue = "${p2.timeout}")
    private int forkedProcessTimeoutInSeconds;

    /**
     * Overridden method, which will be picked up by maven execution context and execute when this mojo is referred.
     *
     * @throws MojoExecutionException throws when the tool breaks for any configuration issues
     * @throws MojoFailureException   throws when any runtime exception occurs. i.e: When director application fails
     *                                to generate the repository
     */
    public void execute() throws MojoExecutionException, MojoFailureException {
        RepositoryGenerator generator = constructRepoGenerator();
        generator.generate();
    }

    private RepositoryGenerator constructRepoGenerator() {
        RepositoryResourceBundle resourceBundle = new RepositoryResourceBundle();
        resourceBundle.setName(this.name);
        resourceBundle.setRepository(this.targetRepository);
        resourceBundle.setFeatureArtifacts(this.features);
        resourceBundle.setBundleArtifacts(this.bundles);
        resourceBundle.setCategories(this.categories);
        resourceBundle.setProject(this.project);
        resourceBundle.setArchive(this.archive);
        resourceBundle.setRepositorySystem(this.repositorySystem);
        resourceBundle.setLocalRepository(this.localRepository);
        resourceBundle.setRemoteRepositories(this.remoteRepositories);
        resourceBundle.setLauncher(this.launcher);
        resourceBundle.setForkedProcessTimeoutInSeconds(this.forkedProcessTimeoutInSeconds);
        resourceBundle.setLog(getLog());
        return new RepositoryGenerator(resourceBundle);
    }
}
