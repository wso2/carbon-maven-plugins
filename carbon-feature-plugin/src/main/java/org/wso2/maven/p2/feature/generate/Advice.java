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

package org.wso2.maven.p2.feature.generate;

/**
 * Bean class to represent an AdviceFile Entry.
 *
 * @since 2.0.0
 */
public class Advice {
    private String name;
    private String value;

    /**
     * Returns the name of the AdviceFile key.
     *
     * @return String AdviceFile key
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the value for a give AdviceFile key.
     *
     * @return String value for a key
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the name of the AdviceFile key.
     *
     * @param name String
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the value for a give AdviceFile key.
     *
     * @param value String
     */
    public void setValue(String value) {
        this.value = value;
    }
}
