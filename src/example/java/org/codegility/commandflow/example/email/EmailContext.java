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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.mail.Message;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;

/**
 * Context class to send an email
 * @author elansma
 */
public class EmailContext {
    private Properties mailProperties;

    private String accountType;

    private String subject;

    private String from;

    private String text;

    private Set<Recipient> recipients = new HashSet<Recipient>();

    private Map<Message.RecipientType, Set<Recipient>> type2Recipient = new HashMap<Message.RecipientType, Set<Recipient>>();

    private List<MimeBodyPart> bodyParts = new ArrayList<MimeBodyPart>();

    public Properties getMailProperties() {
        return mailProperties;
    }

    public void setMailProperties(Properties mailProperties) {
        this.mailProperties = mailProperties;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Set<Recipient> getRecipients() {
        return recipients;
    }

    public void addRecipient(Recipient recipient) {
        recipients.add(recipient);
        Set<Recipient> recipientPerType = type2Recipient.get(recipient.getType());
        if (recipientPerType == null) {
            recipientPerType = new HashSet<Recipient>();
            type2Recipient.put(recipient.getType(), recipientPerType);
        }
        recipientPerType.add(recipient);
    }

    public Set<Recipient> getRecipients(Message.RecipientType type) {
        return type2Recipient.get(type);
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getFrom() {
        return from;
    }

    public void setTo(String to) {
        try {
            addRecipient(new Recipient(Message.RecipientType.TO, new InternetAddress(to)));
        } catch (AddressException e) {
            throw new RuntimeException(e);
        }
    }

    public List<MimeBodyPart> getBodyParts() {
        return bodyParts;
    }

    public void addBodyParts(MimeBodyPart bodyPart) {
        bodyParts.add(bodyPart);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }
}
