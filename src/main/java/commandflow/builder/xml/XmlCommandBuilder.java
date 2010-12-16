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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Map;

import javax.xml.validation.Schema;

import commandflow.Command;
import commandflow.builder.CommandBuilder;
import commandflow.catalog.CommandCatalog;

/**
 * A builder that creates commands from one or more XML files.
 * <p>
 * Note: This class is not thread-safe.
 * @author elansma
 */
public class XmlCommandBuilder<C> implements CommandBuilder<C> {
    /** The command XML resources */
    private List<URL> commandXmlResources;

    /** Holds the stack of created commands */
    private Deque<Command<C>> commandStack = new ArrayDeque<Command<C>>();

    /** The schema used to validate the command XML, not used if <code>null</code> */
    private Schema schema;

    /** The bound XML element builders */
    private Map<String, XmlElementCommandBuilder<C>> elementBuilders;

    /**
     * Creates a new XML command builder with the default settings and builder bindings.
     */
    public XmlCommandBuilder() {

    }

    /** {@inheritDoc} */
    @Override
    public void build(CommandCatalog<C> catalog) {
    }

    /**
     * Adds a XML command file to this builder
     * @param commandXmlFile a command XML file
     * @return this builder (for method chaining)
     */
    public XmlCommandBuilder<C> addCommandXml(File commandXmlFile) {
        try {
            commandXmlResources.add(commandXmlFile.toURI().toURL());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    /**
     * Adds a XML command resource to this builder
     * @param commandResource the command XML resource
     * @return this builder (for method chaining)
     */
    public XmlCommandBuilder<C> addCommandXml(URL commandResource) {
        commandXmlResources.add(commandResource);
        return this;
    }

    /**
     * Adds a XML command classpath resource to this builder
     * @param name the command XML classpath resource, syntax same as {@link ClassLoader#getResource(String)}
     * @return this builder (for method chaining)
     */
    public XmlCommandBuilder<C> addCommandXml(String name) {
        commandXmlResources.add(getClass().getClassLoader().getResource(name));
        return this;
    }

    /**
     * Sets the schema to use for command validation.
     * <p>
     * If custom bindings are added via the {@link #addXmlElementCommandBuilder(String, XmlElementCommandBuilder)} method there are two approaches:
     * <ul>
     * <li>Disable the schema validation by setting the schema to <code>null</code></li>
     * <li>Provide an updated schema with rules for the custom XML element bindings</li>
     * </ul>
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
    public XmlCommandBuilder<C> addXmlElementCommandBuilder(String elementName, XmlElementCommandBuilder<C> elementCommandBuilder) {
        elementBuilders.put(elementName, elementCommandBuilder);
        return this;
    }
}
