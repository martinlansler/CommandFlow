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
package commandflow.catalog;

import commandflow.Command;
import commandflow.builder.BuilderException;
import commandflow.builder.CommandBuilder;
import commandflow.builder.CommandInitialization;
import commandflow.builder.xml.XmlCommandBuilder;

/**
 * The command catalog interface.
 * <p>
 * The catalog is responsible for holding a named set of commands. The catalog works in distinct phases:
 * <ol>
 * <li>Building phase - using a builder such as {@link XmlCommandBuilder} commands are created and added to the catalog</li>
 * <li>Linking phase - all static {@link CommandReference} instances are resolved by the catalog, if any cannot be resolved an error is raised</li>
 * <li>Initialization phase - commands needing initialization (see {@link CommandInitialization}) are initialized</li>
 * <li>Execution phase - the catalog is used to fetch named commands for execution</li>
 * </ol>
 * <p>
 * Implementation of this interface are required to be thread-safe.
 * @param <C> the context class of the command
 * @author elansma
 */
public interface CommandCatalog<C> {
    /**
     * Adds a command builder.
     * <p>
     * If a new command builder is added after {@link #build()} method has been invoked the {@link #build()} method needs to be reinvoked.
     * @param builder the command builder
     * @return the command catalog (for method chaining)
     */
    CommandCatalog<C> addCommandBuilder(CommandBuilder<C> builder);

    /**
     * Builds all commands by invoking the added command builders.
     * <p>
     * If build was previously invoked all existing commands will first be removed by invoking {@link #clean()}.
     * @return the command catalog (for method chaining)
     * @throws BuilderException if a builder related error occurred
     */
    CommandCatalog<C> build() throws BuilderException;

    /**
     * Removes all existing commands held by this catalog.
     * @return the command catalog (for method chaining)
     */
    CommandCatalog<C> clean();

    /**
     * Links all static command references, i.e. replaces all references to these with the actual command.
     * <p>
     * This method should be invoked after {@link #build()}, it can be invoked before or after {@link #init()} .
     * @return the command catalog (for method chaining)
     */
    CommandCatalog<C> link();

    /**
     * Initializes all command that need initialization, command instances that are already initialized will not be re-initialized.
     * <p>
     * This method should be invoked after {@link #build()}, it can be invoked before or after {@link #init()} .
     * @see CommandInitialization
     * @return the command catalog (for method chaining)
     * @throws BuilderException if an initialization related error occurred
     */
    CommandCatalog<C> init() throws BuilderException;

    /**
     * Convenience method that builds, links and initializes the catalog.
     * <p>
     * This method is equivalent to invoking (expect that the whole operation is synchronized):
     * <ol>
     * <li>{@link #build()}</li>
     * <li>{@link #link()}</li>
     * <li>{@link #init()}</li>
     * </ol>
     * @return the command catalog (for method chaining)
     * @throws BuilderException if a builder or initialization related error occurred
     */
    CommandCatalog<C> make() throws BuilderException;

    /**
     * Adds a named command.
     * <p>
     * This method is typically invoked from the {@link CommandBuilder} but it can also be directly invoked to add a command programatically. If a
     * previous command already exists with this name it will be replaced with the new command.
     * <p>
     * If a new command is added after {@link #link()} and {@link #init()} have been invoked these methods need to be reinvoked to ensure that the
     * command is linked and initialized.
     * @param name the command name
     * @param command the command
     * @return the command catalog (for method chaining)
     */
    CommandCatalog<C> addCommand(String name, Command<C> command);

    /**
     * Gets a named command
     * @param name the command name
     * @return the command, <code>null</code> if not found
     */
    Command<C> getCommand(String name);
}
