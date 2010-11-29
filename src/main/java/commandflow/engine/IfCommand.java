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
 * Conditional if command.
 * <p>
 * If the supplied condition command returns command status <code>true</code> the conditional command is executed. If the condition command returns
 * <code>true</code> the command status is also <code>true</code>, otherwise <code>false</code>.
 * @param <C> the context class of the commands
 * @author elansma
 */
public class IfCommand<C> extends WrappingCommand<C> {
    /** The condition command */
    private Command<C> condition;

    /**
     * Creates a new if command
     * @param condition the condition command
     * @param command the conditional command
     */
    public IfCommand(Command<C> condition, Command<C> command) {
        super(command);
        this.condition = condition;
    }

    /** {@inheritDoc} */
    @Override
    public boolean execute(C context) {
        if (condition.execute(context)) {
            executeWrappedCommand(context);
            return true;
        }
        return false;
    }

}