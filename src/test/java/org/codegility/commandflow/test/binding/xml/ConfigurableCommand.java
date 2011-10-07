/**
 * Copyright 20Property0 Martin Lansler (elansma), Anders Jacobsson
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
package org.codegility.commandflow.test.binding.xml;

import org.codegility.commandflow.Command;

public class ConfigurableCommand implements Command<TestContext> {
    private Boolean booleanProperty;
    private Byte byteProperty;
    private Float floatProperty;
    private Integer integer;
    private Long longProperty;
    private Short shortProperty;
    private String string;

    @Override
    public boolean execute(TestContext context) {
        context.setExecuted(true);
        context.setExecutedCommand(this);
        return true;
    }

    public Boolean getBooleanProperty() {
        return booleanProperty;
    }

    public void setBooleanProperty(Boolean booleanProperty) {
        this.booleanProperty = booleanProperty;
    }

    public Byte getByteProperty() {
        return byteProperty;
    }

    public void setByteProperty(Byte byteProperty) {
        this.byteProperty = byteProperty;
    }

    public Float getFloatProperty() {
        return floatProperty;
    }

    public void setFloatProperty(Float floatProperty) {
        this.floatProperty = floatProperty;
    }

    public Integer getIntegerProperty() {
        return integer;
    }

    public void setIntegerProperty(Integer integer) {
        this.integer = integer;
    }

    public Long getLongProperty() {
        return longProperty;
    }

    public void setLongProperty(Long longProperty) {
        this.longProperty = longProperty;
    }

    public Short getShortProperty() {
        return shortProperty;
    }

    public void setShortProperty(Short shortProperty) {
        this.shortProperty = shortProperty;
    }

    public String getStringProperty() {
        return string;
    }

    public void setStringProperty(String string) {
        this.string = string;
    }

}