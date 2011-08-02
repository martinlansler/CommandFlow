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

import static org.codegility.commandflow.command.CommandUtil.asComposite;
import static org.codegility.commandflow.command.CommandUtil.newInstance;

import java.util.Map;

import javax.xml.namespace.QName;

import org.codegility.commandflow.Command;
import org.codegility.commandflow.builder.CompositeCommand;

/**
 * A processor that can create a conditional command (must implement {@link CompositeCommand}) from an XML element.
 * <p>
 * The condition command is created in one of the ways specified in {@link BasicCommandProcessor}. Once the condition is created is pushed as the first command via the
 * {@link CompositeCommand#add(org.codegility.commandflow.Command)} method.
 * @author Martin Lansler
 */
public class ConditionalCommandProcessor<C> extends BasicCommandProcessor<C> {
    /** The conditional command */
    private Class<? extends Command<C>> conditionalCommandClass;

    /**
     * @param conditionalCommandClass the conditional command class
     * @param classAttribute the name of the attribute holding the command class, ignored if <code>null</code>
     * @param refAttribute the name of the attribute holding the command reference, ignored if <code>null</code>
     * @param dynamicRefAttribute the name of the attribute used to decide if the reference is dynamic, ignored if <code>null</code>
     * @param scriptAttribute the name of the attribute holding the command script, ignored if <code>null</code>
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public ConditionalCommandProcessor(Class<? extends Command> conditionalCommandClass, String classAttribute, String refAttribute, String dynamicRefAttribute, String scriptAttribute) {
        super(classAttribute, refAttribute, dynamicRefAttribute, scriptAttribute);
        this.conditionalCommandClass = (Class<? extends Command<C>>) conditionalCommandClass;
    }

    
    @Override
    protected Command<C> createCommand(QName elementName, Map<String, String> attributes) {
        Command<C> condition = super.createCommand(elementName, attributes);
        Command<C> conditionalCommand = newInstance(conditionalCommandClass);
        asComposite(conditionalCommand).add(condition);
        return conditionalCommand;
    }
}
