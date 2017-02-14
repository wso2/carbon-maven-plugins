/*
 *  Copyright (c) 2017, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

package org.wso2.maven.p2.beans.carbon.product.config;

import org.wso2.maven.p2.beans.carbon.product.ConfigIni;
import org.wso2.maven.p2.beans.carbon.product.Feature;
import org.wso2.maven.p2.beans.carbon.product.Launcher;
import org.wso2.maven.p2.beans.carbon.product.LauncherArgs;
import org.wso2.maven.p2.beans.carbon.product.Plugin;
import org.wso2.maven.p2.beans.carbon.product.Plugins;
import org.wso2.maven.p2.beans.carbon.product.Property;
import org.wso2.maven.p2.utils.P2Constants;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Bean to represent product configurations.
 *
 * This is used to capture build-time product configuration
 * data from distribution pom file.
 *
 * @since 3.1.0
 */

@XmlRootElement
public class ProductConfig {

    private String name = P2Constants.ProductFileDefaults.Product.NAME;

    private String uid = P2Constants.ProductFileDefaults.Product.UID;

    private String id = P2Constants.ProductFileDefaults.Product.ID;

    private String application = P2Constants.ProductFileDefaults.Product.APPLICATION;

    private String version; // If not set in project pom, carbon runtime version will be set by default.

    private Boolean useFeatures = P2Constants.ProductFileDefaults.Product.USE_FEATURES;

    private Boolean includeLaunchers = P2Constants.ProductFileDefaults.Product.INCLUDE_LAUNCHERS;

    private ConfigIni configIni = new ConfigIni();

    private LauncherArgs launcherArgs = new LauncherArgs();

    private Launcher launcher = new Launcher();

    private Plugins plugins = new Plugins();

    private List<Feature> featureConfig; // If not set in project pom, carbon runtime feature will be set by default.

    private List<Plugin> pluginConfig = this.getDefaultPluginConfig();

    private List<Property> propertyConfig = this.getDefaultPropertyConfig();

    public String getName() {
        return name;
    }

    @XmlAttribute
    public void setName(String name) {
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    @XmlAttribute
    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getId() {
        return id;
    }

    @XmlAttribute
    public void setId(String id) {
        this.id = id;
    }

    public String getApplication() {
        return application;
    }

    @XmlAttribute
    public void setApplication(String application) {
        this.application = application;
    }

    public String getVersion() {
        return version;
    }

    @XmlAttribute
    public void setVersion(String version) {
        if (version == null || version.isEmpty() ||
                !version.contains(P2Constants.ProductFileDefaults.Feature.VERSION_CHAR_REPLACED)) {
            this.version = version;
        } else {
            this.version = version.replaceAll(P2Constants.ProductFileDefaults.Feature.VERSION_CHAR_REPLACED,
                    P2Constants.ProductFileDefaults.Feature.VERSION_CHAR_REPLACEMENT);
        }
    }

    public Boolean getUseFeatures() {
        return useFeatures;
    }

    @XmlAttribute
    public void setUseFeatures(Boolean useFeatures) {
        this.useFeatures = useFeatures;
    }

    public Boolean getIncludeLaunchers() {
        return includeLaunchers;
    }

    @XmlAttribute
    public void setIncludeLaunchers(Boolean includeLaunchers) {
        this.includeLaunchers = includeLaunchers;
    }

    public ConfigIni getConfigIni() {
        return configIni;
    }

    @XmlElement
    public void setConfigIni(ConfigIni configIni) {
        this.configIni = configIni;
    }

    public LauncherArgs getLauncherArgs() {
        return launcherArgs;
    }

    @XmlElement
    public void setLauncherArgs(LauncherArgs launcherArgs) {
        this.launcherArgs = launcherArgs;
    }

    public Launcher getLauncher() {
        return launcher;
    }

    @XmlElement
    public void setLauncher(Launcher launcher) {
        this.launcher = launcher;
    }

    public Plugins getPlugins() {
        return plugins;
    }

    @XmlElement
    public void setPlugins(Plugins plugins) {
        this.plugins = plugins;
    }

    public List<Feature> getFeatureConfig() {
        return featureConfig;
    }

    @XmlTransient
    public void setFeatureConfig(List<Feature> featureConfig) {
        this.featureConfig = featureConfig;
    }

    public List<Plugin> getPluginConfig() {
        return pluginConfig;
    }

    @XmlTransient
    @SuppressWarnings("unused")
    public void setPluginConfig(List<Plugin> pluginConfig) {
        this.pluginConfig = pluginConfig;
    }

    public List<Property> getPropertyConfig() {
        return propertyConfig;
    }

    @XmlTransient
    @SuppressWarnings("unused")
    public void setPropertyConfig(List<Property> propertyConfig) {
        this.propertyConfig = propertyConfig;
    }

    private List<Property> getDefaultPropertyConfig() {
        List<Property> properties = new ArrayList<>();
        // adding default property, value pairs
        properties.add(new Property("org.eclipse.update.reconcile", false));
        properties.add(new Property("org.eclipse.equinox.simpleconfigurator.useReference", true));
        return properties;
    }

    private List<Plugin> getDefaultPluginConfig() {
        List<Plugin> plugins = new ArrayList<>();
        // adding default plugins wth default autoStart and startLevel configurations
        plugins.add(new Plugin("org.eclipse.core.runtime", true, 4));
        plugins.add(new Plugin("org.eclipse.equinox.common", true, 2));
        plugins.add(new Plugin("org.eclipse.equinox.ds", true, 2));
        plugins.add(new Plugin("org.eclipse.equinox.p2.reconciler.dropins", true, 4));
        plugins.add(new Plugin("org.eclipse.equinox.simpleconfigurator", true, 1));
        plugins.add(new Plugin("org.eclipse.update.configurator", true, 1));
        return plugins;
    }

}
