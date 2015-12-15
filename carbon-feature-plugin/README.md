carbon-feature-plugin
=====================

Carbon-Feature-Plugin (formerly known as carbon-p2-plugin) is a maven plugin developed within the WSO2 and maintained by WSO2 carbon team. The Carbon Feature Plugin is used extensively within WSO2 carbon platform to;
* Generate Carbon features
* Generate p2 repositories
* Publish a product into p2 repository
* Generate product Profiles
* Install carbon features into a product profile
* Uninstall carbon features from a product

These goals are achieved through 6 maven goals defined in the plugin;
* maven goal for generate carbon features 	: generate (formerly known as p2-feature-gen)
* maven goal for generate repositories		: generate-repo (formerly known as p2-repo-gen)
* maven goal for publishing a product		:publish-product
* maven goal for generate profiles		: generate-profile (formerly known as materialize-product)
* maven goal for install features into a product : install (formerly known as p2-profile-gen)
* maven goal for uninstalling features		: uninstall

All these goals are getting executed during the package phase of the default life cycle of maven build(except for generate maven goal, as it follow a different life cycle). The user has the flexibility to configure the behavior of the plugin by passing correct parameters to these maven goals.


##How to Contribute
* Please report issues at [WSO2 JIRA](https://wso2.org/jira/browse/Carbon).
* Send your pull requests to [master branch](https://github.com/wso2/carbon-maven-plugins/tree/master)

##Contact us
WSO2 developers can be contacted via the mailing lists:
* WSO2 Developers List : dev@wso2.org
* WSO2 Architecture List : architecture@wso2.org
