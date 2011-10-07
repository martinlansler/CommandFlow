/**
 * Copyright 2010/2011, Martin Lansler
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
package org.codegility.commandflow.bind.xml;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import javax.xml.namespace.QName;

import org.codegility.commandflow.bind.BindingException;
import org.codegility.commandflow.io.DefaultResourceResolver;
import org.codegility.commandflow.io.Resource;
import org.codegility.commandflow.io.ResourceResolver;

/**
 * An element processor for an import element containing an reference to another command XML.
 * <p>
 * The current {@link XmlBindingHandler} is cloned and invoked to build the import command XML.
 * @author Martin Lansler
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

    @Override
    public void startProcessing() {
    }

    @Override
    public void startElement(XmlBindingHandler<C> handler, QName elementName, Map<String, String> attributes) {
        XmlBindingHandler<C> importHandler = handler.clone();
        try {
            URI resource = new URI(attributes.get(resourceAttribute));
            Resource importResource;
            if (resource.isAbsolute()) {
                importResource = resourceResolver.resolve(resource);
            } else {
                importResource = handler.getCurrentCommandXml().resolveRelative(resource);
            }
            if (importResource == null) {
                throw new BindingException("Could not find import resource %s", resource);
            }
            importHandler.addCommandResource(importResource);
            importHandler.build(handler.getCommandCatalog());
        } catch (URISyntaxException e) {
            throw new BindingException(e);
        }
    }

    @Override
    public void endElement(XmlBindingHandler<C> handler, QName elementName) {
        // not used
    }

    /**
     * @param resourceResolver the resource resolver to use for absolute resources
     */
    public void setResourceResolver(ResourceResolver resourceResolver) {
        this.resourceResolver = resourceResolver;
    }

    @Override
    public void endProcessing() {
    }

    @Override
    public ImportElementProcessor<C> clone() {
        return new ImportElementProcessor<C>(resourceAttribute);
    }

}
