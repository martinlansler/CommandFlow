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
package org.codegility.commandflow.binding.xml;

import java.util.HashMap;
import java.util.Map;

import org.codegility.commandflow.binding.BindingException;
import org.codegility.commandflow.binding.BindingHandler;
import org.codegility.commandflow.binding.BindingFactory.ConcreteBindingFactory;
import org.codegility.commandflow.binding.xml.v1.XmlBindingConfigurerV1;

/**
 * Factory for creating {@link XmlBindingHandler}.
 * <p>
 * Each {@link XmlBindingHandler} is registered in the factory under a given namespace via the {@link #addNamespace(String, XmlBindingConfigurer)} method.
 * <p>
 * This class is thread-safe.
 * @author Martin Lansler
 */
public class XmlBindingFactory implements ConcreteBindingFactory {
    /** The registered namespaces */
    private static Map<String, XmlBindingConfigurer> namespaces = new HashMap<String, XmlBindingConfigurer>();

    // preload the known namespaces (via static initialization blocks in class)...
    static {
        try {
            Class.forName(XmlBindingConfigurerV1.class.getName());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /** Namespace for command flow {@value} */
    public static final String NAMESPACE_COMMANDFLOW_V1 = XmlBindingConfigurerV1.NAMESPACE;

    /** The default XML namespace if none is specified, currently {@value} */
    public static final String DEFAULT_NAMESPACE = NAMESPACE_COMMANDFLOW_V1;

    /**
     * Adds a new command name namespace to this factory
     * @param namespaceURI the namespace URI
     * @param configurer the configurer to configure the {@link XmlBindingHandler}
     */
    public static void addNamespace(String namespaceURI, XmlBindingConfigurer configurer) {
        synchronized (namespaces) {
            namespaces.put(namespaceURI, configurer);
        }
    }

    @Override
    public <C> BindingHandler<C> createHandler(String namespace) {
        synchronized (namespaces) {
            XmlBindingConfigurer configurer = namespaces.get(namespace);
            if (configurer == null) {
                throw new BindingException("Cannot create an %s for namespace %s", XmlBindingHandler.class.getSimpleName(), namespace);
            }
            XmlBindingHandler<C> handler = new XmlBindingHandler<C>();
            configurer.configure(handler);
            return handler;
        }
    }
}
