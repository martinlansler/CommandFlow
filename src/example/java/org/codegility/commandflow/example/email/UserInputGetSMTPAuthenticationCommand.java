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

import static org.codegility.commandflow.example.email.IOUtils.readValidatedInput;

import org.codegility.commandflow.Command;

/**
 * Gets the SMTP authentication settings by prompting on stdin.
 * @author Martin Lansler
 */
public class UserInputGetSMTPAuthenticationCommand implements Command<EmailContext> {

    @Override
    public boolean execute(EmailContext context) {
        if (!hasSetting(context, "mail.smtp.user")) {
            String defaultValue = context.getFrom() != null ? MailUtil.getAddress(context.getFrom()) : null;
            String prompt = String.format("Enter username for %s account", context.getAccountType());
            addSetting(context, "mail.smtp.user", readValidatedInput(prompt, MailUtil.RFC5322_CHARS_PATTERN, "name@emailprovider.com", defaultValue));
        }
        if (!hasSetting(context, "mail.smtp.password")) {
            String prompt = String.format("Enter password for %s account", context.getAccountType());
            addSetting(context, "mail.smtp.user", readValidatedInput(prompt, MailUtil.RFC5322_CHARS_PATTERN, "name@emailprovider.com", null));
        }
        return true;
    }

    private void addSetting(EmailContext context, String key, Object value) {
        context.getMailProperties().put(key, value);
    }

    private boolean hasSetting(EmailContext context, String mailSetting) {
        return context.getMailProperties().contains(mailSetting);
    }
}
