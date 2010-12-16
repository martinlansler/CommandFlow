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
 * Interface to request an initialization call prior to command usage.
 * <p>
 * Commands that implement this interface are guaranteed to have the {@link #init()} method called after all dependencies have been set but before any
 * command execution. Commands created with dependency injection frameworks will typically have other mechanism to perform initialization.
 * @author elansma
 */
public interface CommandInitialization {
    /**
     * Called exactly once to request this command instance to perform any needed validation or initialization actions prior to usage.
     * <p>
     * A command that needs to validate some internal state or other preconditions are suggested to raise a {@link InvalidCommandException} exception
     * if the validation fails, however any other {@link BuilderException} subclass can be used as well.
     * @throws BuilderException if some error occurs during the initialization
     */
    void init() throws BuilderException;
}
