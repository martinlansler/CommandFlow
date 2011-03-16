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
import javax.xml.namespace.QName;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;

import commandflow.builder.xml.AttributeDrivenCommandProcessor;
import commandflow.builder.xml.ConditionalCommandElementProcessor;
import commandflow.builder.xml.FixedCommandProcessor;
import commandflow.builder.xml.XmlBuilderConfigurer;
import commandflow.builder.xml.XmlBuilderFactory;
import commandflow.builder.xml.XmlCommandBuilder;
import commandflow.command.AndCommand;
import commandflow.command.DoWhileCommand;
import commandflow.command.IfCommand;
import commandflow.command.NotCommand;
import commandflow.command.OrCommand;
import commandflow.command.SequenceCommand;
import commandflow.command.WhileCommand;

/**
 * The {@link XmlBuilderConfigurer} for this XML namespace.
 * <p>
 * This class also defines constants for the default element and attributes names from the command schema.
 * @author elansma
 */
public class XmlBuilderConfigurerV1 implements XmlBuilderConfigurer {

    /** The namespace of this command XML */
    public static final String NAMESPACE = "http://commandflow/1";

    /** Element {@value} */
    public static final QName COMMAND_ELEMENT = new QName(NAMESPACE, "command");
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

    /** The {@value} attribute in {@link #COMMAND_ELEMENT} */
    public static final String CLASS_ATTRIBUTE = "class";
    /** The {@value} attribute in {@link #COMMAND_ELEMENT} */
    public static final String VALUE_ATTRIBUTE = "value";
    /** The {@value} attribute in {@link #COMMAND_ELEMENT} */
    public static final String REF_ATTRIBUTE = "ref";
    /** The {@value} attribute in {@link #COMMAND_ELEMENT} */
    public static final String DYNAMIC_REF_ATTRIBUTE = "dynamicRef";

    /** The singleton instance */
    public static final XmlBuilderConfigurerV1 INSTANCE = new XmlBuilderConfigurerV1();

    static {
        // register this namespace...
        XmlBuilderFactory.addNamespace(NAMESPACE, INSTANCE);
    }

    private XmlBuilderConfigurerV1() {

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
        builder.addElementProcessor(COMMAND_ELEMENT, new AttributeDrivenCommandProcessor<C>(CLASS_ATTRIBUTE, REF_ATTRIBUTE, VALUE_ATTRIBUTE));
        //
        builder.addElementProcessor(IF_ELEMENT, new ConditionalCommandElementProcessor<C>(IfCommand.class, CLASS_ATTRIBUTE, REF_ATTRIBUTE, VALUE_ATTRIBUTE));
        builder.addElementProcessor(WHILE_ELEMENT, new ConditionalCommandElementProcessor<C>(WhileCommand.class, CLASS_ATTRIBUTE, REF_ATTRIBUTE, VALUE_ATTRIBUTE));
        builder.addElementProcessor(DO_WHILE_ELEMENT, new ConditionalCommandElementProcessor<C>(DoWhileCommand.class, CLASS_ATTRIBUTE, REF_ATTRIBUTE, VALUE_ATTRIBUTE));
        //
        builder.addElementProcessor(SEQUENCE_ELEMENT, new FixedCommandProcessor<C>(SequenceCommand.class));
        builder.addElementProcessor(NOT_ELEMENT, new FixedCommandProcessor<C>(NotCommand.class));
        builder.addElementProcessor(OR_ELEMENT, new FixedCommandProcessor<C>(OrCommand.class));
        builder.addElementProcessor(AND_ELEMENT, new FixedCommandProcessor<C>(AndCommand.class));
        // TODO: parallel
        // TODO: parallelOr
        // TODO: parallelAnd
    }
}
