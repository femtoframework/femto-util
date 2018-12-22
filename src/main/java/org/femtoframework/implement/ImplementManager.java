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

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;

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
     * @throws ClassNotFoundException either className or interfaceClass is not found
     */
    <T> Class<? extends T> loadClass(String className, Class<? extends T> interfaceClass, ClassLoader loader) throws ClassNotFoundException;

    /**
     * Create instance
     *
     * @param clazz Implementation instance class
     * @param singleton Try to create as singleton mode first.
     * @param interfaceClass
     * @param <T>
     * @return
     */
    <T> T createInstance(Class<?> clazz, boolean singleton, Class<T> interfaceClass);

    /**
     * Get resources from class path
     *
     * @param resourceName Resource Name
     * @param loader Current Class Loader
     */
    Enumeration<URL> getResources(String resourceName, ClassLoader loader) throws IOException;


    /**
     * Post Construct
     *
     * Check which method has <code>@PostConstruct</code>, and invoke it
     *
     * @param implementInstance
     */
    void initialize(Object implementInstance);
}
