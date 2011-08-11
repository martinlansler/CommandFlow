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
package org.codegility.commandflow.example.email;

import org.codegility.commandflow.CommandFlow;
import org.codegility.commandflow.io.ClassPathResource;

/**
 * Sends an email.
 * <p>
 * The the needed SMTP configuration, from, subject, to etc is queried for on stdin during the command flow.
 * @author Martin Lansler
 */
public class SendEmailMain {
    public static void main(String[] args) {
        ClassPathResource resource = new ClassPathResource(SendEmailMain.class.getPackage(), "emailCommand.xml");
        CommandFlow.buildXmlCommandCatalog(resource).execute("sendEmail", new EmailContext());
    }
}
