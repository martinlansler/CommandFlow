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
package org.codegility.commandflow.catalog;

import java.util.Map;

import org.codegility.commandflow.Command;
import org.codegility.commandflow.binding.BindingHandler;

/**
 * The command catalog interface.
 * <p>
 * The catalog is responsible for holding a named set of commands.
 * <p>
 * Implementation of this interface <em>must be thread-safe</em>.
 * @param <C> the context class of the command
 * @author Martin Lansler
 */
public interface CommandCatalog<C> {

    /**
     * Adds a named command.
     * <p>
     * This method is typically invoked during from a {@link BindingHandler} implementation but it can also be directly invoked to add a command programatically. If a previous command already exists
     * with this name it will be replaced with the new command.
     * <p>
     * If a new command is added after {@link #link()} and {@link #init()} have been invoked these methods need to be reinvoked to ensure that the command is linked and initialized.
     * @param name the command name
     * @param command the command
     * @return this command catalog (for method chaining)
     */
    CommandCatalog<C> addCommand(String name, Command<C> command);

    /**
     * Gets a named command
     * @param name the command name
     * @return the command, <code>null</code> if not found
     */
    Command<C> getCommand(String name);

    /**
     * Removes a specified command from the catalog
     * @param name the command to remove
     * @return the removed command, <code>null</code> if the catalog id not contain the named command
     */
    Command<C> removeCommand(String name);

    /**
     * Clears all commands held by this catalog
     * @return this command catalog (for method chaining)
     */
    CommandCatalog<C> clear();

    /**
     * Gets a map with all commands, map key is the command name.
     * <p>
     * The returned map is safe to modify as this method will create a new map each time.
     * @return a mapping of all held commands
     */
    Map<String, Command<C>> getCommands();

    /**
     * Clears all current commands and set the specified commands as specified in the map, mapped via command names.
     * <p>
     * The passed in map is copied so it safe to modify it after this call.
     * @param commands the new commands to set
     * @return this command catalog (for method chaining)
     */
    CommandCatalog<C> setCommands(Map<String, Command<C>> commands);

    /**
     * Convenience method to retrieve and execute the named command from this catalog
     * @param name the name of the command to execute
     * @param context the command context
     * @return the boolean result of executing the command
     * @throws CatalogException if the names command could not be found
     */
    boolean execute(String name, C context) throws CatalogException;
}
