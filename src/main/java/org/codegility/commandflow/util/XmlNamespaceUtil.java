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
package org.codegility.commandflow.util;

import static javax.xml.stream.XMLStreamConstants.START_ELEMENT;

import java.io.IOException;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.codegility.commandflow.io.Resource;

/**
 * Util class to determine the namespaces for an XML document.
 * @author Martin Lansler
 */
public class XmlNamespaceUtil {
    /** StAX factory */
    private static final XMLInputFactory xmlInputFactory;
    static {
        xmlInputFactory = XMLInputFactory.newInstance();
    }

    /**
     * Gets the namespace of the top-level element in the specified XML resource
     * @param resource the XML resource
     * @return the top-level namespace, <code>null</code> if none was found
     */
    public static String getTopLevelNamespace(Resource resource) {
        try {
            return getTopLevelStreamNamespace(xmlInputFactory.createXMLStreamReader(resource.getInputStream()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String getTopLevelStreamNamespace(XMLStreamReader reader) throws XMLStreamException, IOException {
        while (reader.hasNext()) {
            switch (reader.next()) {
            case START_ELEMENT:
                return reader.getNamespaceURI();
            }
        }
        return null;
    }
}
