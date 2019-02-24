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
 * "NamingConvention" to "naming_convention", vice versa
 *
 * It is using in femto-coin
 *
 * @author Sheldon Shao
 * @version 1.0
 */
public interface NamingConvention {

    char SEP = '_';

    /**
     * Convert Java style naming to "_" separated name
     * "NamingFormat" to "naming_format"
     *
     *
     *
     * @param str Java style naming
     * @return "_" separated naming
     */
    static String format(String str)
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
    static String format(Class clazz)
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
    static String parse(String str)
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
    static String parse(String str, boolean firstUpperCase)
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
            else if (Character.isUpperCase(str.charAt(0))) {
                char[] chars = str.toCharArray();
                char ch = chars[0];
                chars[0] = Character.toLowerCase(ch);
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
            sb.append(Character.toLowerCase(ch));
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


    /**
     * Convert property name to method name
     *
     * @param name property Name
     * @param prefix prefix (get or is)
     * @return to Method Name
     */
    static String toMethodName(String name, String prefix) {
        String methodName = NamingConvention.parse(name, true);
        StringBuilder sb = new StringBuilder(name.length() + prefix.length());
        sb.append(prefix);
        sb.append(methodName);
        return sb.toString();
    }

    String GET = "get";
    String SET = "set";
    String IS = "is";

//    public static final HashSet<String> GETTER_IGNORES = new HashSet<String>();
//
//    static {
//        GETTER_IGNORES.add("getClass");
//        GETTER_IGNORES.add("get");
//        GETTER_IGNORES.add("is");
//    }

    /**
     * Convert method name to PropertyName
     *
     * @param methodName Method Name
     * @param type Type
     * @return Property Name
     */
    static String toPropertyName(String methodName, Class<?> type) {
        if (type == boolean.class || type == Boolean.class) {
            return parse(methodName.substring(2), false);
        }
        else {
            return parse(methodName.substring(3), false);
        }
    }

    /**
     * Convert propertyName to Getter Name
     *
     * @param propertyName propertyName
     */
    static String toGetter(String propertyName) {
        return toMethodName(propertyName, GET);
    }

    /**
     * Return Getter Name by propertyName+type
     *
     * @param propertyName propertyName
     * @param type
     */
    static String toGetter(String propertyName, Class<?> type) {
        if (type == Boolean.class || type == boolean.class) {
            return toMethodName(propertyName, IS);
        }
        else {
            return toMethodName(propertyName, GET);
        }
    }

    /**
     * Return Getter Name by propertyName+type
     *
     * @param propertyName propertyName
     * @param type Type in String
     */
    static String toGetter(String propertyName, String type) {
        if (Boolean.class.getName().equalsIgnoreCase(type) || boolean.class.getName().equalsIgnoreCase(type)) {
            return toMethodName(propertyName, IS);
        }
        else {
            return toMethodName(propertyName, GET);
        }
    }

    /**
     * Convert propertyName to Setter Name
     *
     * @param propertyName
     */
    static String toSetter(String propertyName) {
        return toMethodName(propertyName, SET);
    }

    /**
     * Convert propertyName to boolean style getter "isXxx"
     *
     * @param propertyName  Property Name
     */
    static String toBooleanGetter(String propertyName) {
        return toMethodName(propertyName, IS);
    }
}
