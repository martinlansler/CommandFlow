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
package commandflow;

/**
 * Main interface for a command.
 * <p>
 * A command may be created by either implementing this interface or using the {@link commandflow.annotation.Command} annotation.
 * @author elansma
 */
public interface Command<C> {
	/**
	 * Executes the command
	 * @param context the command context
	 * @return the command execution status
	 */
	boolean execute(C context);
}
