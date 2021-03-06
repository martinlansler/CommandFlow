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

/**
 * False command, i.e. always returns command status <code>false</code>.
 * @param <C> the context class of the command
 * @author Martin Lansler
 */
public class FalseCommand<C> implements Command<C> {
    /** A cached instance of the false command */
    private static Command<Object> FALSE = new FalseCommand<Object>();

    /**
     * Gets a cached instance of the false command
     * @param <C> the context class of the commands
     * @return a cached false command
     */
    @SuppressWarnings("unchecked")
    public static <T> Command<T> getInstance() {
        return (Command<T>) FALSE;
    }

    @Override
    public boolean execute(C context) {
        return false;
    }
}
