package org.femtoframework.util;

import static org.femtoframework.util.DataUtil.EMPTY_STRING;

/**
 * String Util
 *
 * @author Sheldon Shao
 * @version 1.0
 */
public interface StringUtil {
    /**
     * Check if the string value is valid
     *
     * @param value
     */
    static boolean isValid(String value) {
        return value != null && !value.isEmpty();
    }

    /**
     * Check if the string value is invalid
     *
     * @param value
     */
    static boolean isInvalid(String value) {
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
    static boolean equals(String str1, String str2) {
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
    static boolean equalsIgnoreCase(String str1,
                                           String str2) {
        if (str1 == null) {
            return str2 == null;
        }
        return str1.equalsIgnoreCase(str2);
    }

    /**
     * Join objects to String
     *
     * @param array Object Array
     * @param sep Separator
     * @return String
     */
    static String toString(Object[] array, char sep) {
        if (array == null) {
            return null;
        }
        return toString(array, 0, array.length, sep);
    }

    /**
     * Join objects to String
     *
     * @param array Object Array
     * @param off Offset
     * @param len Length of Array
     * @param sep Separator
     * @return String
     */
    static String toString(Object[] array, int off, int len, char sep) {
        if (array == null) {
            return null;
        }

        if (off < 0) {
            throw new ArrayIndexOutOfBoundsException("Off=" + off);
        }
        if (len < 0) {
            throw new ArrayIndexOutOfBoundsException("Len=" + len);
        }

        int end = off + len;
        if (end > array.length) {
            throw new ArrayIndexOutOfBoundsException("Invalid off=" + off + " or len=" + len
                    + " array length=" + array.length);
        }

        if (len == 0) {
            return EMPTY_STRING;
        }
        else if (len == 1) {
            return String.valueOf(array[off]);
        }
        else {
            StringBuilder sb = new StringBuilder();
            sb.append(array[off]);
            for (int i = off + 1; i < end; i++) {
                sb.append(sep);
                sb.append(array[i]);
            }
            return sb.toString();
        }
    }

    /**
     * Checks whether the String contains only digit characters.
     * Null and blank string will return false.
     *
     * @param str the string to check
     * @return boolean contains only unicode numeric
     */
    static boolean isDigits(String str) {
        if (isInvalid(str)) {
            return false;
        }
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}
