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

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Message.RecipientType;

/**
 * Holder for an address and recipient type.
 * <p>
 * Also implements equality and hash operation to allow holder to be used in collection classes.
 * 
 * @author Martin Lansler
 */
public class Recipient {
    private Address address;
    private Message.RecipientType type;

    public Recipient(RecipientType type, Address address) {
        this.type = type;
        this.address = address;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((address == null) ? 0 : address.getType().hashCode());
        result = prime * result + ((type == null) ? 0 : type.toString().hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Recipient other = (Recipient) obj;
        if (address == null) {
            if (other.address != null)
                return false;
        } else if (!address.getType().equals(other.address.getType()))
            return false;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.toString().equals(other.type.toString()))
            return false;
        return true;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Message.RecipientType getType() {
        return type;
    }

    public void setType(Message.RecipientType type) {
        this.type = type;
    }

}
