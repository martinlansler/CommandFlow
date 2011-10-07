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
package org.codegility.commandflow.example.email.commands;

import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;

import org.codegility.commandflow.Command;
import org.codegility.commandflow.example.email.EmailContext;
import org.codegility.commandflow.example.email.EmailException;
import org.codegility.commandflow.example.email.util.IOUtils;

/**
 * Adds a textual mail body.
 * <p>
 * This command adds a new {@link MimeBodyPart} rather than using the {@link EmailContext#setText(String)} method.
 */
public class InputTextBodyCommand implements Command<EmailContext> {

    @Override
    public boolean execute(EmailContext context) {
        MimeBodyPart textPart = new MimeBodyPart();
        IOUtils.message("Message (ctrl+D to end message):\n");
        try {
            textPart.setText(IOUtils.readMultipleLineInput());
        } catch (MessagingException e) {
            throw new EmailException(e);
        }
        context.addBodyParts(textPart);
        return true;
    }

}
