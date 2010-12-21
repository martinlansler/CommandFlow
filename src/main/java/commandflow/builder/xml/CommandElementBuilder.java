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

import static commandflow.builder.xml.XmlBuilderUtil.newInstance;
import static javax.xml.bind.DatatypeConverter.parseBoolean;

import java.util.Map;

import commandflow.Command;
import commandflow.builder.BuilderException;
import commandflow.catalog.CommandReference;
import commandflow.command.ScriptCommand;

/**
 * A builder that can create a command (simple or composite) from an XML element.
 * <p>
 * The command can create the command in three different ways (in the given priority order):
 * <ul>
 * <li>Via an attribute containing the class name of the command to instantiate</li>
 * <li>Via an attribute containing a named reference to another command</li>
 * <li>Via an attribute containing a script</li>
 * </ul>
 * Hence either a {@link Command}, {@link CommandReference} or {@link ScriptCommand} instance is created.
 * @author elansma
 */
public class CommandElementBuilder<C> implements ElementBuilder<C> {
    /** The name of the class attribute, may be <code>null</code> */
    private String classAttribute;
    /** The name of the reference attribute, may be <code>null</code> */
    private String refAttribute;
    /** The name of the attribute used to decide if {@link #refAttribute} is a dynamic reference, may be <code>null</code> */
    private String dynamicRefAttribut;
    /** The name of the script attribute, may be <code>null</code> */
    private String scriptAttribute;

    /**
     * Creates a new simple command builder using attribute lookup.
     * @param classAttribute the name of the attribute holding the command class, ignored if <code>null</code>
     * @param refAttribute the name of the attribute holding the command reference, ignored if <code>null</code>
     * @param scriptAttribute the name of the attribute holding the command script, ignored if <code>null</code>
     */
    public CommandElementBuilder(String classAttribute, String refAttribute, String scriptAttribute) {
        this.classAttribute = classAttribute;
        this.refAttribute = refAttribute;
        this.scriptAttribute = scriptAttribute;
    }

    /** {@inheritDoc} */
    @Override
    public Command<C> build(String elementName, Map<String, String> attributes) {
        Command<C> command;

        if (hasAttribute(getClassAttribute(), attributes)) {
            command = newInstance(attributes.get(getClassAttribute()));
        } else if (hasAttribute(getRefAttribute(), attributes)) {
            String ref = attributes.get(getRefAttribute());
            boolean isDynamic = hasAttribute(getDynamicRefAttribut(), attributes) ? parseBoolean(attributes.get(getDynamicRefAttribut())) : false;
            command = new CommandReference<C>(ref, isDynamic);
        } else if (hasAttribute(getScriptAttribute(), attributes)) {
            command = new ScriptCommand<C>(attributes.get(getScriptAttribute()));
        } else {
            throw new BuilderException("Cannot build command with element name '%s' and attributes '%s'", elementName, attributes);
        }
        return command;
    }

    /**
     * Checks if the given attribute map has the specified attribute
     * @param attribute the attribute to check for
     * @param attributes the attribute map
     * @return <code>true</code> if the attribute exists
     */
    private boolean hasAttribute(String attribute, Map<String, String> attributes) {
        return attribute != null ? attributes.containsKey(attribute) : false;
    }

    /**
     * @return the name of the class attribute, may be <code>null</code>
     */
    public String getClassAttribute() {
        return classAttribute;
    }

    /**
     * Sets the name of the attribute holding the class, if <code>null</code> ignored
     * @param classAttribute the name of the class attribute
     * @return this builder (for method chaining)
     */
    public CommandElementBuilder<C> setClassAttribute(String classAttribute) {
        this.classAttribute = classAttribute;
        return this;
    }

    /**
     * @return the name of the reference attribute, ma be <code>null</code>
     */
    public String getRefAttribute() {
        return refAttribute;
    }

    /**
     * Sets the name of the attribute holding the reference, if <code>null</code> ignored
     * @param refAttribute the name of the reference attribute
     * @return this builder (for method chaining)
     */
    public CommandElementBuilder<C> setRefAttribute(String refAttribute) {
        this.refAttribute = refAttribute;
        return this;
    }

    /**
     * @return name of the dynamic reference attribute, may be <code>null</code>
     */
    public String getDynamicRefAttribut() {
        return dynamicRefAttribut;
    }

    /**
     * Sets name of the attribute used to decide if {@link #getRefAttribute()} is a dynamic reference, may be <code>null</code>
     * @param dynamicRefAttribut name of the dynamic reference attribute
     */
    public void setDynamicRefAttribut(String dynamicRefAttribut) {
        this.dynamicRefAttribut = dynamicRefAttribut;
    }

    /**
     * @return the name of the script attribute, may be <code>null</code>
     */
    public String getScriptAttribute() {
        return scriptAttribute;
    }

    /**
     * Sets the name of the attribute holding the script, if <code>null</code> ignored
     * @param scriptAttribute the name of the script attribute
     * @return this builder (for method chaining)
     */
    public CommandElementBuilder<C> setScriptAttribute(String scriptAttribute) {
        this.scriptAttribute = scriptAttribute;
        return this;
    }

}
