# Using the Carbon Feature Plugin

The Carbon feature plugin was formerly known as the Carbon P2 plugin. It has gone through several development cycles in the previous WSO2 Carbon versions. However, the plugin introduced from WSO2 Carbon 5 (Carbon 5.x.x platform) is redesigned to update the Maven APIs, to redefine the configurations by removing any existing inconsistencies, to make its architecture solid, and also to improve the understandability and maintainability of its coding standards.

## Introduction

The WSO2 Carbon feature plugin is a Maven plugin, which is used within the WSO2 Carbon platform. The Maven goals that are achieved through this plugin are explained below:

* `generate`: For generating Carbon features. Formerly known as `P2-feature-gen`. See the instructions on [configuring the `generate` Maven goal](#configuring-the-generate-maven-goal). 
* `generate-repo`: For generating P2 repositories. Formerly known as 'P2-repo-gen'. See the instructions on [configuring the `generate-repo` Maven goal](#configuring-the-generate-repo-maven-goal).
* `publish-product`: For publishing a product into a P2 repository. See the instructions on [configuring the `publish-product` Maven goal](#configuring-the-publish-product-maven-goal).
* `generate-runtime`: For generating product runtimes. Formerly known as `materialize-product`. See the instructions on [configuring the `generate-runtime` Maven goal](#configuring-the-generate-runtime-maven-goal).
* `install`: For installing Carbon features into a product runtime. Formerly known as `p2-profile-gen`. See the instructions on [configuring the install Maven goal](#configuring-the-install-maven-goal).
* `uninstall`: For uninstalling Carbon features from a product. See the instructions on [configuring the uninstall Maven goal](#configuring-the-uninstall-maven-goal).

All these goals (except the generate Maven goal) are executed during the package phase in the default life cycle of the Maven build. The generate Maven goal follows a different life cycle. You have the flexibility to configure the behavior of the plugin by passing the relevant parameters to these Maven goals.

## What is a Carbon feature?

A Carbon feature is an installable form of one or more logically related Carbon components. You can create a Carbon feature by grouping one or more existing Carbon features together as well. You can install these features into Carbon-based products using the feature manager in the management console of the product, or via the [`install` Maven goal](#configuring-the-install-maven-goal) of the Carbon feature plugin.

## What is a P2 repository?

A P2 repository is a collection of Carbon features. It acts as a feature aggregator. You can point a Carbon product to a P2 repository and install one or more feature(s) you find in that repository. Once a feature is installed, the feature is copied into the Carbon product.

## What is a product runtime?

A product runtime is a logical grouping of a set of features/components that creates a virtual boundary for execution. Every Carbon product has a default runtime shipped with it. With the Carbon feature plugin, you can create runtimes and install Carbon features to that runtime.

A Carbon product (WSO2 ESB, WSO2 AS etc.) is made by installing the relevant features on top of the Carbon kernel using the following steps:

1. Create a runtime on top of the Carbon kernel using the [generate-runtime Maven goal](#configuring-the-generate-runtime-maven-goal). 
2. Install the relevant features using the [`install` Maven goal](#configuring-the-install-maven-goal).

## Carbon feature plugin configurations

The following sections describe the configurations of the Maven goals that are implemented in the Carbon feature plugin. 

### Configuring the generate Maven goal

A sample pom.xml file configuration of the `generate` Maven goal is shown below.

            <feature>
               <id>org.wso2.ciphertool.feature.group</id>
               <version>4.4.0</version>
            </feature>
        </features>
 
 > Add the ID of the feature being installed as the value of the `<ID>` property, and add the version of the feature being installed as the value of the `<version>` property.
 
* `deleteOldRuntimeFiles`: Specifies whether to delete old *.profile folders located in the `<CARBON_HOME>/repository/components/p2/org.eclipse.equinox.p2.engine/profileRegistry/` directory. The default value is set to true.

 NOT MANDATORY.
 Example: `<deleteOldRuntimeFiles>false</deleteOldRuntimeFiles>`.
 
### Configuring the publish-product Maven goal

A sample pom.xml file configuration of the `publish-product` Maven goal is shown below.

      <execution>
                <id>publishing products</id>
                <phase>package</phase>
                <goals>
                        <goal>publish-product</goal>
                </goals>
                <configuration>
                        <!-- configurations related to this goal go under here -->
                                <!-- [1] product file related configurations : 
                                
                                        You can keep no configuration at all for this which would mean that
                                        plugin will generate a temporary product file during build-time with default 
                                        settings. But if <productConfigurationFile> element is present, that 
                                        will override any other configuration related to a product file and use any 
                                        already existing (static) product file located at a path specified as value 
                                        of the element. For example, please refer to the following. -->
                                        
                                            <productConfigurationFile>
                                                    ${basedir}/carbon.product
                                            </productConfigurationFile>

                                        <!-- following is optional and can be used if customization 
                                        is required for a dynamically generated product file during build-time. -->
                                        
                                            <productFileConfig>
                                                    <!-- see `productFileConfig` section for 
                                                    available customization options -->
                                            </productFileConfig>
                                        
                                <!-- [2] product-publishing executable related configurations : -->
                                
                                            <executable>
                                                    <!-- executable path is given here. -->
                                            </executable>
                                        
                                <!-- [3] Carbon feature repository based configurations : -->
                                
                                            <repositoryURL>
                                                    <!-- Carbon feature repository path is given here. -->
                                            </repositoryURL>
                </configuration>
      </execution>
      
You can add any mandatory or optional configuration elements related to this goal under \<configuration\> element as depicted above.
In the meantime, each element is described as follows.
 
1. `productConfigurationFile`: With respect to product file related configurations, this element is mandatory if you are choosing to refer 
    to any already existing (static) product file in publishing the product. The element will contain absolute path to such product file as value.
    
    Note: You can instead go ahead with referring to a dynamically generated product file during build-time in achieving "publish-product" goal.
    This can be achieved by having no configurations at all in the pom, so that a product file will be dynamically generated during build-time with
    default settings. This is an improvement added to carbon-feature-plugin such that no further manual intervention is required in updating 
    a statically maintained product file during build-time for the release process as in the case of \<productConfigurationFile\> setting.
    
2. `productFileConfig`: This is an optional configuration that you can set if and only if customization is required for a 
    dynamically generated product file during build-time over provided default settings.
    
    Available customization options:
    
    \<productFileConfig\>
    
        <!-- [1] pdeVersion (Number): Default value is 3.5. -->
        <pdeVersion>.....</pdeVersion>
        
        <productConfig>
        
                <!-- [2] name (String): If not set, default value is 'Carbon Product'. -->
                <name>.....</name>
            
                <!-- [3] uid (String): If not set, default value is 'carbon.product.id'. -->
                <uid>.....</uid>
            
                <!-- [4] application (String): If not set, default value is 'carbon.application'. -->
                <application>.....</application>
                
                <!-- [5] version (String): If not set, carbon runtime version will be taken as default value. -->
                <version>.....</version>
            
                <!-- [6] useFeatures (true or false): If not set, default value is 'true'. -->
                <useFeatures>.....</useFeatures>
            
                <!-- [7] includeLaunchers (true or false): If not set, default value is 'true'. -->
                <includeLaunchers>.....</includeLaunchers>
                
                <!-- [8] configIni: If not set, default value for use is 'default'. -->
                <configIni>
                        <use>.....</use>
                </configIni>
                
                <!-- [9] launcher: If not set, default value for name is 'eclipse'. -->
                <launcher>
                        <name>eclipse</name>
                </launcher>
                
                <!-- [10] featureConfig: If not set, by default, 
                featureConfig will hold 'org.wso2.carbon.runtime' feature. -->
                <featureConfig>
                        <feature>
                                <id>.....</id>
                                <version>.....</version>
                        </feature>
                        .....
                </featureConfig>
                
                <!-- [11] pluginConfig: If not set, by default, 
                pluginConfig will hold 6 plugins, namely org.eclipse.core.runtime, org.eclipse.equinox.common,
                org.eclipse.equinox.ds, org.eclipse.equinox.p2.reconciler.dropins, org.eclipse.equinox.simpleconfigurator,
                and org.eclipse.update.configurator. -->
                <pluginConfig>
                        <plugin>
                                <id>.....</id>
                                <autoStart>.....</autoStart>
                                <startLevel>.....</startLevel>
                        </plugin>
                        .....                    
                        <plugin></plugin>
                </pluginConfig>
                
                <!-- [12] propertyConfig: If not set, by default, 
                propertyConfig will hold 2 properties, namely org.eclipse.update.reconcile and 
                org.eclipse.update.recon. -->
                <propertyConfig>
                        <property>
                                <name>.....</name>
                                <value>.....</value>
                        </property>
                        .....
                </propertyConfig>
        </productConfig>
        
    \</productFileConfig\>
    
4. `executable`: product-publishing executable related configurations.

        <executable>
                <!-- product-publishing executable path is given here. -->
        </executable>
    
5. `repositoryURL`: Carbon feature repository based configurations.

        <repositoryURL>
                <!-- Carbon feature repository path is given here. -->
        </repositoryURL>

### Configuring the generate-runtime Maven goal

A sample pom.xml file configuration of the `generate-runtime` Maven goal is shown below.

      <execution>
                <id>materialize-product</id>
                <phase>package</phase>
                <goals>
                        <goal>generate-runtime</goal>
                </goals>
                <configuration>
                        <!-- configurations related to this goal go under here -->
                                <!-- [1] product file related configurations : 
                                
                                In here, you can either have no configuration at all or
                                <productConfigurationFile> setting.
                                
                                <productConfigurationFile> element is mandatory 
                                if you are choosing to refer to any already existing (static) product file 
                                in generating a runtime. The element will contain absolute path to 
                                such product file as value. 
                                
                                And, having no configuration would mean that you are referring to a dynamically
                                generated product file during build-time in generating a runtime.
                                
                                        <productConfigurationFile>
                                                ${basedir}/carbon.product
                                        </productConfigurationFile>
                                        
                                        <!-- or nothing. -->
                                        
                                <!-- [2] Carbon feature repository based configurations : -->
                                        <repositoryURL>
                                                <!-- Carbon feature repository path is given here. -->
                                        </repositoryURL>
                                
                                <!-- [3] Target location for the carbon features to be copied in server pack : -->
                                        <targetPath>
                                                <!-- Target location for the carbon features to be copied 
                                                in server pack is given here. -->
                                        </targetPath>
                                        
                                <!-- [4] Runtime type : -->
                                        <runtime>
                                                <!-- Runtime type is given here. -->
                                        </runtime>
                </configuration>
      </execution>
      
You can add any mandatory or optional configuration elements related to this goal under \<configuration\> element as depicted above.
In the meantime, each element is described as follows.

1. `productConfigurationFile`: With respect to product file related configurations, this element is mandatory if you are choosing to refer 
    to any statically existing product file in generating-runtime. The element will contain absolute path to such product file with the name of the file.
    
2. `repositoryURL`: Carbon feature repository based configurations.

        <repositoryURL>
                <!-- Carbon feature repository path is given here. -->
        </repositoryURL>
        
3. `targetPath`: Target location for the carbon features to be copied in server pack.

        <targetPath>
                <!-- Target location for the carbon features to be copied in server pack is given here. -->
        </targetPath>
    
4. `runtime`: Runtime type.
    
        <runtime>
                <!-- Runtime type is given here. -->
        </runtime>

### Configuring the uninstall Maven goal

A sample pom.xml file configuration of the uninstall Maven goal is shown below.

      <build>
    	<plugins>
        	<plugin>
            	<groupId>org.wso2.carbon.maven</groupId>
            	<artifactId>carbon-feature-plugin</artifactId>
            	<version>${carbon.feature.plugin.version}</version>
            	<executions>
                	<execution>
                    	<id>p2-feature-generation</id>
                    	<phase>package</phase>
                    	<goals>
                        	<goal>uninstall</goal>
                    	</goals>
                    	<configuration>
                        	//plugin configuration goes here.
                    	</configuration>
                	</execution>
            	</executions>
        	</plugin>
            </plugins>
          </build>
	  
You can modify the above file to add the configurations of the plugin by adding the following parameters within the `<configuration>` element of it. 

* `destination`: Points to the `<CARBON_HOME>/wso2` directory.

 MANDATORY property.
 Example: `<destination>/home/Carbon/wso2carbon-5.2.0/wso2</destination>`
 
* `runtime`: the runtime in the destination from where you need to uninstall the features. 

 MANDATORY property.
 Example: `<runtime>default</runtime>`.
 
* `features`: List of features to be uninstalled from the destination runtime.

 MANDATORY
 Example:
 
       <features>
             <feature>
                <id>org.wso2.carbon.registry.contentsearch.feature.group</id>
                <version>4.4.0</version>
             </feature>
             <feature>
                <id>org.wso2.ciphertool.feature.group</id>
                <version>4.4.0</version>
             </feature>
        </features>
        

## P2 Touchpoint
### Equinox P2 Touchpoint
Features can have configuration files that need to be copied to a particular location when installing the feature. This can be done by specifying the location in the p2.inf in the feature. For example:
Example:
instructions.configure = \
org.eclipse.equinox.p2.touchpoint.natives.copy(source:${installFolder}/../lib/features/org.wso2.carbon.kernel_${feature.version}/launcher/,target:${installFolder}/../../bin/bootstrap/,overwrite:true);\

All equinox p2 touchpoint are available at [Provisioning Actions and Touchpoints](http://help.eclipse.org/neon/index.jsp?topic=/org.eclipse.platform.doc.isv/guide/p2_actions_touchpoints.html)
