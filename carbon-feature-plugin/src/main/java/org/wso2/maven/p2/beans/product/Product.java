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

import org.wso2.maven.p2.beans.product.config.ProductConfig;
import org.wso2.maven.p2.utils.P2Constants;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Bean to represent a product.
 *
 * This is used to write product data to carbon.product file.
 *
 * @since 3.1.0
 */
@XmlRootElement
public class Product extends ProductConfig {

    private Boolean useFeatures = P2Constants.ProductFile.Product.USE_FEATURES;
    private Configurations configurations;

    @SuppressWarnings("unused")
    public Boolean getUseFeatures() {
        return useFeatures;
    }

    @XmlAttribute
    @SuppressWarnings("unused")
    public void setUseFeatures(Boolean useFeatures) {
        this.useFeatures = useFeatures;
    }

    @SuppressWarnings("unused")
    public Configurations getConfigurations() {
        return configurations;
    }

    @XmlElement
    public void setConfigurations(Configurations configurations) {
        this.configurations = configurations;
    }
}
