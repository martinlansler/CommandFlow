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
package commandflow.builder.xml;

import java.util.HashMap;
import java.util.Map;

import commandflow.builder.BuilderException;

/**
 * Factory for creating {@link XmlCommandBuilder}.
 * <p>
 * Each {@link XmlCommandBuilder} is registered in the factory under a given namespace via the
 * <p>
 * This class is thread-safe.
 * @author elansma
 */
public class XmlBuilderFactory {
    /** The registered namespaces */
    private static Map<String, XmlBuilderConfigurer> namespaces = new HashMap<String, XmlBuilderConfigurer>();

    // preload the known namespaces...
    static {
        try {
            Class.forName(XmlBuilderFactory.class.getPackage().getName() + ".v1.Configurer").newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Adds a new command name namespace to this factory
     * @param namespaceURI the namespace URI
     * @param configurer the configurer to configure the {@link XmlCommandBuilder}
     */
    public synchronized static void addNamespace(String namespaceURI, XmlBuilderConfigurer configurer) {
        namespaces.put(namespaceURI, configurer);
    }

    public synchronized static <C> XmlCommandBuilder<C> createBuilder(String namespaceURI) {
        XmlBuilderConfigurer configurer = namespaces.get(namespaceURI);
        if (configurer == null) {
            throw new BuilderException("Cannot create an %s for namespace %s", XmlCommandBuilder.class.getSimpleName(), namespaceURI);
        }
        XmlCommandBuilder<C> builder = new XmlCommandBuilder<C>();
        configurer.configure(builder);
        return builder;
    }
}
