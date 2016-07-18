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

import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;
import org.eclipse.sisu.equinox.launching.internal.P2ApplicationLauncher;

import java.net.URL;
import java.util.List;

/**
 * Bean class containing all the parameters entered to the mojo through plugin configuration.
 * <p>
 * The purpose of this class is to make any configuration property accessible from any class by simply passing this
 * bean as a parameter.
 *
 * @since 2.0.0
 */
public class FeatureInstallResourceBundle {

    private String destination;
    private String profile;
    private URL repository;
    private List<Feature> features;
    private boolean deleteOldProfileFiles;
    private MavenProject project;
    private P2ApplicationLauncher launcher;
    private int forkedProcessTimeoutInSeconds;
    private Log log;

    /**
     * Returns the destination of the profile to install features.
     *
     * @return {@code String}
     */
    public String getDestination() {
        return destination;
    }

    /**
     * Sets the destination of the profile to install features.
     *
     * @param destination {@code String}
     */
    public void setDestination(String destination) {
        this.destination = destination;
    }

    /**
     * Returns the name of the profile that the features will be installed.
     *
     * @return {@code String}
     */
    public String getProfile() {
        return profile;
    }

    /**
     * Sets the name of the profile that the features will be installed.
     *
     * @param profile {@code String}
     */
    public void setProfile(String profile) {
        this.profile = profile;
    }

    /**
     * Returns the repository url where the features taken from.
     *
     * @return {@link URL}
     */
    public URL getRepository() {
        return repository;
    }

    /**
     * Sets the repository url where the features taken from.
     *
     * @param repository {@link URL}
     */
    public void setRepository(URL repository) {
        this.repository = repository;
    }

    /**
     * Returns the features list to be installed.
     *
     * @return {@code List<Feature>}
     */
    public List<Feature> getFeatures() {
        return features;
    }

    /**
     * Sets the features list to be installed.
     *
     * @param features {@code List<Feature>}
     */
    public void setFeatures(List<Feature> features) {
        this.features = features;
    }

    /**
     * Returns a boolean which specifies whether the old profile files should be deleted or not.
     *
     * @return {@code boolean}
     */
    public boolean isDeleteOldProfileFiles() {
        return deleteOldProfileFiles;
    }

    /**
     * Specify whether old profile files should be deleted or not.
     *
     * @param deleteOldProfileFiles {@code boolean}
     */
    public void setDeleteOldProfileFiles(boolean deleteOldProfileFiles) {
        this.deleteOldProfileFiles = deleteOldProfileFiles;
    }

    /**
     * Returns the maven project injected by the maven runtime.
     *
     * @return {@link MavenProject}
     */
    public MavenProject getProject() {
        return project;
    }

    /**
     * Sets the maven project injected by the maven runtime.
     *
     * @param project {@link MavenProject}
     */
    public void setProject(MavenProject project) {
        this.project = project;
    }

    /**
     * Returns the P2ApplicationLauncher injected by the maven runtime.
     *
     * @return {@link P2ApplicationLauncher}
     */
    public P2ApplicationLauncher getLauncher() {
        return launcher;
    }

    /**
     * Sets the P2ApplicationLauncher injected by the maven runtime.
     *
     * @param launcher {@link P2ApplicationLauncher}
     */
    public void setLauncher(P2ApplicationLauncher launcher) {
        this.launcher = launcher;
    }

    /**
     * Returns the forkedProcessTimeout in seconds.
     * @return {@code int}
     */
    public int getForkedProcessTimeoutInSeconds() {
        return forkedProcessTimeoutInSeconds;
    }

    /**
     * Sets the forkedProcessTimeout in seconds.
     * @param forkedProcessTimeoutInSeconds {@code int}
     */
    public void setForkedProcessTimeoutInSeconds(int forkedProcessTimeoutInSeconds) {
        this.forkedProcessTimeoutInSeconds = forkedProcessTimeoutInSeconds;
    }

    /**
     * Returns the Log.
     *
     * @return {@link Log}
     */
    public Log getLog() {
        return log;
    }

    /**
     * Sets the Log.
     *
     * @param log {@link Log}
     */
    public void setLog(Log log) {
        this.log = log;
    }
}
