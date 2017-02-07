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

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Bean to represent launcher arguments.
 * This is used to
 * [1] Capture build-time product file launcher argument data from distribution pom file.
 * [2] Write launcher argument data to carbon.product file.
 */

@XmlRootElement
public class LauncherArgs { }
