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
package org.codegility.commandflow.binding;

import java.util.HashMap;
import java.util.Map;

import org.codegility.commandflow.binding.xml.XmlBindingFactory;

/**
 * Factory class for creating a handler for a given binding.
 * <p>
 * Each binding is represented by a specific namespace and a suitable handler can be created via the {@link #createHandler(String)} method. *
 * <p>
 * This factory will delegate handler creation to a concrete factory as implemented by the {@link ConcreteBindingFactory}. Binding implementations should add their factories via the
 * @author Martin Lansler
 */
public class BindingFactory {
    /**
     * Factory interface for delegating to actual binding implementations.
     */
    public static interface ConcreteBindingFactory {
        /**
         * Called to allow implementation of a binding to create a handler
         * @param namespace the namespace of the handler
         * @return the handler
         * @param <C>
         */
        <C> BindingHandler<C> createHandler(String namespace);
    }

    /** The registered handlers per namespace */
    private static Map<String, ConcreteBindingFactory> namespace2handler = new HashMap<String, ConcreteBindingFactory>();

    // preload the known namespaces (via static initialization blocks in class)...
    static {
        try {
            Class.forName(XmlBindingFactory.class.getName());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Used by binding implementations to add a concerete binding factory for a given namespace.
     * @param namespace the namespace URI
     * @param concreteBindingFactory the concrete binding factory
     */
    public static void addConcreteBindingFactory(String namespace, ConcreteBindingFactory concreteBindingFactory) {
        synchronized (namespace2handler) {
            namespace2handler.put(namespace, concreteBindingFactory);
        }
    }

    /**
     * Creates a handler for the given namespace
     * @param <C>
     * @param namespace
     * @return
     */
    public static <C> BindingHandler<C> createHandler(String namespace) {
        synchronized (namespace2handler) {
            ConcreteBindingFactory concreteBindingFactory = namespace2handler.get(namespace);
            if (concreteBindingFactory == null) {
                throw new BindingException("Could not find a concrete binding factory for namespace %s", namespace);
            }
            return concreteBindingFactory.createHandler(namespace);
        }
    }
}
