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
package commandflow.util;

import java.net.URI;
import java.util.Map;

/**
 * The default resource resolver.
 * <p>
 * The resolver works be delegating the actual resolving to concrete resource resolver bound in this resolver via the URI schema, i.e. hence all used resource URI must be absolute an specify a schema
 * (such as 'file:'). It is possible to set a default resolver without a schema, this will be used as a fallback for all relative resource URI without a schema.
 * @author elansma
 */
public class DefaultResourceResolver implements ResourceResolver {
    /** Maps the bound URI schema to their respective resolvers */
    private Map<String, ResourceResolver> resourceResolvers;

    /**
     * Creates a new delegating resolver with the out-of-the-box resource resolvers.
     */
    public DefaultResourceResolver() {
        // TODO
    }

    /** {@inheritDoc} */
    @Override
    public Resource resolve(URI uri) {
        // TODO
        return null;
    }

}
