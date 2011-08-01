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
package org.codegility.commandflow.catalog;

import org.codegility.commandflow.core.UncheckedException;

/**
 * Unchecked exception for all exception raised by the catalog.
 * <p>
 * Note: This exception is only related to the catalog implementation itself and not to the runtime execution of commands handled by the catalog.
 * @author elansma
 */
public class CatalogException extends UncheckedException {
    private static final long serialVersionUID = -6632328115517415046L;

    public CatalogException(String message, Object... formattingArgs) {
        super(message, formattingArgs);
    }

    public CatalogException(Throwable cause, String message, Object... formattingArgs) {
        super(cause, message, formattingArgs);
    }

    public CatalogException(Throwable cause) {
        super(cause);
    }
}
