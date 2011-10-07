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
package org.codegility.commandflow.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Class related utilities.
 * @author Martin Lansler
 */
public class ClassUtil {
    private ClassUtil() {
    }

    private static final Map<Class<?>, Class<?>> PRIMITIVE_2_WRAPPER = new HashMap<Class<?>, Class<?>>();
    static {
        PRIMITIVE_2_WRAPPER.put(void.class, Void.class);
        PRIMITIVE_2_WRAPPER.put(boolean.class, Boolean.class);
        PRIMITIVE_2_WRAPPER.put(byte.class, Byte.class);
        PRIMITIVE_2_WRAPPER.put(char.class, Character.class);
        PRIMITIVE_2_WRAPPER.put(short.class, Short.class);
        PRIMITIVE_2_WRAPPER.put(int.class, Integer.class);
        PRIMITIVE_2_WRAPPER.put(float.class, Float.class);
        PRIMITIVE_2_WRAPPER.put(double.class, Double.class);
        PRIMITIVE_2_WRAPPER.put(long.class, Long.class);
    }

    /**
     * Gets the boxed class for the given primitive type, for example {@link Integer} for <code>int.class</code>.
     * @param clazz
     * @return the boxed class, or the specified class if it is not primitive
     */
    public static Class<?> boxPrimitiveType(Class<?> clazz) {
        if (!clazz.isPrimitive()) {
            return clazz;
        }
        Class<?> boxedClass = PRIMITIVE_2_WRAPPER.get(clazz);
        if (boxedClass == null) {
            throw new RuntimeException("Unsupported primitive class: " + clazz);
        }
        return boxedClass;
    }
}
