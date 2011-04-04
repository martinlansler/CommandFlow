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
package commandflow.test.builder.xml;

import org.junit.Before;

import commandflow.builder.xml.ElementCommandNameLookup;
import commandflow.builder.xml.XmlCommandBuilder;
import commandflow.builder.xml.XmlElementProcessor;
import commandflow.catalog.DefaultCommandCatalog;
import commandflow.io.ClassPathResource;

/**
 * Base class for testing {@link XmlElementProcessor} implementations.
 * @author elansma
 */
public abstract class AbstractXmlElementProcessorTest {
    private DefaultCommandCatalog<Object> commandCatalog;
    private XmlCommandBuilder<Object> xmlCommandBuilder;

    @Before
    public void init() {
        commandCatalog = new DefaultCommandCatalog<Object>();
        xmlCommandBuilder = new XmlCommandBuilder<Object>();
        commandCatalog.addCommandBuilder(xmlCommandBuilder);
        testInit();
        xmlCommandBuilder.addCommandXml(new ClassPathResource(getClass().getPackage(), getTestResourceName()));
        xmlCommandBuilder.setCommandNameLookup(new ElementCommandNameLookup<Object>());
        commandCatalog.make();
    }

    protected abstract String getTestResourceName();

    protected abstract void testInit();

    protected DefaultCommandCatalog<Object> getCommandCatalog() {
        return commandCatalog;
    }

    protected XmlCommandBuilder<Object> getXmlCommandBuilder() {
        return xmlCommandBuilder;
    }
}
