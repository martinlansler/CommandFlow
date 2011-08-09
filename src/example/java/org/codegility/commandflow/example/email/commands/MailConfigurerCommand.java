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

import java.util.Properties;
import java.util.regex.Pattern;

import org.codegility.commandflow.Command;
import org.codegility.commandflow.example.email.EmailContext;

/**
 * Command to configure needed java mail SMTP settings.
 * <p>
 * A regexp pattern is specified to match on "from" address, from this the email provider is determined (from example "google", "yahoo" etc).
 * @author Martin Lansler
 */
public class MailConfigurerCommand implements Command<EmailContext> {
    private String accountType;

    private Pattern fromAddressRegexp;

    private String host;
    private String user;
    private String password;
    private Integer port;
    private Boolean doAuth;
    private Boolean doStartTTLS;

    @Override
    public boolean execute(EmailContext context) {
        if (fromAddressRegexp.matcher(context.getFrom()).matches()) {
            configureMail(context);
            return true;
        }
        return false;
    }

    /**
     * @param context
     */
    public void configureMail(EmailContext context) {
        Properties mailProperties = context.getMailProperties();
        putIfSet(mailProperties, "mail.smtp.host", host);
        putIfSet(mailProperties, "mail.smtp.user", user);
        putIfSet(mailProperties, "mail.smtp.password", password);
        putIfSet(mailProperties, "mail.smtp.port", port);
        putIfSet(mailProperties, "mail.smtp.auth", doAuth);
        putIfSet(mailProperties, "mail.smtp.starttls.enable", doStartTTLS);

        context.setAccountType(accountType);
    }

    private void putIfSet(Properties properties, String key, Object value) {
        if (value == null) {
            return;
        }
        properties.put(key, String.valueOf(value));
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setDoAuth(boolean doAuth) {
        this.doAuth = doAuth;
    }

    public void setDoStartTTLS(boolean doStartTTLS) {
        this.doStartTTLS = doStartTTLS;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public void setFromAddressRegexp(Pattern fromAddressRegexp) {
        this.fromAddressRegexp = fromAddressRegexp;
    }

}
