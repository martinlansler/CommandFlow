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
package org.codegility.commandflow.builder;

import org.codegility.commandflow.core.UncheckedException;

/**
 * Base exception for all builder level exception.
 * @author elansma
 */
public class BuilderException extends UncheckedException {
    private static final long serialVersionUID = 743597909102060267L;

    public BuilderException(String message, Object... formattingArgs) {
        super(message, formattingArgs);
    }

    public BuilderException(Throwable cause, String message, Object... formattingArgs) {
        super(cause, message, formattingArgs);
    }

    public BuilderException(Throwable cause) {
        super(cause);
    }

    /**
     * Throws a suitable exception for composite commands not containing the required number of commands.
     * @param commandName the command name
     * @param minNumber the minimum number of expected commands
     * @param maxNumber the maximum number of expected commands
     * @param actualNumber the actual number of expected commands
     * @throws BuilderException an new builder command exception
     */
    public static void raiseWrongNumberContainedCommands(String commandName, int minNumber, int maxNumber, int actualNumber) throws BuilderException {
        throw new BuilderException("Command %s expects between %s to %s contained commands, is %s", commandName, minNumber, maxNumber, actualNumber);
    }

}
