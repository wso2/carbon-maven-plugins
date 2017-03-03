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

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Bean to represent a property.
 *
 * This is used to
 * [1] Capture build-time product file property data from distribution pom file.
 * [2] Write property data to carbon.product file.
 *
 * @since 3.1.0
 */

@XmlRootElement
public class Property {

    private String name;

    private Boolean value;

    @SuppressWarnings("unused")
    public Property() {}

    public Property(String name, Boolean value) {
        this.setName(name);
        this.setValue(value);
    }

    public String getName() {
        return name;
    }

    @XmlAttribute
    public void setName(String name) {
        this.name = name;
    }

    public Boolean getValue() {
        return value;
    }

    @XmlAttribute
    public void setValue(Boolean value) {
        this.value = value;
    }

}
