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

import org.codegility.commandflow.bind.BindingFactory;
import org.codegility.commandflow.bind.BindingHandler;
import org.codegility.commandflow.bind.xml.XmlBindingFactory;
import org.codegility.commandflow.builder.CommandBuilder;
import org.codegility.commandflow.builder.DefaultCommandBuilder;
import org.codegility.commandflow.catalog.CommandCatalog;
import org.codegility.commandflow.catalog.DefaultCommandCatalog;
import org.codegility.commandflow.io.Resource;
import org.codegility.commandflow.util.XmlNamespaceUtil;

/**
 * "One stop shop" for the most common command flow setups.
 * @author Martin Lansler
 */
public class CommandFlow {
    /** Util class */
    private CommandFlow() {
    }

    /**
     * Builds a {@link CommandCatalog} from one or more XML resources.
     * <p>
     * The XML namespace is determined by examining the namespace set on the top-level element of the first specified resource, if none is found {@link XmlBindingFactory#DEFAULT_NAMESPACE} is assumed.
     * @param resources the XML command resources
     * @return the command catalog
     * @param <C> the command context class
     */
    public static <C> CommandCatalog<C> buildXmlCommandCatalog(Resource... resources) {
        if (resources.length == 0) {
            throw new IllegalArgumentException("At least one XML resource must be specified");
        }
        String namespace = XmlNamespaceUtil.getTopLevelNamespace(resources[0]);
        namespace = namespace == null ? XmlBindingFactory.DEFAULT_NAMESPACE : namespace;

        return buildCommandCatalog(namespace, resources);
    }

    /**
     * Builds a {@link CommandCatalog} from one or more resources.
     * @param namespace the binding namespace for all the resources, see {@link BindingFactory}
     * @param resources the XML command resources
     * @return the command catalog
     * @param <C> the command context class
     */
    public static <C> CommandCatalog<C> buildCommandCatalog(String namespace, Resource... resources) {
        if (resources.length == 0) {
            throw new IllegalArgumentException("At least one resource must be specified");
        }
        BindingHandler<C> bindingHandler = BindingFactory.createHandler(namespace);
        for (Resource resource : resources) {
            bindingHandler.addCommandResource(resource);
        }

        CommandCatalog<C> catalog = new DefaultCommandCatalog<C>();

        CommandBuilder<C> builder = new DefaultCommandBuilder<C>(catalog);
        builder.addBindingHandler(bindingHandler);
        builder.make();

        return catalog;
    }

}
