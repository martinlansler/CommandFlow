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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

/**
 * A {@link File} based resource.
 * @author elansma
 */
public class FileResource extends AbstractResource {
    /** The underlying file */
    private File file;

    /**
     * Creates a new file backed resource
     * @param file to underlying file
     */
    public FileResource(File file) {
        super(file.toURI());
        this.file = file;
    }

    /**
     * Creates a new file backed resource
     * @param uri the file descriptor
     * @throws IllegalArgumentException if the URI is not a valid file descriptor
     */
    public FileResource(URI uri) {
        super(uri);
        this.file = new File(uri);
    }

    /** {@inheritDoc} */
    @Override
    public InputStream getInputStream() throws IOException {
        return new FileInputStream(file);
    }

    /** {@inheritDoc} */
    @Override
    public Resource resolveRelative(URI uri) {
        return new FileResource(new File(file, uri.getSchemeSpecificPart()));
    }

    /**
     * A file resource resolver
     * @author elansma
     */
    public static class FileResourceResolver implements ResourceResolver {
        /** The schema used for file resources */
        public static final String SCHEMA = "file";

        /** {@inheritDoc} */
        @Override
        public Resource resolve(URI uri) {
            return new FileResource(uri);
        }
    }
}
