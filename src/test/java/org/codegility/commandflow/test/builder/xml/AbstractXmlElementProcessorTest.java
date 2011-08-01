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
package org.codegility.commandflow.test.builder.xml;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import javax.xml.namespace.QName;

import org.codegility.commandflow.Command;
import org.codegility.commandflow.builder.xml.AttributeCommandNameLookup;
import org.codegility.commandflow.builder.xml.IgnoreElementProcessor;
import org.codegility.commandflow.builder.xml.XmlCommandBuilder;
import org.codegility.commandflow.builder.xml.XmlElementProcessor;
import org.codegility.commandflow.catalog.DefaultCommandCatalog;
import org.codegility.commandflow.io.ClassPathResource;
import org.junit.Before;


/**
 * Base class for testing {@link XmlElementProcessor} implementations.
 * @author elansma
 */
public abstract class AbstractXmlElementProcessorTest {
    private DefaultCommandCatalog<TestContext> commandCatalog;
    private XmlCommandBuilder<TestContext> xmlCommandBuilder;

    @Before
    public void init() {
        commandCatalog = new DefaultCommandCatalog<TestContext>();
        xmlCommandBuilder = new XmlCommandBuilder<TestContext>();
        commandCatalog.addCommandBuilder(xmlCommandBuilder);
        xmlCommandBuilder.addCommandXml(new ClassPathResource(getClass().getPackage(), getTestResourceName()));
        // common test settings
        xmlCommandBuilder.addElementProcessor(new QName("commands"), new IgnoreElementProcessor<TestContext>());
        xmlCommandBuilder.setCommandNameLookup(new AttributeCommandNameLookup<TestContext>("name"));
        setupCommandBuilder();
        commandCatalog.make();
    }

    protected abstract String getTestResourceName();

    protected abstract void setupCommandBuilder();

    protected DefaultCommandCatalog<TestContext> getCommandCatalog() {
        return commandCatalog;
    }

    protected XmlCommandBuilder<TestContext> getXmlCommandBuilder() {
        return xmlCommandBuilder;
    }

    protected void hasCommand(String commandName, Class<?> expectedClass) {
        assertThat(getCommandCatalog().getCommand(commandName), notNullValue());
        assertThat(getCommandCatalog().getCommand(commandName), is(expectedClass));
    }

    protected TestContext assertExecute(String commandName, boolean expectedResult) {
        Command<TestContext> command = getCommandCatalog().getCommand(commandName);
        TestContext context = new TestContext();
        assertThat(command.execute(context), is(expectedResult));
        return context;
    }
}
