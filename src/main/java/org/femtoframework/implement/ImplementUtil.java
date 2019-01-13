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


import java.util.*;


/**
 * Implementation Utility
 *
 * It is NOT a replacement for any IoC container.
 * It is a "Separating interface and implementation" helper class for some libraries or utilities. Libraries has less dependencies,
 * they should not rely on any system. Since it has very less dependencies.
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
 *
 * ImplementUtil provides couple "Separating interface and implementation" design pattern support.
 * 1. Singleton or Pan-Singleton, One interface and one implementation or replacement
 *    In this case, one interface usually has one implementation, but it could be replaced by new implementation
 * 2. One interface, but it has multiple implementations in general.
 *    Each implementation can be replaced either. In this case, it uses interface + name to address one type of implementation
 *    For example:
 * <code>
 *     public interface Foo {
 *         void someMethod();
 *     }
 *
 *     #implements.properties
 *     Foo=foo1:FooImpl1,foo2:FooImpl2
 *
 *     If there is same Foo with foo1 in different properties, the last implementation will be used.
 * </code>
 *     You can replace  Foo + "foo1" with new implementation  "NewFooImpl1" by put new configuration
 *
 */
public class ImplementUtil {

    private static ImplementManager manager = new SimpleImplementManager();

    public static void setImplementManager(ImplementManager manager) {
        ImplementUtil.manager = manager;
    }

    public static ImplementManager getImplementManager() {
        return manager;
    }

    protected ImplementUtil() {
    }

    /**
     * Get a singleton of by interfaceClass
     * The singleton will be cached and @PostContruct method got invoked.
     *
     * @param interfaceClass Interface Class
     * @param <T> expected type
     * @return Created instance or instanced cached in this Util
     */
    public static <T> T getInstance(Class<T> interfaceClass) {
        return manager.getInstance(interfaceClass);
    }

    /**
     * Get a singleton of by interfaceClass
     * The singleton will be cached and @PostContruct method got invoked.
     *
     * @param interfaceClass Interface Class
     * @param <T> expected type
     * @return Created instance or instanced cached in this Util
     */
    public static <T> T getInstance(Class<T> interfaceClass, ClassLoader loader) {
        return manager.getInstance(interfaceClass, loader);
    }

    /**
     * Get an instance of by interfaceClass
     *
     * @param interfaceClass Interface Class
     * @param singleton Whether it is singleton or not, if it is true, it will be cached and @PostContruct method got invoked.
     * @param <T> expected type
     * @return Created instance or instanced cached in this Util
     */
    public static <T> T getInstance(Class<T> interfaceClass, boolean singleton) {
        return manager.getInstance(interfaceClass, singleton);
    }

    /**
     * Get an instance of by interfaceClass
     *
     * @param interfaceClass Interface Class
     * @param singleton Whether it is singleton or not, if it is true, it will be cached and @PostContruct method got invoked.
     * @param <T> expected type
     * @return Created instance or instanced cached in this Util
     */
    public static <T> T getInstance(Class<T> interfaceClass, boolean singleton, ClassLoader loader) {
        return manager.getInstance(interfaceClass, singleton, loader);
    }

    /**
     * Get an implement class by interfaceClass
     *
     * @param interfaceClass Interface Class
     * @param <T> expected Type
     * @return Class
     */
    public static <T> Class<?> getImplement(Class<T> interfaceClass) {
        return manager.getImplement(interfaceClass);
    }


    /**
     * Get an implement class by interfaceClass
     *
     * @param interfaceClass Interface Class
     * @param loader Class Loader
     * @param <T> expected Type
     * @return Class
     */
    public static <T> Class<?> getImplement(Class<T> interfaceClass, ClassLoader loader) {
        return manager.getImplement(interfaceClass, loader);
    }

    /**
     * Get a singleton of the name+interfaceClass pair
     *
     * @param name Name
     * @param interfaceClass Interface Class
     * @param <T> expected type
     * @return Created instance or instanced cached in this Util
     */
    public static <T> T getInstance(String name, Class<T> interfaceClass) {
        return manager.getInstance(name, interfaceClass);
    }


    /**
     * Get a singleton of the name+interfaceClass pair
     *
     * @param name Name
     * @param interfaceClass Interface Class
     * @param <T> expected type
     * @return Created instance or instanced cached in this Util
     */
    public static <T> T getInstance(String name, Class<T> interfaceClass, ClassLoader loader) {
        return manager.getInstance(name, interfaceClass, loader);
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
    public static <T> T getInstance(String name, Class<T> interfaceClass, boolean singleton) {
        return manager.getInstance(name, interfaceClass, singleton);
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
    public static <T> T getInstance(String name, Class<T> interfaceClass, boolean singleton, ClassLoader loader) {
        return manager.getInstance(name, interfaceClass, singleton, loader);
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
    public static <T> Class<?> getImplement(String name, Class<T> interfaceClass) {
        return manager.getImplement(name, interfaceClass);
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
    public static <T> Class<?> getImplement(String name, Class<T> interfaceClass, ClassLoader loader) {
        return manager.getImplement(name, interfaceClass);
    }


    /**
     * Returns an iterator of all the declared implementations
     *
     * @param interfaceClass The service's abstract service class
     * @return Iterator
     * @throws IllegalArgumentException If a provider-configuration file violates the specified format
     *                                  or names a provider class that cannot be found and instantiated
     */
    public static <T> ImplementConfig<T> getImplementConfig(Class<T> interfaceClass) {
        return manager.getImplementConfig(interfaceClass);
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
    public static <T> ImplementConfig<T> getImplementConfig(Class<T> interfaceClass, ClassLoader loader) {
        return manager.getImplementConfig(interfaceClass, loader);
    }


    /**
     * Run the @PostContruct for given instance.
     * Singleton's @PostContruct method will be executed automatically.
     * But non-singleton's method won't be executed.
     *
     * Check which method has <code>@PostConstruct</code>, and invoke it
     *
     * @param implementInstance
     */
    public static void initialize(Object implementInstance) {
        manager.initialize(implementInstance);
    }

    /**
     * Apply multiple instances
     *
     * @param interfaceClass Interface class
     * @param function How to handle function
     * @param <T> Instance Type
     */
    public static <T> void applyInstances(Class<T> interfaceClass, InstancesFunction<String, T> function) {
        manager.applyInstances(interfaceClass, function);
    }


    /**
     * Apply multiple instances
     *
     * @param interfaceClass Interface class
     * @param function How to handle function
     * @param <T> Instance Type
     */
    public static <T> void applyInstances(Class<T> interfaceClass, InstanceFunction<T> function) {
        manager.applyInstances(interfaceClass, function);
    }

    /**
     * Apply one instance
     *
     * @param interfaceClass Interface class
     * @param function How to handle function
     * @param <T> Instance Type
     */
    public <T> void applyInstance(Class<T> interfaceClass, InstanceFunction<T> function) {
        manager.applyInstance(interfaceClass, function);
    }
}
