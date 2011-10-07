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
package org.codegility.commandflow.example.email.util;

import java.util.regex.Pattern;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

/**
 * Email related util & constants.
 * @author Martin Lansler
 */
public class MailUtil {
    public final static String RFC5322_CHARS_REGEXP = "[a-zA-Z0-9!#$&'*+-/=?_`{|}~]";
    public final static Pattern RFC5322_CHARS_PATTERN;
    static {
        RFC5322_CHARS_PATTERN = Pattern.compile(RFC5322_CHARS_REGEXP);
    }

    public final static String RAW_ADDRESS_REGEXP;
    static {
        RAW_ADDRESS_REGEXP = String.format("%s+@%s+\\.%s+", RFC5322_CHARS_REGEXP, RFC5322_CHARS_REGEXP, RFC5322_CHARS_REGEXP);
    }

    public final static String RFC822_ADDRESS_REGEXP;
    static {
        RFC822_ADDRESS_REGEXP = String.format("((.*)\\s*<%s>)|%s", RAW_ADDRESS_REGEXP, RAW_ADDRESS_REGEXP);
    }

    public final static Pattern RFC822_ADDRESS_PATTERN;
    static {
        RFC822_ADDRESS_PATTERN = Pattern.compile(RFC822_ADDRESS_REGEXP);
    }

    public final static String EMAIL_SUBJECT_REGEXP;
    static {
        EMAIL_SUBJECT_REGEXP = String.format("%s*", RFC5322_CHARS_REGEXP);
    }

    public final static Pattern EMAIL_SUBJECT_PATTERN;
    static {
        EMAIL_SUBJECT_PATTERN = Pattern.compile(EMAIL_SUBJECT_REGEXP);
    }

    public final static String ALLOW_ALL_REGEXP;
    static {
        ALLOW_ALL_REGEXP = ".*";
    }

    public final static Pattern ALLOW_ALL_PATTERN;
    static {
        ALLOW_ALL_PATTERN = Pattern.compile(ALLOW_ALL_REGEXP);
    }

    public static String getAddress(String rfc822Address) {
        try {
            return new InternetAddress(rfc822Address).getAddress();
        } catch (AddressException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getPersonal(String rfc822Address) {
        try {
            return new InternetAddress(rfc822Address).getPersonal();
        } catch (AddressException e) {
            throw new RuntimeException(e);
        }
    }

}
