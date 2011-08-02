/**
 * Copyright 2010/2011, Martin Lansler
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
package org.codegility.commandflow.test.bind.xml;

import org.codegility.commandflow.Command;

/**
 * 
 * @author Martin Lansler
 */
public class SetIsExecuted implements Command<TestContext> {

    /** {@inheritDoc} */
    @Override
    public boolean execute(TestContext context) {
        context.setExecuted(true);
        return true;
    }

}
