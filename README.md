#WSO2 Carbon Maven Plugins

This repository provides supplementary maven plugins for WSO2 carbon developers.

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

* `destination`: Points to the `<CARBON_HOME>/repository/components/` directory.

 MANDATORY property.
 Example: `<destination>/home/Carbon/wso2carbon-4.4.0/repository/components</destination>`
 
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

### Copy to specific runtime
In Carbon 5, there are product-specific runtimes, which the developer will not know about at the time of creating the feature. In order to support dynamically copying files to the runtime location during feature installation, WSO2 has introduced a custom touchpoint. 

The following should be added to the p2.inf in order to copy a particular file to a runtime location:

> metaRequirements.0.namespace = org.eclipse.equinox.p2.iu
> metaRequirements.0.name = org.wso2.carbon.p2.touchpoint
> 
> instructions.configure = \
> org.wso2.carbon.p2.touchpoint.copy(source:${installFolder}/../lib/features/org.wso2.carbon.touchpoint.sample_${feature.version}/bin/,target:${installFolder}/../\<runtime\>/bin/, overwrite:true);\
> org.wso2.carbon.p2.touchpoint.copy(source:${installFolder}/../lib/features/org.wso2.carbon.touchpoint.sample_${feature.version}/conf/osgi/launch.properties,target:${installFolder}/../\<runtime\>/conf/osgi/launch.properties, overwrite:true);\

* `<runtime>`: which is replaced with the runtime name at the feature installation

This custom touchpoint should be available in the p2-repo. Therefore it should be added to `generate-repo` goal as below in the product generation:
 
        <plugin>
            <groupId>org.wso2.carbon.maven</groupId>
            <artifactId>carbon-feature-plugin</artifactId>
            <executions>
                <execution>
                    <id>p2-repo-generation</id>
                    <phase>package</phase>
                    <goals>
                        <goal>generate-repo</goal>
                    </goals>
                    <configuration>
                        <targetRepository>file:${basedir}/target/p2-repo</targetRepository>
                        <features>
                            <feature>
                                <id>org.wso2.carbon.p2.touchpoint.feature</id>
                                <version>${carbon.maven.version}</version>
                            </feature>
                        </features>
                    </configuration>
                </execution>
                ...
            </executions>
            ...
        </plugin>    
                
# How to Contribute
* Please report issues at [Issues](https://github.com/wso2/carbon-maven-plugins/issues).
* Send your pull requests to [master branch](https://github.com/wso2/carbon-maven-plugins/tree/master)

#Contact us
WSO2 developers can be contacted via the mailing lists:
* WSO2 Developers List : dev@wso2.org
* WSO2 Architecture List : architecture@wso2.org