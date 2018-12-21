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

import org.femtoframework.lang.reflect.Reflection;
import org.femtoframework.lang.reflect.ReflectionException;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.Enumeration;

/**
 * Simple ImplementManager implementation
 *
 * @author fengyun
 * @version 1.00 11-8-18 19:55
 */
public enum SimpleImplementManager implements ImplementManager {
    INSTANCE;

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
    public <T> Class<? extends T> loadClass(String className, Class<? extends T> interfaceClass, ClassLoader loader) throws ClassNotFoundException {
        return (Class<? extends T>) Reflection.loadClass(loader, className);
    }


    /**
     * Create implementation
     *
     * @param clazz          Service implementation class
     * @param singleton      Try to create as singleton mode first.
     * @param interfaceClass
     * @return
     */
    @Override
    public <T> T createImplement(Class<?> clazz, boolean singleton, Class<T> interfaceClass) {
        if (clazz != null) {
            if (singleton) {
                return (T)Reflection.newSingleton(clazz);
            }
            else {
                try {
                    return (T)clazz.newInstance();
                }
                catch (Exception e) {
                    //Try static getInstance method
                    try {
                        Method method = clazz.getMethod("getInstance");
                        if (Modifier.isStatic(method.getModifiers())) {
                            return (T)Reflection.invoke(null, method);
                        }
                    }
                    catch (NoSuchMethodException ex) {
                    }
                    catch (ReflectionException ex) {
                    }
                    throw new IllegalStateException(
                            "Provider " + clazz.getName() + " could not be instantiated: " + e + " for service interface:" +
                                    interfaceClass, e);
                }
            }
        }
        throw new IllegalStateException("No implement of the service " + interfaceClass);
    }

    /**
     * Get resources from class path
     *
     * @param resourceName Resource Name
     * @param loader       Current Class Loader
     */
    @Override
    public Enumeration<URL> getResources(String resourceName, ClassLoader loader) throws IOException {
        return Reflection.getResources(loader, resourceName);
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

    private void initialize(Object implementInstance, Class clazz) {
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
