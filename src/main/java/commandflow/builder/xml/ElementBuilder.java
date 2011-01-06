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

import commandflow.Command;
import commandflow.builder.BuilderException;

/**
 * Interface for a builder that can create a command instance or otherwise process an XML element with associated attributes.
 * <p>
 * Normally the {@link #build(XmlCommandBuilder, String, Map)} creates a new command instance, however it is also possible to do other types of processing such as handling import clauses, defining
 * settings etc. In the latter case since no command is created <code>null</code> is returned.
 * @author elansma
 * @param <C> the context class of the commands
 */
public interface ElementBuilder<C> {
    /**
     * Called to create a new command instance or otherwise process the given XML element.
     * @param xmlCommandBuilder the XML command builder
     * @param elementName the name of the XML element
     * @param attributes the map of attributes (maps from attribute name to value)
     * @return a new command instance or <code>null</code> if no command was created
     * @throws BuilderException if the command could not be built or some other error occurred
     */
    Command<C> build(XmlCommandBuilder<C> xmlCommandBuilder, String elementName, Map<String, String> attributes);
}
