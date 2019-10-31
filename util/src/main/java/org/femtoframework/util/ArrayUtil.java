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

import org.femtoframework.lang.Octet;

import java.lang.reflect.Array;
import java.util.*;

/**
 * Array Utility
 *
 * @author fengyun
 * @version 1.00 2010-2-5 20:44:57
 */
public interface ArrayUtil
{
    /**
     * 索引检查
     *
     * @param off   数组起始偏指
     * @param len   有效访问长度
     * @param index 索引
     * @throws ArrayIndexOutOfBoundsException <code>index<off||index>=off+len</code>
     */
    static void indexCheck(int off, int len, int index)
    {
        if (index < off || index >= off + len) {
            throw new ArrayIndexOutOfBoundsException("Index out of bound:" + index);
        }
    }

    /**
     * Not Found
     */
    int NOT_FOUND = -1;

    /**
     * Check whether byte array A has target bytes
     *
     * @param source Source array
     * @param target Target bytes to be checked
     * @return the index if found, otherwise it returns <code>#NOT_FOUND</code>
     */
    static int indexOf(byte[] source, byte[] target)
    {
        return indexOf(source, 0, source.length, target);
    }

    /**
     * Check whether byte array A has target bytes
     *
     * @param source Source array
     * @param target Target bytes to be checked
     * @return the index if found, otherwise it returns <code>#NOT_FOUND</code>
     */
    static int indexOf(byte[] source, int from, int to, byte[] target)
    {
        int sourceCount = to - from;
        int targetCount = target.length;
        if (from >= sourceCount) {
            return (targetCount == 0 ? sourceCount : -1);
        }
        if (from < 0) {
            from = 0;
        }
        if (targetCount == 0) {
            return from;
        }

        byte first = target[0];
        int i = from;
        int max = sourceCount - targetCount + i;

        startSearchForFirstChar:
        while (true) {
            /* Look for first character. */
            while (i <= max && source[i] != first) {
                i++;
            }
            if (i > max) {
                return NOT_FOUND;
            }

            /* Found first character, now look at the rest of v2 */
            int j = i + 1;
            int end = j + targetCount - 1;
            int k = 1;
            while (j < end) {
                if (source[j++] != target[k++]) {
                    i++;
                    /* Look for str's first char again. */
                    continue startSearchForFirstChar;
                }
            }
            return i;    /* Found whole string. */
        }
    }

    /**
     * a != null && a.length > 0
     *
     * @param a Array
     */
    static boolean isValid(Object[] a)
    {
        return a != null && a.length > 0;
    }

    /**
     * a == null || a.length == 0
     *
     * @param a Array
     */
    static boolean isInvalid(Object[] a)
    {
        return a == null || a.length == 0;
    }

    /**
     * Extends one more space and adds one more element
     *
     * @param array Array
     * @param obj   Object
     * @return New Array
     */
    static <O> O[] add(O[] array, O obj)
    {
        O[] newArray = (O[])Arrays.copyOf(array, array.length + 1, array.getClass());
        newArray[array.length] = obj;
        return newArray;
    }

    /**
     * Extends the array and add the element in specific position
     *
     * @param array Array
     * @param obj
     * @param pos  [0, array.length]
     * @return New Array
     */
    static <O> O[] add(O[] array, int pos, O obj)
    {
        int len = array.length;
        if (pos < 0 || pos > len) {
            throw new IndexOutOfBoundsException("Invalid pos:" + pos + " length:" + len);
        }
        Class clazz = array.getClass().getComponentType();
        O[] newArray = (O[])Array.newInstance(clazz, len + 1);
        if (pos > 0) {
            System.arraycopy(array, 0, newArray, 0, pos);
        }
        int c = len - pos;
        if (c > 0) {
            System.arraycopy(array, pos, newArray, pos + 1, c);
        }
        newArray[pos] = obj;
        return newArray;
    }

    /**
     * Remove the object from the array and reduce the size
     *
     * @param array
     * @param obj
     * @param <O>
     * @return
     */
    static <O> O[] remove(O[] array, O obj)
    {
        for (int i = 0; i < array.length; i++) {
            if (obj.equals(array[i])) {
                return removeNoCheck(array, i);
            }
        }
        return array;
    }

    /**
     * Remove the object from given index in the array and reduce the size
     *
     * @param array
     * @param index
     * @return
     */
    static Object[] remove(Object[] array, int index)
    {
        if (index < 0 || index >= array.length) {
            throw new ArrayIndexOutOfBoundsException(index);
        }
        return removeNoCheck(array, index);
    }

    static <O> O[] removeNoCheck(O[] array, int index)
    {
        Class clazz = array.getClass().getComponentType();
        int size = array.length - 1;
        O[] newArray = (O[])Array.newInstance(clazz, size);
        if (index > 0) {
            System.arraycopy(array, 0, newArray, 0, index);
        }
        if (index < size) {
            System.arraycopy(array, index + 1, newArray, index, size - index);
        }
        return newArray;
    }

    //Matches
    /**
     * Matches the byte array a2 in a1
     *
     * @param a1
     * @param off1
     * @param a2
     * @param off2
     * @param length
     * @return
     */
    static boolean matches(byte[] a1, int off1,
                                  byte[] a2, int off2,
                                  int length)
    {
        if (a1 == a2) {
            return true;
        }
        if (a1 == null || a2 == null) {
            return false;
        }

        return matchesNoCheck(a1, off1, a2, off2, length, false);
    }

    /**
     * Matches the byte array a2 in a1
     *
     * @param a1
     * @param off1
     * @param a2
     * @param off2
     * @param length
     * @param ignoreCase
     * @return
     */
    static boolean matches(byte[] a1, int off1,
                                  byte[] a2, int off2,
                                  int length, boolean ignoreCase)
    {
        return a1 == a2 || !(a1 == null || a2 == null) && matchesNoCheck(a1, off1, a2, off2, length, ignoreCase);
    }

    static boolean matchesNoCheck(byte[] a1, int off1,
                                  byte[] a2, int off2,
                                  int length,
                                  boolean ignoreCase)
    {
        int i = off1, max = off1 + length;
        int j = off2;
        if (ignoreCase) {
            for (; i < max; i++, j++) {
                if (Octet.toUpperCase(a1[i]) != Octet.toUpperCase(a2[j])) {
                    return false;
                }
            }
        }
        else {
            for (; i < max; i++, j++) {
                if (a1[i] != a2[j]) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 判断两个数组中的数组大小和每个元素是否相等<br>
     *
     * @param a1     数组1
     * @param off1   起始位置1
     * @param a2     数组2
     * @param off2   起始位置2
     * @param length 比较长度
     * @return <tt>true</tt> 是否匹配
     */
    static boolean matches(Object[] a1, int off1,
                                  Object[] a2, int off2,
                                  int length)
    {
        return a1 == a2 || !(a1 == null || a2 == null) && matchesNoCheck(a1, off1, a2, off2, length);
    }

    /**
     * 判断两个数组中的数组大小和每个元素是否相等<br>
     *
     * @param a1     数组1
     * @param off1   起始位置1
     * @param a2     数组2
     * @param off2   起始位置2
     * @param length 比较长度
     * @return <tt>true</tt> 是否匹配
     */
    static boolean matchesNoCheck(Object[] a1, int off1,
                                    Object[] a2, int off2,
                                    int length)
    {
        int i = off1, max = off1 + length;
        int j = off2;
        Object obj1, obj2;
        for (; i < max; i++, j++) {
            obj1 = a1[i];
            obj2 = a2[j];
            if (!(obj1 == null ? obj2 == null : obj1.equals(obj2))) {
                return false;
            }
        }
        return true;
    }

    // Search

    /**
     * Search given value in the array and address the index
     *
     * @param a Array
     * @param key Key
     * @return the index or NOT_FOUND
     */
    static int search(long[] a, long key)
    {
        return search(a, 0, a.length, key);
    }

    /**
     * Search given value in the array and address the index
     *
     * @param a Array
     * @param key Key
     * @param from From index
     * @param to To index(not include)
     * @return the index or NOT_FOUND
     */
    static int search(long[] a, int from, int to, long key)
    {
        for (int i = from; i < to; i++) {
            if (a[i] == key) {
                return i;
            }
        }
        return NOT_FOUND;
    }

    /**
     * Search given value in the array and address the index
     *
     * @param a Array
     * @param key Key
     * @return the index or NOT_FOUND
     */
    static int search(int[] a, int key)
    {
        return search(a, 0, a.length, key);
    }

    /**
     * Search given value in the array and address the index
     *
     * @param a Array
     * @param key Key
     * @param from From index
     * @param to To index(not include)
     * @return the index or NOT_FOUND
     */
    static int search(int[] a, int from, int to, int key)
    {
        for (int i = from; i < to; i++) {
            if (a[i] == key) {
                return i;
            }
        }
        return NOT_FOUND;
    }

    /**
     * Search given value in the array and address the index
     *
     * @param a Array
     * @param key Key
     * @return the index or NOT_FOUND
     */
    static int search(char[] a, char key)
    {
        return search(a, 0, a.length, key);
    }

    /**
     * Search given value in the array and address the index
     *
     * @param a Array
     * @param key Key
     * @param from From index
     * @param to To index(not include)
     * @return the index or NOT_FOUND
     */
    static int search(char[] a, int from, int to, char key)
    {
        for (int i = from; i < to; i++) {
            if (a[i] == key) {
                return i;
            }
        }
        return NOT_FOUND;
    }
    /**
     * Search given value in the array and address the index
     *
     * @param a Array
     * @param key Key
     * @return the index or NOT_FOUND
     */
    static int search(byte[] a, byte key)
    {
        return search(a, 0, a.length, key);
    }

    /**
     * Search given value in the array and address the index
     *
     * @param a Array
     * @param key Key
     * @param from From index
     * @param to To index(not include)
     * @return the index or NOT_FOUND
     */
    static int search(byte[] a, int from, int to, byte key)
    {
        for (int i = from; i < to; i++) {
            if (a[i] == key) {
                return i;
            }
        }
        return NOT_FOUND;
    }

    /**
     * Search given value in the array and address the index
     *
     * @param a Array
     * @param key Key
     * @return the index or NOT_FOUND
     */
    static <T> int search(T[] a, T key)
    {
        return search(a, 0, a.length, key);
    }

    /**
     * Search given value in the array and address the index
     *
     * @param a Array
     * @param key Key
     * @param from From index
     * @param to To index(not include)
     * @return the index or NOT_FOUND
     */
    static <T> int search(T[] a, int from, int to, T key)
    {
        for (int i = from; i < to; i++) {
            if (key == a[i] || key.equals(a[i])) {
                return i;
            }
        }
        return NOT_FOUND;
    }

    /**
     * Search given value in the array and address the index
     *
     * @param a Array
     * @param key Key
     * @return the index or NOT_FOUND
     */
    static <T> int search(T[] a, T key, Comparator<T> comparator)
    {
        return search(a, 0, a.length, key, comparator);
    }

    /**
     * Search given value in the array and address the index
     *
     * @param a Array
     * @param key Key
     * @param from From index
     * @param to To index(not include)
     * @return the index or NOT_FOUND
     */
    static <T> int search(T[] a, int from, int to, T key, Comparator<T> comparator)
    {
        for (int i = from; i < to; i++) {
            if (comparator.compare(key, a[i]) == 0) {
                return i;
            }
        }
        return NOT_FOUND;
    }

    /**
     * 对象数组到Iterator的转换
     *
     * @param array 对象数组
     */
    static <T> Enumeration<T> enumerate(T... array)
    {
        if (isInvalid(array)) {
            return Collections.emptyEnumeration();
        }
        else {
            return new ArrayIterator<T>(array);
        }
    }

    /**
     * 对象数组到Iterator的转换
     *
     * @param array 对象数组
     * @param off   起始偏值
     * @param len   需要被枚举的数组长度
     *              Iterator的转换
     */
    static <T> Enumeration<T> enumerate(T[] array, int off, int len)
    {
        if (isInvalid(array) || len == 0 || off > len) {
            return Collections.emptyEnumeration();
        }
        else {
            return new ArrayIterator<T>(array, off, len);
        }
    }

    /**
     * Returns a string representation of the contents of the specified array.
     * The string representation consists of a list of the array's elements,
     * enclosed in square brackets (<tt>"[]"</tt>).  Adjacent elements are
     * separated by the characters <tt>", "</tt> (a comma followed by a
     * space).  Elements are converted to strings as by
     * <tt>String.valueOf(long)</tt>.  Returns <tt>"null"</tt> if <tt>a</tt>
     * is <tt>null</tt>.
     *
     * @param a the array whose string representation to return
     *          a string representation of <tt>a</tt>
     * @since 1.5
     */
    public static String toString(long[] a)
    {
        if (a == null) {
            return "null";
        }
        if (a.length == 0) {
            return "[]";
        }

        StringBuilder buf = new StringBuilder();
        buf.append('[');
        buf.append(a[0]);

        for (int i = 1; i < a.length; i++) {
            buf.append(", ");
            buf.append(a[i]);
        }

        buf.append("]");
        return buf.toString();
    }

    /**
     * Returns a string representation of the contents of the specified array.
     * The string representation consists of a list of the array's elements,
     * enclosed in square brackets (<tt>"[]"</tt>).  Adjacent elements are
     * separated by the characters <tt>", "</tt> (a comma followed by a
     * space).  Elements are converted to strings as by
     * <tt>String.valueOf(int)</tt>.  Returns <tt>"null"</tt> if <tt>a</tt> is
     * <tt>null</tt>.
     *
     * @param a the array whose string representation to return
     *          a string representation of <tt>a</tt>
     * @since 1.5
     */
    public static String toString(int[] a)
    {
        if (a == null) {
            return "null";
        }
        if (a.length == 0) {
            return "[]";
        }

        StringBuilder buf = new StringBuilder();
        buf.append('[');
        buf.append(a[0]);

        for (int i = 1; i < a.length; i++) {
            buf.append(", ");
            buf.append(a[i]);
        }

        buf.append("]");
        return buf.toString();
    }

    /**
     * Returns a string representation of the contents of the specified array.
     * The string representation consists of a list of the array's elements,
     * enclosed in square brackets (<tt>"[]"</tt>).  Adjacent elements are
     * separated by the characters <tt>", "</tt> (a comma followed by a
     * space).  Elements are converted to strings as by
     * <tt>String.valueOf(short)</tt>.  Returns <tt>"null"</tt> if <tt>a</tt>
     * is <tt>null</tt>.
     *
     * @param a the array whose string representation to return
     *          a string representation of <tt>a</tt>
     * @since 1.5
     */
    public static String toString(short[] a)
    {
        if (a == null) {
            return "null";
        }
        if (a.length == 0) {
            return "[]";
        }

        StringBuilder buf = new StringBuilder();
        buf.append('[');
        buf.append(a[0]);

        for (int i = 1; i < a.length; i++) {
            buf.append(", ");
            buf.append(a[i]);
        }

        buf.append("]");
        return buf.toString();
    }

    /**
     * Returns a string representation of the contents of the specified array.
     * The string representation consists of a list of the array's elements,
     * enclosed in square brackets (<tt>"[]"</tt>).  Adjacent elements are
     * separated by the characters <tt>", "</tt> (a comma followed by a
     * space).  Elements are converted to strings as by
     * <tt>String.valueOf(char)</tt>.  Returns <tt>"null"</tt> if <tt>a</tt>
     * is <tt>null</tt>.
     *
     * @param a the array whose string representation to return
     *          a string representation of <tt>a</tt>
     * @since 1.5
     */
    public static String toString(char[] a)
    {
        if (a == null) {
            return "null";
        }
        if (a.length == 0) {
            return "[]";
        }

        StringBuilder buf = new StringBuilder();
        buf.append('[');
        buf.append(a[0]);

        for (int i = 1; i < a.length; i++) {
            buf.append(", ");
            buf.append(a[i]);
        }

        buf.append("]");
        return buf.toString();
    }

    /**
     * Returns a string representation of the contents of the specified array.
     * The string representation consists of a list of the array's elements,
     * enclosed in square brackets (<tt>"[]"</tt>).  Adjacent elements
     * are separated by the characters <tt>", "</tt> (a comma followed
     * by a space).  Elements are converted to strings as by
     * <tt>String.valueOf(byte)</tt>.  Returns <tt>"null"</tt> if
     * <tt>a</tt> is <tt>null</tt>.
     *
     * @param a the array whose string representation to return
     *          a string representation of <tt>a</tt>
     * @since 1.5
     */
    public static String toString(byte[] a)
    {
        if (a == null) {
            return "null";
        }
        if (a.length == 0) {
            return "[]";
        }

        StringBuilder buf = new StringBuilder();
        buf.append('[');
        buf.append(a[0]);

        for (int i = 1; i < a.length; i++) {
            buf.append(", ");
            buf.append(a[i]);
        }

        buf.append("]");
        return buf.toString();
    }

    /**
     * Returns a string representation of the contents of the specified array.
     * The string representation consists of a list of the array's elements,
     * enclosed in square brackets (<tt>"[]"</tt>).  Adjacent elements are
     * separated by the characters <tt>", "</tt> (a comma followed by a
     * space).  Elements are converted to strings as by
     * <tt>String.valueOf(boolean)</tt>.  Returns <tt>"null"</tt> if
     * <tt>a</tt> is <tt>null</tt>.
     *
     * @param a the array whose string representation to return
     *          a string representation of <tt>a</tt>
     * @since 1.5
     */
    public static String toString(boolean[] a)
    {
        if (a == null) {
            return "null";
        }
        if (a.length == 0) {
            return "[]";
        }

        StringBuilder buf = new StringBuilder();
        buf.append('[');
        buf.append(a[0]);

        for (int i = 1; i < a.length; i++) {
            buf.append(", ");
            buf.append(a[i]);
        }

        buf.append("]");
        return buf.toString();
    }

    /**
     * Returns a string representation of the contents of the specified array.
     * The string representation consists of a list of the array's elements,
     * enclosed in square brackets (<tt>"[]"</tt>).  Adjacent elements are
     * separated by the characters <tt>", "</tt> (a comma followed by a
     * space).  Elements are converted to strings as by
     * <tt>String.valueOf(float)</tt>.  Returns <tt>"null"</tt> if <tt>a</tt>
     * is <tt>null</tt>.
     *
     * @param a the array whose string representation to return
     *          a string representation of <tt>a</tt>
     * @since 1.5
     */
    public static String toString(float[] a)
    {
        if (a == null) {
            return "null";
        }
        if (a.length == 0) {
            return "[]";
        }

        StringBuilder buf = new StringBuilder();
        buf.append('[');
        buf.append(a[0]);

        for (int i = 1; i < a.length; i++) {
            buf.append(", ");
            buf.append(a[i]);
        }

        buf.append("]");
        return buf.toString();
    }

    /**
     * Returns a string representation of the contents of the specified array.
     * The string representation consists of a list of the array's elements,
     * enclosed in square brackets (<tt>"[]"</tt>).  Adjacent elements are
     * separated by the characters <tt>", "</tt> (a comma followed by a
     * space).  Elements are converted to strings as by
     * <tt>String.valueOf(double)</tt>.  Returns <tt>"null"</tt> if <tt>a</tt>
     * is <tt>null</tt>.
     *
     * @param a the array whose string representation to return
     *          a string representation of <tt>a</tt>
     * @since 1.5
     */
    public static String toString(double[] a)
    {
        if (a == null) {
            return "null";
        }
        if (a.length == 0) {
            return "[]";
        }

        StringBuilder buf = new StringBuilder();
        buf.append('[');
        buf.append(a[0]);

        for (int i = 1; i < a.length; i++) {
            buf.append(", ");
            buf.append(a[i]);
        }

        buf.append("]");
        return buf.toString();
    }

    /**
     * Returns a string representation of the contents of the specified array.
     * If the array contains other arrays as elements, they are converted to
     * strings by the {@link Object#toString} method inherited from
     * <tt>Object</tt>, which describes their <i>identities</i> rather than
     * their contents.
     * <p/>
     * <p>The value returned by this method is equal to the value that would
     * be returned by <tt>Arrays.asList(a).toString()</tt>, unless <tt>a</tt>
     * is <tt>null</tt>, in which case <tt>"null"</tt> is returned.
     *
     * @param a the array whose string representation to return
     *          a string representation of <tt>a</tt>
     * @see #deepToString(Object[])
     * @since 1.5
     */
    public static String toString(Object[] a)
    {
        if (a == null) {
            return "null";
        }
        if (a.length == 0) {
            return "[]";
        }

        StringBuilder buf = new StringBuilder();

        for (int i = 0; i < a.length; i++) {
            if (i == 0) {
                buf.append('[');
            }
            else {
                buf.append(", ");
            }

            buf.append(String.valueOf(a[i]));
        }

        buf.append("]");
        return buf.toString();
    }

    /**
     * Returns a string representation of the "deep contents" of the specified
     * array.  If the array contains other arrays as elements, the string
     * representation contains their contents and so on.  This method is
     * designed for converting multidimensional arrays to strings.
     * <p/>
     * <p>The string representation consists of a list of the array's
     * elements, enclosed in square brackets (<tt>"[]"</tt>).  Adjacent
     * elements are separated by the characters <tt>", "</tt> (a comma
     * followed  by a space).  Elements are converted to strings as by
     * <tt>String.valueOf(Object)</tt>, unless they are themselves
     * arrays.
     * <p/>
     * <p>If an element <tt>e</tt> is an array of a primitive type, it is
     * converted to a string as by invoking the appropriate overloading of
     * <tt>Arrays.toString(e)</tt>.  If an element <tt>e</tt> is an array of a
     * reference type, it is converted to a string as by invoking
     * this method recursively.
     * <p/>
     * <p>To avoid infinite recursion, if the specified array contains itself
     * as an element, or contains an indirect reference to itself through one
     * or more levels of arrays, the self-reference is converted to the string
     * <tt>"[...]"</tt>.  For example, an array containing only a reference
     * to itself would be rendered as <tt>"[[...]]"</tt>.
     * <p/>
     * <p>This method returns <tt>"null"</tt> if the specified array
     * is <tt>null</tt>.
     *
     * @param a the array whose string representation to return
     *          a string representation of <tt>a</tt>
     * @see #toString(Object[])
     * @since 1.5
     */
    public static String deepToString(Object[] a)
    {
        if (a == null) {
            return "null";
        }

        int bufLen = 20 * a.length;
        if (a.length != 0 && bufLen <= 0) {
            bufLen = Integer.MAX_VALUE;
        }
        StringBuilder buf = new StringBuilder(bufLen);
        deepToString(a, buf, new HashSet<>());
        return buf.toString();
    }

    static void deepToString(Object[] a, StringBuilder buf, Set<Object> dejaVu)
    {
        if (a == null) {
            buf.append("null");
            return;
        }
        dejaVu.add(a);
        buf.append('[');
        for (int i = 0; i < a.length; i++) {
            if (i != 0) {
                buf.append(", ");
            }

            Object element = a[i];
            if (element == null) {
                buf.append("null");
            }
            else {
                Class eClass = element.getClass();

                if (eClass.isArray()) {
                    if (eClass == byte[].class) {
                        buf.append(toString((byte[])element));
                    }
                    else if (eClass == short[].class) {
                        buf.append(toString((short[])element));
                    }
                    else if (eClass == int[].class) {
                        buf.append(toString((int[])element));
                    }
                    else if (eClass == long[].class) {
                        buf.append(toString((long[])element));
                    }
                    else if (eClass == char[].class) {
                        buf.append(toString((char[])element));
                    }
                    else if (eClass == float[].class) {
                        buf.append(toString((float[])element));
                    }
                    else if (eClass == double[].class) {
                        buf.append(toString((double[])element));
                    }
                    else if (eClass == boolean[].class) {
                        buf.append(toString((boolean[])element));
                    }
                    else { // element is an array of object references
                        if (dejaVu.contains(element)) {
                            buf.append("[...]");
                        }
                        else {
                            deepToString((Object[])element, buf, dejaVu);
                        }
                    }
                }
                else {  // element is non-null and not an array
                    buf.append(element.toString());
                }
            }
        }
        buf.append("]");
        dejaVu.remove(a);
    }
}
