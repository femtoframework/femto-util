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
package org.femtoframework.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * In some case, we just want to keep using "Separating interface and implementation" design pattern.
 * And make extensible for future.
 * But we can have a quick implementation together with the interface and make this simple declare whether the default implementation
 *
 * The idea is similar as that in Guice, but it use the class name instead.
 * https://github.com/google/guice/blob/master/core/src/com/google/inject/ImplementedBy.java
 *
 * @author fengyun
 * @version 1.00 2011-08-27 17:48
 */
@Retention (RetentionPolicy.RUNTIME)
@Target ({ElementType.TYPE})
@Documented
public @interface ImplementedBy {

    /**
     * Indicating the default implementation class
     * It should be a class name.
     * Why don't use Class instead?  We don't want to expose the Class in the interface class
     *
     * When using interface try to get implementation from META-INF/spec
     * if no other implements, take this type.
     *
     * @return class name of Implementation
     */
    String value();
}
