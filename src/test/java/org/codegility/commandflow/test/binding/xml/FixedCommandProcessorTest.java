package org.codegility.commandflow.test.binding.xml;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import javax.xml.namespace.QName;

import org.codegility.commandflow.binding.xml.ElementCommandNameLookup;
import org.codegility.commandflow.binding.xml.FixedCommandProcessor;
import org.codegility.commandflow.binding.xml.XmlBindingHandler;
import org.codegility.commandflow.command.TrueCommand;
import org.junit.Test;


public class FixedCommandProcessorTest extends AbstractXmlElementProcessorTest {

    @Override
    protected String getTestResourceName() {
        return "fixedCommandProcessorTest.xml";
    }

    @Override
    protected void setupBindingHandler(XmlBindingHandler<TestContext> xmlBindingHandler) {
        xmlBindingHandler.addElementProcessor(new QName("true"), new FixedCommandProcessor<TestContext>(TrueCommand.class));
        xmlBindingHandler.setCommandNameLookup(new ElementCommandNameLookup<TestContext>());
    }

    @Test
    public void test() {
        assertThat(getCommandCatalog().getCommands().size(), is(1));
        assertThat(getCommandCatalog().getCommand("true"), notNullValue());
        assertThat(getCommandCatalog().getCommand("true"), is(TrueCommand.class));
    }

}
