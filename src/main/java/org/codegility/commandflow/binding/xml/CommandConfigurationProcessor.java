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
package org.codegility.commandflow.binding.xml;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import javax.xml.namespace.QName;

import org.codegility.commandflow.Command;
import org.codegility.commandflow.binding.BindingException;
import org.codegility.commandflow.util.ClassUtil;

/**
 * Command processor that can be used to configure a created command.
 * <p>
 * It assumes that the command to be configured is {@link XmlBindingHandler#peekCommand()}.
 * <p>
 * The property (setter) to be configured is specified via an attribute. The value is either specified as an attribute or taken as the element value.
 * @author Martin Lansler
 */
public class CommandConfigurationProcessor<C> implements XmlElementProcessor<C> {
    private String propertyNameAttribute;

    private String propertyValueAttribute;

    private Map<Class<?>, Map<String, Method>> settersCache;

    /**
     * Creates a new command configuration processor
     * @param propertyNameAttribute the name of the attribute holding property to configure
     * @param propertyValueAttribute the name of the attribute holding the value of the property
     */
    public CommandConfigurationProcessor(String propertyNameAttribute, String propertyValueAttribute) {
        this.propertyNameAttribute = propertyNameAttribute;
        this.propertyValueAttribute = propertyValueAttribute;
    }

    @Override
    public void startProcessing() {
        settersCache = new HashMap<Class<?>, Map<String, Method>>();
    }

    @Override
    public void startElement(XmlBindingHandler<C> handler, QName elementName, Map<String, String> attributes) {
        String propertyName = attributes.get(propertyNameAttribute);
        String propertyValue = attributes.get(propertyValueAttribute);
        Command<C> bean = handler.peekCommand();
        Method method = getSetter(propertyName, bean);
        try {
            method.invoke(bean, coerceToType(getSetterType(method), propertyValue));
        } catch (Exception e) {
            throw new BindingException(e, "Failed to configure property %s with value %s", propertyName, propertyValue);
        }
    }

    private Class<?> getSetterType(Method method) {
        Class<?>[] types = method.getParameterTypes();
        if (types.length != 1) {
            throw new BindingException("Illegal setter %s in class %s, must have exatcly one parameter", method.getName(), method.getDeclaringClass().getName());
        }
        return ClassUtil.boxPrimitiveType(types[0]);
    }

    /**
     * Coerces the given string value to the specified class type.
     * <p>
     * <p>
     * The supported type coercions (in order) are:
     * <ol>
     * <li>Types that have a public static method called valueOf which accepts a single argument of type String and whose return type is the same as the class on which the method is declared. The
     * java.lang primitive wrapper classes have such methods.</li>
     * <li>Types that have a public constructor which accepts a single argument of type String.</li>
     * <li>Regexp in for of compiled {@link Pattern}</li>
     * <li>Subclass extensions via {@link #coerceToTypeExtension(Class, String)}</li>
     * </ol>
     * Subclasses may override this method to provide other coercion mechanism.
     * 
     * @param clazz the type the value should be coerced to
     * @param value the textual value
     * @return the coerced value
     * @throws BindingException if the value could not be type coerced
     */
    @SuppressWarnings("unchecked")
    protected <T> T coerceToType(Class<T> clazz, String value) {
        T t;
        t = coerceToTypeViaValueOf(clazz, value);
        if (t != null) {
            return t;
        }
        t = coerceToTypeViaConstructor(clazz, value);
        if (t != null) {
            return t;
        }
        if (Pattern.class.equals(clazz)) {
            return (T) Pattern.compile(value);
        }
        t = coerceToTypeExtension(clazz, value);
        if (t != null) {
            return t;
        }
        throw new BindingException("Could not coerce value %s to type %s", value, clazz);
    }

    /**
     * Allows subclasses to add additional type coercions as specified in {@link #coerceToType(Class, String)}.
     * <p>
     * By default this method returns <code>null</code>.
     * @param clazz the type the value should be coerced to
     * @param value the textual value
     * @return the coerced value, null if the value could not be coerced
     */
    protected <T> T coerceToTypeExtension(Class<T> clazz, String value) {
        return null;
    }

    protected <T> T coerceToTypeViaConstructor(Class<T> clazz, String value) {
        try {
            Constructor<T> constructor = clazz.getConstructor(String.class);
            return constructor.newInstance(value);
        } catch (NoSuchMethodException e) {
            // not found...
        } catch (Exception e) {
            throw new BindingException(e);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    protected <T> T coerceToTypeViaValueOf(Class<T> clazz, String value) {
        try {
            Method valueOf = clazz.getMethod("valueOf", String.class);
            if (Modifier.isStatic(valueOf.getModifiers()) && valueOf.getReturnType().equals(clazz)) {
                return (T) valueOf.invoke(null, value);
            }
        } catch (NoSuchMethodException e) {
            // not found...
        } catch (Exception e) {
            throw new BindingException(e);
        }
        return null;
    }

    @Override
    public void endElement(XmlBindingHandler<C> handler, QName elementName) {
        // not used
    }

    /**
     * Gets a named setter.
     * <p>
     * This method will use the internal cache first, if not found then all setters for the given class will be determined via introspection.
     * @param name the setter name without the "set", for example "name" (for "setName")
     * @param bean the bean instance to find the setter for
     * @return the setter
     * @throws BindingException if the setter could not be found
     */
    private Method getSetter(String name, Object bean) {
        Map<String, Method> setters = settersCache.get(bean.getClass());
        if (setters == null) {
            cacheSetters(bean.getClass());
            setters = settersCache.get(bean.getClass());
        }
        Method method = setters.get(name);
        if (method == null) {
            throw new BindingException("Could not find matching setter for property '%s' in class %s", name, bean.getClass());
        }
        return method;
    }

    /**
     * Loads all bean setters of the given class into the {@link #settersCache}.
     * @param clazz the class to load setters for
     */
    private void cacheSetters(Class<? extends Object> clazz) {
        try {
            Map<String, Method> setters = new HashMap<String, Method>();
            BeanInfo beanInfo = Introspector.getBeanInfo(clazz);
            for (PropertyDescriptor propertyDescriptor : beanInfo.getPropertyDescriptors()) {
                if (propertyDescriptor.getWriteMethod() != null) {
                    setters.put(propertyDescriptor.getName(), propertyDescriptor.getWriteMethod());
                }
            }
            settersCache.put(clazz, setters);
        } catch (IntrospectionException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void endProcessing() {
        settersCache = null;
    }

    @Override
    public CommandConfigurationProcessor<C> clone() {
        return new CommandConfigurationProcessor<C>(propertyNameAttribute, propertyValueAttribute);
    }

}
