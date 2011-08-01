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
package org.codegility.commandflow.test.io;

import java.io.File;
import java.net.URL;

import org.codegility.commandflow.io.ClassPathResource;
import org.codegility.commandflow.io.Resource;
import org.codegility.commandflow.io.URLResource;


/**
 * Tests {@link ClassPathResource}
 * 
 * @author elansma
 */
public class URLResourceTest extends AbstractResourceTest {
    public static final String EXISTING_RESOURCE;
    public static final String NON_EXISTING_RESOURCE;
    static {
        File f = new File("src/test/java/" + URLResourceTest.class.getPackage().getName().replace('.', '/') + "/test.txt");
        EXISTING_RESOURCE = "file:" + f.getAbsolutePath();
        NON_EXISTING_RESOURCE = "file:src/test/java/" + URLResourceTest.class.getPackage().getName().replace('.', '/') + "/nonexisting";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Resource createExistingResource() throws Exception {
        return new URLResource(new URL(EXISTING_RESOURCE));
    }

    /** {@inheritDoc} */
    @Override
    protected Resource createNonExistingResource() throws Exception {
        return new URLResource(new URL(NON_EXISTING_RESOURCE));
    }
}
