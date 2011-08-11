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
package org.codegility.commandflow.test.bind.xml;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import javax.xml.namespace.QName;

import org.codegility.commandflow.Command;
import org.codegility.commandflow.bind.xml.AttributeCommandNameLookup;
import org.codegility.commandflow.bind.xml.IgnoreElementProcessor;
import org.codegility.commandflow.bind.xml.XmlBindingHandler;
import org.codegility.commandflow.bind.xml.XmlElementProcessor;
import org.codegility.commandflow.builder.CommandBuilder;
import org.codegility.commandflow.builder.DefaultCommandBuilder;
import org.codegility.commandflow.catalog.DefaultCommandCatalog;
import org.codegility.commandflow.io.ClassPathResource;
import org.junit.Before;

/**
 * Base class for testing {@link XmlElementProcessor} implementations.
 * @author Martin Lansler
 */
public abstract class AbstractXmlElementProcessorTest {
    private DefaultCommandCatalog<TestContext> commandCatalog;
    private XmlBindingHandler<TestContext> xmlBindingHandler;

    @Before
    public void init() {
        commandCatalog = new DefaultCommandCatalog<TestContext>();

        xmlBindingHandler = new XmlBindingHandler<TestContext>();
        xmlBindingHandler.addCommandResource(new ClassPathResource(getClass().getPackage(), getTestResourceName()));
        // common test settings
        xmlBindingHandler.addElementProcessor(new QName("commands"), new IgnoreElementProcessor<TestContext>());
        xmlBindingHandler.setCommandNameLookup(new AttributeCommandNameLookup<TestContext>("name"));

        setupBindingHandler(xmlBindingHandler);

        CommandBuilder<TestContext> builder = new DefaultCommandBuilder<TestContext>(commandCatalog);
        builder.addBindingHandler(xmlBindingHandler);
        builder.make();
    }

    protected abstract String getTestResourceName();

    protected abstract void setupBindingHandler(XmlBindingHandler<TestContext> xmlBindingHandler);

    protected DefaultCommandCatalog<TestContext> getCommandCatalog() {
        return commandCatalog;
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
