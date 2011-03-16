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
package commandflow.builder.xml;

import java.util.Map;

import javax.xml.namespace.QName;

import commandflow.Command;

/**
 * Suitable base class for processors that create commands.
 * @author elansma
 */
public abstract class AbstractCommandProcessor<C> implements XmlElementProcessor<C> {

    /**
     * Pushes the created command onto the command stack
     */
    @Override
    public void startElement(XmlCommandBuilder<C> xmlCommandBuilder, QName elementName, Map<String, String> attributes) {
        Command<C> command = createCommand(elementName, attributes);
        xmlCommandBuilder.pushCommand(command, null); // TODO name
    }

    /**
     * Called to create the command in the subclass
     * @return the created command
     */
    protected abstract Command<C> createCommand(QName elementName, Map<String, String> attributes);

    /**
     * Pops the created command
     */
    @Override
    public void endElement(XmlCommandBuilder<C> xmlCommandBuilder, QName elementName) {
        xmlCommandBuilder.popCommand();
    }

}