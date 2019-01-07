/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/**
 * Copyright (c) 2016 eBay Software Foundation. All rights reserved.
 *
 * Licensed under the MIT license.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 *
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.femtoframework.parameters;


import java.util.*;

import org.femtoframework.util.DataUtil;
import org.femtoframework.util.convert.ConverterUtil;

/**
 * Parameters to simplify the data accessing
 *
 * The original published version is https://github.com/eBay/Winder/blob/master/winder-core/src/main/java/org/ebayopensource/common/util/Parameters.java
 *
 *
 * Modifid by Sheldon Shao(xshao) on 12/08/2018
 * Created by xshao on 9/16/16.
 */
public interface Parameters<V> extends Map<String, V> {

    String DEFAULT_STRING = null;
    int DEFAULT_INT = 0;
    boolean DEFAULT_BOOLEAN = false;
    long DEFAULT_LONG = 0L;
    double DEFAULT_DOUBLE = 0.0d;

    String[] DEFAULT_STRING_ARRAY = DataUtil.EMPTY_STRING_ARRAY;

    int[] DEFAULT_INT_ARRAY = DataUtil.EMPTY_INT_ARRAY;

    default String getString(String key) {
        return getString(key, DEFAULT_STRING);
    }

    default String getString(String key, String defValue) {
        return DataUtil.getString(get(key), defValue);
    }

    default boolean getBoolean(String key) {
        return getBoolean(key, DEFAULT_BOOLEAN);
    }

    default boolean getBoolean(String key, boolean defValue) {
        return DataUtil.getBoolean(key, defValue);
    }

    default int getInt(String key) {
        return getInt(key, DEFAULT_INT);
    }

    default int getInt(String key, int defValue) {
        return DataUtil.getInt(get(key), defValue);
    }

    default double getDouble(String key) {
        return getDouble(key, DEFAULT_DOUBLE);
    }

    default double getDouble(String key, double defValue) {
        return DataUtil.getDouble(get(key), defValue);
    }

    default long getLong(String key) {
        return getLong(key, DEFAULT_LONG);
    }

    default long getLong(String key, long defValue) {
        return DataUtil.getLong(get(key), defValue);
    }

    default V get(String key, V defValue) {
        V value = get(key);
        return value == null ? defValue : value;
    }

    default int[] getInts(String key) {
        return getInts(key, null);
    }

    default int[] getInts(String key, int[] defValue) {
        return DataUtil.getInts(get(key), defValue);
    }

    default Object[] getObjects(String key) {
        return getObjects(key, null);
    }

    default Object[] getObjects(String key, Object[] defValue) {
        Object obj = get(key);
        return ConverterUtil.getObjects(Object.class, obj, defValue);
    }

    default String[] getStrings(String key) {
        return getStrings(key, null);
    }

    default String[] getStrings(String key, String[] defValue) {
        return DataUtil.getStrings(get(key), defValue);
    }

    /**
     * Return value as Parameters
     *
     * @param key Key
     * @return value as Parameters, if the value is map, the map will wrapped as a Parameters
     */
    Parameters<V> getParameters(String key);

    /**
     * Parsing the String value to List&lt;String&gt;.
     * The string will be split by ",".
     *
     * @param key Key
     * @return A list type of value
     */
    default List<String> getStringList(String key) {
        Object value = get(key);
        return DataUtil.getStringList(value);
    }

    /**
     * Convert value as List as possible
     *
     * @param key Key
     * @return A list type of values
     */
    default <T> List<T> getList(String key) {
        return getList(key, null);
    }

    /**
     * Convert value as List as possible
     *
     * @param key Key
     * @return A list type of values
     */
    default <T> List<T> getList(String key, List defValue) {
        Object obj = get(key);
        return DataUtil.getList(obj, defValue);
    }


    /**
     * Convert value as Set as possible
     *
     * @param key Key
     * @return A set of values
     */
    default <T> Set<T> getSet(String key) {
        return getSet(key, Collections.emptySet());
    }

    /**
     * Convert value as Set as possible
     *
     * @param key Key
     * @return A set of values
     */
    default <T> Set<T> getSet(String key, Set<T> defValue) {
        Object obj = get(key);
        return DataUtil.getSet(obj, defValue);
    }

    /**
     * Convert parameter as Date
     *
     * @param key Key
     * @return convert long or Date as Date
     */
    default Date getDate(String key) {
        return getDate(key, null);
    }


    /**
     * Convert parameter as Date
     *
     * @param key Key
     * @return convert long or Date as Date
     */
    default Date getDate(String key, Date defaultValue) {
        Object obj = get(key);
        return DataUtil.getDate(obj, defaultValue);
    }

    /**
     * Convert string or int as Enum
     *
     * @param key Key
     * @return Convert string or int as Enum
     */
    default <T extends Enum> T getEnum(Class<T> clazz, String key) {
        return getEnum(clazz, key, null);
    }

    /**
     * Convert string or int as Enum
     *
     * @param key Key
     * @return Convert string or int as Enum
     */
    default <T extends Enum> T getEnum(Class<T> clazz, String key, T defaultValue) {
        Object obj = get(key);
        return DataUtil.getEnum(clazz, obj, defaultValue);
    }

    /**
     * To Map
     *
     * @return Convert to Map
     */
    default Map<String, V> toMap() {
        return this;
    }

    /**
     * Static method to convert Map to Parameters
     *
     * @param value Map
     * @param <V> Expected Value type
     * @return Parameters
     */
    static <V> Parameters<V> toParameters(Map value) {
        if (value instanceof Parameters) {
            return (Parameters)value;
        }
        else {
            return new ParametersMap<>(value);
        }
    }
//    /**
//     * Convert the parameters to json format
//     *
//     * @return Json Format
//     */
//    String toJson();

}
