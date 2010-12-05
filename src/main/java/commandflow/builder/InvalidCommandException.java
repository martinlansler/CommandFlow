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
 * A checked exception used to indicate that a command is invalid.
 * @author elansma
 */
public class InvalidCommandException extends InitializationException {
    /** Serial id */
    private static final long serialVersionUID = -6445344906888825249L;

    public InvalidCommandException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidCommandException(String message) {
        super(message);
    }

    public InvalidCommandException(Throwable cause) {
        super(cause);
    }

}
