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
 * Suitable base class for commands that wrap a single command.
 * @param <C> the context class of the commands
 * @author elansma
 */
public abstract class WrappingCommand<C> implements Command<C> {
    /** The wrapped command */
    private Command<C> command;

    /**
     * Creates a new wrapping command
     * @param command the wrapped command
     */
    public WrappingCommand(Command<C> command) {
        this.command = command;
    }
    
    /**
     * Gets the wrapped command
     * @return the wrapped command
     */
    public Command<C> getWrappedCommand() {
        return command;
    }
    
    /**
     * Executes the wrapped command
     * @param context the command context
     * @return the wrapped command status
     */
    protected boolean executeWrappedCommand(C context) {
        return command.execute(context);
    }

}