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

import static javax.xml.stream.XMLStreamConstants.END_ELEMENT;
import static javax.xml.stream.XMLStreamConstants.START_ELEMENT;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.stax.StAXSource;
import javax.xml.validation.Schema;

import org.codegility.commandflow.Command;
import org.codegility.commandflow.bind.BindingException;
import org.codegility.commandflow.bind.BindingHandler;
import org.codegility.commandflow.builder.CompositeCommand;
import org.codegility.commandflow.catalog.CommandCatalog;
import org.codegility.commandflow.io.Resource;

import com.sun.org.apache.xml.internal.resolver.Catalog;

/**
 * A binding handler for XML based command representations.
 * <p>
 * Note: This class is not thread-safe.
 * @author Martin Lansler
 */
public class XmlBindingHandler<C> implements BindingHandler<C> {
    /** The command XML resources */
    private List<Resource> commandXmlResources;

    /** The command XML currently being processed */
    private Resource currentCommandXml;

    /** Holds the stack of created commands */
    private Deque<Command<C>> commandStack;

    /** The schema used to validate the command XML, not used if <code>null</code> */
    private Schema schema;

    /** The bound XML element processors */
    private Map<QName, XmlElementProcessor<C>> xmlElementProcessors;

    /** Holds the resources already processed, used to detect circular dependencies between resources */
    private Set<Resource> processedResources;

    /** The command catalog */
    private CommandCatalog<C> catalog;

    /** Command name lookup, may be <code>null</code> */
    private XmlCommandNameLookup<C> xmlCommandNameLookup;

    /** StAX factory */
    private static final XMLInputFactory xmlInputFactory;
    static {
        xmlInputFactory = XMLInputFactory.newInstance();
    }

    public XmlBindingHandler() {
        commandXmlResources = new ArrayList<Resource>();
        xmlElementProcessors = new HashMap<QName, XmlElementProcessor<C>>();
    }

    
    @Override
    public void build(CommandCatalog<C> catalog) {
        init(catalog);
        for (Resource commandResource : commandXmlResources) {
            build(commandResource);
        }
    }

    /**
     * Initializes
     * @param catalog the catalog to initialize the handler for
     */
    private void init(CommandCatalog<C> catalog) {
        this.catalog = catalog;
        commandStack = new ArrayDeque<Command<C>>();
        processedResources = new HashSet<Resource>();
    }

    /**
     * Builds the given command resource
     * @param commandResource the resource to build
     */
    private void build(Resource commandResource) {
        if (processedResources.contains(commandResource)) {
            throw new BindingException("Circular dependecy detected to resource %s", commandResource);
        }
        processedResources.add(commandResource);
        validate(commandResource);
        processCommandXML(commandResource);
    }

    /**
     * Processes the given command XML and builds the contained commands
     * @param commandResource the command XML
     */
    private void processCommandXML(Resource commandResource) {
        try {
            currentCommandXml = commandResource;
            processCommandStream(xmlInputFactory.createXMLStreamReader(commandResource.getInputStream()));
        } catch (Exception e) {
            throw new BindingException(e);
        } finally {
            currentCommandXml = null;
        }
    }

    /**
     * Processes the given command XML stream
     * @param reader the stream reader
     * @throws XMLStreamException
     */
    private void processCommandStream(XMLStreamReader reader) throws XMLStreamException {
        while (reader.hasNext()) {
            switch (reader.next()) {
            case START_ELEMENT:
                Map<String, String> attributes = getAttributes(reader);
                processStartElement(reader.getName(), attributes);
                break;
            case END_ELEMENT:
                processEndElement(reader.getName());
                break;
            }
        }
    }

    /**
     * Processes the end of an element via the associated {@link XmlElementProcessor}
     * @param name name of the command element
     */
    private void processEndElement(QName name) {
        XmlElementProcessor<C> processor = getCommandProcessor(name);
        processor.endElement(this, name);
    }

    /**
     * Pops the top command from the stack
     */
    public void popCommand() {
        commandStack.pop();
    }

    /**
     * Pushes a command to the top of the stack.
     * <p>
     * If the command is a top level command it will be added to the associated {@link Catalog} otherwise it will be wired to the surrounding composite command found on top of the stack.
     * @param command the command to push
     * @param name optional command name, must be specified if the current stack is empty as this is then a top level command that will be bound in the catalog, <code>null</code> if not specified
     */
    public void pushCommand(Command<C> command, String name) {
        if (commandStack.isEmpty()) {
            if (name == null) {
                throw new BindingException("Command name must be specified for top level command %s", command);
            }
            catalog.addCommand(name, command);
        } else {
            addToCompositeParent(command);
        }
        commandStack.push(command);
    }

    /**
     * Processes the start of element via the associated {@link XmlElementProcessor}
     * @param name name of the command element
     * @param attributes the command attributes
     */
    private void processStartElement(QName name, Map<String, String> attributes) {
        XmlElementProcessor<C> processor = getCommandProcessor(name);
        processor.startElement(this, name, attributes);
    }

    /**
     * @return the command processor for the given element name
     */
    private XmlElementProcessor<C> getCommandProcessor(QName name) {
        XmlElementProcessor<C> processor = xmlElementProcessors.get(name);
        if (processor == null) {
            throw new BindingException("Cannot find processor for element %s", name);
        }
        return processor;
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
            throw new BindingException("Cannot add command %s to non-composite command %s", command, commandStack.peek());
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
        try {
            StAXSource commandXml = new StAXSource(xmlInputFactory.createXMLStreamReader(commandResource.getInputStream()));
            schema.newValidator().validate(commandXml);
        } catch (Exception e) {
            throw new BindingException(e, "Failed to validate resource: %s according to schema: %s", commandResource.getURI(), schema);
        }
    }

    @Override
    public XmlBindingHandler<C> addCommandResource(Resource... resources) {
        for (Resource resource : resources) {
            commandXmlResources.add(resource);
        }
        return this;
    }

    /**
     * Sets the schema to use for command validation.
     * @param schema the schema, if <code>null</code> schema validation will be disabled
     * @return this handler (for method chaining)
     */
    public XmlBindingHandler<C> setCommandSchema(Schema schema) {
        this.schema = schema;
        return this;
    }

    /**
     * Adds a command element processor
     * @param elementName the element name this processor binds to, if the element name already has a binding it will be replaced by this binding
     * @param elementProcessor the command element processor
     * @return this handler (for method chaining)
     */
    public XmlBindingHandler<C> addElementProcessor(QName elementName, XmlElementProcessor<C> elementProcessor) {
        xmlElementProcessors.put(elementName, elementProcessor);
        return this;
    }

    /**
     * Sets the command name lookup to use
     * @param xmlCommandNameLookup the command name lookup, may be <code>null</code>
     * @return this handler (for method chaining)
     */
    public XmlBindingHandler<C> setCommandNameLookup(XmlCommandNameLookup<C> xmlCommandNameLookup) {
        this.xmlCommandNameLookup = xmlCommandNameLookup;
        return this;
    }

    /**
     * @return the configured name lookup, <code>null</code> if none is set
     */
    public XmlCommandNameLookup<C> getCommandNameLookup() {
        return xmlCommandNameLookup;
    }

    /**
     * Shortcut method to lookup command name via set {@link XmlCommandNameLookup}
     * @param command the command instance
     * @param elementName the XML element the command was created from
     * @param attributes the XMl attributes of the XML element
     * @return the command name, <code>null</code> if a name could not be determined
     */
    public String getCommandName(Command<C> command, QName elementName, Map<String, String> attributes) {
        String name = null;
        if (xmlCommandNameLookup != null) {
            name = xmlCommandNameLookup.getName(command, elementName, attributes);
        }
        return name;
    }

    /**
     * Clears all bindings.
     * <p>
     * This method is mainly intended to be used when a custom XML binding is needed and the default bindings should not be used.
     * @return this handler (for method chaining)
     */
    public XmlBindingHandler<C> clearBindings() {
        xmlElementProcessors.clear();
        return this;
    }

    
    @Override
    public XmlBindingHandler<C> clone() {
        XmlBindingHandler<C> clone = new XmlBindingHandler<C>();
        clone.processedResources = new HashSet<Resource>(processedResources);
        clone.schema = this.schema;
        clone.xmlCommandNameLookup = this.xmlCommandNameLookup;
        clone.xmlElementProcessors = new HashMap<QName, XmlElementProcessor<C>>(this.xmlElementProcessors);
        return (XmlBindingHandler<C>) clone;
    }

    /**
     * @return the current command XML
     */
    public Resource getCurrentCommandXml() {
        return currentCommandXml;
    }

    /**
     * @return the command catalog currently used for this handler
     */
    public CommandCatalog<C> getCommandCatalog() {
        return catalog;
    }
}
