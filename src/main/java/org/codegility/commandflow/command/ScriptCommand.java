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
package org.codegility.commandflow.command;

import java.util.HashSet;
import java.util.Set;

import javax.script.Bindings;
import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.script.SimpleBindings;

import org.codegility.commandflow.Command;


/**
 * A command that executes a script.
 * <p>
 * The script execution is done via an associated {@link ScriptEngine}. The command context instance (of type <code>C</code>) is default available for
 * the script during execution as binding variable {@link #DEFAULT_CONTEXT_BINDING_NAME}, the binding name can be changed prior to script execution
 * via the {@link #setContextBindingName(String)} method. The script must evaluate to a {@link Boolean} return type which will be used as the command
 * status, any other return type will cause a runtime exception.
 * <p>
 * The default script engine used is {@link #DEFAULT_SCRIPT_ENGINE}, another script engine can be set via the {@link #setEngine(ScriptEngine)} method
 * prior to command execution. The used script engine <em>must be thread-safe</em>, more specifically the associated {@link ScriptEngineFactory} must
 * return either <code>MULTITHREADED</code>, <code>THREAD-ISOLATED</code> or <code>STATELESS</code> when queried for its <code>THREADING</code>
 * parameter via the {@link ScriptEngineFactory#getParameter(String)} method.
 * <p>
 * If the used script engine supports compiled scripts, i.e. implements the {@link Compilable} interface, the script will be compiled for maximum
 * performance.
 * <p>
 * Runtime exceptions raised by the script in the form of {@link ScriptException} are wrapped in a {@link RuntimeException} and propagated.
 * @author Martin Lansler
 */
public class ScriptCommand<C> implements Command<C> {
    /** The default binding name {@value} used for the command context instance */
    public static final String DEFAULT_CONTEXT_BINDING_NAME = "c";

    /** The default script engine {@value} used for script evaluation */
    private static final String DEFAULT_SCRIPT_ENGINE = "JavaScript";

    /** The allowed threading levels for the engines associated {@link ScriptEngineFactory} */
    private static final Set<String> ALLOWED_THREADING_LEVELS;
    static {
        ALLOWED_THREADING_LEVELS = new HashSet<String>();
        ALLOWED_THREADING_LEVELS.add("MULTITHREADED");
        ALLOWED_THREADING_LEVELS.add("THREAD-ISOLATED");
        ALLOWED_THREADING_LEVELS.add("STATELESS");
    }

    /** The script to execute */
    private String script;

    /** Compiled version of {@link #script}, <code>null</code> if engine does not support compilation */
    private CompiledScript compiledScript;

    /** The name to use when binding the command context prior to script execution */
    private String contextBindingName;

    /** The script engine used to execute the command */
    private ScriptEngine engine;

    /**
     * Creates a new script command
     * @param script the script to execute
     */
    public ScriptCommand(String script) {
        this.script = script;
        setContextBindingName(DEFAULT_CONTEXT_BINDING_NAME);
        setEngine(new ScriptEngineManager().getEngineByName(DEFAULT_SCRIPT_ENGINE));
    }

    /**
     * Sets the binding name to use for the command context instance.
     * <p>
     * <em>NOTE: Normally 'context' is bound by the script engine in {@link ScriptContext#ENGINE_SCOPE} scope to point to the {@link ScriptContext}, hence
     * this is not a suitable binding name to use.</em>
     * @param contextBindingName the binding name
     */
    public void setContextBindingName(String contextBindingName) {
        this.contextBindingName = contextBindingName;
    }

    /**
     * @return the binding name used for the command context instance
     */
    public String getContextBindingName() {
        return contextBindingName;
    }

    /**
     * Sets the script engine to use for script evaluation
     * @param engine the script engine
     */
    public void setEngine(ScriptEngine engine) {
        String threading = (String) engine.getFactory().getParameter("THREADING");
        if (threading == null || !ALLOWED_THREADING_LEVELS.contains(threading)) {
            throw new IllegalArgumentException("The provided script engine is not threadsafe");
        }
        this.engine = engine;
        compile();
    }

    /**
     * @return the used script engine
     */
    public ScriptEngine getEngine() {
        return engine;
    }

    /**
     * Compiles the script if supported by the script engine.
     */
    private void compile() {
        if (engine instanceof Compilable) {
            try {
                compiledScript = ((Compilable) engine).compile(script);
            } catch (ScriptException e) {
                throw new RuntimeException(e);
            }
        }
    }

    
    @Override
    public boolean execute(C context) {
        Bindings bindings = new SimpleBindings();
        bindings.put(getContextBindingName(), context);
        try {
            return (Boolean) (compiledScript != null ? compiledScript.eval(bindings) : engine.eval(script, bindings));
        } catch (ScriptException e) {
            throw new RuntimeException(e);
        }
    }
}
