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
import static org.junit.Assert.assertThat;

import javax.xml.namespace.QName;

import org.codegility.commandflow.bind.xml.BasicCommandProcessor;
import org.codegility.commandflow.bind.xml.XmlBindingHandler;
import org.codegility.commandflow.catalog.CommandReference;
import org.codegility.commandflow.command.ScriptCommand;
import org.codegility.commandflow.command.TrueCommand;
import org.junit.Test;

/**
 * 
 * @author Martin Lansler
 */
public class BasicCommandProcessorTest extends AbstractXmlElementProcessorTest {

    public static class TestClassCommand extends TrueCommand<TestContext> {
    }

    /** {@inheritDoc} */
    @Override
    protected String getTestResourceName() {
        return "basicCommandProcessorTest.xml";
    }

    /** {@inheritDoc} */
    @Override
    protected void setupBindingHandler(XmlBindingHandler<TestContext> xmlBindingHandler) {
        xmlBindingHandler.addElementProcessor(new QName("command"), new BasicCommandProcessor<TestContext>("class", "ref", "dynamic", "value"));
    }

    @Test
    public void testClassCommand() {
        hasCommand("commandViaClassAttribute", TestClassCommand.class);
    }

    @Test
    public void testRefCommand() {
        hasCommand("commandViaRef", TestClassCommand.class);
    }

    @Test
    public void testDynamicRefCommand() {
        final String commandName = "commandViaDynamicRef";
        hasCommand(commandName, CommandReference.class);
        CommandReference<TestContext> commandRef = (CommandReference<TestContext>) getCommandCatalog().getCommand(commandName);
        assertThat(commandRef.getReferencedCommand(), is(TestClassCommand.class));
    }

    @Test
    public void testViaEL() {
        testScript("commandViaScript", true);
    }

    public void testScript(String commandName, boolean expectedValue) {
        hasCommand(commandName, ScriptCommand.class);
        assertExecute(commandName, expectedValue);
    }

    @Test
    public void testViaValue() {
        testScript("commandViaValue", true);
    }
}
