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
package commandflow.io;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

/**
 * Represents a resource.
 * <p>
 * This class is heavily influenced by the Spring <code>org.springframework.core.io.Resource</code> interface however to avoid a runtime dependency to Spring this inspired interface is used for the
 * core parts of commandflow. The difference compared to the Spring resource is that each resource <em>must</em> have a resource identifier in the form of a {@link URI}, this identifier is used when
 * resolving a resource, see {@link ResourceResolver}.
 * @author elansma
 */
public interface Resource {
    /**
     * Checks if this resource exists.
     * <p>
     * This method provides a guarantee that the resource pointed to by this resource actually exists.
     * @return <code>true</code> if this resource exists
     */
    boolean exist();

    /**
     * Opens a new input stream to read the resource.
     * <p>
     * Note: Each invocation of this method <em>must</em> return a new stream. It is also expected that the latest version of the resource is returned if this is applicable, for instance a file on
     * disk that can be changed.
     * <p>
     * It's the callers responsibility to ensure the stream is properly closed.
     * @return a new stream to read the resource
     * @throws ResourceNotFoundException if the stream could not be found
     * @throws IOException if an IO error occurs
     */
    InputStream getInputStream() throws IOException;

    /**
     * Gets the identifier for the resource
     * @return the resource identifier
     */
    URI getURI();

    /**
     * Resolves a relative resource to this resource, i.e. a resource with a relative identifier without a schema specifier
     * @param uri the relative resource identifier
     * @return the resolved resource, <code>null</code> if the resource cannot be resolved
     */
    Resource resolveRelative(URI uri);
}
