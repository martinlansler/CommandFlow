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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.regex.Pattern;

/**
 * Simple IO utilities.
 * @author Martin Lansler
 */
public class IOUtils {
    public static Class<IOUtils> message(String format, Object... args) {
        System.out.format(format, args);
        return IOUtils.class;
    }

    public static Class<IOUtils> error(String format, Object... args) {
        System.err.format(format, args);
        return IOUtils.class;
    }

    public static String readInput() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            return reader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String readMultipleLineInput() {
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = readInput()) != null) {
            sb.append(line);
            sb.append("\r\n");
        }
        return sb.toString();
    }

    public static String readValidatedInput(String prompt, Pattern validationPattern, String inputExample, String defaultValue) {
        String answer;
        showPrompt(prompt, defaultValue);
        while (true) {
            answer = readInput();
            if (validationPattern.matcher(answer).matches()) {
                break;
            }
            error("\nInvalid input, example input '%s'\n", inputExample);
            showPrompt(prompt, defaultValue);
        }
        return answer;
    }

    private static void showPrompt(String prompt, String defaultValue) {
        message("%s%s ", prompt, defaultValue != null ? String.format(" [%s]", defaultValue) : "");
    }

    public static String join(Object[] array, String glue) {
        return join(Arrays.asList(array), glue);
    }

    public static <T> String join(Collection<T> collection, String glue) {
        StringBuilder b = new StringBuilder();
        for (Iterator<T> iter = collection.iterator(); iter.hasNext();) {
            T t = iter.next();
            b.append(t);
            if (iter.hasNext()) {
                b.append(glue);
            }
        }
        return b.toString();
    }

}
