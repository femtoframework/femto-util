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



import org.femtoframework.util.DataUtil;
import org.femtoframework.util.convert.ConverterUtil;

import java.util.*;

/**
 * Abstract Parameters
 *
 * Created by xshao on 9/16/16.
 */
public abstract class AbstractParameters<V> extends AbstractMap<String, V>
        implements Parameters<V> {

    public String getString(String key) {
        return getString(key, DEFAULT_STRING);
    }

    public String getString(String key, String defValue) {
        return DataUtil.getString(get(key), defValue);
    }

    public String[] getStrings(String key) {
        return getStrings(key, DEFAULT_STRING_ARRAY);
    }

    public String[] getStrings(String key, String[] defValue) {
        return DataUtil.getStrings(get(key), defValue);
    }

    public boolean getBoolean(String key) {
        return getBoolean(key, DEFAULT_BOOLEAN);
    }

    public boolean getBoolean(String key, boolean defValue) {
        return DataUtil.getBoolean(get(key), defValue);
    }

    public int getInt(String key) {
        return getInt(key, DEFAULT_INT);
    }

    public int getInt(String key, int defValue) {
        return DataUtil.getInt(get(key), defValue);
    }

    public int[] getInts(String key) {
        return getInts(key, DEFAULT_INT_ARRAY);
    }

    public int[] getInts(String key, int[] defValue) {
        return DataUtil.getInts(get(key), defValue);
    }

    public double getDouble(String key) {
        return getDouble(key, DEFAULT_DOUBLE);
    }

    public double getDouble(String key, double defValue) {
        return DataUtil.getDouble(get(key), defValue);
    }


    public long getLong(String key) {
        return getLong(key, DEFAULT_LONG);
    }

    public long getLong(String key, long defValue) {
        return DataUtil.getLong(get(key), defValue);
    }

    public V get(String key, V defValue) {
        V value = get(key);
        return value == null ? defValue : value;
    }

    public Object[] getObjects(String key) {
        return getObjects(key, null);
    }

    public Object[] getObjects(String key, Object[] defValue) {
        Object obj = get(key);
        return ConverterUtil.getObjects(Object.class, obj, defValue);
    }

    public static <V> Parameters<V> toParameters(Map value) {
        if (value instanceof Parameters) {
            return (Parameters)value;
        }
        else {
            return new ParametersMap<>(value);
        }
    }

    /**
     * Parsing the String value to List&lt;String&gt;.
     * The string will be split by ",".
     *
     * @param key Key
     * @return A list type value
     */
    public List<String> getStringList(String key) {
        Object value = get(key);
        return DataUtil.getStringList(value);
    }

    /**
     * Convert value as List as possible
     *
     * @param key Key
     * @return A list type of value
     */
    @Override
    public <T> List<T> getList(String key) {
        Object obj = get(key);
        return DataUtil.getList(obj);
    }

    /**
     * Convert parameter as Date
     *
     * @param key Key
     * @return convert long or Date as Date
     */
    public Date getDate(String key) {
        return getDate(key, null);
    }

    /**
     * Convert parameter as Date
     *
     * @param key Key
     * @return convert long or Date as Date
     */
    public Date getDate(String key, Date defaultValue) {
        Object obj = get(key);
        return DataUtil.getDate(obj, defaultValue);
    }

    /**
     * Convert string or int as Enum
     *
     * @param key Key
     * @return Convert string or int as Enum
     */
    public <T extends Enum> T getEnum(Class<T> clazz, String key) {
        return getEnum(clazz, key, null);
    }


    /**
     * Convert string or int as Enum
     *
     * @param key Key
     * @return Convert string or int as Enum
     */
    public <T extends Enum> T getEnum(Class<T> clazz, String key, T defaultValue) {
        Object obj = get(key);
        return DataUtil.getEnum(clazz, obj, defaultValue);
    }

    public Map<String, V> toMap() {
        return this;
    }

//    /**
//     * Convert the parameters to json format
//     *
//     * @return Json Format
//     */
//    public String toJson() {
//        try {
//            return DataBindUtil.writeValueAsString(this);
//        } catch (IOException e) {
//            throw new IllegalStateException("IOException when converting to json", e);
//        }
//    }
}