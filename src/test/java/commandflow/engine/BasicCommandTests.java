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

import junit.framework.Assert;

import org.testng.annotations.Test;

import commandflow.Command;

/**
 * Tests of the basic commands.
 * @author elansma
 */
public class BasicCommandTests {
    @Test(groups = { "engine", "core-commands" })
    public void and() {
        Command<Object> c;
        //
        c = new AndCommand<Object>();
        Assert.assertEquals(false, c.execute(null));
        //
        c = new AndCommand<Object>().add(TrueCommand.getInstance()).add(TrueCommand.getInstance());
        Assert.assertEquals(true, c.execute(null));
        //
        c = new AndCommand<Object>().add(TrueCommand.getInstance()).add(FalseCommand.getInstance());
        Assert.assertEquals(false, c.execute(null));
    }

    @Test(groups = { "engine", "core-commands" })
    public void doWhile() {
        InvocationCountingCommand<Object> counter;
        int times;
        int[] ranges = { 0, 1, 10, 1000 };
        for (int i : ranges) {
            times = i;
            new DoWhileCommand<Object>(new LoopConditionCommand<Object>().answerFalseAfter(times), counter = new InvocationCountingCommand<Object>())
                    .execute(null);
            Assert.assertEquals(times + 1, counter.getCount());
        }
    }
}
