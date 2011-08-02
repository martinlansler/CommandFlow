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
package org.codegility.commandflow.bind.xml;

import java.util.Map;

import javax.xml.namespace.QName;

import org.codegility.commandflow.Command;


/**
 * A command name lookup that names the command the same as the XML element defining it.
 * <p>
 * Only the local name of the element name is used.
 * @author Martin Lansler
 */
public class ElementCommandNameLookup<C> implements XmlCommandNameLookup<C> {

    @Override
    public String getName(Command<C> command, QName elementName, Map<String, String> attributes) {
        return elementName.getLocalPart();
    }

}
