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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.net.URI;

import org.codegility.commandflow.io.Resource;
import org.junit.Assert;
import org.junit.Test;


/**
 * Basic tests for {@link Resource} implementations.
 * @author elansma
 */
public abstract class AbstractResourceTest {
    protected abstract Resource createExistingResource() throws Exception;

    protected abstract Resource createNonExistingResource() throws Exception;

    @Test
    public void testExists() throws Exception {
        Resource resource = createExistingResource();
        assertTrue(resource.exist());
        //
        resource = createNonExistingResource();
        assertFalse(resource.exist());
    }

    @Test
    public void testGetStream() throws Exception {
        Resource resource = createExistingResource();
        assertNotNull(resource.getInputStream());
        //
        resource = createNonExistingResource();
        try {
            resource.getInputStream();
            Assert.fail();
        } catch (Exception e) {
            // OK
        }
    }

    @Test
    public void testGetUri() throws Exception {
        Resource resource = createExistingResource();
        assertNotNull(resource.getURI());
        //
        resource = createNonExistingResource();
        assertNotNull(resource.getURI());
    }

    @Test
    public void testResolveRelative() throws Exception {
        Resource resource = createExistingResource();
        Resource test2 = resource.resolveRelative(new URI("test2.txt"));
        assertTrue(test2.exist());
    }

}
