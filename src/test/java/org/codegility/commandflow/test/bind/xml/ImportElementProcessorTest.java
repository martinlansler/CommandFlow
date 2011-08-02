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

import javax.xml.namespace.QName;

import org.codegility.commandflow.bind.xml.BasicCommandProcessor;
import org.codegility.commandflow.bind.xml.ImportElementProcessor;
import org.codegility.commandflow.bind.xml.XmlBindingHandler;
import org.codegility.commandflow.command.TrueCommand;
import org.junit.Test;


/**
 * Tests {@link ImportElementProcessor}
 * @author Martin Lansler
 */
public class ImportElementProcessorTest extends AbstractXmlElementProcessorTest {
    public static class CommandRelativeUnique extends TrueCommand<TestContext> {
    }

    public static class CommandRelativeOverride extends TrueCommand<TestContext> {
    }

    public static class CommandAbsoluteUnique extends TrueCommand<TestContext> {
    }

    public static class CommandAbsoluteOverride extends TrueCommand<TestContext> {
    }

    public static class CommandOverride extends TrueCommand<TestContext> {
    }

    /** {@inheritDoc} */
    @Override
    protected String getTestResourceName() {
        return "importElementProcessorTest.xml";
    }

    /** {@inheritDoc} */
    @Override
    protected void setupBindingHandler(XmlBindingHandler<TestContext> xmlBindingHandler) {
        xmlBindingHandler.addElementProcessor(new QName("import"), new ImportElementProcessor<TestContext>("resource"));
        xmlBindingHandler.addElementProcessor(new QName("command"), new BasicCommandProcessor<TestContext>("class", "ref", "dynamic", "value"));
    }

    @Test
    public void testImport() {
        hasCommand("relativeUnique", CommandRelativeUnique.class);
        hasCommand("override", CommandRelativeOverride.class);
        hasCommand("absoluteUnique", CommandAbsoluteUnique.class);
        hasCommand("override2", CommandOverride.class);
    }

}
