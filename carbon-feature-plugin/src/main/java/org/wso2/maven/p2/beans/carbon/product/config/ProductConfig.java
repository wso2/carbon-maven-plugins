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

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Bean to represent product configurations.
 * This is used to capture build-time product configuration
 * data from distribution pom file.
 */

@XmlRootElement
public class ProductConfig {

    private String name;

    private String uid;

    private String id;

    private String application;

    private String version;

    private Boolean useFeatures;

    private Boolean includeLaunchers;

    private ConfigIni configIni;

    private LauncherArgs launcherArgs;

    private Launcher launcher;

    private Plugins plugins;

    private List<Feature> featureConfig;

    private List<Plugin> pluginConfig;

    private List<Property> propertyConfig;

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
        if (version == null || version.isEmpty()) {
            this.version = version;
        } else {
            this.version = version.replaceAll("-", ".");
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

    public void setFeatureConfig(List<Feature> featureConfig) {
        if (featureConfig == null || featureConfig.size() == 0) {
            this.featureConfig = featureConfig;
        } else {
            List<Feature> featuresWithVersionFormatUpdates = new ArrayList<>();
            for (Feature feature : featureConfig) {
                Feature featureWithVersionFormatUpdates = new Feature();
                featureWithVersionFormatUpdates.setId(feature.getId());
                featureWithVersionFormatUpdates.setVersion(feature.getVersion().replaceAll("-", "."));
                featuresWithVersionFormatUpdates.add(featureWithVersionFormatUpdates);
            }
            this.featureConfig = featuresWithVersionFormatUpdates;
        }
    }

    public List<Plugin> getPluginConfig() {
        return pluginConfig;
    }

    public void setPluginConfig(List<Plugin> pluginConfig) {
        this.pluginConfig = pluginConfig;
    }

    public List<Property> getPropertyConfig() {
        return propertyConfig;
    }

    public void setPropertyConfig(List<Property> propertyConfig) {
        this.propertyConfig = propertyConfig;
    }

}
