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

import java.util.Collection;

/**
 * Impelement Config
 *
 * @author Sheldon Shao
 * @version 1.0
 */
public interface ImplementConfig<T> {

    /**
     * Interface class name
     *
     * @return Interface class name
     */
    Class<T> getInterfaceClass();

    /**
     * Interface name, interface could have two patterns
     * 1. One interface, one implementation
     * 2. One interface, many implementations
     *
     * In the #2 pattern, it should have a "name" to address different implementations.
     * For #1, it returns null
     */
    default String getName() {
        return null;
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
    String getImplementation();


    /**
     * Return the implementation which should be used in current context.
     * It could be,
     * 1. The default implementation specified by @ImplementedBy
     * 2. The implementation override by "implements.properties"
     * 3. The implementation override by "#setImplementation"
     *
     * @return the final used implementation
     */
    Class<? extends T> getImplementationClass();

    /**
     * Set the implementation class
     *
     * @param implementationClass The class of implementation
     */
    void setImplementation(String implementationClass);

    /**
     * The default implementation which is the default implementation provided on interface class.
     * It could be,
     * 1. The default implementation specified by @ImplementedBy
     * 2. The first implementation could be found in class path "implements.properties"
     *
     * @return default implementation
     */
    String getDefaultImplementation();

    /**
     * Return all implementations
     *
     * @return all implementations
     */
    Collection<String> getImplementations();
}
