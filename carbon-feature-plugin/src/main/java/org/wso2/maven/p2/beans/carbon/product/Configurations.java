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

package org.wso2.maven.p2.beans.carbon.product;

import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Bean to represent configurations.
 *
 * This is used write configurations data to carbon.product file.
 *
 * @since 3.1.0
 */

@XmlRootElement
public class Configurations {

    private List<Plugin> plugins;

    private List<Property> properties;

    @SuppressWarnings("unused")
    public Configurations() {}

    public Configurations(List<Plugin> plugin, List<Property> property) {
        this.setPlugins(plugin);
        this.setProperties(property);
    }

    public List<Plugin> getPlugins() {
        return plugins;
    }

    @XmlElement(name = "plugin")
    public void setPlugins(List<Plugin> plugins) {
        this.plugins = plugins;
    }

    public List<Property> getProperties() {
        return properties;
    }

    @XmlElement(name = "property")
    public void setProperties(List<Property> properties) {
        this.properties = properties;
    }

}
