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

import org.codegility.commandflow.Command;
import org.codegility.commandflow.example.email.EmailContext;

/**
 * Shows a message on stdout
 * @author Martin Lansler
 */
public class OutputMessageCommand implements Command<EmailContext> {
    private String message;

    @Override
    public boolean execute(EmailContext context) {
        System.out.println(message);
        return true;
    }

    protected void setMessage(String message) {
        this.message = message;
    }

}
