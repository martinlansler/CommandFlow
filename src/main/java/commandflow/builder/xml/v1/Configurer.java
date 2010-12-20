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

import commandflow.builder.xml.AttributeLookupCommandBuilder;
import commandflow.builder.xml.XmlCommandBuilder;
import commandflow.builder.xml.XmlCommandBuilderConfigurer;
import commandflow.builder.xml.XmlCommandBuilderFactory;

/**
 * The {@link XmlCommandBuilderConfigurer} for this XML namespace.
 * <p>
 * This class also defines constants for the default element and attributes names from the command schema.
 * @author elansma
 */
public class Configurer implements XmlCommandBuilderConfigurer {
    /** The namespace of this cmmand XML */
    public static final String NAMESPACE = "http://commandflow/1";

    /** Element {@value} */
    public static final String COMMAND_ELEMENT = "command";

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
        builder.addElementCommandBuilder(COMMAND_ELEMENT, new AttributeLookupCommandBuilder<C>(CLASS_ATTRIBUTE, REF_ATTRIBUTE, VALUE_ATTRIBUTE));
    }
}
