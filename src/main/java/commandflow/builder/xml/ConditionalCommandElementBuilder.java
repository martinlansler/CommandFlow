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

import static commandflow.builder.xml.XmlBuilderUtil.asComposite;
import static commandflow.builder.xml.XmlBuilderUtil.newInstance;

import java.util.Map;

import commandflow.Command;
import commandflow.builder.CompositeCommand;

/**
 * A builder that can create a conditional command (must implement {@link CompositeCommand}) from an XML element.
 * <p>
 * The condition command is created in one of the ways specified in {@link CommandElementBuilder}. Once the condition is created is pushed as the first command via the
 * {@link CompositeCommand#add(commandflow.Command)} method.
 * @author elansma
 */
public class ConditionalCommandElementBuilder<C> extends CommandElementBuilder<C> {
    /** The conditional command */
    private Class<? extends Command<C>> conditionalCommandClass;

    /**
     * @param conditionalCommandClass the conditional command class
     * @param classAttribute the name of the attribute holding the command class, ignored if <code>null</code>
     * @param refAttribute the name of the attribute holding the command reference, ignored if <code>null</code>
     * @param scriptAttribute the name of the attribute holding the command script, ignored if <code>null</code>
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public ConditionalCommandElementBuilder(Class<? extends Command> conditionalCommandClass, String classAttribute, String refAttribute, String scriptAttribute) {
        super(classAttribute, refAttribute, scriptAttribute);
        this.conditionalCommandClass = (Class<? extends Command<C>>) conditionalCommandClass;
    }

    /** {@inheritDoc} */
    @Override
    public Command<C> build(XmlCommandBuilder<C> xmlCommandBuilder, String elementName, Map<String, String> attributes) {
        Command<C> condition = super.build(xmlCommandBuilder, elementName, attributes);
        Command<C> conditionalCommand = newInstance(conditionalCommandClass);
        asComposite(conditionalCommand).add(condition);
        return conditionalCommand;
    }
}
