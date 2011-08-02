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
package org.codegility.commandflow.test.command;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

import org.codegility.commandflow.Command;
import org.codegility.commandflow.builder.BuilderException;
import org.codegility.commandflow.builder.CommandInitialization;
import org.codegility.commandflow.command.DoWhileCommand;
import org.codegility.commandflow.command.FalseCommand;
import org.codegility.commandflow.command.IfCommand;
import org.codegility.commandflow.command.NotCommand;
import org.codegility.commandflow.command.OrCommand;
import org.codegility.commandflow.command.ScriptCommand;
import org.codegility.commandflow.command.SequenceCommand;
import org.codegility.commandflow.command.TrueCommand;
import org.codegility.commandflow.command.WhileCommand;
import org.junit.Test;


/**
 * Tests of the basic commands.
 * 
 * @author Martin Lansler
 */
public class BasicCommandTests {
	private boolean ex(Command<?> command) {
		return command.execute(null);
	}

	private Command<?> init(Command<?> command) {
		if (command instanceof CommandInitialization) {
			try {
				((CommandInitialization) command).init();
			} catch (BuilderException e) {
				throw new RuntimeException(e);
			}
		}
		return command;
	}

	private <T> CounterCommand<T> newCounter() {
		return new CounterCommand<T>();
	}

	@Test
	public void andCommand() {
		Command<Object> c;
		//
		c = new SequenceCommand<Object>();
		assertFalse(ex(c));
		//
		c = new SequenceCommand<Object>().add(TrueCommand.getInstance()).add(
				TrueCommand.getInstance());
		assertTrue(ex(c));
		//
		c = new SequenceCommand<Object>().add(TrueCommand.getInstance()).add(
				FalseCommand.getInstance());
		assertFalse(ex(c));
	}

	@Test
	public void doWhileCommand() {
		CounterCommand<Object> counter = null;
		int times;
		int[] ranges = { 0, 1, 10, 1000 };
		for (int i : ranges) {
			times = i;
			assertFalse(ex(init((new DoWhileCommand<Object>()
					.add(new ConditionCommand<Object>().answerFalseAfter(times))
					.add((counter = newCounter()).alwaysFalse())))));
			assertEquals(times + 1, counter.getCount());
		}
	}

	@Test
	public void falseCommand() {
		assertFalse(ex(new FalseCommand<Object>()));
	}

	@Test
	public void trueCommand() {
		assertTrue(ex(new TrueCommand<Object>()));
	}

	@Test
	public void ifCommand() {
		CounterCommand<Object> counter;
		ex(init(new IfCommand<Object>().add(FalseCommand.getInstance()).add(
				counter = newCounter())));
		assertEquals(0, counter.getCount());
		//
		ex(init(new IfCommand<Object>().add(TrueCommand.getInstance()).add(
				counter = newCounter())));
		assertEquals(1, counter.getCount());
	}

	@Test
	public void notCommand() {
		assertFalse(ex(init(new NotCommand<Object>().add(TrueCommand
				.getInstance()))));
		assertTrue(ex(init(new NotCommand<Object>().add(FalseCommand
				.getInstance()))));
	}

	@Test
	public void orCommand() {
		Command<Object> or;
		CounterCommand<Object> counter1, counter2;
		//
		or = new OrCommand<Object>();
		assertFalse(ex(or));
		//
		Boolean[][] cases = { { false, false }, { true, false },
				{ false, true }, { true, true } };
		Object[][] asserts = { { false, 1L, 1L }, { true, 1L, 0L },
				{ true, 1L, 1L }, { true, 1L, 0L } };
		for (int i = 0; i < cases.length; i++) {
			Boolean[] c = cases[i];
			Object[] a = asserts[i];
			int n = 0;
			//
			or = new OrCommand<Object>().add(
					(counter1 = newCounter()).always(c[n++])).add(
					(counter2 = newCounter()).always(c[n++]));
			n = 0;
			assertEquals(a[n++], (Boolean) ex(init(or)));
			assertEquals(a[n++], (Long) counter1.getCount());
			assertEquals(a[n++], (Long) counter2.getCount());
		}
	}

	@Test
	public void sequenceCommand() {
		Command<Object> seq;
		CounterCommand<Object> counter1, counter2;
		//
		seq = new SequenceCommand<Object>();
		assertFalse(ex(seq));
		//
		Boolean[][] cases = { { false, false }, { true, false },
				{ false, true }, { true, true } };
		Object[][] asserts = { { false, 1L, 1L }, { false, 1L, 1L },
				{ true, 1L, 1L }, { true, 1L, 1L } };
		for (int i = 0; i < cases.length; i++) {
			Boolean[] c = cases[i];
			Object[] a = asserts[i];
			int n = 0;
			//
			seq = new SequenceCommand<Object>().add(
					(counter1 = newCounter()).always(c[n++])).add(
					(counter2 = newCounter()).always(c[n++]));
			n = 0;
			assertEquals(a[n++], (Boolean) ex(init(seq)));
			assertEquals(a[n++], (Long) counter1.getCount());
			assertEquals(a[n++], (Long) counter2.getCount());
		}
	}

	@Test
	public void whileCommand() {
		CounterCommand<Object> counter = null;
		Object[][] cases = { { true, 0 }, { true, 1 }, { false, 10 } };
		boolean[] asserts = { false, true, false };
		for (int j = 0; j < asserts.length; j++) {
			boolean lastCmd = (Boolean) cases[j][0];
			int times = (Integer) cases[j][1];
			assertEquals(
					asserts[j],
					ex(init(new WhileCommand<Object>().add(
							new ConditionCommand<Object>()
									.answerFalseAfter(times)).add(
							(counter = newCounter()).always(lastCmd)))));
			assertEquals(times, counter.getCount());
		}
	}

	@Test
	public void testScriptCommand() {
		ScriptCommand<Object> script = new ScriptCommand<Object>("true");
		assertTrue(script.execute(null));
		//
		final String context = "test";
		ScriptCommand<String> script2 = new ScriptCommand<String>(
				String.format("c.length == %s", context.length()));
		assertTrue(script2.execute(context));
		assertFalse(script2.execute("not" + context));
	}
}
