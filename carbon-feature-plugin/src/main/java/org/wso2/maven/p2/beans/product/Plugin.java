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

package org.wso2.maven.p2.beans.product;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Bean to represent a plugin.
 *
 * This is used to
 * [1] Capture build-time product file individual plugin data from distribution pom file.
 * [2] Write individual plugin data to carbon.product file.
 *
 * @since 3.1.0
 */
@XmlRootElement
public class Plugin {

    private String id;
    private Boolean autoStart;
    private Integer startLevel;

    public Plugin() {}

    public Plugin(String id, Boolean autoStart, Integer startLevel) {
        this.setId(id);
        this.setAutoStart(autoStart);
        this.setStartLevel(startLevel);
    }

    public String getId() {
        return id;
    }

    @XmlAttribute
    public void setId(String id) {
        this.id = id;
    }

    @SuppressWarnings("unused")
    public Boolean getAutoStart() {
        return autoStart;
    }

    @XmlAttribute
    public void setAutoStart(Boolean autoStart) {
        this.autoStart = autoStart;
    }

    @SuppressWarnings("unused")
    public Integer getStartLevel() {
        return startLevel;
    }

    @XmlAttribute
    public void setStartLevel(Integer startLevel) {
        this.startLevel = startLevel;
    }
}
