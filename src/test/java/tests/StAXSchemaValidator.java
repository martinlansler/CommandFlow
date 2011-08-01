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
package tests;

import java.io.InputStream;

import javax.xml.XMLConstants;
import javax.xml.stream.XMLInputFactory;
import javax.xml.transform.stax.StAXSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

/**
 * Tests to validate XML from a {@link StAXSource} via a schema {@link Validator}
 * 
 * @author elansma
 */
public class StAXSchemaValidator {
    public void testValidation() throws Exception {
        InputStream is = getResource("org.codegility.commandflow/builder/xml/v1/command.xml");
        StAXSource commandXml = new StAXSource(XMLInputFactory.newInstance().createXMLStreamReader(is));
        is = getResource("org.codegility.commandflow/builder/xml/v1/command.xsd");
        Schema schema = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI).newSchema(new StreamSource(is));
        schema.newValidator().validate(commandXml);
    }

    private InputStream getResource(String resource) {
        return getClass().getClassLoader().getResourceAsStream(resource);
    }
}
