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
package org.codegility.commandflow.io;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * The default resource resolver.
 * <p>
 * The resolver works be delegating the actual resolving to concrete resource resolvers bound in this resolver via the URI schema, i.e. hence all used resource URI must be absolute and thus specify a
 * schema (such as 'file:'). If no resolver is found for the URI schema this resolver will fallback to resolving via {@link URLResource}.
 * <p>
 * Resolver can be bound on two levels - either as default resolver available for all created instances of this class, see static method {@link #addDefaultResolver(String, ResourceResolver)}, or
 * specifically bound to a specific instance of this resolver, see method {@link #addResolver(String, ResourceResolver)}.
 * @author Martin Lansler
 */
public class DefaultResourceResolver implements ResourceResolver {
    /** Maps the bound URI schema to their respective resolvers */
    private Map<String, ResourceResolver> resourceResolvers = new HashMap<String, ResourceResolver>();

    /** The default out-of-the-box resource resolvers */
    private static final Map<String, ResourceResolver> defaultResourceResolvers;
    static {
        defaultResourceResolvers = new HashMap<String, ResourceResolver>();
        //
        defaultResourceResolvers.put(FileResource.FileResourceResolver.SCHEMA, new FileResource.FileResourceResolver());
        defaultResourceResolvers.put(ClassPathResource.ClassPathResourceResolver.SCHEMA, new ClassPathResource.ClassPathResourceResolver());
    }

    /**
     * Creates a new delegating resolver with the out-of-the-box resource resolvers.
     */
    public DefaultResourceResolver() {
        synchronized (defaultResourceResolvers) {
            resourceResolvers.putAll(defaultResourceResolvers);
        }
    }

    /**
     * Binds a resource resolver to a specific schema.
     * <p>
     * If an existing resolver exists for the schema it will be replaced.
     * @param schema the schema to bind, if <code>null</code> the resolver will be used as the default resolver
     * @param resolver the resource resolver
     * @return this resolver (for command chaining)
     */
    public DefaultResourceResolver addResolver(String schema, ResourceResolver resolver) {
        resourceResolvers.put(schema, resolver);
        return this;
    }

    /**
     * Adds a default resource resolver to a specific schema, the resolver will be available as out-of-the-box for all for instances of this delegating resolver.
     * <p>
     * If an existing resolver exists for the schema it will be replaced.
     * @param schema the schema to bind, if <code>null</code> the resolver will be used as the default resolver
     * @param resolver the resource resolver
     */
    public static void addDefaultResolver(String schema, ResourceResolver resolver) {
        synchronized (defaultResourceResolvers) {
            defaultResourceResolvers.put(schema, resolver);
        }
    }

    
    @Override
    public Resource resolve(URI uri) {
        if (!uri.isAbsolute()) {
            throw new IllegalArgumentException(String.format("Resource uri %s must be absolute", uri.toString()));
        }
        ResourceResolver resolver = resourceResolvers.get(uri.getScheme());
        return resolver != null ? resolver.resolve(uri) : new URLResource(uri);
    }
}
