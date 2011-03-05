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

/**
 * An element builder that can build an import element containing an reference to another command XML.
 * @author elansma
 */
public class ImportElementBuilder<C> implements ElementBuilder<C> {

    /** {@inheritDoc} */
    @Override
    public Command<C> build(XmlCommandBuilder<C> xmlCommandBuilder, String elementName, Map<String, String> attributes) {
        XmlCommandBuilder<C> clone = xmlCommandBuilder.clone();

        // TODO
        return null;
    }

}
