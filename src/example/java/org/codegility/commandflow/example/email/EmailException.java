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

import javax.mail.MessagingException;

/**
 * Domain specific exception for email sending commands.
 * @author Martin Lansler
 */
public class EmailException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public EmailException(String format, Object args) {
        super(String.format(format, args));
    }

    public EmailException(Throwable e, String errMesg) {
        super(errMesg, e);
    }

    public EmailException(MessagingException e) {
        super(e);
    }
}
