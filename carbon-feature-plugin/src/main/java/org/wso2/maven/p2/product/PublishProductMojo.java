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
package org.wso2.maven.p2.product;


import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.eclipse.sisu.equinox.launching.internal.P2ApplicationLauncher;
import org.wso2.maven.p2.beans.carbon.product.ConfigIni;
import org.wso2.maven.p2.beans.carbon.product.Configurations;
import org.wso2.maven.p2.beans.carbon.product.Feature;
import org.wso2.maven.p2.beans.carbon.product.Features;
import org.wso2.maven.p2.beans.carbon.product.Launcher;
import org.wso2.maven.p2.beans.carbon.product.Plugin;
import org.wso2.maven.p2.beans.carbon.product.Product;
import org.wso2.maven.p2.beans.carbon.product.Property;
import org.wso2.maven.p2.beans.carbon.product.config.BuildTimeProductFileConfig;
import org.wso2.maven.p2.beans.carbon.product.config.ProductConfig;
import org.wso2.maven.p2.utils.P2ApplicationLaunchManager;
import org.wso2.maven.p2.utils.P2Constants;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

/**
 * Publish a given product using the .product file to the repository.
 *
 * @since 2.0.0
 */
@Mojo(name = "publish-product")
public class PublishProductMojo extends AbstractMojo {

    @Parameter(defaultValue = "${project}")
    protected MavenProject project;

    @Parameter
    private URL repositoryURL;

    @Parameter
    private String executable;

    /**
     * The product configuration, a .product file. This file manages all aspects
     * of a product definition from its constituent plug-ins to configuration
     * files to branding.
     */
    @Parameter
    private File productConfigurationFile;

    /**
     * Bean to capture build-time product file configurations
     * from distribution pom.
     */
    @Parameter
    private BuildTimeProductFileConfig buildTimeProductFileConfig;

    @Component
    private P2ApplicationLauncher launcher;

    /**
     * Kill the forked test process after a certain number of seconds. If set to 0, wait forever for
     * the process, never timing out.
     */
    @Parameter(defaultValue = "${p2.timeout}")
    private int forkedProcessTimeoutInSeconds;

    /**
     * Overridden method of AbstractMojo class. This is picked up by the maven runtime for execution.
     *
     * @throws MojoExecutionException throws when any runtime exception occurs. i.e: fail to read write file, fail to
     *                                parse a configuration xml
     * @throws MojoFailureException   throws when the tool breaks for any configuration issues
     */
    public void execute() throws MojoExecutionException, MojoFailureException {
        try {
            publishProduct();
        } catch (IOException e) {
            throw new MojoExecutionException("Cannot generate P2 metadata", e);
        }
    }

    /**
     * Validate buildTimeProductFileConfig for required values and update corresponding property
     * with default configurations when optional values are not present in distribution pom.
     *
     * @throws MojoFailureException throws when the tool breaks for any configuration issues
     */
    private void validateBuildTimeProductFileConfig() throws MojoFailureException {
        if (buildTimeProductFileConfig == null) {
            throw new MojoFailureException("Cannot Proceed as build-time product file " +
                    "configurations are not set in distribution pom.");
        }

        if (buildTimeProductFileConfig.getProductFilePath() == null ||
                buildTimeProductFileConfig.getProductFilePath().isEmpty()) {
            throw new MojoFailureException("Cannot Proceed as productFilePath under build-time product file " +
                    "configurations is not set in distribution pom.");
        }

        if (buildTimeProductFileConfig.getPdeVersion() == null ||
                buildTimeProductFileConfig.getPdeVersion() == 0.0F) {
            buildTimeProductFileConfig.setPdeVersion(P2Constants.ProductFile.PDE_VERSION_DEFAULT);
        }

        ProductConfig productConfig = buildTimeProductFileConfig.getProductConfig();

        if (productConfig == null) {
            throw new MojoFailureException("Cannot Proceed as ProductConfig under build-time product file " +
                    "configurations is not set in distribution pom.");
        }

        if (productConfig.getVersion() == null || productConfig.getVersion().isEmpty()) {
            throw new MojoFailureException("Cannot Proceed as required value productConfig -> version under " +
                    "build-time product file configurations is not set in distribution pom.");
        }

        /**
         * If all above values are set with appropriate values,
         * everything else is optional and if not present,
         * default values will be set as of the specification.
         */

        if (productConfig.getName() == null || productConfig.getName().isEmpty()) {
            productConfig.setName(P2Constants.ProductFile.PRODUCT_NAME_DEFAULT);
        }
        if (productConfig.getUid() == null || productConfig.getUid().isEmpty()) {
            productConfig.setUid(P2Constants.ProductFile.PRODUCT_UID_DEFAULT);
        }
        if (productConfig.getId() == null || productConfig.getId().isEmpty()) {
            productConfig.setId(P2Constants.ProductFile.PRODUCT_ID_DEFAULT);
        }
        if (productConfig.getApplication() == null || productConfig.getApplication().isEmpty()) {
            productConfig.setApplication(P2Constants.ProductFile.APPLICATION_DEFAULT);
        }
        if (productConfig.getUseFeatures() == null) {
            productConfig.setUseFeatures(P2Constants.ProductFile.USE_FEATURES_DEFAULT);
        }
        if (productConfig.getIncludeLaunchers() == null) {
            productConfig.setIncludeLaunchers(P2Constants.ProductFile.INCLUDE_LAUNCHERS_DEFAULT);
        }
        if (productConfig.getConfigIni() == null) {
            ConfigIni configIni = new ConfigIni();
            configIni.setUse(P2Constants.ProductFile.CONFIG_INI_USE_DEFAULT);
            productConfig.setConfigIni(configIni);
        }
        if (productConfig.getLauncher() == null) {
            Launcher launcher = new Launcher();
            launcher.setName(P2Constants.ProductFile.LAUNCHER_NAME_DEFAULT);
            productConfig.setLauncher(launcher);
        }
        if (productConfig.getFeatureConfig() == null) {
            List<Feature> productFeatures = new ArrayList<>();
            // adding default features with versions
            productFeatures.add(new Feature("org.wso2.carbon.runtime", productConfig.getVersion()));
            productConfig.setFeatureConfig(productFeatures);
        }
        if (productConfig.getPluginConfig() == null) {
            List<Plugin> plugins = new ArrayList<>();
            // adding default plugins wth default autoStart and startLevel configurations
            plugins.add(new Plugin("org.eclipse.core.runtime", true, 4));
            plugins.add(new Plugin("org.eclipse.equinox.common", true, 2));
            plugins.add(new Plugin("org.eclipse.equinox.ds", true, 2));
            plugins.add(new Plugin("org.eclipse.equinox.p2.reconciler.dropins", true, 4));
            plugins.add(new Plugin("org.eclipse.equinox.simpleconfigurator", true, 1));
            plugins.add(new Plugin("org.eclipse.update.configurator", true, 1));
            productConfig.setPluginConfig(plugins);
        }
        if (productConfig.getPropertyConfig() == null) {
            List<Property> properties = new ArrayList<>();
            // adding default property, value pairs
            properties.add(new Property("org.eclipse.update.reconcile", false));
            properties.add(new Property("org.eclipse.equinox.simpleconfigurator.useReference", true));
            productConfig.setPropertyConfig(properties);
        }
    }

    /**
     * Get a product bean populated with pom based values
     * to be written to product file.
     *
     * @return A Product instance.
     * @throws MojoFailureException throws when the tool breaks for any configuration issues.
     */
    private Product getProductFromConfig() throws MojoFailureException {
        // validating values captured from distribution pom for buildTimeProductFileConfig
        validateBuildTimeProductFileConfig();

        Product product = new Product();
        ProductConfig productConfig = buildTimeProductFileConfig.getProductConfig();

        product.setName(productConfig.getName());
        product.setUid(productConfig.getUid());
        product.setId(productConfig.getId());
        product.setApplication(productConfig.getApplication());
        product.setVersion(productConfig.getVersion());
        product.setUseFeatures(productConfig.getUseFeatures());
        product.setIncludeLaunchers(productConfig.getIncludeLaunchers());
        product.setConfigIni(productConfig.getConfigIni());
        product.setLauncherArgs(productConfig.getLauncherArgs());
        product.setLauncher(productConfig.getLauncher());
        product.setPlugins(productConfig.getPlugins());

        Features features = new Features(productConfig.getFeatureConfig());

        Configurations configurations = new Configurations(productConfig.getPluginConfig(),
                productConfig.getPropertyConfig());

        product.setFeatures(features);
        product.setConfigurations(configurations);

        return product;
    }

    /**
     * Write Product bean as an XML to product file.
     *
     * @param product Product bean to be written to product file.
     * @param productFilename Name of the product file.
     * @throws javax.xml.bind.JAXBException Is thrown if an error occurs in writing configurations into product file.
     */
    private void writeToFile(Product product, String productFilename) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(Product.class);
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

        jaxbMarshaller.setProperty("com.sun.xml.internal.bind.xmlHeaders",
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<?pde version=\"" +
                        buildTimeProductFileConfig.getPdeVersion() + "\"?>");
        jaxbMarshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        File productFile = new File(buildTimeProductFileConfig.getProductFilePath(), productFilename);

        jaxbMarshaller.marshal(product, productFile);
        jaxbMarshaller.marshal(product, System.out);
    }

    /**
     * Perform the product publish action.
     *
     * @throws MojoFailureException     throws when the tool breaks for any configuration issues.
     * @throws IOException              throws if fail to read file canonical path.
     * @throws MojoExecutionException   throws when any runtime exception occurs.
     *                                  i.e: fail to read write file, fail to parse a configuration xml.
     */
    private void publishProduct() throws MojoFailureException, IOException, MojoExecutionException {
        P2ApplicationLaunchManager p2LaunchManager = new P2ApplicationLaunchManager(this.launcher);
        p2LaunchManager.setWorkingDirectory(project.getBasedir());
        p2LaunchManager.setApplicationName("org.eclipse.equinox.p2.publisher.ProductPublisher");

        if (productConfigurationFile != null) {
            p2LaunchManager.addPublishProductArguments(repositoryURL, productConfigurationFile, executable);
        } else if (buildTimeProductFileConfig != null) {
            try {
                writeToFile(getProductFromConfig(), P2Constants.ProductFile.FILE_NAME);
            } catch (JAXBException e) {
                throw new MojoExecutionException("Cannot proceed as there is an error in " +
                        "writing configurations to product file.", e);
            }
            File productFile = new File(buildTimeProductFileConfig.getProductFilePath(),
                    P2Constants.ProductFile.FILE_NAME);
            p2LaunchManager.addPublishProductArguments(repositoryURL, productFile, executable);
        } else {
            throw new MojoFailureException("Cannot Proceed as product file " +
                    "configurations are not set in distribution pom.");
        }
        p2LaunchManager.performAction(forkedProcessTimeoutInSeconds);
    }
}
