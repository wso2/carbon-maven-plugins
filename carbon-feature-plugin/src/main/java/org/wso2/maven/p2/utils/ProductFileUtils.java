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

package org.wso2.maven.p2.utils;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.wso2.maven.p2.beans.carbon.product.Configurations;
import org.wso2.maven.p2.beans.carbon.product.Feature;
import org.wso2.maven.p2.beans.carbon.product.Features;
import org.wso2.maven.p2.beans.carbon.product.Product;
import org.wso2.maven.p2.beans.carbon.product.config.ProductConfig;
import org.wso2.maven.p2.beans.carbon.product.config.ProductFileConfig;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

/**
 * Utility class for helper functions in generating and reading a product file at build-time.
 *
 * This class is to be used by publish-product and generate-runtime maven goals.
 *
 * @since 3.1.0
 */
public class ProductFileUtils {

    /**
     * Write Product bean as an XML to a product file.
     *
     * @param productFileConfig productFileConfig bean as read from project pom.
     * @param mavenProject mavenProject bean as read from project pom.
     * @throws javax.xml.bind.JAXBException Is thrown if an error occurs in writing configurations into product file.
     */
    public static void writeToFile(ProductFileConfig productFileConfig,
                                   MavenProject mavenProject) throws JAXBException, MojoExecutionException {
        // validating version and featureConfig values captured from project pom
        ProductConfig validatedProductConfig = ProductFileUtils.
                validateProductConfigForVersionAndFeatureConfig(productFileConfig.getProductConfig(), mavenProject);

        // populating a product instance with validated values for writing to a product file
        Product product = new Product();

        product.setName(validatedProductConfig.getName());
        product.setUid(validatedProductConfig.getUid());
        product.setId(validatedProductConfig.getId());
        product.setApplication(validatedProductConfig.getApplication());
        product.setVersion(validatedProductConfig.getVersion());
        product.setUseFeatures(validatedProductConfig.getUseFeatures());
        product.setIncludeLaunchers(validatedProductConfig.getIncludeLaunchers());
        product.setVersion(validatedProductConfig.getVersion());
        product.setConfigIni(validatedProductConfig.getConfigIni());
        product.setLauncherArgs(validatedProductConfig.getLauncherArgs());
        product.setLauncher(validatedProductConfig.getLauncher());
        product.setPlugins(validatedProductConfig.getPlugins());

        Features features = new Features(validatedProductConfig.getFeatureConfig());
        Configurations configurations = new Configurations(validatedProductConfig.getPluginConfig(),
                validatedProductConfig.getPropertyConfig());

        product.setFeatures(features);
        product.setConfigurations(configurations);

        // writing to a product file
        JAXBContext jaxbContext = JAXBContext.newInstance(Product.class);
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

        jaxbMarshaller.setProperty("com.sun.xml.bind.xmlHeaders",
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<?pde version=\"" +
                        productFileConfig.getPdeVersion() + "\"?>");
        jaxbMarshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        File productFile = new File(ProductFileUtils.
                getAbsoluteFilePath(mavenProject), P2Constants.ProductFileDefaults.NAME);
        jaxbMarshaller.marshal(product, productFile);
        jaxbMarshaller.marshal(product, System.out);
    }

    /**
     * Read dynamically generated product file as a new file instance.
     *
     * @param mavenProject mavenProject bean as read from distribution pom.
     * @return A product file.
     */
    public static File readFile(MavenProject mavenProject) throws MojoExecutionException {
        return new File(ProductFileUtils.
                getAbsoluteFilePath(mavenProject), P2Constants.ProductFileDefaults.NAME);
    }

    private static ProductConfig
    validateProductConfigForVersionAndFeatureConfig(ProductConfig productConfig, MavenProject mavenProject)
            throws MojoExecutionException {

        if (productConfig.getVersion() == null) {
            productConfig.setVersion(ProductFileUtils.getDependencyVersion(mavenProject,
                    P2Constants.ProductFileDefaults.Product.RUNTIME_FEATURE));
        }
        if (productConfig.getFeatureConfig() == null) {
            productConfig.setFeatureConfig(ProductFileUtils.getDefaultFeatureConfig(mavenProject));
        }

        return productConfig;
    }

    private static List<Feature> getDefaultFeatureConfig(MavenProject mavenProject) throws MojoExecutionException {
        List<Feature> features = new ArrayList<>();
        // adding default features with versions
        features.add(new Feature(P2Constants.ProductFileDefaults.Product.RUNTIME_FEATURE,
                ProductFileUtils.getDependencyVersion(mavenProject,
                        P2Constants.ProductFileDefaults.Product.RUNTIME_FEATURE)));
        return features;
    }

    private static String getDependencyVersion(MavenProject mavenProject,
                                               String dependency) throws MojoExecutionException {
        if (mavenProject == null) {
            throw new MojoExecutionException("Unable to read maven project for finding dependencies.");
        } else if (dependency == null || dependency.isEmpty()) {
            throw new MojoExecutionException("Unable to find version for invalid dependency value.");
        } else {
            Artifact artifact = mavenProject.getDependencyArtifacts().stream().
                    filter(a -> a.getArtifactId().contains(dependency)).findFirst().orElse(null);
            if (artifact == null) {
                throw new MojoExecutionException("Unable to find version as there seems to be " +
                        "no pom based configuration for the provided dependency.");
            }
            String dependencyVersion = artifact.getBaseVersion();
            if (dependencyVersion == null || dependencyVersion.isEmpty()) {
                throw new MojoExecutionException("Unable to find version for the provided dependency.");
            }
            return dependencyVersion;
        }
    }

    private static String getAbsoluteFilePath(MavenProject mavenProject) throws MojoExecutionException {
        if (mavenProject == null) {
            throw new MojoExecutionException("Unable to read maven project for finding project path.");
        } else {
            return mavenProject.getBasedir().getAbsolutePath() + File.separator +
                    P2Constants.ProductFileDefaults.TARGET_DIRECTORY;
        }
    }

}
