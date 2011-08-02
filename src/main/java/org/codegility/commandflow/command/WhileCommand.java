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


/**
 * A while command.
 * <p>
 * The command executes its wrapped command in a loop while command status of the condition command is <code>true</code>, the condition is checked
 * prior to loop execution. The command status of this command is the last returned command status of the wrapped command, <code>false</code> if the
 * loop never executes.
 * @param <C> the context class of the command
 * @author Martin Lansler
 */
public class WhileCommand<C> extends AbstractConditionalCommand<C> {

    @Override
    public boolean execute(C context) {
        boolean status = false;
        while (executeCondition(context)) {
            status = executeAction(context);
        }
        return status;
    }
}
