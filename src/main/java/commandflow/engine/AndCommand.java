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
 * A short-circuit and command.
 * <p>
 * The command executes its contained commands until one command returns <code>false</code>. If all commands return <code>true</code> the command
 * status is also <code>true</code>. An empty and command always returns <code>false</code>.
 * @param <C> the context class of the command
 * @author elansma
 */
public class AndCommand<C> extends AbstractCompositeCommand<C> {
    /** {@inheritDoc} */
    @Override
    public boolean execute(C context) {
        for (Command<C> command : getCommands()) {
            if (!command.execute(context)) {
                return false;
            }
        }
        return getCommands().isEmpty() ? false : true;
    }

}
