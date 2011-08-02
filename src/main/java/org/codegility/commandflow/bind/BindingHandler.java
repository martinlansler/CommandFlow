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
package org.codegility.commandflow.bind;

import org.codegility.commandflow.builder.CommandBuilder;
import org.codegility.commandflow.catalog.CommandCatalog;
import org.codegility.commandflow.io.Resource;

/**
 * A handler for an external command binding.
 * <p>
 * The handler is responsible for creating instances of commands from the external representation and registering the commands with the supplied {@link CommandCatalog}. The binding handler should
 * generally not initialize or resolve command references, this is handled by the {@link CommandBuilder}.
 * <p>
 * A handler can/may be reused to rebuild commands, for instance from a command catalog that is reloadable.
 * <p>
 * Implementation of this interface are not thread-safe if not otherwise noted.
 * @author Martin Lansler
 */
public interface BindingHandler<C> {
    /**
     * Adds one or more command resources to this handler
     * @param resource the command resource(s)
     * @return this handler (for method chaining)
     */
    BindingHandler<C> addCommandXml(Resource... resources);

    /**
     * Called to request the handler to create the commands from the external binding format.
     * <p>
     * This command may be called several times so the handler is expected to be able to clear internal state between invocations.
     * @param catalog the command catalog that to which the created commands should be added
     * @throws BindingException if a binding level error occurs
     */
    void build(CommandCatalog<C> catalog) throws BindingException;

}
