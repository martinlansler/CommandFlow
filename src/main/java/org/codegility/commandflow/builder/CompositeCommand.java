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
package org.codegility.commandflow.builder;

import java.util.Collection;
import java.util.List;
import java.util.RandomAccess;

import org.codegility.commandflow.Command;


/**
 * Interface for a command that can contain other commands.
 * <p>
 * This interface is only of relevance form a builder perspective when wiring commands together, each composite command in turn is a normal
 * {@link Command} from an execution point-of-view (it's to emphasize this distinction that this interface does not inherit from {@link Command}).
 * Commands that need to validate the added command can implement the {@link CommandInitialization} interface.
 * @param <C> the context class of the command
 * @author elansma
 */
public interface CompositeCommand<C> {
    /**
     * Adds a new contained command
     * @param command the command to add
     * @return this command (for method chaining)
     */
    CompositeCommand<C> add(Command<C> command);

    /**
     * Adds all the commands in the given collection
     * @param commands the connection of commands, the order of the commands is determined by the iterator returned by the collection.
     * @return this command (for method chaining)
     */
    CompositeCommand<C> addAll(Collection<Command<C>> commands);

    /**
     * Gets the commands contained in this command.
     * <p>
     * This method may be used to directly manipulate the contained commands.
     * <p>
     * The returned list should be suitable for index based operations, i.e. implement {@link RandomAccess}.
     * @return the list of contained commands
     */
    List<Command<C>> getCommands();
}
