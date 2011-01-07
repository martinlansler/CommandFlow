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
 * A classpath resource.
 * <p>
 * Search order for classpath resources are:
 * <ol>
 * <li>The classloader associated with this class, see {@link Class#getClassLoader()}</li>
 * <li>The classloader of the current thread, see {@link Thread#getContextClassLoader()}</li>
 * </ol>
 * @author elansma
 */
public class ClassPathResource extends AbstractResource {

    /**
     * Creates a new classpath resource using the current threads context class loader, see {@link Thread#getContextClassLoader()}.
     * @param uri classpath resource identifier
     */
    public ClassPathResource(URI uri) {
        super(uri);
    }

    /** {@inheritDoc} */
    @Override
    public Resource resolveRelative(URI uri) {
        return new ClassPathResource(getURI().resolve(uri));
    }

    /** {@inheritDoc} */
    @Override
    public InputStream getInputStream() throws IOException {
        String clazz = getURI().getSchemeSpecificPart();
        ClassLoader cl = getClass().getClassLoader();
        InputStream is = cl.getResourceAsStream(clazz);
        if (is != null) {
            return is;
        }
        cl = Thread.currentThread().getContextClassLoader();
        is = cl.getResourceAsStream(clazz);
        if (is != null) {
            return is;
        }
        throw new IOException(String.format("Could not find classpath resource '%s'", clazz));
    }

    /**
     * A classpath resource resolver
     * @author elansma
     */
    public static class ClassPathResourceResolver implements ResourceResolver {
        /** The schema used for classpath resources */
        public static final String SCHEMA = "classpath";

        /** {@inheritDoc} */
        @Override
        public Resource resolve(URI uri) {
            return new ClassPathResource(uri);
        }
    }
}
