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
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * A {@link URL} backed resource.
 * @author elansma
 */
public class URLResource extends AbstractResource {
    /** The backed URL */
    private URL url;

    /**
     * Creates a new URL backed resource
     * @param url the backing URL
     */
    public URLResource(URL url) {
        this.url = url;
        try {
            setUri(url.toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Creates a new URL backed resource
     * @param uri the backing URI
     */
    public URLResource(URI uri) {
        super(uri);
        try {
            this.url = uri.toURL();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    /** {@inheritDoc} */
    @Override
    public boolean exist() {
        return URLResource.exists(getURI());
    }

    protected static boolean exists(URI uri) {
        InputStream is = null;
        try {
            is = uri.toURL().openStream();
            return is != null;
        } catch (IOException e) {
            return false;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    ; // ignore
                }
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    public InputStream getInputStream() throws IOException {
        return url.openStream();
    }

    /** {@inheritDoc} */
    @Override
    public Resource resolveRelative(URI uri) {
        return new URLResource(getURI().resolve(uri));
    }

}
