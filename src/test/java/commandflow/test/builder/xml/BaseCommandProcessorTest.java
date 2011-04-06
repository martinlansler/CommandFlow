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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import javax.xml.namespace.QName;

import org.junit.Test;

import commandflow.builder.xml.BaseCommandProcessor;
import commandflow.catalog.CommandReference;
import commandflow.command.ScriptCommand;
import commandflow.command.TrueCommand;

/**
 * 
 * @author elansma
 */
public class BaseCommandProcessorTest extends AbstractXmlElementProcessorTest {

    public static class TestClassCommand extends TrueCommand<Object> {
    }

    /** {@inheritDoc} */
    @Override
    protected String getTestResourceName() {
        return "baseCommandProcessorTest.xml";
    }

    /** {@inheritDoc} */
    @Override
    protected void testInit() {
        getXmlCommandBuilder().addElementProcessor(new QName("command"), new BaseCommandProcessor<Object>("class", "ref", "dynamic", "value"));
    }

    @Test
    public void testClassCommand() {
        hasCommand("commandViaClassAttribute", TestClassCommand.class);
    }

    private void hasCommand(String commandName, Class<?> expectedClass) {
        assertThat(getCommandCatalog().getCommand(commandName), notNullValue());
        assertThat(getCommandCatalog().getCommand(commandName), is(expectedClass));
    }

    @Test
    public void testRefCommand() {
        hasCommand("commandViaRef", TestClassCommand.class);
    }

    @Test
    public void testDynamicRefCommand() {
        final String commandName = "commandViaDynamicRef";
        hasCommand(commandName, CommandReference.class);
        CommandReference<Object> commandRef = (CommandReference<Object>) getCommandCatalog().getCommand(commandName);
        assertThat(commandRef.getReferencedCommand(), is(TestClassCommand.class));
    }

    @Test
    public void testViaEL() {
        testScript("commandViaScript", true);
    }

    public void testScript(String commandName, boolean expectedValue) {
        hasCommand(commandName, ScriptCommand.class);
        ScriptCommand<Object> scriptCommand = (ScriptCommand<Object>) getCommandCatalog().getCommand(commandName);
        assertThat(scriptCommand.execute(new Object()), is(expectedValue));
    }

    @Test
    public void testViaValue() {
        testScript("commandViaValue", true);
    }
}
