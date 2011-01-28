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
package commandflow.test.io;

import java.io.File;

import commandflow.io.ClassPathResource;
import commandflow.io.FileResource;
import commandflow.io.Resource;

/**
 * Tests {@link ClassPathResource}
 * 
 * @author elansma
 */
public class FileResourceTest extends AbstractResourceTest {
    public static final String EXISTING_RESOURCE;
    static {
        EXISTING_RESOURCE = "src/test/java/" + FileResourceTest.class.getPackage().getName().replace('.', '/') + "/test.txt";
    }

    /** {@inheritDoc} */
    @Override
    protected Resource createExistingResource() {
        return new FileResource(new File(EXISTING_RESOURCE));
    }

    /** {@inheritDoc} */
    @Override
    protected Resource createNonExistingResource() {
        // TODO
        return null;
    }

}
