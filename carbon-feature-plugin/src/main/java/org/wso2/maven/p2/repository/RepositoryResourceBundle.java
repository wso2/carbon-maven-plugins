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
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;
import org.apache.maven.repository.RepositorySystem;
import org.eclipse.sisu.equinox.launching.internal.P2ApplicationLauncher;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * Bean class containing all the parameters entered to the mojo through plugin configuration.
 * The purpose of this class is to make any configuration property accessible from any class by simply passing this
 * bean as a parameter.
 *
 * @since 2.0.0
 */
public class RepositoryResourceBundle {

    private String name;
    private URL repository;

    private List<Feature> featureArtifacts;
    private List<Bundle> bundleArtifacts;
    private List categories;

    private MavenProject project;


    private boolean archive;

    private RepositorySystem repositorySystem;
    private ArtifactRepository localRepository;
    private List<ArtifactRepository> remoteRepositories;
    private P2ApplicationLauncher launcher;
    private int forkedProcessTimeoutInSeconds;

    private Log log;

    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }

    public URL getRepository() {
        return repository;
    }

    public void setRepository(URL repository) {
        this.repository = repository;
    }

    public List<Feature> getFeatureArtifacts() {
        if (featureArtifacts == null) {
            return new ArrayList<>();
        }
        return featureArtifacts;
    }

    public void setFeatureArtifacts(List<Feature> featureArtifacts) {
        this.featureArtifacts = featureArtifacts;
    }

    public List<Bundle> getBundleArtifacts() {
        if (bundleArtifacts == null) {
            return new ArrayList<>();
        }
        return bundleArtifacts;
    }

    public void setBundleArtifacts(List<Bundle> bundleArtifacts) {
        this.bundleArtifacts = bundleArtifacts;
    }

    public List getCategories() {
        return categories;
    }

    public void setCategories(List categories) {
        this.categories = categories;
    }

    public MavenProject getProject() {
        return project;
    }

    public void setProject(MavenProject project) {
        this.project = project;
    }

    public boolean isArchive() {
        return archive;
    }

    public void setArchive(boolean archive) {
        this.archive = archive;
    }

    public RepositorySystem getRepositorySystem() {
        return repositorySystem;
    }

    public void setRepositorySystem(RepositorySystem repositorySystem) {
        this.repositorySystem = repositorySystem;
    }

    public ArtifactRepository getLocalRepository() {
        return localRepository;
    }

    public void setLocalRepository(ArtifactRepository localRepository) {
        this.localRepository = localRepository;
    }

    public List<ArtifactRepository> getRemoteRepositories() {
        return remoteRepositories;
    }

    public void setRemoteRepositories(List<ArtifactRepository> remoteRepositories) {
        this.remoteRepositories = remoteRepositories;
    }

    public P2ApplicationLauncher getLauncher() {
        return launcher;
    }

    public void setLauncher(P2ApplicationLauncher launcher) {
        this.launcher = launcher;
    }

    public int getForkedProcessTimeoutInSeconds() {
        return forkedProcessTimeoutInSeconds;
    }

    public void setForkedProcessTimeoutInSeconds(int forkedProcessTimeoutInSeconds) {
        this.forkedProcessTimeoutInSeconds = forkedProcessTimeoutInSeconds;
    }

    public Log getLog() {
        return log;
    }

    public void setLog(Log log) {
        this.log = log;
    }

}
