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
package org.codegility.commandflow.builder;

import org.codegility.commandflow.binding.BindingHandler;
import org.codegility.commandflow.catalog.CommandCatalog;
import org.codegility.commandflow.catalog.CommandReference;

/**
 * Interface for a command builder.
 * <p>
 * The builder is responsible for creating instances of commands via one or more supplied {@link BindingHandler} instances and registering the commands with the supplied {@link CommandCatalog}.
 * <p>
 * The builder works in distinct phases:
 * <ol>
 * <li>Building phase - using a {@link BindingHandler} commands are created and added to the catalog</li>
 * <li>Initialization phase - commands needing initialization (see {@link CommandInitialization}) are initialized</li>
 * <li>Linking phase - all {@link CommandReference} instances are resolved by the builder, if any cannot be resolved an error is raised</li>
 * </ol>
 * <p>
 * Implementation of this interface are not thread-safe if not otherwise noted.
 * @author Martin Lansler
 * @param <C> the context class of the command
 */
public interface CommandBuilder<C> {
    /**
     * Adds a binding handler.
     * <p>
     * If a new binding handler is added after {@link #build()} method has been invoked the {@link #build()} method needs to be reinvoked.
     * @param handler the binding builder
     * @return this command builder (for method chaining)
     */
    CommandBuilder<C> addBindingHandler(BindingHandler<C> handler);

    /**
     * Sets the command catalog that this builder uses
     * @param catalog the command catalog
     * @return this command builder (for method chaining)
     */
    CommandBuilder<C> setCommandCatalog(CommandCatalog<C> catalog);

    /**
     * Sets the associated command catalog
     * @return this associated command catalog
     */
    CommandCatalog<C> getCommandCatalog();

    /**
     * Builds all commands by invoking the added binding handlers.
     * <p>
     * If build was previously invoked all existing commands in the catalog will first be removed by invoking {@link #clean()}.
     * @return this command builder (for method chaining)
     * @throws BuilderException if a builder related error occurred
     */
    CommandBuilder<C> build() throws BuilderException;

    /**
     * Removes all existing commands held by the associated catalog.
     * @return this command builder (for method chaining)
     */
    CommandBuilder<C> clean();

    /**
     * Links all static command references, i.e. replaces all references to these with the actual command.
     * <p>
     * This method should be invoked after {@link #build()}, it can be invoked before or after {@link #init()} .
     * @return this command builder (for method chaining)
     */
    CommandBuilder<C> link();

    /**
     * Initializes all command that need initialization, command instances that are already initialized will not be re-initialized.
     * <p>
     * This method should be invoked after {@link #build()}, it can be invoked before or after {@link #init()} .
     * @see CommandInitialization
     * @return this command builder (for method chaining)
     * @throws BuilderException if an initialization related error occurred
     */
    CommandBuilder<C> init() throws BuilderException;

    /**
     * Convenience method that builds, links and initializes the commands.
     * <p>
     * This method is equivalent to invoking (expect that the whole operation is synchronized):
     * <ol>
     * <li>{@link #build()}</li>
     * <li>{@link #link()}</li>
     * <li>{@link #init()}</li>
     * </ol>
     * @return this command builder (for method chaining)
     * @throws BuilderException if a builder or initialization related error occurred
     */
    CommandBuilder<C> make() throws BuilderException;
}
