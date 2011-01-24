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
package commandflow.builder.xml;

import java.io.File;
import java.net.URL;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.validation.Schema;

import commandflow.Command;
import commandflow.builder.BuilderException;
import commandflow.builder.CommandBuilder;
import commandflow.catalog.CommandCatalog;
import commandflow.io.ClassPathResource;
import commandflow.io.FileResource;
import commandflow.io.Resource;
import commandflow.io.URLResource;

/**
 * A builder that creates commands from one or more XML files.
 * <p>
 * Note: This class is not thread-safe.
 * @author elansma
 */
public class XmlCommandBuilder<C> implements CommandBuilder<C> {
    /** The command XML resources */
    private List<Resource> commandXmlResources;

    /** Holds the stack of created commands */
    private Deque<Command<C>> commandStack = new ArrayDeque<Command<C>>();

    /** The schema used to validate the command XML, not used if <code>null</code> */
    private Schema schema;

    /** The bound XML element builders */
    private Map<String, ElementBuilder<C>> elementBuilders;

    /** Holds the resources already processed, used to detect circular dependencies between resources */
    private Set<Resource> processedResources = new HashSet<Resource>();

    /**
     * Creates a new XML command builder
     */
    XmlCommandBuilder() {
        ; // no more
    }

    /** {@inheritDoc} */
    @Override
    public void build(CommandCatalog<C> catalog) {
        for (Resource commandResource : commandXmlResources) {
            build(commandResource, catalog);
        }
    }

    /**
     * Builds the given command resource
     * @param commandResource
     * @param catalog
     */
    private void build(Resource commandResource, CommandCatalog<C> catalog) {
        if (processedResources.contains(commandResource)) {
            throw new BuilderException("Circular dependecy detected to resource %s", commandResource);
        }
        processedResources.add(commandResource);
        // TODO
    }

    /**
     * Adds a XML command file to this builder
     * @param commandXmlFile a command XML file
     * @return this builder (for method chaining)
     */
    public XmlCommandBuilder<C> addCommandXml(File commandXmlFile) {
        commandXmlResources.add(new FileResource(commandXmlFile));
        return this;
    }

    /**
     * Adds a XML command resource to this builder
     * @param commandResource the command XML resource
     * @return this builder (for method chaining)
     */
    public XmlCommandBuilder<C> addCommandXml(URL commandResource) {
        commandXmlResources.add(new URLResource(commandResource));
        return this;
    }

    /**
     * Adds a XML command classpath resource to this builder
     * @param name the command XML classpath resource, syntax same as {@link ClassLoader#getResource(String)}
     * @return this builder (for method chaining)
     */
    public XmlCommandBuilder<C> addCommandXml(String name) {
        commandXmlResources.add(new ClassPathResource(name));
        return this;
    }

    /**
     * Adds a XML command resource to this builder
     * @param resource the XML command resource
     * @return this builder (for method chaining)
     */
    public XmlCommandBuilder<C> addCommandXml(Resource resource) {
        commandXmlResources.add(resource);
        return this;
    }

    /**
     * Sets the schema to use for command validation.
     * @param schema the schema, if <code>null</code> schema validation will be disabled
     * @return this builder (for method chaining)
     */
    public XmlCommandBuilder<C> setCommandSchema(Schema schema) {
        this.schema = schema;
        return this;
    }

    /**
     * Adds a command element builder
     * @param elementName the element name this builder binds to, if the element name already has a binding it will be replaced by this binding
     * @param elementCommandBuilder the command element builder
     * @return this builder (for method chaining)
     */
    public XmlCommandBuilder<C> addElementBuilder(String elementName, ElementBuilder<C> elementCommandBuilder) {
        elementBuilders.put(elementName, elementCommandBuilder);
        return this;
    }

    /**
     * Clears all bindings.
     * <p>
     * This method is mainly intended to be used when a custom XML binding is needed and the default bindings should not be used.
     * @return this builder (for method chaining)
     */
    public XmlCommandBuilder<C> clearBindings() {
        elementBuilders.clear();
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public XmlCommandBuilder<C> clone() {
        // TODO
        return (XmlCommandBuilder<C>) null;
    }
}
