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
}
