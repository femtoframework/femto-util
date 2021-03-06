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
package org.femtoframework.bean;

/**
 * Entire Bean lifecycle
 *
 * @author Sheldon Shao
 * @version 1.0
 */
public interface Lifecycle extends Initializable, Startable, Stoppable, Destroyable {

    /**
     * Initialize the bean
     *
     * @throws org.femtoframework.bean.exception.InitializeException
     */
    default void init() {
    }

    /**
     * Start
     *
     * @throws org.femtoframework.bean.exception.StartException
     */
    default void start() {

    }

    /**
     * Stop the bean
     */
    default void stop() {

    }

    /**
     * Destroy the bean
     */
    default void destroy() {

    }
}
