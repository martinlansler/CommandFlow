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

import org.codegility.commandflow.Command;

/**
 * A reference to a named command.
 * <p>
 * The reference can either be static or dynamic. A static reference is always resolved before command execution, a dynamic at the moment of execution. Using a static reference is better
 * performance-wise, however a dynamic reference adds a level of indirection and hence a flexibility in allowing reference target command to be changed during execution.
 * <p>
 * Before the command reference can be executed the associated {@link CommandCatalog} must be set. Note: A static reference can never be executed, attempting this raises a runtime exception.
 * @author elansma
 */
public class CommandReference<C> implements Command<C> {
    /** Name of command this reference refers to */
    private String referenceName;
    /** If <code>true</code> this is a dynamic reference */
    private boolean isDynamic;

    /** The command catalog */
    private CommandCatalog<C> catalog;

    /**
     * Creates a new static command reference
     * @param referenceName the referenceName of the command to refer to
     */
    public CommandReference(String referenceName) {
        this.referenceName = referenceName;
        this.isDynamic = false;
    }

    /**
     * Creates a new static command reference
     * @param referenceName the referenceName of the command to refer to
     * @param isDynamic <code>true</code> if the reference is dynamic
     */
    public CommandReference(String name, boolean isDynamic) {
        this.referenceName = name;
        this.isDynamic = isDynamic;
    }

    /**
     * Sets the command catalog.
     * <p>
     * The catalog is needed to resolve references.
     * @param catalog the command catalog
     */
    public void setCommandCatalog(CommandCatalog<C> catalog) {
        this.catalog = catalog;
    }

    /**
     * @return <code>true</code> if the reference is dynamic
     */
    public boolean isDynamic() {
        return isDynamic;
    }

    /** {@inheritDoc} */
    @Override
    public boolean execute(C context) {
        if (!isDynamic) {
            throw new CatalogException("Cannot execute static command reference '%s'", referenceName);
        }
        if (catalog == null) {
            throw new CatalogException("No command catalog is set for dynamic command reference '%s'", referenceName);
        }
        Command<C> command = getReferencedCommand();
        return command.execute(context);
    }

    /**
     * @return the references command
     */
    public Command<C> getReferencedCommand() {
        Command<C> command = catalog.getCommand(referenceName);
        if (command == null) {
            throw new CatalogException("Cannot resolve dynamic command reference '%s'", referenceName);
        }
        return command;
    }

    /**
     * @return the name of the command this reference refers to
     */
    public String getReferenceName() {
        return referenceName;
    }

}
