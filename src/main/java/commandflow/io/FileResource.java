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
        super(file.getAbsoluteFile().toURI());
        this.file = file;
    }

    /**
     * Creates a new file backed resource
     * @param uri the file descriptor
     * @throws IllegalArgumentException if the URI is not a valid file descriptor
     */
    public FileResource(URI uri) {
        super(uri);
        this.file = new File(uri).getAbsoluteFile();
    }

    /** {@inheritDoc} */
    @Override
    public boolean exist() {
        return FileResource.exist(file);
    }

    /**
     * Static form of {@link #exist()}
     * @param file the file to check
     * @return <code>true</code> if the file exists
     */
    protected static boolean exist(File file) {
        return file.isFile() && file.canRead();
    }

    /** {@inheritDoc} */
    @Override
    public InputStream getInputStream() throws IOException {
        return new FileInputStream(file);
    }

    /** {@inheritDoc} */
    @Override
    public Resource resolveRelative(URI uri) {
        return new FileResource(getURI().resolve(uri));
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
            File file = new File(uri);
            return FileResource.exist(file) ? new FileResource(file) : null;
        }
    }
}