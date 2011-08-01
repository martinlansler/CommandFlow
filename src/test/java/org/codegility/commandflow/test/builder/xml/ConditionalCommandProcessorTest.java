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

import javax.xml.namespace.QName;

import org.codegility.commandflow.builder.xml.BasicCommandProcessor;
import org.codegility.commandflow.builder.xml.ConditionalCommandProcessor;
import org.codegility.commandflow.command.IfCommand;
import org.junit.Assert;
import org.junit.Test;


/**
 * 
 * @author elansma
 */
public class ConditionalCommandProcessorTest extends AbstractXmlElementProcessorTest {

    /** {@inheritDoc} */
    @Override
    protected String getTestResourceName() {
        return "conditionalCommandProcessorTest.xml";
    }

    /** {@inheritDoc} */
    @Override
    protected void setupCommandBuilder() {
        getXmlCommandBuilder().addElementProcessor(new QName("if"), new ConditionalCommandProcessor<TestContext>(IfCommand.class, "class", "ref", "dynamic", "value"));
        getXmlCommandBuilder().addElementProcessor(new QName("command"), new BasicCommandProcessor<TestContext>("class", "ref", "dynamic", "value"));
    }

    @Test
    public void testIf() {
        final String commandName = "ifCommand";
        hasCommand(commandName, IfCommand.class);
        TestContext context = assertExecute(commandName, true);
        Assert.assertThat(context.isExecuted(), is(true));
    }
}
