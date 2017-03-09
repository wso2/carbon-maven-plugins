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
import org.wso2.maven.p2.beans.product.Configurations;
import org.wso2.maven.p2.beans.product.Product;
import org.wso2.maven.p2.beans.product.config.ProductConfig;
import org.wso2.maven.p2.beans.product.config.ProductFileConfig;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
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
    public static void generateProductFile(ProductFileConfig productFileConfig,
                                           MavenProject mavenProject)
                                           throws JAXBException, MojoExecutionException, IOException {
        // validating version and featureConfig values captured from project pom
        ProductConfig validatedProductConfig = ProductFileUtils
                .validateProductConfig(productFileConfig.getProductConfig(), mavenProject);

        // populating a product instance with validated values for writing to a product file
        Product product = new Product();

        product.setName(validatedProductConfig.getName());
        product.setUid(validatedProductConfig.getUid());
        product.setId(validatedProductConfig.getId());
        product.setApplication(validatedProductConfig.getApplication());
        product.setVersion(validateProductVersion(validatedProductConfig.getVersion()));
        product.setIncludeLaunchers(validatedProductConfig.getIncludeLaunchers());
        product.setConfigIni(validatedProductConfig.getConfigIni());
        product.setLauncher(validatedProductConfig.getLauncher());
        product.setConfigurations(new Configurations(validatedProductConfig.getPluginConfig(),
                validatedProductConfig.getPropertyConfig()));

        // writing to a product file
        StringWriter stringWriter = new StringWriter();
        stringWriter.append("<?xml version=\"").append(P2Constants.ProductFile.XML_VERSION.toString())
                    .append("\" encoding=\"").append(P2Constants.DEFAULT_ENCODING).append("\"?>")
                    .append(System.lineSeparator())
                    .append("<?pde version=\"").append(productFileConfig.getPdeVersion().toString()).append("\"?>")
                    .append(System.lineSeparator());

        JAXBContext jaxbContext = JAXBContext.newInstance(Product.class);
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

        jaxbMarshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        jaxbMarshaller.marshal(product, stringWriter);

        Files.write(Paths.get(ProductFileUtils.getProductFilePath(mavenProject)),
                stringWriter.toString().getBytes(Charset.forName(P2Constants.DEFAULT_ENCODING)));
    }

    public static String getProductFilePath(MavenProject mavenProject) throws MojoExecutionException {
        if (mavenProject == null) {
            throw new MojoExecutionException("Unable to read maven project for finding project path.");
        } else {
            return mavenProject.getBasedir().getAbsolutePath() + File.separator +
                P2Constants.ProductFile.TARGET_DIRECTORY + File.separator + P2Constants.ProductFile.NAME;
        }
    }

    private static ProductConfig
        validateProductConfig(ProductConfig productConfig, MavenProject mavenProject)
            throws MojoExecutionException {
        if (productConfig.getVersion() == null) {
            productConfig.setVersion(ProductFileUtils.getDependencyVersion(mavenProject,
                    P2Constants.ProductFile.Product.RUNTIME_FEATURE));
        }
        return productConfig;
    }

    private static String validateProductVersion(String version) {
        if (version != null && !version.isEmpty() &&
                version.contains(P2Constants.ProductFile.Feature.VERSION_CHAR_REPLACED)) {
            version = version.replaceAll(P2Constants.ProductFile.Feature.VERSION_CHAR_REPLACED,
                    P2Constants.ProductFile.Feature.VERSION_CHAR_REPLACEMENT);
        }
        return version;
    }

    private static String getDependencyVersion(MavenProject mavenProject,
                                               String dependency) throws MojoExecutionException {
        if (mavenProject == null) {
            throw new MojoExecutionException("Unable to read maven project for finding dependencies.");
        } else if (dependency == null || dependency.isEmpty()) {
            throw new MojoExecutionException("Unable to find version for invalid dependency value.");
        } else {
            Artifact selectedArtifact = mavenProject.getDependencyArtifacts()
                    .stream()
                    .filter(artifact -> artifact.getArtifactId().contains(dependency))
                    .findFirst()
                    .orElseThrow(() -> new MojoExecutionException("Unable to find version as there seems to be " +
                            "no pom based configuration for the provided dependency."));

            String dependencyVersion = selectedArtifact.getBaseVersion();
            if (dependencyVersion == null || dependencyVersion.isEmpty()) {
                throw new MojoExecutionException("Unable to find version for the provided dependency.");
            }
            return dependencyVersion;
        }
    }
}
