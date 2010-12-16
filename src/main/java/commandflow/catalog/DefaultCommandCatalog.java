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

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.RandomAccess;
import java.util.Set;

import commandflow.Command;
import commandflow.builder.BuilderException;
import commandflow.builder.CommandBuilder;
import commandflow.builder.CommandInitialization;
import commandflow.builder.CompositeCommand;
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
    /** The added command builders */
    private Set<CommandBuilder<C>> builders = new HashSet<CommandBuilder<C>>();

    /** The current set of named commands */
    private Map<String, Command<C>> commands = new HashMap<String, Command<C>>();

    /** The set of initialized commands, used to ensure commands are only initialized once */
    private Set<Command<C>> initializedCommands = new HashSet<Command<C>>();

    /** {@inheritDoc} */
    @Override
    public synchronized CommandCatalog<C> addCommandBuilder(CommandBuilder<C> builder) {
        builders.add(builder);
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public synchronized CommandCatalog<C> build() throws BuilderException {
        for (CommandBuilder<C> builder : builders) {
            builder.build(this);
        }
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public CommandCatalog<C> clean() {
        commands.clear();
        initializedCommands.clear();
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public synchronized CommandCatalog<C> link() {
        for (Map.Entry<String, Command<C>> entry : commands.entrySet()) {
            Command<C> command = entry.getValue();
            entry.setValue(link(command));
        }
        return this;
    }

    /**
     * Links a command if it is a static reference
     * @param command the command to link
     * @return the linked command or the unchanged command
     */
    private Command<C> link(Command<C> command) {
        if (isStaticCommandRef(command)) {
            CommandReference<C> reference = (CommandReference<C>) command;
            return getExistingCommand(reference.getReferenceName());
        } else if (command instanceof CompositeCommand) {
            @SuppressWarnings("unchecked")
            CompositeCommand<C> compositeCommand = (CompositeCommand<C>) command;
            link(compositeCommand.getCommands());
        }
        return command;
    }

    /**
     * Links a list of commands
     * @param commands the commands to link
     */
    private void link(List<Command<C>> commands) {
        if (!(commands instanceof RandomAccess)) {
            throw new CatalogException("Lists used for composite commands should implement RandomAccess");
        }
        for (int i = 0; i < commands.size(); i++) {
            commands.set(i, link(commands.get(i)));
        }
    }

    /**
     * Gets an existing command.
     * <p>
     * If the command does not exist an exception is raised.
     * @param name the command name
     * @return
     */
    private Command<C> getExistingCommand(String name) {
        Command<C> command = getCommand(name);
        if (command == null) {
            throw new CatalogException(String.format("Required command '%s' does not exist", name));
        }
        return command;
    }

    /**
     * Checks if the given command is a static {@link CommandReference}
     * @param command the command to check
     * @return <code>true</code> if the command is a static reference
     */
    private boolean isStaticCommandRef(Command<C> command) {
        if (command instanceof CommandReference<?>) {
            CommandReference<C> ref = (CommandReference<C>) command;
            return !ref.isDynamic();
        }
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public synchronized CommandCatalog<C> init() throws BuilderException {
        for (Command<C> command : commands.values()) {
            init(command);
        }
        return this;
    }

    /**
     * Initializes the needed command if it implements {@link CommandInitialization}.
     * <p>
     * If the command is a {@link CompositeCommand} the contained commands are recursively initialized as well.
     * @param command the command to initialize
     * @throws BuilderException if an initialization error occurs
     */
    private void init(Command<C> command) throws BuilderException {
        if (command instanceof CommandInitialization && !initializedCommands.contains(command)) {
            ((CommandInitialization) command).init();
            initializedCommands.add(command);
        }
        if (command instanceof CompositeCommand) {
            @SuppressWarnings("unchecked")
            List<Command<C>> containedCommands = ((CompositeCommand<C>) command).getCommands();
            for (Command<C> containedCommand : containedCommands) {
                init(containedCommand);
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    public synchronized CommandCatalog<C> make() throws BuilderException {
        build();
        link();
        init();
        return this;
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
