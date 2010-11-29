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
 * Conditional if-else command.
 * <p>
 * If the supplied condition command returns command status <code>true</code> the conditional command is executed, otherwise the else command is executed. If the condition command returns
 * <code>true</code> the command status is also <code>true</code>, otherwise <code>false</code>.
 * @param <C> the context class of the commands
 * @author elansma
 */
public class IfElseCommand<C> extends IfCommand<C> {
    /** Else command */
    private Command<C> elseCommand;

    /**
     * Creates a new if command
     * @param condition the condition command
     * @param command the conditional command, executed if condition evaluates to <code>true</code>
     * @param elseCommand the else command, executed if condition command evaluates to <code>false</code>
     */
    public IfElseCommand(Command<C> condition, Command<C> command, Command<C> elseCommand) {
        super(condition, command);
        this.elseCommand = elseCommand;
    }

    @Override
    public boolean execute(C context) {
        boolean status = super.execute(context);
        if (!status) {
            elseCommand.execute(context);
        }
        return status;
    }
}
