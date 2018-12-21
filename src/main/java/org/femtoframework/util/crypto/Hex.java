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
package org.femtoframework.util.crypto;


import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

/**
 * Hex
 *
 * @author fengyun
 * @version 1.00 Aug 23, 2002 2:20:36 PM
 */
public class Hex {

    public static final char[] HEX_CHARS
            = {'0', '1', '2', '3', '4', '5', '6', '7',
            '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    public static final byte[] HEX_BYTES
            = {(byte)'0', (byte)'1', (byte)'2', (byte)'3',
            (byte)'4', (byte)'5', (byte)'6', (byte)'7',
            (byte)'8', (byte)'9', (byte)'A', (byte)'B',
            (byte)'C', (byte)'D', (byte)'E', (byte)'F'};

    /**
     * Table for HEX to DEC byte translation.
     */
    public static final int[] DEC = {
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            0, 1, 2, 3, 4, 5, 6, 7, 8, 9, -1, -1, -1, -1, -1, -1,
            -1, 10, 11, 12, 13, 14, 15, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, 10, 11, 12, 13, 14, 15, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    };

    /**
     * Convert hex string to String
     */
    public static String decode(String encode) {
        return decode(encode, null);
    }

    /**
     * Convert hex string to String
     */
    public static String decode(String encode, String enc) {
        if (encode == null) {
            return null;
        }
        else if (encode.length() == 0) {
            return encode;
        }

        if (enc == null) {
            return new String(decodeToBytes(encode));
        }
        else {
            try {
                return new String(decodeToBytes(encode), enc);
            }
            catch (UnsupportedEncodingException e) {
                throw new IllegalArgumentException("Unsupported Encoding:"+enc);
            }
        }
    }

    public static byte[] EMPTY_BYTE_ARRAY = new byte[0];

    public static byte[] decodeToBytes(String encode) {
        if (encode == null) {
            return null;
        }
        else if (encode.length() == 0) {
            return EMPTY_BYTE_ARRAY;
        }

        int len = encode.length();
        if (len % 2 != 0) {
            throw new IllegalArgumentException("Invalid string:" + encode);
        }

        byte[] bytes = new byte[len / 2];
        char ch;
        int k, h = 0;
        for (int i = 0, j = 0; i < len; i++) {
            ch = encode.charAt(i);
            if (ch >= '0' && ch <= '9') {
                k = (ch - '0');
            }
            else if (ch >= 'A' && ch <= 'F') {
                k = (ch - 'A') + 10;
            }
            else if (ch >= 'a' && ch <= 'f') {
                k = (ch - 'a') + 10;
            }
            else {
                throw new IllegalArgumentException("Invalid hex string:" + encode);
            }
            if (i % 2 == 0) {
                h = k;
            }
            else {
                bytes[j++] = (byte)(((h << 4) & 0xF0) | (k & 0x0F));
            }
        }
        return bytes;
    }

    /**
     * convert byte array to hex string
     */
    public static String encode(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        return encode(bytes, 0, bytes.length);
    }

    /**
     * convert byte array to hex string
     */
    public static String encode(byte[] bytes, int off, int len) {
        if (bytes == null) {
            return null;
        }

        if (len == 0) {
            return "";
        }

        StringBuilder sb = new StringBuilder(len * 2);
        int end = off + len;
        for (int i = off; i < end; i++) {
            sb.append(HEX_CHARS[(bytes[i] >> 4) & 0xF]);
            sb.append(HEX_CHARS[(bytes[i]) & 0xF]);
        }
        return sb.toString();
    }

    /**
     * convert byte array to hex string
     */
    public static String encode(char[] chars) {
        if (chars == null) {
            return null;
        }

        int len = chars.length;
        if (len == 0) {
            return "";
        }

        StringBuilder sb = new StringBuilder(len * 4);
        for (int i = 0; i < len; i++) {
            sb.append(HEX_CHARS[(chars[i] >> 12) & 0xF]);
            sb.append(HEX_CHARS[(chars[i] >> 8) & 0xF]);
            sb.append(HEX_CHARS[(chars[i] >> 4) & 0xF]);
            sb.append(HEX_CHARS[(chars[i]) & 0xF]);
        }
        return sb.toString();
    }

    //TO BYTE
    public static byte toByte(byte[] bytes) {
        return toByte(bytes, 0);
    }

    public static byte toByte(byte[] bytes, int off) {
        return (byte)toInt(bytes, off, 2);
    }

    public static byte toByte(char[] chars) {
        return toByte(chars, 0);
    }

    public static byte toByte(char[] chars, int off) {
        return (byte)toInt(chars, off, 2);
    }

    public static byte toByte(String str) {
        return toByte(str, 0);
    }

    public static byte toByte(String str, int off) {
        return (byte)toInt(str, off, 2);
    }

    //TO CHAR
    public static char toChar(byte[] bytes) {
        return toChar(bytes, 0);
    }

    public static char toChar(byte[] bytes, int off) {
        return (char)toInt(bytes, off, 4);
    }

    public static char toChar(char[] chars) {
        return toChar(chars, 0);
    }

    public static char toChar(char[] chars, int off) {
        return (char)toInt(chars, off, 4);
    }

    public static char toChar(String str) {
        return toChar(str, 0);
    }

    public static char toChar(String str, int off) {
        return (char)toInt(str, off, 4);
    }

    //TO INT
    public static int toInt(byte[] bytes) {
        return toInt(bytes, 0);
    }

    public static int toInt(byte[] bytes, int off) {
        return toInt(bytes, off, 8);
    }

    public static int toInt(char[] chars) {
        return toInt(chars, 0);
    }

    public static int toInt(char[] chars, int off) {
        return toInt(chars, off, 8);
    }

    public static int toInt(String str) {
        return toInt(str, 0);
    }

    public static int toInt(String str, int off) {
        return toInt(str, off, 8);
    }


    //TO LONG
    public static long toLong(byte[] bytes) {
        return toLong(bytes, 0);
    }

    public static long toLong(byte[] bytes, int off) {
        long l = toInt(bytes, off, 8) & 0xFFFFFFFFL;
        return l << 32 | (toInt(bytes, off + 8, 8) & 0xFFFFFFFFL);
    }

    public static long toLong(char[] chars) {
        return toLong(chars, 0);
    }

    public static long toLong(char[] chars, int off) {
        long l = toInt(chars, off, 8) & 0xFFFFFFFFL;
        return l << 32 | (toInt(chars, off + 8, 8) & 0xFFFFFFFFL);
    }

    public static long toLong(String str) {
        return toLong(str, 0);
    }

    public static long toLong(String str, int off) {
        long l = toInt(str, off, 8) & 0xFFFFFFFFL;
        return l << 32 | (toInt(str, off + 8, 8) & 0xFFFFFFFFL);
    }

    public static int toInt(byte[] bytes, int off, int len) {
        int r = 0;
        int ch;
        int end = off + len;
        for (int i = off; i < end; i++) {
            ch = bytes[i];
            if (ch >= '0' && ch <= '9') {
                ch = (ch - '0');
            }
            else if (ch >= 'A' && ch <= 'F') {
                ch = (ch - 'A') + 10;
            }
            else if (ch >= 'a' && ch <= 'f') {
                ch = (ch - 'a') + 10;
            }
            else {
                throw new IllegalArgumentException("Invalid hex string:"
                        + new String(bytes, off, end - off));
            }
            r <<= 4;
            r |= ch;
        }
        return r;
    }

    public static int toInt(char[] chars, int off, int len) {
        int r = 0;
        int ch;
        int end = off + len;
        for (int i = off; i < end; i++) {
            ch = chars[i];
            if (ch >= '0' && ch <= '9') {
                ch = (ch - '0');
            }
            else if (ch >= 'A' && ch <= 'F') {
                ch = (ch - 'A') + 10;
            }
            else if (ch >= 'a' && ch <= 'f') {
                ch = (ch - 'a') + 10;
            }
            else {
                throw new IllegalArgumentException("Invalid hex string:"
                        + new String(chars, off, end - off));
            }
            r <<= 4;
            r |= ch;
        }
        return r;
    }

    public static int toInt(String str, int off, int len) {
        int r = 0;
        int ch;
        int end = off + len;
        for (int i = off; i < end; i++) {
            ch = str.charAt(i);
            if (ch >= '0' && ch <= '9') {
                ch = (ch - '0');
            }
            else if (ch >= 'A' && ch <= 'F') {
                ch = (ch - 'A') + 10;
            }
            else if (ch >= 'a' && ch <= 'f') {
                ch = (ch - 'a') + 10;
            }
            else {
                throw new IllegalArgumentException("Invalid hex string:" + str);
            }
            r <<= 4;
            r |= ch;
        }
        return r;
    }

    public static byte[] toBytes(byte b) {
        byte[] bytes = new byte[2];
        bytes[0] = HEX_BYTES[(b >> 4) & 0xF];
        bytes[1] = HEX_BYTES[b & 0xF];
        return bytes;
    }

    public static byte[] toBytes(char c) {
        byte[] bytes = new byte[4];
        bytes[0] = HEX_BYTES[(c >> 12) & 0xF];
        bytes[1] = HEX_BYTES[(c >> 8) & 0xF];
        bytes[2] = HEX_BYTES[(c >> 4) & 0xF];
        bytes[3] = HEX_BYTES[c & 0xF];
        return bytes;
    }

    public static byte[] toBytes(short s) {
        byte[] bytes = new byte[4];
        bytes[0] = HEX_BYTES[(s >> 12) & 0xF];
        bytes[1] = HEX_BYTES[(s >> 8) & 0xF];
        bytes[2] = HEX_BYTES[(s >> 4) & 0xF];
        bytes[3] = HEX_BYTES[s & 0xF];
        return bytes;
    }

    public static byte[] toBytes(int i) {
        byte[] bytes = new byte[8];
        bytes[0] = HEX_BYTES[(i >> 28) & 0xF];
        bytes[1] = HEX_BYTES[(i >> 24) & 0xF];
        bytes[2] = HEX_BYTES[(i >> 20) & 0xF];
        bytes[3] = HEX_BYTES[(i >> 16) & 0xF];
        bytes[4] = HEX_BYTES[(i >> 12) & 0xF];
        bytes[5] = HEX_BYTES[(i >> 8) & 0xF];
        bytes[6] = HEX_BYTES[(i >> 4) & 0xF];
        bytes[7] = HEX_BYTES[i & 0xF];
        return bytes;
    }

    public static byte[] toBytes(long l) {
        //不用循环要快
        byte[] bytes = new byte[16];
        int h = (int)((l >> 32) & 0xFFFFFFFFL);
        int i = (int)(l & 0xFFFFFFFFL);

        bytes[0] = HEX_BYTES[(h >> 28) & 0xF];
        bytes[1] = HEX_BYTES[(h >> 24) & 0xF];
        bytes[2] = HEX_BYTES[(h >> 20) & 0xF];
        bytes[3] = HEX_BYTES[(h >> 16) & 0xF];
        bytes[4] = HEX_BYTES[(h >> 12) & 0xF];
        bytes[5] = HEX_BYTES[(h >> 8) & 0xF];
        bytes[6] = HEX_BYTES[(h >> 4) & 0xF];
        bytes[7] = HEX_BYTES[h & 0xF];

        bytes[8] = HEX_BYTES[(i >> 28) & 0xF];
        bytes[9] = HEX_BYTES[(i >> 24) & 0xF];
        bytes[10] = HEX_BYTES[(i >> 20) & 0xF];
        bytes[11] = HEX_BYTES[(i >> 16) & 0xF];
        bytes[12] = HEX_BYTES[(i >> 12) & 0xF];
        bytes[13] = HEX_BYTES[(i >> 8) & 0xF];
        bytes[14] = HEX_BYTES[(i >> 4) & 0xF];
        bytes[15] = HEX_BYTES[i & 0xF];
        return bytes;
    }

    public static byte[] toBytes(float f) {
        return toBytes(Float.floatToRawIntBits(f));
    }

    public static byte[] toBytes(double d) {
        return toBytes(Double.doubleToRawLongBits(d));
    }

    public static char[] toChars(byte b) {
        char[] chars = new char[2];
        chars[0] = HEX_CHARS[(b >> 4) & 0xF];
        chars[1] = HEX_CHARS[b & 0xF];
        return chars;
    }

    public static char[] toChars(char c) {
        char[] chars = new char[4];
        chars[0] = HEX_CHARS[(c >> 12) & 0xF];
        chars[1] = HEX_CHARS[(c >> 8) & 0xF];
        chars[2] = HEX_CHARS[(c >> 4) & 0xF];
        chars[3] = HEX_CHARS[c & 0xF];
        return chars;
    }

    public static char[] toChars(short s) {
        char[] chars = new char[4];
        chars[0] = HEX_CHARS[(s >> 12) & 0xF];
        chars[1] = HEX_CHARS[(s >> 8) & 0xF];
        chars[2] = HEX_CHARS[(s >> 4) & 0xF];
        chars[3] = HEX_CHARS[s & 0xF];
        return chars;
    }

    public static char[] toChars(int i) {
        char[] chars = new char[8];
        chars[0] = HEX_CHARS[(i >> 28) & 0xF];
        chars[1] = HEX_CHARS[(i >> 24) & 0xF];
        chars[2] = HEX_CHARS[(i >> 20) & 0xF];
        chars[3] = HEX_CHARS[(i >> 16) & 0xF];
        chars[4] = HEX_CHARS[(i >> 12) & 0xF];
        chars[5] = HEX_CHARS[(i >> 8) & 0xF];
        chars[6] = HEX_CHARS[(i >> 4) & 0xF];
        chars[7] = HEX_CHARS[i & 0xF];
        return chars;
    }

    public static char[] toChars(long l) {
        //不用循环要快
        char[] chars = new char[16];
        int h = (int)((l >> 32) & 0xFFFFFFFFL);
        int i = (int)(l & 0xFFFFFFFFL);

        chars[0] = HEX_CHARS[(h >> 28) & 0xF];
        chars[1] = HEX_CHARS[(h >> 24) & 0xF];
        chars[2] = HEX_CHARS[(h >> 20) & 0xF];
        chars[3] = HEX_CHARS[(h >> 16) & 0xF];
        chars[4] = HEX_CHARS[(h >> 12) & 0xF];
        chars[5] = HEX_CHARS[(h >> 8) & 0xF];
        chars[6] = HEX_CHARS[(h >> 4) & 0xF];
        chars[7] = HEX_CHARS[h & 0xF];

        chars[8] = HEX_CHARS[(i >> 28) & 0xF];
        chars[9] = HEX_CHARS[(i >> 24) & 0xF];
        chars[10] = HEX_CHARS[(i >> 20) & 0xF];
        chars[11] = HEX_CHARS[(i >> 16) & 0xF];
        chars[12] = HEX_CHARS[(i >> 12) & 0xF];
        chars[13] = HEX_CHARS[(i >> 8) & 0xF];
        chars[14] = HEX_CHARS[(i >> 4) & 0xF];
        chars[15] = HEX_CHARS[i & 0xF];
        return chars;
    }

    public static char[] toChars(float f) {
        return toChars(Float.floatToRawIntBits(f));
    }

    public static char[] toChars(double d) {
        return toChars(Double.doubleToRawLongBits(d));
    }

    public static String toString(byte b) {
        return new String(toChars(b));
    }

    public static String toString(char c) {
        return new String(toChars(c));
    }

    public static String toString(short s) {
        return new String(toChars(s));
    }

    public static String toString(int i) {
        return new String(toChars(i));
    }

    public static String toString(long l) {
        return new String(toChars(l));
    }

    public static String toString(float f) {
        return new String(toChars(f));
    }

    public static String toString(double d) {
        return new String(toChars(d));
    }

    public static StringBuilder append(StringBuilder sb, byte b) {
        sb.append(HEX_CHARS[(b >> 4) & 0xF]);
        sb.append(HEX_CHARS[b & 0xF]);
        return sb;
    }

    public static StringBuilder append(StringBuilder sb, char c) {
        sb.append(HEX_CHARS[(c >> 12) & 0xF]);
        sb.append(HEX_CHARS[(c >> 8) & 0xF]);
        sb.append(HEX_CHARS[(c >> 4) & 0xF]);
        sb.append(HEX_CHARS[c & 0xF]);
        return sb;
    }

    public static StringBuilder append(StringBuilder sb, short s) {
        sb.append(HEX_CHARS[(s >> 12) & 0xF]);
        sb.append(HEX_CHARS[(s >> 8) & 0xF]);
        sb.append(HEX_CHARS[(s >> 4) & 0xF]);
        sb.append(HEX_CHARS[s & 0xF]);
        return sb;
    }

    public static StringBuilder append(StringBuilder sb, int i) {
        sb.append(HEX_CHARS[(i >> 28) & 0xF]);
        sb.append(HEX_CHARS[(i >> 24) & 0xF]);
        sb.append(HEX_CHARS[(i >> 20) & 0xF]);
        sb.append(HEX_CHARS[(i >> 16) & 0xF]);
        sb.append(HEX_CHARS[(i >> 12) & 0xF]);
        sb.append(HEX_CHARS[(i >> 8) & 0xF]);
        sb.append(HEX_CHARS[(i >> 4) & 0xF]);
        sb.append(HEX_CHARS[i & 0xF]);
        return sb;
    }

    public static StringBuilder append(StringBuilder sb, long l) {
        int h = (int)((l >> 32) & 0xFFFFFFFFL);
        int i = (int)(l & 0xFFFFFFFFL);

        sb.append(HEX_CHARS[(h >> 28) & 0xF]);
        sb.append(HEX_CHARS[(h >> 24) & 0xF]);
        sb.append(HEX_CHARS[(h >> 20) & 0xF]);
        sb.append(HEX_CHARS[(h >> 16) & 0xF]);
        sb.append(HEX_CHARS[(h >> 12) & 0xF]);
        sb.append(HEX_CHARS[(h >> 8) & 0xF]);
        sb.append(HEX_CHARS[(h >> 4) & 0xF]);
        sb.append(HEX_CHARS[h & 0xF]);

        sb.append(HEX_CHARS[(i >> 28) & 0xF]);
        sb.append(HEX_CHARS[(i >> 24) & 0xF]);
        sb.append(HEX_CHARS[(i >> 20) & 0xF]);
        sb.append(HEX_CHARS[(i >> 16) & 0xF]);
        sb.append(HEX_CHARS[(i >> 12) & 0xF]);
        sb.append(HEX_CHARS[(i >> 8) & 0xF]);
        sb.append(HEX_CHARS[(i >> 4) & 0xF]);
        sb.append(HEX_CHARS[i & 0xF]);
        return sb;
    }

    public static StringBuilder append(StringBuilder sb, float f) {
        return append(sb, Float.floatToRawIntBits(f));
    }

    public static StringBuilder append(StringBuilder sb, double d) {
        return append(sb, Double.doubleToRawLongBits(d));
    }

    /**
     * convert char array to hex string
     */
    public static String encode(String str, String enc) {
        try {
            return encode(str.getBytes(enc));
        }
        catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("Unsupported encoding:" + enc);
        }
    }

    /**
     * convert byte array to hex string
     */
    public static String encode(String str) {
        return encode(str.getBytes());
    }

    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            System.out.println("Usage: java con.bolango.util.crypto.Hex encode|decode string [encoding]");
            System.exit(0);
        }
        String encoding = null;
        if (args.length == 3) {
            encoding = args[2];
        }
        if ("encode".equals(args[0])) {
            System.out.println(encode(args[1], encoding));
        }
        else if ("decode".equals(args[0])) {
            System.out.println(decode(args[1], encoding));
        }
        else {
            System.out.println("Invalid argument:" + args[0]);
            System.exit(0);
        }
    }

    public static ByteBuffer append(ByteBuffer bb, byte b) {
        bb.put(HEX_BYTES[(b >> 4) & 0xF]);
        bb.put(HEX_BYTES[b & 0xF]);
        return bb;
    }

    public static ByteBuffer append(ByteBuffer bb, char c) {
        bb.put(HEX_BYTES[(c >> 12) & 0xF]);
        bb.put(HEX_BYTES[(c >> 8) & 0xF]);
        bb.put(HEX_BYTES[(c >> 4) & 0xF]);
        bb.put(HEX_BYTES[c & 0xF]);
        return bb;
    }

    public static ByteBuffer append(ByteBuffer bb, short s) {
        bb.put(HEX_BYTES[(s >> 12) & 0xF]);
        bb.put(HEX_BYTES[(s >> 8) & 0xF]);
        bb.put(HEX_BYTES[(s >> 4) & 0xF]);
        bb.put(HEX_BYTES[s & 0xF]);
        return bb;
    }

    public static ByteBuffer append(ByteBuffer bb, int i) {
        bb.put(HEX_BYTES[(i >> 28) & 0xF]);
        bb.put(HEX_BYTES[(i >> 24) & 0xF]);
        bb.put(HEX_BYTES[(i >> 20) & 0xF]);
        bb.put(HEX_BYTES[(i >> 16) & 0xF]);
        bb.put(HEX_BYTES[(i >> 12) & 0xF]);
        bb.put(HEX_BYTES[(i >> 8) & 0xF]);
        bb.put(HEX_BYTES[(i >> 4) & 0xF]);
        bb.put(HEX_BYTES[i & 0xF]);
        return bb;
    }

    public static ByteBuffer append(ByteBuffer bb, long l) {
        int h = (int)((l >> 32) & 0xFFFFFFFFL);
        int i = (int)(l & 0xFFFFFFFFL);

        bb.put(HEX_BYTES[(h >> 28) & 0xF]);
        bb.put(HEX_BYTES[(h >> 24) & 0xF]);
        bb.put(HEX_BYTES[(h >> 20) & 0xF]);
        bb.put(HEX_BYTES[(h >> 16) & 0xF]);
        bb.put(HEX_BYTES[(h >> 12) & 0xF]);
        bb.put(HEX_BYTES[(h >> 8) & 0xF]);
        bb.put(HEX_BYTES[(h >> 4) & 0xF]);
        bb.put(HEX_BYTES[h & 0xF]);

        bb.put(HEX_BYTES[(i >> 28) & 0xF]);
        bb.put(HEX_BYTES[(i >> 24) & 0xF]);
        bb.put(HEX_BYTES[(i >> 20) & 0xF]);
        bb.put(HEX_BYTES[(i >> 16) & 0xF]);
        bb.put(HEX_BYTES[(i >> 12) & 0xF]);
        bb.put(HEX_BYTES[(i >> 8) & 0xF]);
        bb.put(HEX_BYTES[(i >> 4) & 0xF]);
        bb.put(HEX_BYTES[i & 0xF]);
        return bb;
    }

    public static ByteBuffer append(ByteBuffer bb, float f) {
        return append(bb, Float.floatToRawIntBits(f));
    }

    public static ByteBuffer append(ByteBuffer bb, double d) {
        return append(bb, Double.doubleToRawLongBits(d));
    }
}

