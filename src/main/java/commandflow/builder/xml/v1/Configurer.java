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
package commandflow.builder.xml.v1;

import java.io.InputStream;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;

import commandflow.builder.xml.CommandElementBuilder;
import commandflow.builder.xml.ConditionalCommandElementBuilder;
import commandflow.builder.xml.FixedClassElementBuilder;
import commandflow.builder.xml.XmlCommandBuilder;
import commandflow.builder.xml.XmlCommandBuilderConfigurer;
import commandflow.builder.xml.XmlCommandBuilderFactory;
import commandflow.command.AndCommand;
import commandflow.command.DoWhileCommand;
import commandflow.command.IfCommand;
import commandflow.command.NotCommand;
import commandflow.command.OrCommand;
import commandflow.command.SequenceCommand;
import commandflow.command.WhileCommand;

/**
 * The {@link XmlCommandBuilderConfigurer} for this XML namespace.
 * <p>
 * This class also defines constants for the default element and attributes names from the command schema.
 * @author elansma
 */
public class Configurer implements XmlCommandBuilderConfigurer {

    /** The namespace of this command XML */
    public static final String NAMESPACE = "http://commandflow/1";

    /** Element {@value} */
    public static final String COMMAND_ELEMENT = "command";
    /** Element {@value} */
    private static final String SEQUENCE_ELEMENT = "sequence";
    /** Element {@value} */
    public static final String NOT_ELEMENT = "not";
    /** Element {@value} */
    public static final String OR_ELEMENT = "or";
    /** Element {@value} */
    public static final String AND_ELEMENT = "and";
    /** Element {@value} */
    private static final String IF_ELEMENT = "if";
    /** Element {@value} */
    private static final String WHILE_ELEMENT = "while";
    /** Element {@value} */
    private static final String DO_WHILE_ELEMENT = "doWhile";

    /** The {@value} attribute in {@link #COMMAND_ELEMENT} */
    public static final String CLASS_ATTRIBUTE = "class";
    /** The {@value} attribute in {@link #COMMAND_ELEMENT} */
    public static final String VALUE_ATTRIBUTE = "value";
    /** The {@value} attribute in {@link #COMMAND_ELEMENT} */
    public static final String REF_ATTRIBUTE = "ref";
    /** The {@value} attribute in {@link #COMMAND_ELEMENT} */
    public static final String DYNAMIC_REF_ATTRIBUTE = "dynamicRef";

    /** The singleton instance */
    public static final Configurer INSTANCE = new Configurer();

    static {
        // register this namespace...
        XmlCommandBuilderFactory.addNamespace(NAMESPACE, INSTANCE);
    }

    private Configurer() {

    }

    /** The default XML command schema */
    public static final Schema COMMAND_SCHEMA;

    static {
        InputStream is = XmlCommandBuilder.class.getClassLoader().getResourceAsStream("commandflow/builder/xml/v1/command.xsd");
        try {
            COMMAND_SCHEMA = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI).newSchema(new StreamSource(is));
        } catch (SAXException e) {
            throw new RuntimeException(e);
        }
    }

    /** {@inheritDoc} */
    @Override
    public <C> void configure(XmlCommandBuilder<C> builder) {
        builder.setCommandSchema(COMMAND_SCHEMA);
        //
        builder.addElementBuilder(COMMAND_ELEMENT, new CommandElementBuilder<C>(CLASS_ATTRIBUTE, REF_ATTRIBUTE, VALUE_ATTRIBUTE));
        //
        builder.addElementBuilder(IF_ELEMENT, new ConditionalCommandElementBuilder<C>(IfCommand.class, CLASS_ATTRIBUTE, REF_ATTRIBUTE, VALUE_ATTRIBUTE));
        builder.addElementBuilder(WHILE_ELEMENT, new ConditionalCommandElementBuilder<C>(WhileCommand.class, CLASS_ATTRIBUTE, REF_ATTRIBUTE, VALUE_ATTRIBUTE));
        builder.addElementBuilder(DO_WHILE_ELEMENT, new ConditionalCommandElementBuilder<C>(DoWhileCommand.class, CLASS_ATTRIBUTE, REF_ATTRIBUTE, VALUE_ATTRIBUTE));
        //
        builder.addElementBuilder(SEQUENCE_ELEMENT, new FixedClassElementBuilder<C>(SequenceCommand.class));
        builder.addElementBuilder(NOT_ELEMENT, new FixedClassElementBuilder<C>(NotCommand.class));
        builder.addElementBuilder(OR_ELEMENT, new FixedClassElementBuilder<C>(OrCommand.class));
        builder.addElementBuilder(AND_ELEMENT, new FixedClassElementBuilder<C>(AndCommand.class));
        // TODO: parallel
        // TODO: parallelOr
        // TODO: parallelAnd
    }
}
