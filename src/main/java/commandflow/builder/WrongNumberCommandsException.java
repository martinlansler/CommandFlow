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
package commandflow.builder;

/**
 * Used to indicate that the number of contained commands in a {@link CompositeCommand} is not as expected or required by the command.
 * @author elansma
 */
public class WrongNumberCommandsException extends InvalidCommandException {
    /** Serial id */
    private static final long serialVersionUID = -3078928951753441966L;

    /**
     * Creates a new instance
     * @param minNumber the minimum number of excepted contained commands
     * @param maxNumber the maximum number of excepted contained commands
     * @param actualNumber the actual number of excepted contained commands
     */
    public WrongNumberCommandsException(int minNumber, int maxNumber, int actualNumber) {
        super(String.format("Excepted between %s to %s contained commands, is %s", minNumber, maxNumber, actualNumber));
    }
}
