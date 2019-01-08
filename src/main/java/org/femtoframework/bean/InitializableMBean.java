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
package org.femtoframework.bean;

/**
 * Initializable MBean
 *
 * Guarantee that the initialize method won't be executed twice
 *
 * @author Sheldon Shao
 * @version 1.0
 */
public interface InitializableMBean extends Initializable {

    /**
     * Return whether it is initialized
     *
     * @return whether it is initialized
     */
    boolean isInitialized();


    /**
     * Initialized setter for internal
     *
     * @param initialized BeanPhase
     */
    void _doSetInitialized(boolean initialized);

    /**
     * Initialize the bean
     *
     * @throws org.femtoframework.bean.exception.InitializeException
     */
    default void initialize() {
        if (isInitialized()) {
            _doSetInitialized(false);
            _doInitialize();
            _doSetInitialized(true);
        }
    }

    /**
     * Initiliaze internally
     */
    void _doInitialize();

}
