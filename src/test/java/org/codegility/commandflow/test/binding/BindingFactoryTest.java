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
package org.codegility.commandflow.test.binding;

import static org.junit.Assert.assertNotNull;

import org.codegility.commandflow.binding.BindingFactory;
import org.codegility.commandflow.binding.BindingHandler;
import org.codegility.commandflow.binding.xml.XmlBindingFactory;
import org.junit.Test;

/**
 * Tests {@link BindingFactory}
 * @author Martin Lansler
 */
public class BindingFactoryTest {
    @Test
    public void test() {
        assertKnownNamespaceBindingHanders(XmlBindingFactory.DEFAULT_NAMESPACE, XmlBindingFactory.NAMESPACE_COMMANDFLOW_V1);
    }

    public void assertKnownNamespaceBindingHanders(String... namespaces) {
        for (String namespace : namespaces) {
            BindingHandler<Object> handler = BindingFactory.createHandler(namespace);
            assertNotNull(handler);
        }
    }
}
