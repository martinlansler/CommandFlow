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

import java.util.regex.Pattern;

import org.codegility.commandflow.Command;

/**
 * Base command for querying user for input on stdin.
 * @author Martin Lansler
 */
public abstract class AbstractUserInputCommand<C> implements Command<C> {
    private String prompt;
    private Pattern validationPattern;
    private String inputExample;

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public Pattern getValidationPattern() {
        return validationPattern;
    }

    public void setValidationPattern(Pattern validationPattern) {
        this.validationPattern = validationPattern;
    }

    public String getInputExample() {
        return inputExample;
    }

    public void setInputExample(String inputExample) {
        this.inputExample = inputExample;
    }

    protected String getDefaultValue() {
        return null;
    }

    protected String getInput() {
        return IOUtils.readValidatedInput(getInput(), getValidationPattern(), getInputExample(), getDefaultValue());
    }
}
