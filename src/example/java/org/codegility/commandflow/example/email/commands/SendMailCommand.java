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

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.codegility.commandflow.Command;
import org.codegility.commandflow.example.email.EmailContext;
import org.codegility.commandflow.example.email.EmailException;
import org.codegility.commandflow.example.email.util.IOUtils;
import org.codegility.commandflow.example.email.util.Recipient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Command to send an email
 * @author Martin Lansler
 */
public class SendMailCommand implements Command<EmailContext> {
    Logger logger = LoggerFactory.getLogger(SendMailCommand.class);

    @Override
    public boolean execute(EmailContext context) {
        try {
            sendEmail(context);
            return true;
        } catch (Exception e) {
            String errMesg = String.format("Failed to send email '%s', to '%s'", context.getSubject());
            logger.error(errMesg, IOUtils.join(context.getRecipients(Message.RecipientType.TO), ","));
            throw new EmailException(e, errMesg);
        }
    }

    private void sendEmail(EmailContext context) throws MessagingException, AddressException {
        Session session = Session.getDefaultInstance(context.getMailProperties());
        MimeMessage msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(context.getFrom()));
        msg.setSubject(context.getSubject());
        for (Recipient recipient : context.getRecipients()) {
            msg.addRecipient(recipient.getType(), recipient.getAddress());
        }

        Multipart multipart = new MimeMultipart();
        if (context.getText() != null) {
            MimeBodyPart textPart = new MimeBodyPart();
            textPart.setText(context.getText());
            multipart.addBodyPart(textPart);
        }
        for (MimeBodyPart mimeBodyPart : context.getBodyParts()) {
            multipart.addBodyPart(mimeBodyPart);
        }
        msg.setContent(multipart);
        Transport.send(msg);
    }

}
