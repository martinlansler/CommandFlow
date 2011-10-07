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
package org.codegility.commandflow.test.binding.xml;

import static org.hamcrest.CoreMatchers.is;

import javax.xml.namespace.QName;

import org.codegility.commandflow.binding.xml.BasicCommandProcessor;
import org.codegility.commandflow.binding.xml.ConditionalCommandProcessor;
import org.codegility.commandflow.binding.xml.XmlBindingHandler;
import org.codegility.commandflow.command.IfCommand;
import org.junit.Assert;
import org.junit.Test;


/**
 * 
 * @author Martin Lansler
 */
public class ConditionalCommandProcessorTest extends AbstractXmlElementProcessorTest {

    /** {@inheritDoc} */
    @Override
    protected String getTestResourceName() {
        return "conditionalCommandProcessorTest.xml";
    }

    /** {@inheritDoc} */
    @Override
    protected void setupBindingHandler(XmlBindingHandler<TestContext> xmlBindingHandler) {
        xmlBindingHandler.addElementProcessor(new QName("if"), new ConditionalCommandProcessor<TestContext>(IfCommand.class, "class", "ref", "dynamic", "value"));
        xmlBindingHandler.addElementProcessor(new QName("command"), new BasicCommandProcessor<TestContext>("class", "ref", "dynamic", "value"));
    }

    @Test
    public void testIf() {
        final String commandName = "ifCommand";
        hasCommand(commandName, IfCommand.class);
        TestContext context = assertExecute(commandName, true);
        Assert.assertThat(context.isExecuted(), is(true));
    }
}
