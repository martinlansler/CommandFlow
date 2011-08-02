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

import static org.junit.Assert.assertEquals;

import javax.xml.namespace.QName;

import org.codegility.commandflow.bind.xml.IgnoreElementProcessor;
import org.codegility.commandflow.bind.xml.XmlBindingHandler;
import org.junit.Test;


/**
 * Tests {@link IgnoreElementProcessor}
 * @author Martin Lansler
 */
public class IgnoreElementProcessorTest extends AbstractXmlElementProcessorTest {

    @Override
    public void setupBindingHandler(XmlBindingHandler<TestContext> xmlBindingHandler) {
        xmlBindingHandler.addElementProcessor(new QName("ignoreMe"), new IgnoreElementProcessor<TestContext>());
        xmlBindingHandler.addElementProcessor(new QName("andMe"), new IgnoreElementProcessor<TestContext>());
    }

    @Override
    protected String getTestResourceName() {
        return "ignoreElementProcessorTest.xml";
    }

    @Test
    public void test() {
        assertEquals(0, getCommandCatalog().getCommands().size());
    }

}
