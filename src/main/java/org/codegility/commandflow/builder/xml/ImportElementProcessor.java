/**
 * Copyright 2010 Martin Lansler (elansma), Anders Jacobsson
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.codegility.commandflow.builder.xml;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import javax.xml.namespace.QName;

import org.codegility.commandflow.builder.BuilderException;
import org.codegility.commandflow.io.DefaultResourceResolver;
import org.codegility.commandflow.io.Resource;
import org.codegility.commandflow.io.ResourceResolver;

/**
 * An element processor for an import element containing an reference to another command XML.
 * <p>
 * The current {@link XmlCommandBuilder} is cloned and invoked to build the import command XML.
 * @author elansma
 */
public class ImportElementProcessor<C> implements XmlElementProcessor<C> {
    /** The name of the attribute naming the import resource */
    private String resourceAttribute;

    /** The resource resolver for absolute resources */
    private ResourceResolver resourceResolver;

    public ImportElementProcessor(String resourceAttribute) {
        this.resourceAttribute = resourceAttribute;
        this.resourceResolver = new DefaultResourceResolver();
    }

    /** {@inheritDoc} */
    @Override
    public void startElement(XmlCommandBuilder<C> xmlCommandBuilder, QName elementName, Map<String, String> attributes) {
        XmlCommandBuilder<C> importCommandBuilder = xmlCommandBuilder.clone();
        try {
            URI resource = new URI(attributes.get(resourceAttribute));
            Resource importResource;
            if (resource.isAbsolute()) {
                importResource = resourceResolver.resolve(resource);
            } else {
                importResource = xmlCommandBuilder.getCurrentCommandXml().resolveRelative(resource);
            }
            if (importResource == null) {
                throw new BuilderException("Could not find import resource %s", resource);
            }
            importCommandBuilder.addCommandXml(importResource);
            importCommandBuilder.build(xmlCommandBuilder.getCommandCatalog());
        } catch (URISyntaxException e) {
            throw new BuilderException(e);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void endElement(XmlCommandBuilder<C> xmlCommandBuilder, QName elementName) {
        // not used
    }

    /**
     * @param resourceResolver the resource resolver to use for absolute resources
     */
    public void setResourceResolver(ResourceResolver resourceResolver) {
        this.resourceResolver = resourceResolver;
    }

}
