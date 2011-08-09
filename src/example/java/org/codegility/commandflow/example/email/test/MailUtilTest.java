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
package org.codegility.commandflow.example.email.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.codegility.commandflow.example.email.util.MailUtil;
import org.junit.Test;

/**
 * Tests regexps defined in {@link MailUtil}
 * @author Martin Lansler
 */
public class MailUtilTest {
    @Test
    public void test() {
        assertIsEmail("user@domain.com");
        assertIsEmail("Personal Name <user@domain.com>");
        assertIsEmail("  Personal Name   <user@domain.com>");
        assertIsNotEmail("Abc.example.com");
        assertIsNotEmail("A@b@c@example.com");
        assertIsNotEmail("()[]\\;:,<>@example.com");

    }

    private void assertIsNotEmail(String email) {
        assertFalse(email, MailUtil.RFC822_ADDRESS_PATTERN.matcher(email).matches());
    }

    private void assertIsEmail(String email) {
        assertTrue(email, MailUtil.RFC822_ADDRESS_PATTERN.matcher(email).matches());
    }

    @Test
    public void testGetAddress() {
        final String address = "user@host.domain";
        assertThat(MailUtil.getAddress(String.format("Personal Name <%s>", address)), equalTo(address));
    }

    @Test
    public void testGetPersonal() {
        final String personal = "Personal Name";
        assertThat(MailUtil.getPersonal(String.format("%s <user@host.domain>", personal)), equalTo(personal));
    }

}
