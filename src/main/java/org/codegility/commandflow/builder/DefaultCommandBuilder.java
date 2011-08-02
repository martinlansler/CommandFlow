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

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.RandomAccess;
import java.util.Set;

import org.codegility.commandflow.Command;
import org.codegility.commandflow.bind.BindingHandler;
import org.codegility.commandflow.catalog.CommandCatalog;
import org.codegility.commandflow.catalog.CommandReference;

/**
 * The default command builder.
 * @param <C> the context class of the command
 * @author Martin Lansler
 */
public class DefaultCommandBuilder<C> implements CommandBuilder<C> {
    /** The added binding handlers */
    private Set<BindingHandler<C>> handlers = new HashSet<BindingHandler<C>>();

    /** The associated catalog */
    private CommandCatalog<C> catalog;

    /** The set of initialized commands, used to ensure commands are only initialized once */
    private Set<Command<C>> initializedCommands = new HashSet<Command<C>>();

    /**
     * Creates a new command builder.
     * <p>
     * The command catalog must be set via the {@link #setCommandCatalog(CommandCatalog)} method prior to use.
     */
    public DefaultCommandBuilder() {
    }

    /**
     * Creates a new command builder
     * @param catalog the command catalog
     */
    public DefaultCommandBuilder(CommandCatalog<C> catalog) {
        this.catalog = catalog;
    }

    @Override
    public CommandBuilder<C> setCommandCatalog(CommandCatalog<C> catalog) {
        this.catalog = catalog;
        return this;
    }

    @Override
    public CommandCatalog<C> getCommandCatalog() {
        return catalog;
    }

    @Override
    public synchronized CommandBuilder<C> addBindingHandler(BindingHandler<C> handler) {
        handlers.add(handler);
        return this;
    }

    @Override
    public synchronized CommandBuilder<C> build() throws BuilderException {
        for (BindingHandler<C> handler : handlers) {
            handler.build(catalog);
        }
        return this;
    }

    @Override
    public CommandBuilder<C> clean() {
        catalog.clear();
        initializedCommands.clear();
        return this;
    }

    @Override
    public synchronized CommandBuilder<C> link() {
        Map<String, Command<C>> commands = catalog.getCommands();
        for (Map.Entry<String, Command<C>> entry : commands.entrySet()) {
            Command<C> command = entry.getValue();
            entry.setValue(link(command));
        }
        catalog.setCommands(commands);
        return this;
    }

    /**
     * Links a command if it is a static reference
     * @param command the command to link
     * @return the linked command or the unchanged command
     */
    private Command<C> link(Command<C> command) {
        if (isCommandRef(command)) {
            CommandReference<C> reference = (CommandReference<C>) command;
            if (reference.isDynamic()) {
                reference.setCommandCatalog(catalog);
            } else {
                return getExistingCommand(reference.getReferenceName());
            }
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
            throw new BuilderException("Lists used for composite commands should implement RandomAccess");
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
        Command<C> command = catalog.getCommand(name);
        if (command == null) {
            throw new BuilderException("Required command '%s' does not exist", name);
        }
        return command;
    }

    /**
     * Checks if the given command is a {@link CommandReference}
     * @param command the command to check
     * @return <code>true</code> if the command is a reference
     */
    private boolean isCommandRef(Command<C> command) {
        return command instanceof CommandReference<?>;
    }

    @Override
    public synchronized CommandBuilder<C> init() throws BuilderException {
        for (Command<C> command : catalog.getCommands().values()) {
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

    @Override
    public synchronized CommandBuilder<C> make() throws BuilderException {
        build();
        link();
        init();
        return this;
    }

}
