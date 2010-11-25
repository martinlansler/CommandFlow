/**
 * Copyright 2010 Martin Lansler (elansma)
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
package se.ekamartin.commandflow.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Command annotation.
 * <p>
 * Using the annotation any method may be marked as being a command, the method must either be a no-arg or take a single argument of the context type. The return type of the method may either be void or boolean, if using a void return type the command is assumed to always return <code>false</code>.
 * Optionally a command name may be provided.
 * @author elansma
 */
@Documented
@Target(ElementType.METHOD) 
@Retention(RetentionPolicy.RUNTIME)
public @interface Command {
	String name() default "";
}
