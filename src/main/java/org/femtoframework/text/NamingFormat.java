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
package org.femtoframework.text;

/**
 * Convert Java style naming to "_" separated name, for examples,
 *
 * "NamingFormat" to "naming_format", vice versa
 *
 * It is using in femto-coin
 *
 * @author Sheldon Shao
 * @version 1.0
 */
public class NamingFormat {

    public static final char SEP = '_';

    /**
     * Convert Java style naming to "_" separated name
     * "NamingFormat" to "naming_format"
     *
     *
     *
     * @param str Java style naming
     * @return "_" separated naming
     */
    public static String format(String str)
    {
        if (str == null) {
            return null;
        }

        int len = str.length();
        if (len == 0) {
            return str;
        }

        StringBuilder sb = new StringBuilder(len + 3);
        char ch = str.charAt(0);
        sb.append(Character.toLowerCase(ch));
        for (int i = 1; i < len; i++) {
            ch = str.charAt(i);
            if (Character.isUpperCase(ch)) {
                sb.append(SEP);
                sb.append(Character.toLowerCase(ch));
            }
            else {
                sb.append(ch);
            }
        }
        return sb.toString();
    }

    /**
     * Convert Java style naming to "_" separated name
     * "NamingFormat" to "naming_format"
     *
     *
     * @param clazz Class Name
     * @return "_" separated naming
     */
    public static String format(Class clazz)
    {
        return format(clazz.getSimpleName());
    }

    /**
     * Convert "_" separated name to Java style naming
     * "naming_format" to "NamingFormat"
     *
     * @param str "_" separated name
     * @return
     */
    public static String parse(String str)
    {
        return parse(str, true);
    }

    /**
     * Convert "_" separated name to Java style naming
     * "naming_format" to "NamingFormat"
     *
     * @param str "_" separated name
     * @param firstUpperCase First Letter should be upper case or not
     * @return
     */
    public static String parse(String str, boolean firstUpperCase)
    {
        if (str == null) {
            return null;
        }

        int len = str.length();
        if (len == 0) {
            return str;
        }

        if (str.indexOf(SEP) < 0) {
            if (firstUpperCase) {
                char[] chars = str.toCharArray();
                char ch = chars[0];
                chars[0] = Character.toUpperCase(ch);
                return new String(chars);
            }
            return str;
        }

        StringBuilder sb = new StringBuilder(len + 3);
        char ch = str.charAt(0);
        if (firstUpperCase) {
            sb.append(Character.toUpperCase(ch));
        }
        else {
            sb.append(ch);
        }
        for (int i = 1; i < len; i++) {
            ch = str.charAt(i);
            if (ch == SEP) {
                if (i < len - 1) {
                    ch = str.charAt(++i);
                    sb.append(Character.toUpperCase(ch));
                }
            }
            else {
                sb.append(ch);
            }
        }
        return sb.toString();
    }
}
