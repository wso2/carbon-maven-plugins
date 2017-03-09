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
package org.wso2.maven.p2.utils;

/**
 * Contains P2 related constants. File is kept to preserve the history in case if needed later.
 *
 * @since 1.0.0
 */
public class P2Constants {

    /**
     * Prevent instantiating the Constants class.
     */
    private P2Constants() {}

    public static final String DEFAULT_PROFILE_ID = "WSO2CarbonProfile";
    public static final String P2_DIRECTORY = "@config.dir/../lib/p2/";
    public static final String LIB = "lib";
    public static final String P2 = "p2";
    public static final String DEFAULT_ENCODING = "UTF-8";

    /**
     * Launcher Constants.
     */
    public static class Launcher {
        public static final String METADATA_REPOSITORY = "-metadataRepository";
        public static final String ARTIFACT_REPOSITORY = "-artifactRepository";
        public static final String DESTINATION = "-destination";
        public static final String BUNDLEPOOL = "-bundlepool";
        public static final String SHARED = "-shared";
        public static final String PROFILE = "-profile";
        public static final String PROFILE_PROPERTIES = "-profileProperties";
        public static final String ECLIPSE_UPDATE_FEATURE_TRUE = "org.eclipse.update.install.features=true";
        public static final String ROAMING = "-roaming";
        public static final String INSTALLIU = "-installIU";
    }

    /**
     * Constants class related to product file default settings.
     */
    public static class ProductFile {
        public static final String NAME = "carbon.product";
        public static final String TARGET_DIRECTORY = "target";
        public static final Float XML_VERSION = 1.0F;
        public static final Float PDE_VERSION = 3.5F;

        /**
         * Constants class related to Product element default settings of product file.
         */
        public static class Product {
            public static final String NAME = "Carbon Product";
            public static final String UID = "carbon.product.id";
            public static final String ID = "carbon.product";
            public static final String APPLICATION = "carbon.application";
            public static final String RUNTIME_FEATURE = "org.wso2.carbon.runtime";
            public static final Boolean USE_FEATURES = false;
            public static final Boolean INCLUDE_LAUNCHERS = true;
            public static final String CONFIG_INI_USE = "default";
            public static final String LAUNCHER_NAME = "eclipse";
        }

        /**
         * Constants class related to default character replacement settings of
         * version values appearing in a product file.
         */
        public static class Feature {
            public static final String VERSION_CHAR_REPLACEMENT = ".";
            public static final String VERSION_CHAR_REPLACED = "-";
        }
    }
}
