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

import static javax.xml.stream.XMLStreamConstants.END_ELEMENT;
import static javax.xml.stream.XMLStreamConstants.START_ELEMENT;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.stax.StAXSource;
import javax.xml.validation.Schema;

import commandflow.Command;
import commandflow.builder.BuilderException;
import commandflow.builder.CommandBuilder;
import commandflow.builder.CompositeCommand;
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
    private Deque<Command<C>> commandStack;

    /** The schema used to validate the command XML, not used if <code>null</code> */
    private Schema schema;

    /** The bound XML element builders */
    private Map<String, ElementBuilder<C>> elementBuilders;

    /** Holds the resources already processed, used to detect circular dependencies between resources */
    private Set<Resource> processedResources;

    /** The command catalog */
    private CommandCatalog<C> catalog;

    /** StAX factory */
    private static final XMLInputFactory xmlInputFactory;
    static {
        xmlInputFactory = XMLInputFactory.newInstance();
    }

    /** {@inheritDoc} */
    @Override
    public void build(CommandCatalog<C> catalog) {
        init(catalog);
        for (Resource commandResource : commandXmlResources) {
            build(commandResource);
        }
    }

    /**
     * Initializes
     * @param catalog the catalog to initialize the builder for
     */
    private void init(CommandCatalog<C> catalog) {
        this.catalog = catalog;
        commandStack = new ArrayDeque<Command<C>>();
        elementBuilders = new HashMap<String, ElementBuilder<C>>();
        processedResources = new HashSet<Resource>();
    }

    /**
     * Builds the given command resource
     * @param commandResource the resource to build
     */
    private void build(Resource commandResource) {
        if (processedResources.contains(commandResource)) {
            throw new BuilderException("Circular dependecy detected to resource %s", commandResource);
        }
        processedResources.add(commandResource);
        validate(commandResource);
        parseCommandXML(commandResource);
    }

    /**
     * Parses the given command XML and builds the contained commands
     * @param commandResource the command XML
     */
    private void parseCommandXML(Resource commandResource) {
        try {
            XMLStreamReader reader = xmlInputFactory.createXMLStreamReader(commandResource.getInputStream());
            while (reader.hasNext()) {
                parseCommandElement(reader);
            }
        } catch (Exception e) {
            throw new BuilderException(e);
        }
    }

    /**
     * Parses a given command element, either a start element or end element tag
     * @param reader the stream reader
     * @throws XMLStreamException
     */
    private void parseCommandElement(XMLStreamReader reader) throws XMLStreamException {
        switch (reader.nextTag()) {
        case START_ELEMENT:
            String elementName = reader.getName().getLocalPart();
            Map<String, String> attributes = getAttributes(reader);
            Command<C> command = buildCommand(elementName, attributes);
            wireCommandAndPushToStack(getCommandName(attributes), command);
            break;
        case END_ELEMENT:
            popCommandFromStack();
            break;
        default:
            throw new BuilderException("Unexpected tag type %s", reader.getEventType());
        }
    }

    /**
     * Pops the top command from the stack
     */
    private void popCommandFromStack() {
        commandStack.pop();
    }

    /**
     * Gets the name of a command from its element attributes
     * @return the name of the command, <code>null</code> if no name is found
     */
    private String getCommandName(Map<String, String> attributes) {
        return attributes.get(attributes.get("name")); // TODO: make lookup interface
    }

    /**
     * Wires the command by adding it to its parent (if any) and pushes it onto the command stack
     * @param elementName the command name
     * @param command
     */
    private void wireCommandAndPushToStack(String elementName, Command<C> command) {
        if (commandStack.isEmpty()) {
            catalog.addCommand(elementName, command);
        } else {
            addToCompositeParent(command);
        }
        commandStack.push(command);
    }

    /**
     * Builds the specified command
     * @param elementName name of the command element
     * @param attributes the command attributes
     * @return
     */
    private Command<C> buildCommand(String elementName, Map<String, String> attributes) {
        ElementBuilder<C> builder = getCommandBuilder(elementName);
        return builder.build(this, elementName, attributes);
    }

    /**
     * @return the command builder for the given element name
     */
    private ElementBuilder<C> getCommandBuilder(String elementName) {
        ElementBuilder<C> builder = elementBuilders.get(elementName);
        if (builder == null) {
            throw new BuilderException("Cannot find builder for element %s", elementName);
        }
        return builder;
    }

    /**
     * Gets the attribute map of the current reader element
     * @param reader the reader
     * @return the attribute map
     */
    private Map<String, String> getAttributes(XMLStreamReader reader) {
        Map<String, String> attributes = new HashMap<String, String>();
        for (int i = 0; i < reader.getAttributeCount(); i++) {
            attributes.put(reader.getAttributeLocalName(i), reader.getAttributeValue(i));
        }
        return attributes;
    }

    /**
     * Pushes a command to the top composite command, if the top command is not composite a runtime error is raised.
     * @param command the command to push
     */
    private void addToCompositeParent(Command<C> command) {
        if (!(commandStack.peek() instanceof CompositeCommand)) {
            throw new BuilderException("Cannot add command %s to non-composite command %s", command, commandStack.peek());
        }
        @SuppressWarnings("unchecked")
        CompositeCommand<C> parentCommand = (CompositeCommand<C>) commandStack.peek();
        parentCommand.add(command);
    }

    /**
     * Validates the given command XML according to the set schema
     * @param commandResource the command XML
     * 
     */
    private void validate(Resource commandResource) {
        if (schema == null) {
            return;
        }
        InputStream is;
        try {
            is = commandResource.getInputStream();
            StAXSource commandXml = new StAXSource(xmlInputFactory.createXMLStreamReader(is));
            schema.newValidator().validate(commandXml);
        } catch (Exception e) {
            throw new BuilderException(e, "Failed to validate resource: %s according to schema: %s", commandResource.getURI(), schema);
        }
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
