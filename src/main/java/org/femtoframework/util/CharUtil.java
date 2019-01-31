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

import org.femtoframework.util.crypto.Hex;

/**
 * Character related Utilities
 *
 * @author Sheldon Shao
 */
public interface CharUtil {

    static char toUnicodeChar(String str, int begin)
    {
        if (str == null || (str.length() - begin) < 6) {
            throw new IllegalArgumentException("Malformed \\uxxxx encoding.");
        }

        int i = begin;

        char c = str.charAt(i++);
        if (c != '\\') {
            throw new IllegalArgumentException("Malformed \\uxxxx encoding.");
        }
        c = str.charAt(i++);

        if (c != 'u' && c != 'U') {
            throw new IllegalArgumentException("Malformed \\uxxxx encoding.");
        }

        int value = 0;
        for (; i < begin + 6; i++) {
            c = str.charAt(i);
            if (c >= '0' && c <= '9') {
                value = (value << 4) + c - '0';
            }
            else if (c >= 'a' && c <= 'f') {
                value = (value << 4) + 10 + c - 'a';
            }
            else if (c >= 'A' && c <= 'F') {
                value = (value << 4) + 10 + c - 'A';
            }
            else {
                throw new IllegalArgumentException("Malformed \\uxxxx encoding.");
            }
        }
        return (char)value;
    }

    /**
     * Convert "\\uxxxx" to unicode char
     */
    static char toUnicodeChar(String str)
    {
        return toUnicodeChar(str, 0);
    }

    /**
     * Convert char to "\\uxxxx"
     */
    static String toUnicodeStr(char c)
    {
        StringBuilder sb = new StringBuilder(6);
        sb = toUnicodeStr(sb, c);
        return sb.toString();
    }

    static StringBuilder toUnicodeStr(StringBuilder sb, char c)
    {
        sb.append('\\');
        sb.append('u');
        sb.append(toHex((c >> 12) & 0xF));
        sb.append(toHex((c >> 8) & 0xF));
        sb.append(toHex((c >> 4) & 0xF));
        sb.append(toHex(c & 0xF));
        return sb;
    }

    /**
     * Convert a nibble to a hex character
     *
     * @param nibble the nibble to convert.
     */
    static char toHex(int nibble)
    {
        return Hex.HEX_CHARS[(nibble & 0xF)];
    }
}
