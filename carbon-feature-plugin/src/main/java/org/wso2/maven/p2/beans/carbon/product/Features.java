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
 * Bean to represent features.
 *
 * This is used to write features data to
 * carbon.product file.
 *
 * @since 3.1.0
 */

@XmlRootElement
public class Features {

    private List<Feature> features;

    @SuppressWarnings("unused")
    public Features() {}

    public Features(List<Feature> features) {
        this.setFeatures(features);
    }

    public List<Feature> getFeatures() {
        return features;
    }

    @XmlElement(name = "feature")
    public void setFeatures(List<Feature> features) {
        this.features = features;
    }

}
