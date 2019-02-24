/**
 * Licensed to the FemtoFramework under one or more
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Simple Implementation Config
 *
 * @author Sheldon Shao
 * @version 1.0
 */
public class SimpleImplementConfig<T> implements ImplementConfig<T> {

    private Class<T> interfaceClass;
    private ImplementManager implementManager;
    private List<String> implementations;
    private String name;

    public SimpleImplementConfig(Class<T> interfaceClass, String name, ImplementManager manager, List<String> implementations) {
        this.interfaceClass = interfaceClass;
        this.implementManager = manager;
        this.implementations = implementations;
        this.name = name;
    }

    /**
     * Interface name, interface could have two patterns
     * 1. One interface, one implementation
     * 2. One interface, many implementations
     *
     * In the #2 pattern, it should have a "name" to address different implementations.
     * For #1, it returns null
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Interface class name
     *
     * @return Interface class name
     */
    @Override
    public Class<T> getInterfaceClass() {
        return interfaceClass;
    }

    /**
     * Return the implementation which should be used in current context.
     * It could be,
     * 1. The default implementation specified by @ImplementedBy
     * 2. The implementation override by "implements.properties"
     * 3. The implementation override by "#setImplementation"
     *
     * @return the final used implementation
     */
    @Override
    public String getImplementation() {
        return implementations.isEmpty() ? getDefaultImplementation() : implementations.get(implementations.size()-1);
    }

    /**
     * Return the implementation which should be used in current context.
     * It could be,
     * 1. The default implementation specified by @ImplementedBy
     * 2. The implementation override by "implements.properties"
     * 3. The implementation override by "#setImplementation"
     *
     * @return the final used implementation
     */
    @Override
    public Class<? extends T> getImplementationClass() {
        String impl = getImplementation();
        if (impl == null) {
            throw new IllegalStateException("No implementation found for " + interfaceClass);
        }
        Class<? extends T>  clazz = implementManager.loadClass(impl, interfaceClass, Thread.currentThread().getContextClassLoader());
        if (!interfaceClass.isAssignableFrom(clazz)) {
            throw new IllegalStateException("The " + clazz + " is not implements " + " interface:" + interfaceClass);
        }
        return clazz;
    }

    /**
     * Set the implementation class
     *
     * @param implementationClass The class of implementation
     */
    @Override
    public void setImplementation(String implementationClass) {
        if (implementations == Collections.EMPTY_LIST) {
            implementations = new ArrayList<>(2);
            implementations.add(implementationClass);
        }
        else {
            implementations.add(implementationClass);
        }
    }

    /**
     * The default implementation which is the default implementation provided on interface class.
     * It could be,
     * 1. The default implementation specified by @ImplementedBy
     * 2. The first implementation could be found in class path "implements.properties"
     *
     * @return default implementation
     */
    @Override
    public String getDefaultImplementation() {
        ImplementedBy implementedBy = interfaceClass.getAnnotation(ImplementedBy.class);
        if (implementedBy != null) {
            return implementedBy.value();
        }

        Collection<String> classes = getImplementations();
        if (classes.isEmpty()) {
            return null;
        }
        //The first one
        return classes.iterator().next();
    }

    /**
     * Return all implementations
     *
     * @return all implementations
     */
    @Override
    public Collection<String> getImplementations() {
        return implementations;
    }
}
