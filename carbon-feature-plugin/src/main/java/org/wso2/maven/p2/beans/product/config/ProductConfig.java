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

package org.wso2.maven.p2.beans.product.config;

import org.wso2.maven.p2.beans.product.ConfigIni;
import org.wso2.maven.p2.beans.product.Launcher;
import org.wso2.maven.p2.beans.product.Plugin;
import org.wso2.maven.p2.beans.product.Property;
import org.wso2.maven.p2.utils.P2Constants.ProductFile;

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

    private String name = ProductFile.Product.NAME;
    private String uid = ProductFile.Product.UID;
    private String id = ProductFile.Product.ID;
    private String application = ProductFile.Product.APPLICATION;
    private String version; // If not set in project pom, carbon runtime version will be set by default.
    private Boolean includeLaunchers = ProductFile.Product.INCLUDE_LAUNCHERS;
    private ConfigIni configIni = new ConfigIni();
    private Launcher launcher = new Launcher();
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
        this.version = version;
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

    public Launcher getLauncher() {
        return launcher;
    }

    @XmlElement
    public void setLauncher(Launcher launcher) {
        this.launcher = launcher;
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
        plugins.add(new Plugin("org.eclipse.update.configurator", true, 1));
        plugins.add(new Plugin("org.eclipse.equinox.simpleconfigurator", true, 1));
        plugins.add(new Plugin("org.eclipse.equinox.ds", true, 2));
        plugins.add(new Plugin("org.eclipse.equinox.common", true, 2));
        plugins.add(new Plugin("org.eclipse.core.runtime", true, 4));
        plugins.add(new Plugin("org.eclipse.equinox.p2.reconciler.dropins", true, 4));
        return plugins;
    }
}
