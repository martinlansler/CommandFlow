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

import java.util.concurrent.atomic.AtomicLong;

import commandflow.Command;

/**
 * Test command to steer command status, thread safe.
 * @author elansma
 */
public class ConditionCommand<C> implements Command<C> {
    private boolean result;
    private AtomicLong times = new AtomicLong();

    @Override
    public boolean execute(C context) {
        try {
            return times.get() <= 0 ? result : !result;
        } finally {
            times.decrementAndGet();
        }
    }

    public ConditionCommand<C> answerTrueAfter(int times) {
        result = true;
        this.times = new AtomicLong(times);
        return this;
    }

    public ConditionCommand<C> alwaysTrue() {
        result = true;
        this.times = new AtomicLong();
        return this;
    }

    public ConditionCommand<C> answerFalseAfter(int times) {
        result = false;
        this.times = new AtomicLong(times);
        return this;
    }

    public ConditionCommand<C> alwaysFalse() {
        result = false;
        this.times = new AtomicLong();
        return this;
    }

    public ConditionCommand<C> always(boolean result) {
        this.result = result;
        this.times = new AtomicLong();
        return this;
    }

}
