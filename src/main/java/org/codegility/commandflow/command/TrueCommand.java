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
 * True command, i.e. always returns command status <code>true</code>.
 * @param <C> the context class of the command
 * @author Martin Lansler
 */
public class TrueCommand<C> implements Command<C> {
    /** A cached instance of the true command */
    private static Command<Object> TRUE = new TrueCommand<Object>();

    /**
     * Gets a cached instance of the true command
     * @param <C> the context class of the commands
     * @return a cached true command
     */
    @SuppressWarnings("unchecked")
    public static <T> Command<T> getInstance() {
        return (Command<T>) TRUE;
    }

    @Override
    public boolean execute(C context) {
        return true;
    }
}
