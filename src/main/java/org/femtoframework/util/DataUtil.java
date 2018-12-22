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
package org.femtoframework.util;

import org.femtoframework.parameters.Parameters;
import org.femtoframework.parameters.ParametersMap;
import org.femtoframework.text.SimpleDate;
import org.femtoframework.text.SimpleTime;

import java.lang.reflect.Array;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Data Util
 *
 * to make the type conversion easily.
 *
 * Java is not a type safe language. But in some use cases, for example, convert properties to java object or reflection in runtime,
 * Using default java code will throw exception when got an unexpected data, in real world, something is better to have some default value instead of breaking the code.
 * DataUtil and .convert.* are based on this idea to treat java like a type safe language.
 *
 * The last version was published here.
 *
 * https://github.com/eBay/Winder/blob/master/winder-core/src/main/java/org/ebayopensource/common/util/DataUtil.java
 *
 *
 * Modified by Sheldon Shao(xshao) on 12/09/2018), include it in femtoframework/femto-util and changed license to APL v2
 * Created by xshao on 9/16/16.
 */
public class DataUtil implements DataTypes {
    /**
     * Convert value to boolean
     *
     * @param value the object to convert from
     * @param defValue default value
     * @return the converted boolean
     */
    public static boolean getBoolean(Object value, boolean defValue)
    {
        if (value == null) {
            return defValue;
        }

        boolean result;
        if (value instanceof Boolean) {
            result = ((Boolean)value);
        }
        else if (value instanceof String) {
            return toBoolean((String)value, defValue);
        }
        else if (value instanceof Number) {
            long l = ((Number)value).longValue();
            result = l != 0;
        }
        else {
            result = defValue;
        }
        return result;
    }

    private static final String[] VALID_TRUE
            = {"True", "true", "TRUE", "Yes", "yes", "YES",
            "On", "on", "ON", "T", "t", "Y", "y", "1"};

    private static final String[] VALID_FALSE
            = {"False", "false", "FALSE", "No", "no", "NO",
            "Off", "off", "OFF", "F", "f", "N", "n", "0"};

    private static final HashSet<String> TRUE_SET = new HashSet<String>(VALID_TRUE.length);
    private static final HashSet<String> FALSE_SET = new HashSet<String>(VALID_FALSE.length);

    static {
        Collections.addAll(TRUE_SET, VALID_TRUE);
        Collections.addAll(FALSE_SET, VALID_FALSE);
    }


    /**
     * String to Boolean
     *
     * <pre>
     *    True   --&gt;   TRUE         False  --&gt;   FALSE
     *    true   --&gt;   TRUE         false  --&gt;   FALSE
     *    TRUE   --&gt;   TRUE         FALSE  --&gt;   FALSE
     *    Yes    --&gt;   TRUE         No     --&gt;   FALSE
     *    yes    --&gt;   TRUE         no     --&gt;   FALSE
     *    YES    --&gt;   TRUE         NO     --&gt;   FALSE
     *    On     --&gt;   TRUE         Off    --&gt;   FALSE
     *    on     --&gt;   TRUE         off    --&gt;   FALSE
     *    ON     --&gt;   TRUE         OFF    --&gt;   FALSE
     *    T      --&gt;   TRUE         F      --&gt;   FALSE
     *    t      --&gt;   TRUE         f      --&gt;   FALSE
     *    Y      --&gt;   TRUE         N      --&gt;   FALSE
     *    y      --&gt;   TRUE         n      --&gt;   FALSE
     * </pre>
     *
     * @param value The string to be converted to boolean
     * @param defValue Default value
     * @return Boolean value
     */
    public static boolean toBoolean(String value, boolean defValue)
    {
        return TRUE_SET.contains(value) || !FALSE_SET.contains(value) && defValue;
    }

    /**
     * Convert string to boolean
     * <pre>
     *    True   --&gt;   TRUE         False  --&gt;   FALSE
     *    true   --&gt;   TRUE         false  --&gt;   FALSE
     *    TRUE   --&gt;   TRUE         FALSE  --&gt;   FALSE
     *    Yes    --&gt;   TRUE         No     --&gt;   FALSE
     *    yes    --&gt;   TRUE         no     --&gt;   FALSE
     *    YES    --&gt;   TRUE         NO     --&gt;   FALSE
     *    On     --&gt;   TRUE         Off    --&gt;   FALSE
     *    on     --&gt;   TRUE         off    --&gt;   FALSE
     *    ON     --&gt;   TRUE         OFF    --&gt;   FALSE
     *    T      --&gt;   TRUE         F      --&gt;   FALSE
     *    t      --&gt;   TRUE         f      --&gt;   FALSE
     *    Y      --&gt;   TRUE         N      --&gt;   FALSE
     *    y      --&gt;   TRUE         n      --&gt;   FALSE
     * </pre>
     * @param value String value
     * @return If the string can be converted to boolean, it returns the correct boolean
     */
    public static boolean toBoolean(String value)
    {
        return toBoolean(value, false);
    }

    /**
     * Convert object to boolean[] as much as possible
     * @param object
     * @param defValue
     * @return
     */
    public static boolean[] getBooleans(Object object, boolean[] defValue)
    {
        if (object == null) {
            return defValue;
        }

        boolean[] result = null;
        if (object instanceof boolean[]) {
            result = (boolean[])object;
        }
        else if (object instanceof Boolean) {
            result = new boolean[]{((Boolean)object).booleanValue()};
        }
        else if (object instanceof Number) {
            result = new boolean[]{getBoolean(object, false)};
        }
        else if (object instanceof Object[]) {
            Object[] array = (Object[])object;
            result = new boolean[array.length];
            for (int i = 0; i < array.length; i++) {
                result[i] = getBoolean(array[i], false);
            }
        }
        else if (object.getClass().isArray()) {
            int len = Array.getLength(object);
            result = new boolean[len];
            for (int i = 0; i < len; i++) {
                result[i] = getBoolean(Array.get(object, i), false);
            }
        }
        else if (object instanceof String) {
            String[] array = toStrings((String)object, ',');
            result = new boolean[array.length];
            for (int i = 0; i < array.length; i++) {
                result[i] = getBoolean(array[i], false);
            }
        }
        else {
            result = defValue;
        }
        return result;
    }

    private static int tryRadix(String str, int radix) {
        if (str.length() > 2 && str.charAt(0) == '0') {
            char c = str.charAt(1);
            switch (c) {
                case 'x':
                case 'X':
                    radix = 16;
                    break;
                case 'o':
                case 'O':
                    radix = 8;
                    break;
                case 'b':
                case 'B':
                    radix = 2;
                    break;
            }
            if (radix != 10) {
                str = str.substring(2);
            }
        }
        return radix;
    }

    /**
     * Convert value to byte
     *
     * @param value the object to convert from
     * @return the converted byte
     */
    public static byte getByte(Object value, byte defValue)
    {
        if (value == null) {
            return defValue;
        }

        byte result = defValue;
        if (value instanceof Number) {
            result = ((Number)value).byteValue();
        }
        else if (value instanceof String) {
            String str = (String)value;
            try {
                int radix = tryRadix(str, 10);
                result = (byte)Integer.parseInt(str, radix);
            }
            catch (Exception ex) {
                result = defValue;
            }
        }
        else if (value instanceof String[]) {
            String[] strs = (String[])value;
            if (strs.length > 0) {
                result = getByte(strs[0], defValue);
            }
            else {
                result = defValue;
            }
        }
        else if (value instanceof Boolean) {
            result = ((Boolean)value).booleanValue() ? (byte)1 : (byte)0;
        }
        else {
            result = defValue;
        }

        return result;
    }

    /**
     * Convert value to char
     *
     * @param value the object to convert from
     * @return the converted char
     */
    public static char getChar(Object value, char defValue)
    {
        char result;
        if (value != null) {
            if (value instanceof Character) {
                result = ((Character)value);
            }
            else if (value instanceof String) {
                String str = (String)value;
                if (str.length() > 0) {
                    result = str.charAt(0);
                }
                else {
                    result = defValue;
                }
            }
            else if (value instanceof Boolean) {
                boolean bool = ((Boolean)value);
                result = bool ? 'Y' : 'N';
            }
            else {
                result = defValue;
            }
        }
        else {
            result = defValue;
        }
        return result;
    }

    /**
     * Convert value to byte[]
     *
     * @param value the value to convert from. This must be a byte array, or
     *              null
     * @return a copy of the supplied array, or null
     */
    public static char[] getChars(Object value, char[] defValue)
    {
        char[] result;
        if (value instanceof char[]) {
            result = (char[])value;
        }
        else if (value instanceof String) {
            result = ((String)value).toCharArray();
        }
        else if (value instanceof CharSequence) {
            result = ((CharSequence)value).toString().toCharArray();
        }
        else {
            result = defValue;
        }
        return result;
    }

    /**
     * Convert value to short
     *
     * @param value the object to convert from
     * @return the converted short
     */
    public static short getShort(Object value, short defValue)
    {
        if (value == null) {
            return defValue;
        }

        short result = defValue;
        if (value instanceof Number) {
            result = ((Number)value).shortValue();
        }
        else if (value instanceof String) {
            String str = (String)value;
            str = str.trim();
            try {
                int radix = tryRadix(str, 10);
                result = Short.parseShort(str, radix);
            }
            catch (Exception ex) {
                result = defValue;
            }
        }
        else if (value instanceof String[]) {
            String[] strs = (String[])value;
            if (strs.length > 0) {
                result = getShort(strs[0], defValue);
            }
            else {
                result = defValue;
            }
        }
        else {
            result = defValue;
        }

        return result;
    }

    public static int getInt(Object value, int defValue)
    {
        if (value == null) {
            return defValue;
        }

        int result;
        if (value instanceof Number) {
            result = ((Number)value).intValue();
        }
        else if (value instanceof String) {
            String str = (String)value;
            str = str.trim();
            try {
                int radix = tryRadix(str, 10);
                result = Integer.parseInt(str, radix);
            }
            catch (Exception ex) {
                result = defValue;
            }
        }
        else if (value instanceof Boolean) {
            Boolean b = (Boolean)value;
            return b ? 1 : 0;
        }
        else {
            result = defValue;
        }
        return result;
    }


    /**
     * Convert value to long
     *
     * @param value the object to convert from
     * @return the converted long
     */
    public static long getLong(Object value, long defValue)
    {
        if (value == null) {
            return defValue;
        }

        long result;
        if (value instanceof Number) {
            result = ((Number)value).longValue();
        }
        else if (value instanceof String) {
            String str = (String)value;
            str = str.trim();
            try {
                int radix = tryRadix(str, 10);
                result = Long.parseLong(str, radix);
            }
            catch (Exception ex) {
                result = defValue;
            }
        }
        else if (value instanceof Boolean) {
            Boolean b = (Boolean)value;
            return b ? 1l : 0l;
        }
        else {
            result = defValue;
        }

        return result;
    }

    /**
     * Convert value to float
     *
     * @param value the object to convert from
     * @return the converted float
     */
    public static float getFloat(Object value, float defValue)
    {
        if (value == null) {
            return defValue;
        }

        float result = defValue;
        if (value instanceof Number) {
            result = ((Number)value).floatValue();
        }
        else if (value instanceof String) {
            String str = (String)value;
            str = str.trim();
            try {
                result = Float.parseFloat(str);
            }
            catch (Exception ex) {
                result = defValue;
            }
        }
        else if (value instanceof String[]) {
            String[] strs = (String[])value;
            if (strs.length > 0) {
                result = getFloat(strs[0], defValue);
            }
            else {
                result = defValue;
            }
        }
        else if (value instanceof Boolean) {
            Boolean b = (Boolean)value;
            return b.booleanValue() ? 1f : 0f;
        }
        else {
            result = defValue;
        }

        return result;
    }

    /**
     * Convert value to double
     *
     * @param value the object to convert from
     * @param defValue The default value
     * @return the converted double
     */
    public static double getDouble(Object value, double defValue)
    {
        if (value == null) {
            return defValue;
        }

        double result;
        if (value instanceof Number) {
            result = ((Number)value).doubleValue();
        }
        else if (value instanceof String) {
            String str = (String)value;
            str = str.trim();
            try {
                result = Double.parseDouble(str.trim());
            }
            catch (Exception ex) {
                result = defValue;
            }
        }
        else if (value instanceof Boolean) {
            Boolean b = (Boolean)value;
            return b ? 1d : 0d;
        }
        else {
            result = defValue;
        }

        return result;
    }

    /**
     * Convert value to byte[]
     *
     * @param value the value to convert from. This must be a byte array, or
     *              null
     * @return a copy of the supplied array, or null
     */
    public static byte[] getBytes(Object value, byte[] defValue)
    {
        if (value == null) {
            return defValue;
        }

        byte[] result = null;
        if (value instanceof byte[]) {
            result = (byte[])value;
        }
        else if (value instanceof String) {
            result = ((String)value).getBytes();
        }
        else if (value instanceof Number) {
            result = new byte[]{getByte(value, (byte)0)};
        }
        else if (value instanceof Object[]) {
            Object[] array = (Object[])value;
            result = new byte[array.length];
            for (int i = 0; i < array.length; i++) {
                result[i] = getByte(array[i], (byte)0);
            }
        }
        else if (value.getClass().isArray()) {
            int len = Array.getLength(value);
            result = new byte[len];
            for (int i = 0; i < len; i++) {
                result[i] = getByte(Array.get(value, i), (byte)0);
            }
        }
        else if (value instanceof Boolean) {
            result = new byte[]{getByte(value, (byte)0)};
        }
        else {
            result = defValue;
        }
        return result;
    }

    public static String getString(Object value, String defValue)
    {
        if (value == null) {
            return defValue;
        }

        String str;
        if (value instanceof String) {
            str = (String)value;
        }
        else if (value instanceof Number
                || value instanceof Boolean
                || value instanceof Character) {
            str = value.toString();
        }
        else {
            str = defValue;
        }
        return str;
    }

    public static String[] getStrings(Object object, String[] defValue)
    {
        if (object == null) {
            return defValue;
        }

        String[] result;
        if (object instanceof String[]) {
            result = (String[])object;
        }
        else if (object instanceof String) {
            String str = (String)object;
            str = str.trim();

            result = toStrings(str, ',');
        }
        else if (object instanceof Object[]) {
            Object[] array = (Object[])object;
            result = new String[array.length];
            for (int i = 0; i < array.length; i++) {
                result[i] = getString(array[i], null);
            }
        }
        else if (object instanceof List) {
            List list = (List)object;
            result = new String[list.size()];
            for (int i = 0; i < list.size(); i++) {
                result[i] = getString(list.get(i), null);
            }
        }
        else if (object.getClass().isArray()) {
            int len = Array.getLength(object);
            result = new String[len];
            for (int i = 0; i < len; i++) {
                result[i] = getString(Array.get(object, i), null);
            }
        }
        else if (object instanceof Number
                || object instanceof Boolean
                || object instanceof Character) {
            result = new String[]{object.toString()};
        }
        else {
            result = defValue;
        }
        return result;
    }

    public static int[] getInts(Object object, int[] defValue)
    {
        if (object == null) {
            return defValue;
        }

        int[] result;
        if (object instanceof int[]) {
            result = (int[])object;
        }
        else if (object instanceof Number) {
            result = new int[]{getInt(object, 0)};
        }
        else if (object instanceof Number[]) {
            Number[] array = (Number[])object;
            result = new int[array.length];
            for (int i = 0; i < array.length; i++) {
                result[i] = array[i].intValue();
            }
        }
        else if (object.getClass().isArray()) {
            int len = Array.getLength(object);
            result = new int[len];
            for (int i = 0; i < len; i++) {
                result[i] = getInt(Array.get(object, i), 0);
            }
        }
        else if (object instanceof Boolean) {
            result = new int[]{getInt(object, 0)};
        }
        else {
            result = defValue;
        }
        return result;
    }

    public static <T> List<T> getList(Object obj) {
        return getList(obj, null);
    }

    /**
     * Convert object to List. It supports
     * 1. List
     * 2. Object[] to List
     * 3. primitive[] to List
     * 4. Set to List
     *
     * @param obj
     * @param defaultValue
     * @param <T>
     * @return
     */
    public static <T> List<T> getList(Object obj, List<T> defaultValue) {
        if (obj instanceof List) {
            return (List)obj;
        }
        else if (obj instanceof Object[]) {
            Object[] array = (Object[])obj;
            List<T> list = new ArrayList<>(array.length);
            for (Object anArray : array) {
                list.add((T)anArray);
            }
            return list;
        }
        else if (obj != null && obj.getClass().isArray()) {
            int len = Array.getLength(obj);
            List<T> list = new ArrayList<>(len);
            for (int i = 0; i < len; i++) {
                list.add((T)Array.get(obj, i));
            }
            return list;
        }
        else if (obj instanceof Set) {
            Set set = (Set)obj;
            List list = new ArrayList(set.size());
            list.addAll(set);
            return list;
        }
        return defaultValue;
    }

    public static Map getMap(Object obj) {
        return getMap(obj, null);
    }

    /**
     * Convert object to Map. It supports
     * 1. Map
     * 2. List to Map, the key is index(Integer) in the List
     * 3. Object[] to Map,  the key is index(Integer) in the array
     * 4. primitive[] to Map, the key is index(Integer) in the array
     * 5. Set to Map, Key->TRUE
     *
     * @param obj
     * @param defaultValue
     * @return
     */
    public static Map getMap(Object obj, Map defaultValue) {
        if (obj instanceof Map) {
            return (Map)obj;
        }
        else if (obj instanceof Set) {
            Set set = (Set)obj;
            Map map = new HashMap(set.size());
            for(Object o : set) {
                map.put(o, Boolean.TRUE);
            }
            return map;
        }
        else if (obj instanceof List) {
            List list = (List)obj;
            Map map = new HashMap(list.size());
            int i = 0;
            for(Object o : list) {
                map.put(i++, o);
            }
            return map;
        }
        else if (obj instanceof Object[]) {
            Object[] array = (Object[])obj;
            Map map = new HashMap(array.length);
            for (int i = 0; i < array.length; i ++) {
                map.put(i, array[i]);
            }
            return map;
        }
        else if (obj != null && obj.getClass().isArray()) {
            int len = Array.getLength(obj);
            Map map = new HashMap(len);
            for (int i = 0; i < len; i++) {
                map.put(i, Array.get(obj, i));
            }
            return map;
        }
        return defaultValue;
    }

    /**
     * Convert object to Parameters. It supports
     * 1. Map(String, ?)
     * 2. List to Parameters, the key is index(String) in the List
     * 3. Object[] to Parameters,  the key is index(String) in the array
     * 4. primitive[] to Parameters, the key is index(String) in the array
     * 5. Set(String) to Parameters, Key->TRUE
     *
     * @param obj
     * @param defaultValue
     * @return
     */
    public static Parameters getParameters(Object obj, Parameters defaultValue) {
        if (obj instanceof Parameters) {
            return (Parameters)obj;
        }
        else if (obj instanceof Map) {
            return new ParametersMap((Map)obj);
        }
        else if (obj instanceof Set) {
            Set set = (Set)obj;
            Map map = new HashMap(set.size());
            for(Object o : set) {
                map.put(o, Boolean.TRUE);
            }
            return new ParametersMap(map);
        }
        else if (obj instanceof List) {
            List list = (List)obj;
            ParametersMap map = new ParametersMap();
            int i = 0;
            for(Object o : list) {
                map.put(String.valueOf(i++), o);
            }
            return map;
        }
        else if (obj instanceof Object[]) {
            Object[] array = (Object[])obj;
            ParametersMap map = new ParametersMap();
            for (int i = 0; i < array.length; i ++) {
                map.put(String.valueOf(i), array[i]);
            }
            return map;
        }
        else if (obj != null && obj.getClass().isArray()) {
            int len = Array.getLength(obj);
            ParametersMap map = new ParametersMap();
            for (int i = 0; i < len; i++) {
                map.put(String.valueOf(i), Array.get(obj, i));
            }
            return map;
        }
        return defaultValue;
    }

    /**
     * Convert object to expected Enum.
     * The input value supports,
     * 1. The enum itself
     * 2. Name of the enum (String)
     * 3. Ordinal of the enum (Number in the range)
     *
     * @param clazz Expected Type
     * @param obj Object
     * @param defaultValue Default Value
     * @param <T> Template
     * @return The right enum
     */
    public static <T extends Enum> T getEnum(Class<T> clazz, Object obj, T defaultValue) {
        if (clazz.isInstance(obj)) {
            return (T) obj;
        }
        else if (obj instanceof String) {
            try {
                return (T)Enum.valueOf(clazz, ((String)obj));
            } catch (Exception ex) {
            }
        }
        else if (obj instanceof Number) {
            int v = ((Number)obj).intValue();
            T[] values = clazz.getEnumConstants();
            if (v >= 0 && v < values.length) {
                return values[v];
            }
        }
        return defaultValue;
    }

    public static List<String> getStringList(Object object)
    {
        if (object == null) {
            return null;
        }

        String[] result;
        if (object instanceof List) {
            List<String> orig = (List<String>)object;
            List<String> list = new ArrayList<>(orig.size());
            for(int i = 0; i < orig.size(); i ++) {
                list.add(String.valueOf(orig.get(i)));
            }
            return list;
        }
        else if (object instanceof String[]) {
            result = (String[])object;
            List<String> list = new ArrayList<>(result.length);
            Collections.addAll(list, result);
            return list;
        }
        else if (object instanceof String) {
            String str = (String)object;
            str = str.trim();

            result = toStrings(str, ',');
            List<String> list = new ArrayList<>(result.length);
            Collections.addAll(list, result);
            return list;
        }
        else if (object instanceof Object[]) {
            Object[] array = (Object[])object;
            List<String> list = new ArrayList<>(array.length);
            for (Object anArray : array) {
                list.add(getString(anArray, null));
            }
            return list;
        }
        else if (object.getClass().isArray()) {
            int len = Array.getLength(object);
            List<String> list = new ArrayList<>(len);
            for (int i = 0; i < len; i++) {
                list.add(getString(Array.get(object, i), null));
            }
            return list;
        }
        else if (object instanceof Number
                || object instanceof Boolean
                || object instanceof Character) {
            return Collections.singletonList(object.toString());
        }
        else {
            return null;
        }
    }

    public static final String[] EMPTY_STRING_ARRAY = new String[0];

    public static final int[] EMPTY_INT_ARRAY = new int[0];

    public static final String EMPTY_STRING = "";

    /**
     * Return the count of the char in the String
     * if src == null return -1;
     * if src.length() == 0 return 0;
     */
    public static int countChar(String src, char c) {
        if (src == null) {
            return -1;
        }
        int length = src.length();
        if (length == 0) {
            return 0;
        }

        int count = 0;
        int ch;
        for (int i = 0; i < length; i++) {
            ch = src.charAt(i);
            if (ch == c) {
                count++;
            }
        }
        return count;
    }

    /**
     * Split string to array, no escape
     * It is faster than String.split
     *
     * @param src String
     * @param sep character
     * @return If<code>src == null</code> returns<code>null</code>
     *         If<code>src == ""</code> returns<code>#EMPTY_STRING_ARRAY</code>
     */
    public static String[] toStrings(String src, char sep) {
        if (src == null) {
            return null;
        }
        int length = src.length();
        if (length == 0) {
            return EMPTY_STRING_ARRAY;
        }

        int count = countChar(src, sep);
        String[] array = new String[count + 1];
        int begin = 0;
        int end;
        for (int i = 0; i <= count; i++) {
            end = src.indexOf(sep, begin);
            if (end == -1) {
                array[i] = begin == length ? "" : src.substring(begin);
                break;
            }
            else {
                array[i] = src.substring(begin, end);
                begin = end + 1;
            }
        }
        return array;
    }


    /**
     * Separate the string to array
     * if src == null return null;
     * ""     --> {0}
     * ",1,2" --> {0, 1, 2}
     * "1,,2," --> {1,0,2,0}
     * "a,1,2," --> null (Exception)
     */
    public static int[] toInts(String src, char sep) {
        if (src == null) {
            return null;
        }
        int length = src.length();
        if (length == 0) {
            return EMPTY_INT_ARRAY;
        }

        int count = countChar(src, sep);
        int[] array = new int[count + 1];
        int i = 0;  //index of array
        int value = 0;
        boolean neg = false;
        int index = 0; //index of src
        char c;
        while (index < length) {
            c = src.charAt(index);
            if (c >= '0' && c <= '9') {
                value = 10 * value + (c - '0');
            }
            else if (c == sep) {
                array[i++] = neg ? -value : value;
                value = 0;
                neg = false;
            }
            else if (c == '-') {
                if (value == 0) {
                    neg = true;
                }
                else {
                    //Format error
                    return null;
                }
            }
            else if (c == ' ' || c == '\t') {
                index++;
                continue;
            }
            else {
                return null;
            }
            index++;
        }
        array[i] = neg ? -value : value;
        return array;
    }

    private static DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static Date getDate(Object obj)
    {
        return getDate(obj, format, null);
    }

    public static Date getDate(Object obj, Date defValue)
    {
        return getDate(obj, format, defValue);
    }

    public static Date getDate(Object obj, DateFormat format)
    {
        return getDate(obj, format, null);
    }

    public static Date getDate(Object obj, DateFormat format, Date defValue)
    {
        if (obj instanceof Date) {
            return (Date)obj;
        }
        else {
            if (obj instanceof Timestamp) {
                Timestamp timestamp = (Timestamp)obj;
                return new Date(timestamp.getTime()
                        + timestamp.getNanos() / 1000000);
            }
            else if (obj instanceof Long) {
                return new Date(((Long)obj).longValue());
            }
            else if (obj instanceof String) {
                synchronized (format) {
                    try {
                        return format.parse((String)obj);
                    }
                    catch (Exception e) {
                        return defValue;
                    }
                }
            }
            else if (obj instanceof SimpleDate) {
                return ((SimpleDate)obj).getDate();
            }
            return defValue;
        }
    }

    public static Timestamp getTimestamp(Object obj)
    {
        return getTimestamp(obj, null);
    }

    public static Timestamp getTimestamp(Object obj, Timestamp defValue)
    {
        if (obj instanceof Timestamp) {
            return (Timestamp)obj;
        }
        else if (obj instanceof Date) {
            return new Timestamp(((Date)obj).getTime());
        }
        else if (obj instanceof Long) {
            return new Timestamp(((Long)obj).longValue());
        }
        else if (obj instanceof String) {
            try {
                return Timestamp.valueOf((String)obj);
            }
            catch (Exception e) {
                return defValue;
            }
        }
        else if (obj instanceof SimpleDate) {
            Date date = ((SimpleDate)obj).getDate();
            return new Timestamp(date.getTime());
        }
        return defValue;
    }

    public static Time getTime(Object obj)
    {
        return getTime(obj, null);
    }

    public static Time getTime(Object obj, Time defValue)
    {
        if (obj instanceof Time) {
            return (Time)obj;
        }
        else if (obj instanceof Date) {
            return new Time(((Date)obj).getTime());
        }
        else if (obj instanceof Long) {
            return new Time(((Long)obj).longValue());
        }
        else if (obj instanceof String) {
            try {
                return Time.valueOf((String)obj);
            }
            catch (Exception e) {
                return defValue;
            }
        }
        else if (obj instanceof SimpleTime) {
            SimpleTime time = (SimpleTime)obj;
            Calendar calendar = Calendar.getInstance();
            time.copyTo(calendar);
            return new Time(calendar.getTime().getTime());
        }
        return defValue;
    }

    public static short[] getShorts(Object object, short[] defValue)
    {
        if (object == null) {
            return defValue;
        }

        short[] result = null;
        if (object instanceof short[]) {
            result = (short[])object;
        }
        else if (object instanceof String) {
            String[] array = toStrings((String)object, ',');
            result = new short[array.length];
            for (int i = 0; i < array.length; i++) {
                result[i] = getShort(array[i], (short)0);
            }
        }
        else if (object instanceof Number) {
            result = new short[]{getShort(object, (short)0)};
        }
        else if (object instanceof Object[]) {
            Object[] array = (Object[])object;
            result = new short[array.length];
            for (int i = 0; i < array.length; i++) {
                result[i] = getShort(array[i], (short)0);
            }
        }
        else if (object.getClass().isArray()) {
            int len = Array.getLength(object);
            result = new short[len];
            for (int i = 0; i < len; i++) {
                result[i] = getShort(Array.get(object, i), (short)0);
            }
        }
        else if (object instanceof Boolean) {
            result = new short[]{getShort(object, (short)0)};
        }
        else {
            result = defValue;
        }
        return result;
    }

    public static short[] getShorts(Object object)
    {
        return getShorts(object, null);
    }

    public static long[] getLongs(Object object, long[] defValue)
    {
        if (object == null) {
            return defValue;
        }

        long[] result = null;
        if (object instanceof long[]) {
            result = (long[])object;
        }
        else if (object instanceof String) {
            String[] array = toStrings((String)object, ',');
            result = new long[array.length];
            for (int i = 0; i < array.length; i++) {
                result[i] = getLong(array[i], 0);
            }
        }
        else if (object instanceof Number) {
            result = new long[]{getLong(object, 0)};
        }
        else if (object instanceof Object[]) {
            Object[] array = (Object[])object;
            result = new long[array.length];
            for (int i = 0; i < array.length; i++) {
                result[i] = getLong(array[i], 0);
            }
        }
        else if (object.getClass().isArray()) {
            int len = Array.getLength(object);
            result = new long[len];
            for (int i = 0; i < len; i++) {
                result[i] = getLong(Array.get(object, i), 0l);
            }
        }
        else if (object instanceof Boolean) {
            result = new long[]{getLong(object, 0)};
        }
        else {
            result = defValue;
        }
        return result;
    }

    public static long[] getLongs(Object object)
    {
        return getLongs(object, null);
    }

    public static float[] getFloats(Object object, float[] defValue)
    {
        if (object == null) {
            return defValue;
        }

        float[] result = null;
        if (object instanceof float[]) {
            result = (float[])object;
        }
        else if (object instanceof String) {
            String[] array = toStrings((String)object, ',');
            result = new float[array.length];
            for (int i = 0; i < array.length; i++) {
                result[i] = getFloat(array[i], 0.0f);
            }
        }
        else if (object instanceof Number) {
            result = new float[]{getFloat(object, 0.0f)};
        }
        else if (object instanceof Object[]) {
            Object[] array = (Object[])object;
            result = new float[array.length];
            for (int i = 0; i < array.length; i++) {
                result[i] = getFloat(array[i], 0.0f);
            }
        }
        else if (object.getClass().isArray()) {
            int len = Array.getLength(object);
            result = new float[len];
            for (int i = 0; i < len; i++) {
                result[i] = getFloat(Array.get(object, i), 0.0f);
            }
        }
        else if (object instanceof Boolean) {
            result = new float[]{getFloat(object, 0.0f)};
        }
        else {
            result = defValue;
        }
        return result;
    }

    public static float[] getFloats(Object object)
    {
        return getFloats(object, null);
    }

    public static double[] getDoubles(Object object, double[] defValue)
    {
        if (object == null) {
            return defValue;
        }

        double[] result = null;
        if (object instanceof double[]) {
            result = (double[])object;
        }
        else if (object instanceof String) {
            String[] array = toStrings((String)object, ',');
            result = new double[array.length];
            for (int i = 0; i < array.length; i++) {
                result[i] = getDouble(array[i], 0.0d);
            }
        }
        else if (object instanceof Number) {
            result = new double[]{getDouble(object, 0.0d)};
        }
        else if (object instanceof Object[]) {
            Object[] array = (Object[])object;
            result = new double[array.length];
            for (int i = 0; i < array.length; i++) {
                result[i] = getDouble(array[i], 0.0d);
            }
        }
        else if (object.getClass().isArray()) {
            int len = Array.getLength(object);
            result = new double[len];
            for (int i = 0; i < len; i++) {
                result[i] = getDouble(Array.get(object, i), 0.0d);
            }
        }
        else if (object instanceof Boolean) {
            result = new double[]{getDouble(object, 0.0d)};
        }
        else {
            result = defValue;
        }
        return result;
    }

    public static double[] getDoubles(Object object)
    {
        return getDoubles(object, null);
    }
}
