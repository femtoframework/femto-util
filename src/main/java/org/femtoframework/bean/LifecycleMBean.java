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
 * Lifecycle MBean
 *
 * @author Sheldon Shao
 * @version 1.0
 */
public interface LifecycleMBean extends Lifecycle, InitializabeMBean {

    /**
     * Return whether it is initialized
     *
     * @return whether it is initialized
     */
    default boolean isInitialized() {
        return getBeanPhase().ordinal() >= BeanPhase.INITIALIZED.ordinal();
    }


    /**
     * Initialized setter for internal
     *
     * @param initialized BeanPhase
     */
    default void _doSetInitialized(boolean initialized) {
        if (initialized) {
            _doSetPhase(BeanPhase.INITIALIZED);
        }
        else {
            _doSetPhase(BeanPhase.INITIALIZING);
        }
    }

    /**
     * Return current phase
     *
     * @return current phase
     */
    BeanPhase getBeanPhase();


    /**
     * Phase setter for internal
     *
     * @param phase BeanPhase
     */
    void _doSetPhase(BeanPhase phase);

    /**
     * Initialize the bean
     *
     * @throws org.femtoframework.bean.exception.InitializeException
     */
    default void initialize() {
        if (getBeanPhase().ordinal() < BeanPhase.INITIALIZING.ordinal()) {
            _doSetPhase(BeanPhase.INITIALIZING);
            _doInitialize();
            _doSetPhase(BeanPhase.INITIALIZED);
        }
    }

    /**
     * Initiliaze internally
     */
    default void _doInitialize() {

    }

    /**
     * Start
     *
     * @throws org.femtoframework.bean.exception.StartException
     */
    default void start() {
        if (getBeanPhase().ordinal() < BeanPhase.STARTING.ordinal() ) {
            _doSetPhase(BeanPhase.STARTING);
            _doStart();
            _doSetPhase(BeanPhase.STARTED);
        }
    }

    /**
     * Start internally
     */
    default void _doStart() {
    }

    /**
     * Stop the bean
     */
    default void stop() {
        if (getBeanPhase().ordinal() < BeanPhase.STOPPING.ordinal() ) {
            _doSetPhase(BeanPhase.STOPPING);
            _doStop();
            _doSetPhase(BeanPhase.STOPPED);
        }
    }

    /**
     * Stop internally
     */
    default void _doStop() {
    }


    /**
     * Destroy the bean
     */
    default void destroy() {
        if (getBeanPhase().ordinal() < BeanPhase.DESTROYING.ordinal() ) {
            _doSetPhase(BeanPhase.DESTROYING);
            _doDestroy();
            _doSetPhase(BeanPhase.DESTROYED);
        }
    }

    /**
     * Destroy internally
     */
    default void _doDestroy() {
    }
}
