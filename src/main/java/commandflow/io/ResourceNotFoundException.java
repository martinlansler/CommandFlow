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
package commandflow.io;

import commandflow.core.UncheckedException;

/**
 * Exception raised to indicate that an expected resource could not be loaded.
 * @author elansma
 */
public class ResourceNotFoundException extends UncheckedException {
    private static final long serialVersionUID = 5261535826089209448L;

    public ResourceNotFoundException(String message, Object... formattingArgs) {
        super(message, formattingArgs);
    }

    public ResourceNotFoundException(Throwable cause, String message, Object... formattingArgs) {
        super(cause, message, formattingArgs);
    }

    public ResourceNotFoundException(Throwable cause) {
        super(cause);
    }

}
