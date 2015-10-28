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

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.wso2.maven.p2.repository.CatFeature;
import org.wso2.maven.p2.repository.Category;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;


/**
 * The class contains operations related to Categories, which is out of the scope for current set of tasks. Thus
 * the refactoring is done for a minimum for the moment.
 */
public class P2Utils {
    private static String[] matchList = new String[]{"perfect", "equivalent", "compatible", "greaterOrEqual", "patch",
            "optional"};
    private static final String DEFAULT_ENCODING = "UTF-8";

    /**
     * Traverse the given p2Inf file, find the last index of the property entries in the file and returns. This us
     * used when updating the p2inf with new properties.
     *
     * @param p2InfFile p2Inf Flie object
     * @return last index of the properties
     * @throws IOException throws if unable to read the p2Inf file
     */
    public static int getLastIndexOfProperties(File p2InfFile) throws IOException {
        int min = -1;
        if (p2InfFile.exists()) {

            try (InputStreamReader reader = new InputStreamReader(new FileInputStream(p2InfFile), DEFAULT_ENCODING);
                 BufferedReader in = new BufferedReader(reader)) {
                String line;
                while ((line = in.readLine()) != null) {
                    String[] split = line.split("=");
                    String[] split2 = split[0].split(Pattern.quote("."));
                    if (split2[0].equalsIgnoreCase("properties")) {
                        int index = Integer.parseInt(split2[1]);
                        if (index > min) {
                            min = index;
                        }
                    }
                }
            }
        }
        return min;
    }

    public static boolean isMatchString(String matchStr) {
        for (String match : matchList) {
            if (matchStr.equalsIgnoreCase(match)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Takes a match string and returns the correct matching string correspond to it
     *
     * @param matchStr matching string
     * @return the correct matching rule corresponds to the given matching string
     */
    public static String getMatchRule(String matchStr) {
        if (isPatch(matchStr)) {
            return "perfect";
        }
        for (String match : matchList) {
            if (matchStr.equalsIgnoreCase(match)) {
                return match;
            }
        }
        return null;
    }

    /**
     * Returns whether the given matching string matches the patch matching rule.
     *
     * @param matchStr matching string to check
     * @return whether the given matching rule matches with <i>patch</i> matching rule
     */
    public static boolean isPatch(String matchStr) {
        return matchStr.equalsIgnoreCase("patch");
    }

    /**
     * Create the category file.
     *
     * @param project      Maven project
     * @param categories   categories list
     * @param categoryFile category file
     * @throws ParserConfigurationException throws if fail to generate a manifest document
     * @throws TransformerException         throws when fail to transform the category file
     * @throws MojoExecutionException       throws if fail to process features
     */
    public static void createCategoryFile(MavenProject project, List categories, File categoryFile)
            throws ParserConfigurationException, TransformerException, MojoExecutionException {

        Map featureCategories = new HashMap();

        Document doc = getManifestDocument();
        Element rootElement = doc.getDocumentElement();

        if (rootElement == null) {
            rootElement = doc.createElement("site");
            doc.appendChild(rootElement);
        }

        for (Object object : categories) {
            if (object instanceof Category) {
                Category cat = (Category) object;
                Element categoryDef = doc.createElement("category-def");
                categoryDef.setAttribute("name", cat.getId());
                categoryDef.setAttribute("label", cat.getLabel());
                rootElement.appendChild(categoryDef);
                Element descriptionElement = doc.createElement("description");
                descriptionElement.setTextContent(cat.getDescription());
                categoryDef.appendChild(descriptionElement);
                ArrayList<CatFeature> processedFeatures = cat.getProcessedFeatures(project);
                for (CatFeature feature : processedFeatures) {
                    if (!featureCategories.containsKey(feature.getId() + feature.getVersion())) {
                        ArrayList list = new ArrayList();
                        featureCategories.put((feature.getId() + feature.getVersion()), list);
                        list.add(feature);
                    }
                    ArrayList list = (ArrayList) featureCategories.get(feature.getId() + feature.getVersion());
                    list.add(cat.getId());
                }
            }
        }

        for (Object key : featureCategories.keySet()) {
            Object object = featureCategories.get(key);
            if (object instanceof List) {
                List list = (List) object;
                CatFeature feature = (CatFeature) list.get(0);
                list.remove(0);

                Element featureDef = doc.createElement("feature");
                featureDef.setAttribute("id", feature.getId());
                featureDef.setAttribute("version", BundleUtils.getOSGIVersion(feature.getVersion()));
                for (Object catId : list) {
                    Element category = doc.createElement("category");
                    category.setAttribute("name", catId.toString());
                    featureDef.appendChild(category);
                }
                rootElement.appendChild(featureDef);
            }
        }

        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer;
            transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(categoryFile);
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            transformer.transform(source, result);
        } catch (TransformerConfigurationException e) {
            throw new TransformerConfigurationException("Unable to create feature manifest", e);
        } catch (TransformerException e) {
            throw new TransformerException("Unable to create feature manifest", e);
        }
    }

    /**
     * Returns a blank document.
     *
     * @return org.w3c.dom.Document object
     * @throws ParserConfigurationException throws when fail to build a new xml document
     */
    public static Document getManifestDocument() throws ParserConfigurationException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder;
        documentBuilder = documentBuilderFactory.newDocumentBuilder();
        return documentBuilder.newDocument();
    }
}
