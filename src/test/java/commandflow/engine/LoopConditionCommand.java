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
package commandflow.engine;

import commandflow.Command;

/**
 * Test util to test loop condition commands
 * @author elansma
 */
public class LoopConditionCommand<C> implements Command<C> {
    private boolean result;
    private int times;

    @Override
    public boolean execute(C context) {
        return times-- == 0 ? result : !result;
    }

    LoopConditionCommand<C> answerTrueAfter(int times) {
        result = true;
        this.times = times;
        return this;
    }

    LoopConditionCommand<C> answerFalseAfter(int times) {
        result = false;
        this.times = times;
        return this;
    }

}
