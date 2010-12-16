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
package commandflow.command;

import static commandflow.builder.BuilderException.raiseWrongNumberContainedCommands;

import commandflow.Command;
import commandflow.builder.BuilderException;
import commandflow.builder.CommandInitialization;

/**
 * Suitable base class for composite commands that only contain one command, i.e. wrap another command.
 * <p>
 * The implementation uses {@link CommandInitialization} to ensure only a single command is contained. If more than one commands are added they will
 * be automatically coerced into a single {@link SequenceCommand}.
 * @param <C> the context class of the command
 * @author elansma
 */
public abstract class AbstractContainsOneCommand<C> extends AbstractCompositeCommand<C> implements CommandInitialization {
    /** The wrapped command */
    private Command<C> command;

    /** {@inheritDoc} */
    @Override
    public void init() throws BuilderException {
        if (getCommands().size() == 0) {
            raiseWrongNumberContainedCommands(getClass().getSimpleName(), 1, Integer.MAX_VALUE, getCommands().size());
        }
        if (getCommands().size() > 1) {
            // coerce multiple actions into a sequence
            Command<C> sequence = new SequenceCommand<C>().addAll(getCommands());
            getCommands().clear();
            add(sequence);
        }
        this.command = getCommands().get(0);
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