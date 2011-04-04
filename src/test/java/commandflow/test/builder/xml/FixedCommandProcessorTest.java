package commandflow.test.builder.xml;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import javax.xml.namespace.QName;

import org.junit.Test;

import commandflow.builder.xml.FixedCommandProcessor;
import commandflow.command.TrueCommand;

public class FixedCommandProcessorTest extends AbstractXmlElementProcessorTest {

    @Override
    protected String getTestResourceName() {
        return "fixedCommandProcessorTest.xml";
    }

    @Override
    protected void testInit() {
        getXmlCommandBuilder().addElementProcessor(new QName("true"), new FixedCommandProcessor<Object>(TrueCommand.class));
    }

    @Test
    public void test() {
        assertThat(getCommandCatalog().getCommands().size(), is(1));
        assertThat(getCommandCatalog().getCommand("true"), notNullValue());
        assertThat(getCommandCatalog().getCommand("true"), is(TrueCommand.class));
    }

}
