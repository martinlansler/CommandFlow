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

import commandflow.engine.CommandCatalog;

/**
 * Interface for a command builder.
 * <p>
 * The builder is responsible for creating instances of commands from some external representation and registering the commands with the supplied
 * {@link CommandCatalog}. The builder should generally not initialize or resolve command references, this is handled by the command catalog.
 * <p>
 * A builder can be reused to rebuild commands, for instance from a command catalog that is reloadable.
 * @author elansma
 */
public interface CommandBuilder<C> {
    /**
     * Called to request the builder to build its commands.
     * <p>
     * This command may be called several times so the builder is expected to be able to clear/reset/reload internal state.
     * @param catalog the command catalog that manages the created command
     */
    void build(CommandCatalog<C> catalog);
}
