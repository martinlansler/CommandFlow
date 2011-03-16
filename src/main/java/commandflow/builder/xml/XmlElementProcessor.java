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

/**
 * Interface for processing an XML element with associated attributes.
 * <p>
 * Typical processors can create commands, handle import clauses etc.
 * <p>
 * A command processor that creates a command pushes the command onto the command builder stack using {@link XmlCommandBuilder#pushCommand(commandflow.Command, String)} when processing the start of
 * the element. The created command must subsequently be popped using {@link XmlCommandBuilder#popCommand()} when processing the end of the element.
 * 
 * @author elansma
 * @param <C> the context class of the commands
 */
public interface XmlElementProcessor<C> {
    /**
     * Called to process the start of the given XML element.
     * 
     * @param xmlCommandBuilder the XML command builder
     * @param elementName the qualified name of the XML element
     * @param attributes the map of attributes (maps from attribute name to value)
     */
    void startElement(XmlCommandBuilder<C> xmlCommandBuilder, QName elementName, Map<String, String> attributes);

    /**
     * Called to process the end of the given XML element
     * @param xmlCommandBuilder the XML command builder
     * @param elementName the qualified name of the XML element
     */
    void endElement(XmlCommandBuilder<C> xmlCommandBuilder, QName elementName);
}
