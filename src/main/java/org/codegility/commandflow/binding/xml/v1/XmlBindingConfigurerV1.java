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
package org.codegility.commandflow.binding.xml.v1;

import static org.codegility.commandflow.io.ResourceUtil.getResourceAsStream;

import java.io.InputStream;

import javax.xml.XMLConstants;
import javax.xml.namespace.QName;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.codegility.commandflow.binding.xml.AttributeCommandNameLookup;
import org.codegility.commandflow.binding.xml.BasicCommandProcessor;
import org.codegility.commandflow.binding.xml.CommandConfigurationProcessor;
import org.codegility.commandflow.binding.xml.ConditionalCommandProcessor;
import org.codegility.commandflow.binding.xml.FixedCommandProcessor;
import org.codegility.commandflow.binding.xml.IgnoreElementProcessor;
import org.codegility.commandflow.binding.xml.ImportElementProcessor;
import org.codegility.commandflow.binding.xml.XmlBindingConfigurer;
import org.codegility.commandflow.binding.xml.XmlBindingFactory;
import org.codegility.commandflow.binding.xml.XmlBindingHandler;
import org.codegility.commandflow.command.AndCommand;
import org.codegility.commandflow.command.DoWhileCommand;
import org.codegility.commandflow.command.IfCommand;
import org.codegility.commandflow.command.NotCommand;
import org.codegility.commandflow.command.OrCommand;
import org.codegility.commandflow.command.SequenceCommand;
import org.codegility.commandflow.command.WhileCommand;
import org.xml.sax.SAXException;

/**
 * The {@link XmlBindingConfigurer} for this XML namespace.
 * <p>
 * This class also defines constants for the default element and attributes names from the command schema.
 * @author Martin Lansler
 */
public class XmlBindingConfigurerV1 implements XmlBindingConfigurer {

    /** The namespace of this command XML */
    public static final String NAMESPACE = "http://codegility.org/commandflow/1";

    /** Element {@value} */
    public static final QName COMMANDS_ELEMENT = new QName(NAMESPACE, "commands");
    /** Element {@value} */
    private static final QName IMPORT_ELEMENT = new QName(NAMESPACE, "import");
    /** Element {@value} */
    public static final QName COMMAND_ELEMENT = new QName(NAMESPACE, "command");
    /** Element {@value} */
    public static final QName PROPERTY_ELEMENT = new QName(NAMESPACE, "property");
    /** Element {@value} */
    private static final QName SEQUENCE_ELEMENT = new QName(NAMESPACE, "sequence");
    /** Element {@value} */
    public static final QName NOT_ELEMENT = new QName(NAMESPACE, "not");
    /** Element {@value} */
    public static final QName OR_ELEMENT = new QName(NAMESPACE, "or");
    /** Element {@value} */
    public static final QName AND_ELEMENT = new QName(NAMESPACE, "and");
    /** Element {@value} */
    private static final QName IF_ELEMENT = new QName(NAMESPACE, "if");
    /** Element {@value} */
    private static final QName WHILE_ELEMENT = new QName(NAMESPACE, "while");
    /** Element {@value} */
    private static final QName DO_WHILE_ELEMENT = new QName(NAMESPACE, "doWhile");

    /** The {@value} attribute in {@link #IMPORT_ELEMENT} */
    private static final String RESOURCE_ATTRIBUTE = "resource";
    /** The {@value} attribute in {@link #COMMAND_ELEMENT} */
    public static final String CLASS_ATTRIBUTE = "class";
    /** The {@value} attribute in {@link #COMMAND_ELEMENT} */
    public static final String VALUE_ATTRIBUTE = "value";
    /** The {@value} attribute in {@link #COMMAND_ELEMENT} */
    public static final String REF_ATTRIBUTE = "ref";
    /** The {@value} attribute in {@link #COMMAND_ELEMENT} */
    public static final String DYNAMIC_REF_ATTRIBUTE = "dynamicRef";
    /** Name attribute */
    public static final String NAME_ATTRIBUTE = "name";

    /** The singleton instance */
    public static final XmlBindingConfigurerV1 INSTANCE = new XmlBindingConfigurerV1();

    static {
        // register this namespace...
        XmlBindingFactory.addNamespace(NAMESPACE, INSTANCE);
    }

    private XmlBindingConfigurerV1() {

    }

    /** The default XML command schema */
    public static final Schema COMMAND_SCHEMA;

    static {
        InputStream is = getResourceAsStream(XmlBindingConfigurerV1.class.getPackage(), "commandflow.xsd");
        try {
            COMMAND_SCHEMA = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI).newSchema(new StreamSource(is));
        } catch (SAXException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <C> void configure(XmlBindingHandler<C> handler) {
        handler.setCommandSchema(COMMAND_SCHEMA);

        handler.setCommandNameLookup(new AttributeCommandNameLookup<C>(NAME_ATTRIBUTE));

        handler.addElementProcessor(IMPORT_ELEMENT, new ImportElementProcessor<C>(RESOURCE_ATTRIBUTE));

        handler.addElementProcessor(COMMANDS_ELEMENT, new IgnoreElementProcessor<C>());

        handler.addElementProcessor(COMMAND_ELEMENT, new BasicCommandProcessor<C>(CLASS_ATTRIBUTE, REF_ATTRIBUTE, DYNAMIC_REF_ATTRIBUTE, VALUE_ATTRIBUTE));
        handler.addElementProcessor(PROPERTY_ELEMENT, new CommandConfigurationProcessor<C>(NAME_ATTRIBUTE, VALUE_ATTRIBUTE));

        handler.addElementProcessor(IF_ELEMENT, new ConditionalCommandProcessor<C>(IfCommand.class, CLASS_ATTRIBUTE, REF_ATTRIBUTE, DYNAMIC_REF_ATTRIBUTE, VALUE_ATTRIBUTE));
        handler.addElementProcessor(WHILE_ELEMENT, new ConditionalCommandProcessor<C>(WhileCommand.class, CLASS_ATTRIBUTE, REF_ATTRIBUTE, DYNAMIC_REF_ATTRIBUTE, VALUE_ATTRIBUTE));
        handler.addElementProcessor(DO_WHILE_ELEMENT, new ConditionalCommandProcessor<C>(DoWhileCommand.class, CLASS_ATTRIBUTE, REF_ATTRIBUTE, DYNAMIC_REF_ATTRIBUTE, VALUE_ATTRIBUTE));

        handler.addElementProcessor(SEQUENCE_ELEMENT, new FixedCommandProcessor<C>(SequenceCommand.class));
        handler.addElementProcessor(NOT_ELEMENT, new FixedCommandProcessor<C>(NotCommand.class));
        handler.addElementProcessor(OR_ELEMENT, new FixedCommandProcessor<C>(OrCommand.class));
        handler.addElementProcessor(AND_ELEMENT, new FixedCommandProcessor<C>(AndCommand.class));
        // TODO: parallel
        // TODO: parallelOr
        // TODO: parallelAnd
    }
}
