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

import commandflow.builder.xml.ElementCommandBuilder;
import commandflow.builder.xml.SimpleCommandBuilder;
import commandflow.builder.xml.XmlCommandBuilder;

/**
 * Represents the default XML builder settings.
 * <p>
 * This class also defines constants for the default element and attributes names from the command schema.
 * @author elansma
 */
public class DefaultBuilderSettings {
    /** Element {@value} */
    public static final String COMMAND_ELEMENT = "command";

    /** The {@value} attribute in {@link #COMMAND_ELEMENT} */
    public static final String CLASS_ATTRIBUTE = "class";
    /** The {@value} attribute in {@link #COMMAND_ELEMENT} */
    public static final String VALUE_ATTRIBUTE = "value";
    /** The {@value} attribute in {@link #COMMAND_ELEMENT} */
    public static final String REF = "ref";
    /** The {@value} attribute in {@link #COMMAND_ELEMENT} */
    public static final String DYNAMIC_REF = "dynamicRef";

    /** The default XML command schema */
    public static final Schema COMMAND_SCHEMA;
    static {
        InputStream is = XmlCommandBuilder.class.getClassLoader().getResourceAsStream("commandflow/builder/xml/command.xsd");
        try {
            COMMAND_SCHEMA = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI).newSchema(new StreamSource(is));
        } catch (SAXException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Called to configure a XML builder with the deafult settings.
     * <p>
     * This method enables schema vaidation and sets the schema to {@link #COMMAND_SCHEMA}.
     * <p>
     * All the needed {@link ElementCommandBuilder} are configured for the XML elements in the schema.
     * @param builder the XML builder to configure
     */
    public static <C> void configure(XmlCommandBuilder<C> builder) {
        builder.setCommandSchema(COMMAND_SCHEMA);
        //
        builder.addElementCommandBuilder(COMMAND_ELEMENT, new SimpleCommandBuilder<C>());
    }
}
