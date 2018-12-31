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


import org.femtoframework.annotation.ImplementedBy;
import org.femtoframework.bean.Nameable;
import org.femtoframework.lang.reflect.NoSuchClassException;
import org.femtoframework.util.ClasspathProperties;
import org.femtoframework.util.CollectionUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


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
 *     #properties
 *     foo1=FooImpl1
 *     foo2=FooImpl2
 * </code>
 *     You can replace  Foo + "foo1" with new implementation  "NewFooImpl1" by put new configuration
 *
 */
public class ImplementUtil {

    private static final String PREFIX = "META-INF/spec/";

    private static final String IMPL_SUFFIX = ".impl";
    private static final String PROPERTIES_SUFFIX = ".properties";

    private static Map<Class<?>, Object> serviceCache = new ConcurrentHashMap<>(128);


    private static Map<Class<?>, Map<String, Object>> namedServiceCache = new ConcurrentHashMap<>(128);

    // InterfaceClass --> Properties
    private static Map<Class, Properties> implementProperties = new HashMap<>();


    private static ImplementManager manager = SimpleImplementManager.INSTANCE;

    public static void setImplementManager(ImplementManager manager) {
        ImplementUtil.manager = manager;
    }

    public static ImplementManager getImplementManager() {
        return manager;
    }

    protected ImplementUtil() {
    }

    private static void fail(Class interfaceClass, String msg, Throwable cause)
            throws IllegalArgumentException {
        throw new IllegalArgumentException(interfaceClass.getName() + ": " + msg, cause);
    }

    private static void fail(Class interfaceClass, String msg)
            throws IllegalArgumentException {
        throw new IllegalArgumentException(interfaceClass.getName() + ": " + msg);
    }

    private static void fail(Class interfaceClass, URL u, int line, String msg)
            throws IllegalArgumentException {
        fail(interfaceClass, u + ":" + line + ": " + msg);
    }

    /**
     * Parse a single line from the given configuration file, adding the name
     * on the line to both the names list and the returned set iff the name is
     * not already a member of the returned set.
     */
    private static int parseLine(Class interfaceClass, URL u, BufferedReader r, int lc,
                                 List<String> names, Set<String> returned)
            throws IOException, IllegalArgumentException {
        String ln = r.readLine();
        if (ln == null) {
            return -1;
        }
        int ci = ln.indexOf('#');
        if (ci >= 0) {
            ln = ln.substring(0, ci);
        }
        ln = ln.trim();
        int n = ln.length();
        if (n != 0) {
            if ((ln.indexOf(' ') >= 0) || (ln.indexOf('\t') >= 0)) {
                fail(interfaceClass, u, lc, "Illegal configuration-file syntax");
            }
            if (!Character.isJavaIdentifierStart(ln.charAt(0))) {
                fail(interfaceClass, u, lc, "Illegal provider-class name: " + ln);
            }
            for (int i = 1; i < n; i++) {
                char c = ln.charAt(i);
                if (!Character.isJavaIdentifierPart(c) && (c != '.')) {
                    fail(interfaceClass, u, lc, "Illegal provider-class name: " + ln);
                }
            }
            if (!returned.contains(ln)) {
                names.add(ln);
                returned.add(ln);
            }
        }
        return lc + 1;
    }

    /**
     * Parse the content of the given URL as a provider-configuration file.
     *
     * @param interfaceClass  The interface class for which providers are being sought;
     *                 used to construct error detail strings
     * @param u        The URL naming the configuration file to be parsed
     * @param returned A Set containing the names of provider classes that have already
     *                 been returned.  This set will be updated to contain the names
     *                 that will be yielded from the returned <tt>Iterator</tt>.
     * @return A (possibly empty) <tt>Iterator</tt> that will yield the
     *         provider-class names in the given configuration file that are
     *         not yet members of the returned set
     * @throws IllegalArgumentException If an I/O error occurs while reading from the given URL, or
     *                                  if a configuration-file format error is detected
     */
    private static Iterator parse(Class interfaceClass, URL u, Set<String> returned)
            throws IllegalArgumentException {
        List<String> names = new ArrayList<String>(5);
        try (InputStream in = u.openStream()){
            BufferedReader r = new BufferedReader(new InputStreamReader(in, "utf-8"));
            int lc = 1;
            while ((lc = parseLine(interfaceClass, u, r, lc, names, returned)) >= 0) {
            }
        }
        catch (IOException x) {
            fail(interfaceClass, ": " + x);
        }
        return names.iterator();
    }


    /**
     * Private inner class implementing fully-lazy provider lookup
     */
    private static final class LazyIterator<T> implements Iterator<Class<? extends T>> {
        Class<? extends T> interfaceClass;
        ClassLoader loader;
        Enumeration configs = null;
        Iterator pending = null;
        Set<String> returned = new TreeSet<String>();
        String nextName = null;

        private LazyIterator(Class<? extends T> interfaceClass, ClassLoader loader) {
            this.interfaceClass = interfaceClass;
            this.loader = loader;
        }

        public boolean hasNext() throws IllegalArgumentException {
            if (nextName != null) {
                return true;
            }
            if (configs == null) {
                try {
                    String fullName = PREFIX + interfaceClass.getName() + IMPL_SUFFIX;
                    configs = manager.getResources(fullName, loader);
                }
                catch (IOException x) {
                    fail(interfaceClass, ": " + x);
                }
            }
            while ((pending == null) || !pending.hasNext()) {
                if (!configs.hasMoreElements()) {
                    return false;
                }
                pending = parse(interfaceClass, (URL)configs.nextElement(), returned);
            }
            nextName = (String)pending.next();
            return true;
        }

        public Class<? extends T> next() throws IllegalArgumentException {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            String cn = nextName;
            nextName = null;
            return loadClass(cn, interfaceClass, loader);
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

    }

    private static <T> Class<? extends T> loadClass(String className, Class<? extends T> interfaceClass, ClassLoader loader) {
        try {
            return manager.loadClass(className, interfaceClass, loader);
        }
        catch (ClassNotFoundException x) {
            throw new NoSuchClassException("Implementation " + className + " for interface:" + interfaceClass + " is not found");
        }
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
    public static <T> T getInstance(Class<T> interfaceClass, ClassLoader loader) {
        Class<?> clazz = getImplement(interfaceClass, loader);
        return createInstance(clazz, true, interfaceClass);
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
    public static <T> T getInstance(Class<T> interfaceClass, boolean singleton, ClassLoader loader) {
        Class<?> clazz = getImplement(interfaceClass, loader);
        return createInstance(clazz, singleton, interfaceClass);
    }

    /**
     * Get an implement class by interfaceClass
     *
     * @param interfaceClass Interface Class
     * @param <T> expected Type
     * @return Class
     */
    public static <T> Class<?> getImplement(Class<T> interfaceClass) {
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
    public static <T> Class<?> getImplement(Class<T> interfaceClass, ClassLoader loader) {
        ImplementedBy implementedBy = interfaceClass.getAnnotation(ImplementedBy.class);
        if (implementedBy != null) {
            String className = implementedBy.value();
            try {
                return manager.loadClass(className, interfaceClass, loader);
            }
            catch (ClassNotFoundException x) {
                throw new NoSuchClassException("The class name of @ImplementedBy " + className + " in interface " +
                            interfaceClass.getName() + " is not found.", x);
            }
        }
        Iterator<Class<? extends T>> it = getImplements(interfaceClass, loader);
        Class<?> first = null;
        while (it.hasNext()) {
            Class<?> clazz = it.next();
            if (interfaceClass.isAssignableFrom(clazz)) {
                first = clazz;
                break;
            }
        }
        return first;
    }

    /**
     * Returns an iterator of all the declared implementations
     *
     * @param interfaceClass The service's abstract service class
     * @return Iterator
     * @throws IllegalArgumentException If a provider-configuration file violates the specified format
     *                                  or names a provider class that cannot be found and instantiated
     */
    public static <T> Iterator<Class<? extends T>> getImplements(Class<T> interfaceClass)
            throws IllegalArgumentException {
        return getImplements(interfaceClass, null);
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
    public static <T> Iterator<Class<? extends T>> getImplements(Class<T> interfaceClass, ClassLoader loader)
            throws IllegalArgumentException {
        return new LazyIterator<T>(interfaceClass, loader);
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
    public static <T> T getInstance(String name, Class<T> interfaceClass, ClassLoader loader) {
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
    public static <T> T getInstance(String name, Class<T> interfaceClass, boolean singleton) {
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
    public static <T> T getInstance(String name, Class<T> interfaceClass, boolean singleton, ClassLoader loader) {
        if (name == null) {
            throw new IllegalArgumentException("Null name");
        }
        if (interfaceClass == null) {
            throw new IllegalArgumentException("Can't get instance from Null Service class");
        }
        T instance;
        if (singleton) {
            synchronized (interfaceClass) {
                instance = (T) namedServiceCache.computeIfAbsent(interfaceClass, k -> new HashMap<>()).get(name);
                if (instance == null) {
                    Class<?> clazz = getImplement(name, interfaceClass, loader);
                    instance = createInstance0(clazz, singleton, interfaceClass);
                    setName(clazz, instance, name);
                    manager.initialize(instance);
                    namedServiceCache.get(interfaceClass).put(name, instance);
                }
            }
        }
        else {
            Class<?> clazz = getImplement(name, interfaceClass, loader);
            instance = createInstance0(clazz, singleton, interfaceClass);
            setName(clazz, instance, name);
        }
        return instance;
    }

    protected static void setName(Class<?> clazz, Object obj, String name) {
        if (obj instanceof Nameable) {
            ((Nameable)obj).setName(name);
        }
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
    private static <T> T createInstance(Class<?> clazz, boolean singleton, Class<T> interfaceClass) {
        if (clazz != null) {
            T obj = null;
            if (singleton) {
                obj = (T)serviceCache.get(interfaceClass);
                if (obj == null) {
                    synchronized (interfaceClass) {
                        obj = (T)serviceCache.get(interfaceClass);
                        if (obj == null) {
                            obj = createInstance0(clazz, singleton, interfaceClass);
                            serviceCache.put(interfaceClass, obj);
                        }
                    }
                }
                return obj;
            }
            else {
                return createInstance0(clazz, singleton, interfaceClass);
            }
        }
        throw new IllegalStateException("No implement of the interface " + interfaceClass);
    }

    private static <T> T createInstance0(Class<?> clazz, boolean singleton, Class<T> interfaceClass) {
        if (clazz != null) {
            try {
                return manager.createInstance(clazz, singleton, interfaceClass);
            }
            catch (Exception e) {
                fail(interfaceClass,"Provider " + clazz.getName() + " could not be instantiated: " + e, e);
            }
        }
        throw new IllegalStateException("No implement of the interface " + interfaceClass);
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
    public static <T> Class<?> getImplement(String name, Class<T> interfaceClass, ClassLoader loader) {
        Properties props = getImplementProperties0(interfaceClass, loader, true);
        if (props != null) {
            String className = props.getProperty(name);
            if (className == null) {
                throw new IllegalStateException("No such implement of " + " interface:" + interfaceClass + " by name:" + name);
            }
            Class<?> clazz = loadClass(className, interfaceClass, loader);
            if (!interfaceClass.isAssignableFrom(clazz)) {
                throw new IllegalStateException("The " + clazz + " is not implements " + " interface:" + interfaceClass);
            }
            return clazz;
        }
        throw new IllegalStateException("No implement of the interface " + interfaceClass + " name:" + name);
    }

    /**
     * Load properties from class loader
     *
     * @param resourceName Resource Name
     * @param loader ClassLoader
     * @return Properties
     * @throws IOException
     */
    private static Properties loadProperties(String resourceName, ClassLoader loader) throws IOException {
        if (loader != null) {
            Thread thread = Thread.currentThread();
            ClassLoader oldLoader = thread.getContextClassLoader();
            thread.setContextClassLoader(loader);
            try {
                return new ClasspathProperties(resourceName);
            }
            finally {
                thread.setContextClassLoader(oldLoader);
            }
        }
        else {
            return new ClasspathProperties(resourceName);
        }
    }

    /**
     * Returns the properties which includes a set of implementations of this interface class.
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
     * @param interfaceClass Interface class name
     * @return The properties
     */
    public static Properties getImplementProperties(Class interfaceClass) {
        return getImplementProperties(interfaceClass, null);
    }

    /**
     * Returns the properties which includes a set of implementations of this interface class.
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
     * @param interfaceClass Interface class name
     * @param cache Cache them in this context or not
     * @return The properties
     */
    public static Properties getImplementProperties(Class interfaceClass, boolean cache) {
        return getImplementProperties(interfaceClass, null, cache);
    }

    /**
     * Returns the properties which includes a set of implementations of this interface class.
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
     * @param interfaceClass Interface class name
     * @param loader Class loader for the implementations
     * @param cache Cache them in this context or not
     * @return The properties
     */
    private static Properties getImplementProperties0(Class interfaceClass, ClassLoader loader, boolean cache) {
        if (cache) {
            Properties props = implementProperties.get(interfaceClass);
            if (props != null) {
                return props;
            }
            String resourceName = PREFIX + interfaceClass.getName() + PROPERTIES_SUFFIX;
            try {
                props = loadProperties(resourceName, loader);
            }
            catch (IOException e) {
            }
            if (props != null) {
                implementProperties.put(interfaceClass, props);
            }
            return props;
        }
        else {
            String resourceName = PREFIX + interfaceClass.getName() + PROPERTIES_SUFFIX;
            try {
                return loadProperties(resourceName, loader);
            }
            catch (IOException e) {
                return null;
            }
        }
    }

    /**
     * Returns the properties which includes a set of implementations of this interface class.
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
     * @param interfaceClass Interface class name
     * @param loader Class loader for the implementations
     * @return The properties
     */
    public static Properties getImplementProperties(Class interfaceClass, ClassLoader loader) {
        Properties props = getImplementProperties0(interfaceClass, loader, true);
        return props != null ? props : CollectionUtil.EMPTY_PROPERTIES;
    }

    /**
     * Returns the properties which includes a set of implementations of this interface class.
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
     * @param interfaceClass Interface class name
     * @param loader Class loader for the implementations
     * @param cache Cache them in this context or not
     * @return The properties
     */
    public static Properties getImplementProperties(Class interfaceClass, ClassLoader loader, boolean cache) {
        Properties props = getImplementProperties0(interfaceClass, loader, cache);
        return props != null ? props : CollectionUtil.EMPTY_PROPERTIES;
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
}
