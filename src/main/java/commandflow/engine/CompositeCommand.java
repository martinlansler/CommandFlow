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

/**
 * A super class for all commands that contain an ordered collection of other commands.
 * @param <C> the context class of the commands
 * @author elansma
 */
public abstract class CompositeCommand<C> implements Command<C> {

    /** The collection of commands */
    protected List<Command<C>> commands;
    
    /**
     * Creates a new empty composite command.
     */
    public CompositeCommand() {
        commands = new ArrayList<Command<C>>();
    }

    /**
     * Creates a new composite command from a collection of commands.
     * @param commands the connection of commands, the order of the commands is determined by the iterator returned by the collection.
     */
    public CompositeCommand(Collection<Command<C>> commands) {
        commands = new ArrayList<Command<C>>(commands);
    }

    /**
     * Gets the commands contained in this command.
     * <p>
     * This method may be used to directly manipulate the contained commands.
     * @return the list of contained commands
     */
    public List<Command<C>> getCommands() {
        return commands;
    }
}