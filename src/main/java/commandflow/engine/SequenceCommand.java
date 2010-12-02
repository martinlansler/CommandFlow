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

import java.util.Collection;

import commandflow.Command;

/**
 * A command that executes a list of commands in sequence.
 * <p>
 * The command status is the status of the last command in the sequence, the empty sequence of commands always return <code>false</code>.
 * @param <C> the context class of the commands
 * @author elansma
 */
public class SequenceCommand<C> extends CompositeCommand<C> {
    /**
     * Creates a new empty sequence command
     */
    public SequenceCommand() {
        super();
    }

    /**
     * Creates a new sequence command
     * @param commands the command collection to execute
     */
    public SequenceCommand(Collection<Command<C>> commands) {
        super(commands);
    }

    /** {@inheritDoc} */
    @Override
    public boolean execute(C context) {
        boolean status = false;
        for (Command<C> command : getCommands()) {
            status = command.execute(context);
        }
        return status;
    }

}
