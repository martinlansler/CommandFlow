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
package org.codegility.commandflow.io;

import java.net.URI;

/**
 * Suitable base class for a {@link Resource} implementation.
 * @author elansma
 */
public abstract class AbstractResource implements Resource {
    /** The resource identifier */
    private URI uri;

    /**
     * Create a new resource with the given identifier
     * @param uri the resource identifier
     */
    protected AbstractResource(URI uri) {
        this.uri = uri;
    }

    /**
     * Creates an empty resource, use {@link #setUri(URI)} to set the URI.
     */
    protected AbstractResource() {
    }

    /** {@inheritDoc} */
    @Override
    public URI getURI() {
        return uri;
    }

    /**
     * @param uri the uri to set
     */
    protected void setUri(URI uri) {
        this.uri = uri;
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Resource)) {
            return false;
        }
        Resource resource = (Resource) obj;
        return uri.equals(resource.getURI());
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return uri.hashCode();
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return String.format("Resource - uri: %s", uri);
    }

}
