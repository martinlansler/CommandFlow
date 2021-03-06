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
package org.codegility.commandflow.command;

import org.codegility.commandflow.Command;
import org.codegility.commandflow.builder.BuilderException;
import org.codegility.commandflow.builder.CompositeCommand;

/**
 * Various shared util methods for commands.
 * @author Martin Lansler
 */
public final class CommandUtil {
    /** Util class */
    private CommandUtil() {
    }

    /**
     * Instantiates a new command class
     * @param clazz the command class name
     * @return the command class
     */
    @SuppressWarnings("unchecked")
    public static <C> Command<C> newInstance(String clazz) {
        try {
            return (Command<C>) Class.forName(clazz).newInstance();
        } catch (Exception e) {
            throw new BuilderException(e, "Cannot create command class %s", clazz);
        }
    }

    /**
     * Instantiates a new command class
     * @param clazz the command class
     * @return the command class
     */
    public static <C> Command<C> newInstance(Class<? extends Command<C>> clazz) {
        try {
            return clazz.newInstance();
        } catch (Exception e) {
            throw new BuilderException(e, "Cannot create command class %s", clazz);
        }
    }

    /**
     * Get the given command as a composite command
     * @param command the command
     * @return the composite command
     */
    @SuppressWarnings("unchecked")
    public static <C> CompositeCommand<C> asComposite(Command<C> command) {
        if (!(command instanceof CompositeCommand)) {
            throw new BuilderException("Command %s is not a composite command", command);
        }
        return (CompositeCommand<C>) command;
    }

}
