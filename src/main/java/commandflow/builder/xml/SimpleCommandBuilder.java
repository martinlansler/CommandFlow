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

import static javax.xml.bind.DatatypeConverter.parseBoolean;

import java.util.Map;

import commandflow.Command;
import commandflow.builder.BuilderException;
import commandflow.catalog.CommandReference;
import commandflow.command.ScriptCommand;

/**
 * A builder that can create commands from an XML element.
 * <p>
 * The command can create the command in three different ways (in the given priority order):
 * <ul>
 * <li>Via a class name of the command to instantiate, can either be preset or looked up via an attribute.</li>
 * <li>Via named reference to another command, can either be preset or looked up via an attribute.</li>
 * <li>Via a script, can either be preset or looked up via an attribute.</li>
 * </ul>
 * Hence either a {@link Command}, {@link CommandReference} or {@link ScriptCommand} instance is created.
 * @author elansma
 */
public class SimpleCommandBuilder<C> implements ElementCommandBuilder<C> {
    /** The name of the class attribute, may be <code>null</code> */
    private String classAttribute;
    /** The name of the reference attribute, may be <code>null</code> */
    private String refAttribute;
    /** The name of the attribute used to decide if {@link #refAttribute} is a dynamic reference, may be <code>null</code> */
    private String dynamicRefAttribut;
    /** The name of the script attribute, may be <code>null</code> */
    private String scriptAttribute;
    /** A preset class, if set no attribute lookup will be performed */
    private String clazz;
    /** A preset reference, if set no attribute lookup will be performed */
    private String reference;
    /** A preset dynamic reference, only applicable if {@link #reference} is set */
    private Boolean dynamicReference;
    /** A preset script, if set no attribute lookup will be performed */
    private String script;

    /**
     * Creates a new simple command builder.
     * <p>
     * The strategy to build the command should be set via the <code>setXAttribute</code> and/or <code>setPresetX</code> methods.
     */
    public SimpleCommandBuilder() {
        ; // no more...
    }

    /**
     * Creates a new simple command builder using attribute lookup.
     * @param classAttribute the name of the attribute holding the command class, ignored if <code>null</code>
     * @param refAttribute the name of the attribute holding the command reference, ignored if <code>null</code>
     * @param scriptAttribute the name of the attribute holding the command script, ignored if <code>null</code>
     */
    public SimpleCommandBuilder(String classAttribute, String refAttribute, String scriptAttribute) {
        this.classAttribute = classAttribute;
        this.refAttribute = refAttribute;
        this.scriptAttribute = scriptAttribute;
    }

    /** {@inheritDoc} */
    @Override
    public Command<C> build(String elementName, Map<String, String> attributes) {
        Command<C> command;

        if (getPresetClass() != null || hasAttribute(getClassAttribute(), attributes)) {
            command = newInstance(firstNotNull(getPresetClass(), attributes.get(getClassAttribute())));
        } else if (getPresetReference() != null || hasAttribute(getRefAttribute(), attributes)) {
            String ref;
            boolean isDynamic;
            if (getPresetReference() != null) {
                ref = getPresetReference();
                isDynamic = getPresetDynamicReference() != null ? getPresetDynamicReference() : false;
            } else {
                ref = attributes.get(getRefAttribute());
                isDynamic = hasAttribute(getDynamicRefAttribut(), attributes) ? parseBoolean(attributes.get(getDynamicRefAttribut())) : false;
            }
            command = new CommandReference<C>(ref, isDynamic);
        } else if (getPresetScript() != null || hasAttribute(getScriptAttribute(), attributes)) {
            command = new ScriptCommand<C>(firstNotNull(getPresetScript(), attributes.get(getScriptAttribute())));
        } else {
            throw new BuilderException("Cannot build command with element name '%s' and attributes '%s'", elementName, attributes);
        }
        return command;
    }

    /**
     * Returns the first not <code>null</code> value from the sequence of values.
     * <p>
     * If all values are <code>null</code> an {@link BuilderException} is raised.
     * @param values the sequence of values
     * @return the first not <code>null</code> value
     */
    private <T> T firstNotNull(T... values) {
        for (T t : values) {
            if (t != null) {
                return t;
            }
        }
        throw new BuilderException("Expected at least one not-null value is arguments %", values);
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
     * Instantiates a new command class
     * @param clazz the command class name
     * @return
     */
    @SuppressWarnings("unchecked")
    private Command<C> newInstance(String clazz) {
        try {
            return (Command<C>) Class.forName(clazz).newInstance();
        } catch (Exception e) {
            throw new BuilderException(e, "Cannot create command class %s", clazz);
        }
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
    public SimpleCommandBuilder<C> setClassAttribute(String classAttribute) {
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
    public SimpleCommandBuilder<C> setRefAttribute(String refAttribute) {
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
    public SimpleCommandBuilder<C> setScriptAttribute(String scriptAttribute) {
        this.scriptAttribute = scriptAttribute;
        return this;
    }

    /**
     * @return the preset class name, <code>null</code> if none is set
     */
    public String getPresetClass() {
        return clazz;
    }

    /**
     * Sets a preset class name, if set no attribute lookup will be performed.
     * @param clazz the preset class name
     * @return this builder (for method chaining)
     */
    public SimpleCommandBuilder<C> setPresetClass(String clazz) {
        this.clazz = clazz;
        return this;
    }

    /**
     * @return the preset command reference, <code>null</code> if none is set
     */
    public String getPresetReference() {
        return reference;
    }

    /**
     * Sets a preset command reference, if set no attribute lookup will be performed.
     * @param reference the preset command reference
     * @return this builder (for method chaining)
     */
    public SimpleCommandBuilder<C> setPresetReference(String reference) {
        this.reference = reference;
        return this;
    }

    /**
     * @return the preset command script, <code>null</code> if none is set
     */
    public String getPresetScript() {
        return script;
    }

    /**
     * Sets a preset command script, if set no attribute lookup will be performed.
     * @param script the preset command script
     * @return this builder (for method chaining)
     */
    public SimpleCommandBuilder<C> setPresetScript(String script) {
        this.script = script;
        return this;
    }

    /**
     * @return <code>true</code> if {@link #getPresetReference()} is a dynamic reference, may be <code>null</code>
     */
    public Boolean getPresetDynamicReference() {
        return dynamicReference;
    }

    /**
     * Sets a preset value for whether {@link #getPresetReference()} is a dynamic reference, may be <code>null</code>.
     * <p>
     * This value is only applicable if {@link #getPresetReference()} is set.
     * @param dynamicReference <code>true</code> if {@link #getPresetReference()} is a dynamic reference, may be <code>null</code>
     * @return this builder (for method chaining)
     */
    public SimpleCommandBuilder<C> setPresetDynamicReference(Boolean dynamicReference) {
        this.dynamicReference = dynamicReference;
        return this;
    }
}
