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

import java.util.Map;
import java.util.Set;

import commandflow.Command;
import commandflow.builder.CommandBuilder;
import commandflow.builder.CommandInitialization;
import commandflow.builder.xml.XmlCommandBuilder;

/**
 * The default command catalog.
 * <p>
 * The catalog is responsible for holding a named set of commands. The catalog works in distinct phases:
 * <ol>
 * <li>Building phase - using a builder such as {@link XmlCommandBuilder} commands are created and added to the catalog</li>
 * <li>Initialization phase - commands needing initialization (see {@link CommandInitialization}) are initialized</li>
 * <li>Linking phase - all {@link CommandReference} instances are resolved by the catalog, if any cannot be resolved an error is raised</li>
 * <li>Execution phase - the catalog is used to fetch named commands for execution</li>
 * </ol>
 * @param <C> the context class of the command
 * @author elansma
 */
public class DefaultCommandCatalog<C> implements CommandCatalog<C> {
    private Set<CommandBuilder<C>> builders;
    private Map<String, Command<C>> commands;

    /** {@inheritDoc} */
    @Override
    public synchronized CommandCatalog<C> addCommandBuilder(CommandBuilder<C> builder) {
        builders.add(builder);
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public synchronized CommandCatalog<C> build() {
        for (CommandBuilder<C> builder : builders) {
            builder.build(this);
        }
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public CommandCatalog<C> clean() {
        commands.clear();
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public synchronized CommandCatalog<C> link() {
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public synchronized CommandCatalog<C> init() {
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public synchronized CommandCatalog<C> make() {
        build();
        link();
        init();
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public synchronized CommandCatalog<C> addCommand(String name, Command<C> command) {
        commands.put(name, command);
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public synchronized Command<C> getCommand(String name) {
        return commands.get(name);
    }

}
