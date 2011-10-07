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
package org.codegility.commandflow.binding.xml;

import java.util.Map;

import javax.xml.namespace.QName;

import org.codegility.commandflow.Command;


/**
 * Interface for determining the name of a command.
 * @author Martin Lansler
 * @param <C> the context class of the commands
 */
public interface XmlCommandNameLookup<C> {
    /**
     * Gets the name of the specified command
     * @param command the command instance
     * @param elementName the XML element the command was created from
     * @param attributes the XMl attributes of the XML element
     * @return the command name, <code>null</code> if a name could not be determined
     */
    String getName(Command<C> command, QName elementName, Map<String, String> attributes);
}
