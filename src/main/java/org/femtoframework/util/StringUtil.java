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
package org.femtoframework.util;

/**
 * String Util
 *
 * @author Sheldon Shao
 * @version 1.0
 */
public class StringUtil {
    /**
     * Check if the string value is valid
     *
     * @param value
     */
    public static boolean isValid(String value) {
        return value != null && !value.isEmpty();
    }

    /**
     * Check if the string value is invalid
     *
     * @param value
     */
    public static boolean isInvalid(String value) {
        return value == null || value.isEmpty();
    }


    /**
     * Compares two strings, avoid NullPointerException
     * <p/>
     * If both str1 and str2 are null, then returns true
     *
     * @param str1 String 1
     * @param str2 String 2
     * @return [true|false]
     */
    public static boolean equals(String str1, String str2) {
        if (str1 == null) {
            return str2 == null;
        }

        return str1.equals(str2);
    }

    /**
     * Compares two strings, avoid NullPointerException
     * <p/>
     * If both str1 and str2 are null, then returns true
     *
     * @param str1 String 1
     * @param str2 String 2
     * @return [true|false]
     */
    public static boolean equalsIgnoreCase(String str1,
                                           String str2) {
        if (str1 == null) {
            return str2 == null;
        }
        return str1.equalsIgnoreCase(str2);
    }
}
