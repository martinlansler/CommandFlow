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

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

import org.codegility.commandflow.Command;

/**
 * The default command catalog.
 * <p>
 * The catalog is responsible for holding a named set of commands.
 * @param <C> the context class of the command
 * @author Martin Lansler
 */
public class DefaultCommandCatalog<C> implements CommandCatalog<C> {
    /** The current set of named commands */
    private volatile Map<String, Command<C>> commands = new ConcurrentHashMap<String, Command<C>>();

    @SuppressWarnings("rawtypes")
    private static final AtomicReferenceFieldUpdater<DefaultCommandCatalog, Map> fieldUpdater;
    static {
        fieldUpdater = AtomicReferenceFieldUpdater.newUpdater(DefaultCommandCatalog.class, Map.class, "commands");
    }

    @Override
    public CommandCatalog<C> addCommand(String name, Command<C> command) {
        commands.put(name, command);
        return this;
    }

    @Override
    public Command<C> getCommand(String name) {
        return commands.get(name);
    }

    @Override
    public Command<C> removeCommand(String name) {
        return commands.remove(name);
    }

    @Override
    public Map<String, Command<C>> getCommands() {
        return new HashMap<String, Command<C>>(commands);
    }

    @Override
    public CommandCatalog<C> clear() {
        commands.clear();
        return this;
    }

    @Override
    public CommandCatalog<C> setCommands(Map<String, Command<C>> commands) {
        clear();
        Map<String, Command<C>> newCommands = new ConcurrentHashMap<String, Command<C>>(commands);
        fieldUpdater.set(this, newCommands);
        return this;
    }

}
