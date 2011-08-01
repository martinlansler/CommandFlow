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
package org.codegility.commandflow;

/**
 * Main interface for a command.
 * <p>
 * A command may be created by either implementing this interface or using the {@link org.codegility.commandflow.command.annotation.Command} annotation.
 * <p>
 * A command encapsulates a piece of processing logic that can be composed into greater flows by combining with other commands. By convention a command should be state-less, i.e. no state should be
 * kept in the command that is affected by the processing. This requirements allows the processing flow to handle concurrency without any synchronization. All state is kept in the
 * <em>context class</em>, the type of the context class is generic since its implementation is domain specific. A command has a boolean return type, this is referred to as the <em>command status</em>
 * . By convention <code>true</code> usually means that the processing went well or that some desired condition was fulfilled. Commands that implement flow branching can use the return value to decide
 * the processing flow.
 * <p>
 * Exception can raise unchecked exceptions to terminate the command execution, any unchecked exception may be used.
 * @param <C> the context class of the commands
 * @author elansma
 */
public interface Command<C> {
    /**
     * Executes the command
     * 
     * @param context the command context
     * @return the command execution status
     */
    boolean execute(C context);
}
