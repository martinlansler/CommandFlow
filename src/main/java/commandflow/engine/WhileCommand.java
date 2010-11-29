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
 * A while command.
 * <p>
 * The command executes its wrapped command while command status of the condition command is <code>true</code>. The command status of this command is
 * the last returned command status of the wrapped command, <code>false</code> if the loop never executes.
 * @author elansma
 */
public class WhileCommand<C> extends WrappingCommand<C> {
    /** The while condition command */
    private Command<C> condition;

    /**
     * @param command
     */
    public WhileCommand(Command<C> condition, Command<C> command) {
        super(command);
        this.condition = condition;
    }

    @Override
    public boolean execute(C context) {
        boolean status = false;
        while (condition.execute(context)) {
            status = executeWrappedCommand(context);
        }
        return status;
    }
}
