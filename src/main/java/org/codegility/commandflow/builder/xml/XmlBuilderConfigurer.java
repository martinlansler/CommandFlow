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
package org.codegility.commandflow.builder.xml;

/**
 * Interface for configuring a {@link XmlCommandBuilder}.
 * <p>
 * This callback interface is used by {@link XmlBuilderFactory} when creating and configuring a {@link XmlCommandBuilder} for a given XML
 * namespace.
 * @author elansma
 */
public interface XmlBuilderConfigurer {
    /**
     * Called to configure a new {@link XmlCommandBuilder} instance
     * @param builder the builder to configure
     * @param <C> the type of the command context class
     */
    <C> void configure(XmlCommandBuilder<C> builder);
}
