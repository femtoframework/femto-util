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
 * Data Type definition
 *
 * @author fengyun
 * @version 1.00 2011-8-1 10:03 PM
 */
public interface DataTypes {
    String TYPE_INT = "int";
    String TYPE_BYTE = "byte";
    String TYPE_CHAR = "char";
    String TYPE_STRING = "string";
    String TYPE_INTEGER = "integer";
    String TYPE_BOOLEAN = "boolean";

    String TYPE_CHARACTER = "character";
    String TYPE_LONG = "long";
    String TYPE_FLOAT = "float";
    String TYPE_DOUBLE = "double";
    String TYPE_SHORT = "short";

    //Date
    String TYPE_DATE = "date";
    String TYPE_TIME = "time";
    String TYPE_TIMESTAMP = "timestamp";
    String TYPE_SIMPLE_DATE = "simple_date";
    String TYPE_SIMPLE_TIME = "simple_time";

    //Array
    String TYPE_INTS = "ints";
    String TYPE_INT_JAVA_ARRAY = "int[]";
    String TYPE_BYTES = "bytes";
    String TYPE_BYTE_JAVA_ARRAY = "byte[]";
    String TYPE_CHARS = "chars";
    String TYPE_CHAR_JAVA_ARRAY = "char[]";
    String TYPE_SHORTS = "shorts";
    String TYPE_SHORT_JAVA_ARRAY = "short[]";
    String TYPE_LONGS = "longs";
    String TYPE_LONG_JAVA_ARRAY = "long[]";
    String TYPE_FLOATS = "floats";
    String TYPE_FLOAT_JAVA_ARRAY = "float[]";
    String TYPE_DOUBLES = "doubles";
    String TYPE_DOUBLE_JAVA_ARRAY = "double[]";
    String TYPE_STRINGS = "strings";
    String TYPE_STRING_JAVA_ARRAY = "String[]";

    String TYPE_BOOLEANS = "booleans";
    String TYPE_BOOLEAN_JAVA_ARRAY = "boolean[]";

    String TYPE_OBJECT = "object";
    String TYPE_OBJECTS = "objects";
    String TYPE_OBJECT_JAVA_ARRAY = "Object[]";

    //InetAddress
    String TYPE_INET_ADDRESS = "inet_address";

    //Collections
    String TYPE_LIST = "list"; //ArrayList
    String TYPE_MAP = "map";  //HashMap
    String TYPE_PARAMETERS = "parameters"; //Parameters
}

