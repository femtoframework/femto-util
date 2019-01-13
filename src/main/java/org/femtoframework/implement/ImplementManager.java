/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.femtoframework.implement;

import org.femtoframework.lang.reflect.NoSuchClassException;

import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * Manage the implementation
 *
 * Separating interface and implementation is a very important design pattern in Java.
 *
 * Here are couple reasons from the internet https://softwareengineering.stackexchange.com/questions/142192/why-would-a-programmer-want-to-separate-implementation-from-interface
 * It allows you to change the implementation independently of the interface. This helps deal with changing requirements.
 *
 * The classic example is replacing the storage implementation under an interface with something bigger, better, faster, smaller, or otherwise different without having to change the rest of the system.
 *
 * You can use spring, spring-boot, guice or standard JSR330 annotations.
 * But all the above ways need to introduce dependencies.
 *
 * This ImplementManager provides a very simple way like Java XML ParserFactory.
 * It uses an text file to separate the interface and implementations
 */
public interface ImplementManager {

    /**
     * Loading an implementation
     *
     * @param className Class name of implementation
     * @param interfaceClass An interface class
     * @param loader Class Loader, it maybe null
     * @param <T> Service Interface
     * @return Service implementation
     * @throws NoSuchClassException either className or interfaceClass is not found
     */
    <T> Class<? extends T> loadClass(String className, Class<? extends T> interfaceClass, ClassLoader loader);

    /**
     * Create instance
     *
     * @param clazz Implementation instance class
     * @param interfaceClass
     * @param <T>
     * @return
     */
    <T> T createInstance(Class<? extends T> clazz, Class<T> interfaceClass);

    /**
     * Get a singleton of by interfaceClass
     * The singleton will be cached and @PostContruct method got invoked.
     *
     * @param interfaceClass Interface Class
     * @param <T> expected type
     * @return Created instance or instanced cached in this Util
     */
    default  <T> T getInstance(Class<T> interfaceClass) {
        return getInstance(interfaceClass, true);
    }

    /**
     * Get a singleton of by interfaceClass
     * The singleton will be cached and @PostContruct method got invoked.
     *
     * @param interfaceClass Interface Class
     * @param <T> expected type
     * @return Created instance or instanced cached in this Util
     */
    default  <T> T getInstance(Class<T> interfaceClass, ClassLoader loader) {
        Class<? extends T> clazz = getImplement(interfaceClass, loader);
        return createInstance(clazz, true, interfaceClass);
    }

    /**
     * Create instance based on given implementationClass and interface
     *
     * @param clazz implementation class
     * @param singleton Whether is is singleton or not
     * @param interfaceClass Interface class
     * @param <T> Type
     * @return Created instance
     */
    <T> T createInstance(Class<? extends T> clazz, boolean singleton, Class<T> interfaceClass);

    /**
     * Get an instance of by interfaceClass
     *
     * @param interfaceClass Interface Class
     * @param singleton Whether it is singleton or not, if it is true, it will be cached and @PostContruct method got invoked.
     * @param <T> expected type
     * @return Created instance or instanced cached in this Util
     */
    default  <T> T getInstance(Class<T> interfaceClass, boolean singleton) {
        return getInstance(interfaceClass, singleton, null);
    }

    /**
     * Get an instance of by interfaceClass
     *
     * @param interfaceClass Interface Class
     * @param singleton Whether it is singleton or not, if it is true, it will be cached and @PostContruct method got invoked.
     * @param <T> expected type
     * @return Created instance or instanced cached in this Util
     */
    default  <T> T getInstance(Class<T> interfaceClass, boolean singleton, ClassLoader loader) {
        Class<? extends T> clazz = getImplement(interfaceClass, loader);
        return createInstance(clazz, singleton, interfaceClass);
    }

    /**
     * Get an implement class by interfaceClass
     *
     * @param interfaceClass Interface Class
     * @param <T> expected Type
     * @return Class
     */
    default <T> Class<? extends T> getImplement(Class<T> interfaceClass) {
        return getImplement(interfaceClass, null);
    }


    /**
     * Get an implement class by interfaceClass
     *
     * @param interfaceClass Interface Class
     * @param loader Class Loader
     * @param <T> expected Type
     * @return Class
     */
    <T> Class<? extends T> getImplement(Class<T> interfaceClass, ClassLoader loader);

    /**
     * Returns an iterator of all the declared implementations
     *
     * @param interfaceClass The service's abstract service class
     * @return Iterator
     * @throws IllegalArgumentException If a provider-configuration file violates the specified format
     *                                  or names a provider class that cannot be found and instantiated
     */
    default <T> ImplementConfig<T> getImplementConfig(Class<T> interfaceClass) throws IllegalArgumentException {
        return getImplementConfig(interfaceClass, null);
    }

    /**
     * Returns an iterator of all the declared implementations
     *
     * @param interfaceClass The service's abstract service class
     * @param loader  Class Loader
     * @return Iterator
     * @throws IllegalArgumentException If a provider-configuration file violates the specified format
     *                                  or names a provider class that cannot be found and instantiated
     */
    <T> ImplementConfig<T> getImplementConfig(Class<T> interfaceClass, ClassLoader loader) throws IllegalArgumentException;

    /**
     * Get a singleton of the name+interfaceClass pair
     *
     * @param name Name
     * @param interfaceClass Interface Class
     * @param <T> expected type
     * @return Created instance or instanced cached in this Util
     */
    default <T> T getInstance(String name, Class<T> interfaceClass) {
        return getInstance(name, interfaceClass, null);
    }


    /**
     * Get a singleton of the name+interfaceClass pair
     *
     * @param name Name
     * @param interfaceClass Interface Class
     * @param <T> expected type
     * @return Created instance or instanced cached in this Util
     */
    default <T> T getInstance(String name, Class<T> interfaceClass, ClassLoader loader) {
        return getInstance(name, interfaceClass, true, loader);
    }

    /**
     * Get an instance of the name+interfaceClass pair
     *
     * @param name Name
     * @param interfaceClass Interface Class
     * @param singleton Whether it is singleton
     * @param <T> expected type
     * @return Created instance or instanced cached in this Util
     */
    default <T> T getInstance(String name, Class<T> interfaceClass, boolean singleton) {
        return getInstance(name, interfaceClass, singleton, null);
    }

    /**
     * Get an instance of the name+interfaceClass pair
     *
     * @param name Name
     * @param interfaceClass Interface Class
     * @param singleton Whether it is singleton
     * @param loader ClassLoader
     * @param <T> expected type
     * @return Created instance or instanced cached in this Util
     */
    <T> T getInstance(String name, Class<T> interfaceClass, boolean singleton, ClassLoader loader);

    /**
     * Returns the right implementations of this interface class and name.
     * For examples,
     * <code>
     *     public interface Foo {
     *         void someMethod();
     *     }
     *
     *     #properties
     *     foo1=FooImpl1
     *     foo2=FooImpl2
     * </code>
     * @param name implementation Name
     * @param interfaceClass Interface class name
     * @return The right implementClass
     */
    default <T> Class<? extends T> getImplement(String name, Class<T> interfaceClass) {
        return getImplement(name, interfaceClass, null);
    }

    /**
     * Returns the right implementations of this interface class and name.
     * For examples,
     * <code>
     *     public interface Foo {
     *         void someMethod();
     *     }
     *
     *     #properties
     *     foo1=FooImpl1
     *     foo2=FooImpl2
     * </code>
     * @param name implementation Name
     * @param interfaceClass Interface class name
     * @return The right implementClass
     */
    <T> Class<? extends T> getImplement(String name, Class<T> interfaceClass, ClassLoader loader);

    /**
     * Post Construct
     *
     * Check which method has <code>@PostConstruct</code>, and invoke it
     *
     * @param implementInstance
     */
    void initialize(Object implementInstance);


    /**
     * Returns the properties which includes a set of implementations of this interface class.
     * For examples,
     * <code>
     *     public interface Foo {
     *         void someMethod();
     *     }
     *
     *     #implements.properties
     *     Foo=foo1:FooImpl1,foo2:FooImpl2
     * </code>
     * @param interfaceClass Interface class name
     * @return name to ImplementConfig mapping
     */
    default Map<String, ImplementConfig<?>> getMultipleImplements(Class<?> interfaceClass) {
        return getMultipleImplements(interfaceClass, null);
    }


    /**
     * Returns the properties which includes a set of implementations of this interface class.
     * For examples,
     * <code>
     *     public interface Foo {
     *         void someMethod();
     *     }
     *
     *     #implements.properties
     *     Foo=foo1:FooImpl1,foo2:FooImpl2
     * </code>
     * @param interfaceClass Interface class name
     * @param loader Class loader for the implementations
     * @return name to ImplementConfig mapping
     */
    Map<String, ImplementConfig<?>> getMultipleImplements(Class<?> interfaceClass, ClassLoader loader);


    /**
     * Apply multiple instances
     *
     * @param interfaceClass Interface class
     * @param function How to handle function
     * @param <T> Instance Type
     */
    <T> void applyInstances(Class<T> interfaceClass, InstancesFunction<String, T> function);


    /**
     * Apply multiple instances
     *
     * @param interfaceClass Interface class
     * @param function How to handle function
     * @param <T> Instance Type
     */
    <T> void applyInstances(Class<T> interfaceClass, InstanceFunction<T> function);

    /**
     * Apply one instance
     *
     * @param interfaceClass Interface class
     * @param function How to handle function
     * @param <T> Instance Type
     */
    <T> void applyInstance(Class<T> interfaceClass, InstanceFunction<T> function);
}
