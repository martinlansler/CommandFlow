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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import commandflow.Command;
import commandflow.builder.CompositeCommand;

/**
 * Suitable base class for composite commands.
 * @param <C> the context class of the command
 * @author elansma
 */
public abstract class AbstractCompositeCommand<C> implements Command<C>, CompositeCommand<C> {
    /** The collection of commands */
    private List<Command<C>> commands = new ArrayList<Command<C>>();

    /** {@inheritDoc} */
    @Override
    public AbstractCompositeCommand<C> add(Command<C> command) {
        commands.add(command);
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public CompositeCommand<C> addAll(Collection<Command<C>> commands) {
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public List<Command<C>> getCommands() {
        return commands;
    }
}