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
package org.codegility.commandflow.example.helloworld;

import java.util.HashMap;
import java.util.Map;

import org.codegility.commandflow.Command;

/**
 * Prints "Hello World!"
 * @author Martin Lansler
 */
public class HelloWorld implements Command<Map<String, String>> {

    @Override
    public boolean execute(Map<String, String> context) {
        System.out.println(context.get("message"));
        return true;
    }

    public static void main(String[] args) {
        Map<String, String> context = new HashMap<String, String>();
        context.put("message", "Hello World!");
        new HelloWorld().execute(context);
    }

}
