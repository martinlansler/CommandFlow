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
package org.codegility.commandflow.builder.xml;

import static org.codegility.commandflow.command.CommandUtil.newInstance;

import java.util.Map;

import javax.xml.namespace.QName;

import org.codegility.commandflow.Command;


/**
 * A command processor that always creates a fixed command class.
 * <p>
 * This processor is usually used when the element name determines the command implementation class.
 * @author elansma
 */
public class FixedCommandProcessor<C> extends AbstractCommandProcessor<C> {
    /** The command class to create */
    private Class<? extends Command<C>> clazz;

    /**
     * Creates a new fixed command builder
     * @param clazz the command class
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public FixedCommandProcessor(Class<? extends Command> clazz) {
        this.clazz = (Class<? extends Command<C>>) clazz;
    }

    /** {@inheritDoc} */
    @Override
    protected Command<C> createCommand(QName elementName, Map<String, String> attributes) {
        return newInstance(clazz);
    }
}
