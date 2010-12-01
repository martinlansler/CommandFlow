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
 * A wrapping command with a condition.
 * @param <C> the context class of the commands
 * @author elansma
 */
public abstract class ConditionalWrappingCommand<C> extends WrappingCommand<C> {

    /** The condition command */
    private Command<C> condition;

    /**
     * @param command
     */
    public ConditionalWrappingCommand(Command<C> condition, Command<C> command) {
        super(command);
        this.condition = condition;
    }

    /**
     * Executes the condition command
     * @param context the command context
     * @return the condition command status
     */
    protected boolean executeConditionCommand(C context) {
        return condition.execute(context);
    }
}