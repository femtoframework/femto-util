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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

/**
 * The last version was published here.
 *
 * https://github.com/eBay/Winder/blob/master/winder-core/src/main/java/org/ebayopensource/common/util/DataUtil.java
 *
 *
 * Modified by Sheldon Shao(xshao) on 12/09/2018), include it in femtoframework/femto-util and changed license to APL v2
 * Created by xshao on 9/16/16.
 */
public class DataUtil {
    /**
     * Convert value to boolean
     *
     * @param value the object to convert from
     * @param defValue default value
     * @return the converted boolean
     */
    public static boolean getBoolean(Object value, boolean defValue)
    {
        return doGetBoolean(value, defValue);
    }

    public static Boolean doGetBoolean(Object value, Boolean defValue)
    {
        if (value == null) {
            return defValue;
        }

        boolean result;
        if (value instanceof Boolean) {
            result = ((Boolean)value);
        }
        else if (value instanceof String) {
            return convertToBoolean((String)value, defValue);
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

    public static Boolean convertToBoolean(String value, Boolean defValue)
    {
        if (TRUE_SET.contains(value)) {
            return true;
        }
        else if (FALSE_SET.contains(value)) {
            return false;
        }
        else {
            return defValue;
        }
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
            try {
                result = Integer.parseInt(str.trim(), 10);
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
            try {
                result = Long.parseLong(str.trim(), 10);
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

    public static String[] getStringArray(Object object, String[] defValue)
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

            result = toStringArray(str, ',');
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

    public static int[] getIntArray(Object object, int[] defValue)
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

            result = toStringArray(str, ',');
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
    public static String[] toStringArray(String src, char sep) {
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
    public static int[] toIntArray(String src, char sep) {
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
}
