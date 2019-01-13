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
import org.femtoframework.bean.Initializable;
import org.femtoframework.bean.Nameable;
import org.femtoframework.lang.reflect.NoSuchClassException;
import org.femtoframework.lang.reflect.Reflection;
import org.femtoframework.util.DataUtil;
import org.femtoframework.util.MessageProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Simple ImplementManager implementation
 *
 * @author fengyun
 * @version 1.00 11-8-18 19:55
 */
public class SimpleImplementManager implements ImplementManager {


    protected static final String PROPERTIES = "META-INF/spec/implements.properties";


    private Map<Class<?>, Object> serviceCache = new ConcurrentHashMap<>(128);

    private Map<Class<?>, Map<String, Object>> namedServiceCache = new ConcurrentHashMap<>(128);

    /**
     * implementProperties
     */
    private List<Properties> propertiesList = new ArrayList<>();


    private List<String> urls = new ArrayList<>();

    private Map<Class<?>, Map<String, ImplementConfig<?>>> configs = new ConcurrentHashMap<>();


    private Logger log = LoggerFactory.getLogger(SimpleImplementManager.class);


    public SimpleImplementManager() {
        loadImplementsProperties();
    }

    protected void loadImplementsProperties() {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        if (loader == null) {
            loader = ClassLoader.getSystemClassLoader();
        }

        try {
            Enumeration<URL> enumeration = Reflection.getResources(loader, PROPERTIES);

            Charset utf8 = Charset.forName("utf8");
            while(enumeration.hasMoreElements()) {
                URL url = enumeration.nextElement();
                MessageProperties props = new MessageProperties();
                try (InputStream input = url.openStream()) {
                    props.load(input, utf8);
                }
                urls.add(url.toExternalForm());
                propertiesList.add(props);
            }
        }
        catch(IOException ioe) {
            log.error("Loading 'META-INF/implements.properties' error", ioe);
        }
    }

    /**
     * Loading an implementation
     *
     * @param className      Class name of implementation
     * @param interfaceClass An interface class
     * @param loader         Class Loader, it maybe null
     * @return Service implementation
     * @throws ClassNotFoundException either className or interfaceClass is not found
     */
    @Override
    public <T> Class<? extends T> loadClass(String className, Class<? extends T> interfaceClass, ClassLoader loader) {
        try {
            return Reflection.loadClass(loader, className);
        }
        catch (ClassNotFoundException x) {
            throw new NoSuchClassException("Implementation " + className + " for interface:" + interfaceClass + " is not found");
        }
    }


    /**
     * Create implementation
     *
     * @param clazz          Service implementation class
     * @param interfaceClass
     * @return
     */
    @Override
    public <T> T createInstance(Class<? extends T> clazz, Class<T> interfaceClass) {
        if (clazz != null) {
            try {
                return clazz.newInstance();
            }
            catch (Exception e) {
            }
        }
        throw new IllegalStateException("No implement of the service " + interfaceClass);
    }

    /**
     * Create instance based on given implementationClass and interface
     *
     * @param clazz          implementation class
     * @param singleton      Whether is is singleton or not
     * @param interfaceClass Interface class
     * @return Created instance
     */
    @Override
    public <T> T createInstance(Class<? extends T> clazz, boolean singleton, Class<T> interfaceClass) {
        if (clazz != null) {
            T obj = null;
            if (singleton) {
                obj = (T)serviceCache.get(interfaceClass);
                if (obj == null) {
                    synchronized (interfaceClass) {
                        obj = (T)serviceCache.get(interfaceClass);
                        if (obj == null) {
                            obj = createInstance0(clazz, interfaceClass);
                            serviceCache.put(interfaceClass, obj);
                        }
                    }
                }
                return obj;
            }
            else {
                return createInstance0(clazz, interfaceClass);
            }
        }
        throw new IllegalStateException("No implement of the interface " + interfaceClass);
    }

    private <T> T createInstance0(Class<? extends T> clazz, Class<T> interfaceClass) {
        if (clazz != null) {
            try {
                return createInstance(clazz, interfaceClass);
            }
            catch (Exception e) {
                throw new IllegalStateException(interfaceClass.getName() + ": "
                        + " Implementation " + clazz.getName() + " could not be instantiated: ", e);
            }
        }
        throw new IllegalStateException("No implement of the interface " + interfaceClass);
    }


    /**
     * Get an implement class by interfaceClass
     *
     * @param interfaceClass Interface Class
     * @param loader         Class Loader
     * @return Class
     */
    @Override
    public <T> Class<? extends T> getImplement(Class<T> interfaceClass, ClassLoader loader) {
        ImplementConfig<T> config = getImplementConfig(interfaceClass, loader);
        if (config != null) {
            return config.getImplementationClass();
        }
        else {
            ImplementedBy implementedBy = interfaceClass.getAnnotation(ImplementedBy.class);
            if (implementedBy != null) {
                return loadClass(implementedBy.value(), interfaceClass, loader);
            }
            return null;
        }
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
    public <T> ImplementConfig<T> getImplementConfig(Class<T> interfaceClass, ClassLoader loader) throws IllegalArgumentException {
        Map<String, ImplementConfig<?>> config = configs.get(interfaceClass);
        if (config == null) {
            String className = interfaceClass.getName();
            List<String> implementations = new ArrayList<>();
            for(Properties properties: propertiesList) {
                String value = properties.getProperty(className);
                if (value != null) {
                    String[] strings = DataUtil.toStrings(value, ',');
                    for(String str: strings) {
                        str = str.trim();
                        implementations.add(str);
                    }
                }
            }
            SimpleImplementConfig<T> sic = new SimpleImplementConfig<>(interfaceClass, null, this, implementations);
            Map<String, ImplementConfig<?>> map = new HashMap<>(2);
            map.put(null, sic);
            configs.put(interfaceClass, map);
            return sic;
        }
        return (ImplementConfig<T>)config.get(null);
    }

    protected void setName(Class<?> clazz, Object obj, String name) {
        if (obj instanceof Nameable) {
            ((Nameable)obj).setName(name);
        }
    }

    /**
     * Get an instance of the name+interfaceClass pair
     *
     * @param name           Name
     * @param interfaceClass Interface Class
     * @param singleton      Whether it is singleton
     * @param loader         ClassLoader
     * @return Created instance or instanced cached in this Util
     */
    @Override
    public <T> T getInstance(String name, Class<T> interfaceClass, boolean singleton, ClassLoader loader) {
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
                    Class<? extends T> clazz = getImplement(name, interfaceClass, loader);
                    instance = createInstance0(clazz, interfaceClass);
                    setName(clazz, instance, name);
                    initialize(instance);
                    namedServiceCache.get(interfaceClass).put(name, instance);
                }
            }
        }
        else {
            Class<? extends T> clazz = getImplement(name, interfaceClass, loader);
            instance = createInstance0(clazz, interfaceClass);
            setName(clazz, instance, name);
        }
        return instance;
    }

    /**
     * Returns the right implementations of this interface class and name.
     * For examples,
     * <code>
     * public interface Foo {
     * void someMethod();
     * }
     * <p>
     * #properties
     * foo1=FooImpl1
     * foo2=FooImpl2
     * </code>
     *
     * @param name           implementation Name
     * @param interfaceClass Interface class name
     * @param loader
     * @return The right implementClass
     */
    @Override
    public <T> Class<? extends T> getImplement(String name, Class<T> interfaceClass, ClassLoader loader) {
        Map<String, ImplementConfig<?>> configs = getMultipleImplements(interfaceClass, loader);
        if (configs != null) {
            ImplementConfig config = configs.get(name);
            if (config == null) {
                throw new IllegalStateException("No such implement of "+ interfaceClass + " by name " + name);
            }
            return config.getImplementationClass();
        }
        throw new IllegalStateException("No implement of the interface " + interfaceClass + " name " + name);
    }

    /**
     * Post Construct
     *
     * Check which method has <code>@PostConstruct</code>, and invoke it
     *
     * @param implementInstance
     */
    @Override
    public void initialize(Object implementInstance) {
        initialize(implementInstance, implementInstance.getClass());
    }

    protected void initialize(Object implementInstance, Class clazz) {
        if (implementInstance instanceof Initializable) {
            ((Initializable)implementInstance).initialize();
        }
        else {
            Method[] methods = clazz.getDeclaredMethods();
            boolean invoked = false;
            if (methods != null && methods.length > 0) {
                for(Method method: methods) {
                    if (method.isAnnotationPresent(PostConstruct.class)) {
                        Reflection.invoke(implementInstance, method);
                        invoked = true;
                    }
                }
            }
            if (!invoked) {
                Class superClass = clazz.getSuperclass();
                if (superClass != null && superClass != Object.class) {
                    initialize(implementInstance, superClass);
                }
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
     *     #implements.properties
     *     Foo=foo1:FooImpl1,foo2:FooImpl2
     * </code>
     * @param interfaceClass Interface class name
     * @param loader Class loader for the implementations
     * @return name to ImplementConfig mapping
     */
    public Map<String, ImplementConfig<?>> getMultipleImplements(Class<?> interfaceClass, ClassLoader loader) {
        Map<String, ImplementConfig<?>> config = configs.get(interfaceClass);
        if (config == null) {
            String className = interfaceClass.getName();

            config = new HashMap<>();
            configs.put(interfaceClass, config);

            for(int i = 0; i < propertiesList.size(); i ++) {
                Properties properties = propertiesList.get(i);
                String value = properties.getProperty(className);
                if (value != null) {
                    String[] strings = DataUtil.toStrings(value, ',');
                    for(String str: strings) {
                        str = str.trim();
                        int index = str.indexOf(':');
                        if (index <= 0) {
                            throw new IllegalStateException(
                                    "Invalid property " + className + " in file " + urls.get(i));
                        }
                        String name = str.substring(0, index).trim();
                        String implClass = str.substring(index+1).trim();

                        ImplementConfig<?> ic = config.get(name);
                        if (ic == null) {
                            ic = new SimpleImplementConfig<>(interfaceClass, name, this, Collections.EMPTY_LIST);
                            config.put(name, ic);
                        }
                        ic.setImplementation(implClass);
                    }
                }
            }
        }
        return config;
    }

    /**
     * Apply multiple instances
     *
     * @param interfaceClass Interface class
     * @param function How to handle function
     * @param <T> Instance Type
     */
    public <T> void applyInstances(Class<T> interfaceClass, InstancesFunction<T> function) {
        ImplementManager implementManager = ImplementUtil.getImplementManager();
        Map<String, ImplementConfig<?>> implementations = implementManager.getMultipleImplements(interfaceClass);
        for(Map.Entry<String, ImplementConfig<?>> entry: implementations.entrySet()) {
            String name = entry.getKey();
            Class<? extends T> clazz = (Class<? extends T>)entry.getValue().getImplementationClass();
            T instance = implementManager.createInstance(clazz, interfaceClass);
            function.apply(name, instance);
        }
    }

    /**
     * Apply one instance
     *
     * @param interfaceClass Interface class
     * @param function How to handle function
     * @param <T> Instance Type
     */
    public <T> void applyInstance(Class<T> interfaceClass, InstanceFunction<T> function) {
        ImplementManager implementManager = ImplementUtil.getImplementManager();
        T instance = implementManager.getInstance(interfaceClass);
        function.apply(instance);
    }
}
