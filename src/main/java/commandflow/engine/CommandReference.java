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
package commandflow.engine;

import commandflow.Command;

/**
 * A reference to a named command.
 * <p>
 * The reference can either be static or dynamic. A static reference is always resolved before command execution, a dynamic at the moment of
 * execution. Using a static reference is better performance-wise, however a dynamic reference adds a level of indirection and hence a flexibility in
 * allowing references to be changed during runtime.
 * <p>
 * Before the command reference can be executed the associated {@link DefaultCommandManager} must be set. Note: A static reference can never be
 * executed, attempting this raises are runtime exception.
 * @author elansma
 */
public class CommandReference<C> implements Command<C> {
    /** Name of command this reference refers to */
    private String name;
    /** If <code>true</code> this is a dynamic reference */
    private boolean isDynamic;
    /** The command manager */
    private CommandManager<C> manager;

    /**
     * Creates a new static command reference
     * @param name the name of the command to refer to
     */
    public CommandReference(String name) {
        this.name = name;
        this.isDynamic = false;
    }

    /**
     * Creates a new static command reference
     * @param name the name of the command to refer to
     * @param isDynamic <code>true</code> if the reference is dynamic
     */
    public CommandReference(String name, boolean isDynamic) {
        this.name = name;
        this.isDynamic = isDynamic;
    }

    /**
     * Sets the command manager.
     * <p>
     * The manager is needed to resolve references.
     * @param manager the command manager
     */
    public void setCommandManager(CommandManager<C> manager) {
        this.manager = manager;
    }

    /** {@inheritDoc} */
    @Override
    public boolean execute(C context) {
        if (!isDynamic) {
            throw new CommandException(String.format("Cannot execute static command reference '%s'", name));
        }
        if (manager == null) {
            throw new CommandException(String.format("No command manager is set for dynamic command reference '%s'", name));
        }
        Command<C> command = manager.getCommand(name);
        if (command == null) {
            throw new CommandException(String.format("Cannot resolve dynamic command reference '%s'"));
        }
        return command.execute(context);
    }

}
