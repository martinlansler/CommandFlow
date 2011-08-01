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
package org.codegility.commandflow.core;

import org.codegility.commandflow.Command;

/**
 * Base exception for all unchecked exceptions raised in org.codegility.commandflow.
 * <p>
 * Note: This exception is not mandated to have to be used by {@link Command} implementations.
 * @author elansma
 */
public class UncheckedException extends RuntimeException {
    private static final long serialVersionUID = 3019374003362350935L;

    /**
     * Creates a new exception
     * @param cause the wrapped exception
     * @param message message
     * @param formattingArgs optional formatting arguments for message, syntax as for {@link String#format(String, Object...)}
     */
    public UncheckedException(Throwable cause, String message, Object... formattingArgs) {
        super(String.format(message, formattingArgs), cause);
    }

    /**
     * Creates a new exception
     * @param message message
     * @param formattingArgs optional formatting arguments for message, syntax as for {@link String#format(String, Object...)}
     */
    public UncheckedException(String message, Object... formattingArgs) {
        super(String.format(message, formattingArgs));
    }

    /**
     * Creates a new exception
     * @param cause the wrapped exception
     */
    public UncheckedException(Throwable cause) {
        super(cause);
    }

}