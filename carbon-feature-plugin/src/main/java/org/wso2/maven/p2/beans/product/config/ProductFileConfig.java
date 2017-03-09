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

import org.wso2.maven.p2.utils.P2Constants;

/**
 * Bean to represent build-time product file configurations.
 *
 * This is used to capture build-time product file configuration
 * data from distribution pom file.
 *
 * @since 3.1.0
 */
public class ProductFileConfig {

    private Float pdeVersion = P2Constants.ProductFile.PDE_VERSION;
    private ProductConfig productConfig = new ProductConfig();

    public Float getPdeVersion() {
        return pdeVersion;
    }

    @SuppressWarnings("unused")
    public void setPdeVersion(Float pdeVersion) {
        this.pdeVersion = pdeVersion;
    }

    public ProductConfig getProductConfig() {
        return productConfig;
    }

    @SuppressWarnings("unused")
    public void setProductConfig(ProductConfig productConfig) {
        this.productConfig = productConfig;
    }
}
