package org.femtoframework.lang;

/**
 * 各种数据类型与byte[]之间的转化<br>
 * <p/>
 * append(byte[] bytes, int off, param b):把字节数组bytes（即Binary形式存储）<br>
 * off位置开始的值替换为参数b的字节值；<br>
 * toBytes(param b):把输入参数值转化为字节数组byte[]（即以Binary形式存储）返回；<br>
 * 方法toChar,toDouble，toFloat，toInt等：还原以Binary形式存储的数据为原来形式；<br>
 *
 * @author fengyun
 */
public class Binary
{
    /**
     * 检查位置是否在起始域内
     *
     * @param off   位置
     * @param begin 开始位
     * @param end   结束位
     * @throws IllegalArgumentException
     */
    private static void check(int off, int begin, int end)
    {
        if (off < begin || off >= end) {
            throw new IllegalArgumentException("Invalid offset:" + off);
        }
    }

    /**
     * 把字节数组的off位置替换为字节b
     */
    public static void append(byte[] bytes, int off, byte b)
    {
        check(off, 0, bytes.length);
        bytes[off] = b;
    }

    public static void appendUnsignedByte(byte[] bytes,
                                                int off, int i)
    {
        check(off, 0, bytes.length);
        bytes[off] = (byte)i;
    }

    /**
     * 把字节数组的off位置替换为字符c的字节值
     */
    public static void append(byte[] bytes, int off, char c)
    {
        check(off, 0, bytes.length - 1);
        append0(bytes, off, c);
    }

    /**
     * 把字节数组的off位置替换为字符c的字节值
     */
    private static void append0(byte[] bytes, int off, char c)
    {
        int i = (int)c & 0xFFFF;
        bytes[off] = (byte)(i >> 8);
        bytes[off + 1] = (byte)(i);
    }

    public static void append(byte[] bytes, int off, short s)
    {
        check(off, 0, bytes.length - 1);
        append0(bytes, off, s);
    }

    private static void append0(byte[] bytes, int off, short s)
    {
        bytes[off++] = (byte)(s >> 8);
        bytes[off] = (byte)(s);
    }

    /**
     * 把字节数组的off位置开始的值替换为整形数i的字节值
     */
    public static void append(byte[] bytes, int off, int i)
    {
        check(off, 0, bytes.length - 3);
        append0(bytes, off, i);
    }

    public static void appendUnsignedShort(byte[] bytes,
                                                 int off, int i)
    {
        check(off, 0, bytes.length - 1);
        append1(bytes, off, i);
    }

    private static void append0(byte[] bytes, int off, int i)
    {
        bytes[off++] = (byte)(i >> 24);
        bytes[off++] = (byte)(i >> 16);
        bytes[off++] = (byte)(i >> 8);
        bytes[off] = (byte)(i);
    }

    private static void append1(byte[] bytes, int off, int i)
    {
        bytes[off++] = (byte)(i >> 8);
        bytes[off] = (byte)(i);
    }

    /**
     * 把字节数组的off位置开始的值替换为长整形数i的字节值
     */
    public static void append(byte[] bytes, int off, long i)
    {
        check(off, 0, bytes.length - 7);
        append0(bytes, off, i);
    }

    private static void append0(byte[] bytes, int off, long l)
    {
        bytes[off++] = (byte)(l >> 56);
        bytes[off++] = (byte)(l >> 48);
        bytes[off++] = (byte)(l >> 40);
        bytes[off++] = (byte)(l >> 32);
        bytes[off++] = (byte)(l >> 24);
        bytes[off++] = (byte)(l >> 16);
        bytes[off++] = (byte)(l >> 8);
        bytes[off] = (byte)(l);
    }

    /**
     * 把字节数组的off位置开始的值替换为浮点数f的字节值
     */
    public static void append(byte[] bytes, int off, float f)
    {
        append(bytes, off, Float.floatToIntBits(f));
    }

    /**
     * 把字节数组的off位置开始的值替换为双精度d的字节值
     */
    public static void append(byte[] bytes, int off, double d)
    {
        append(bytes, off, Double.doubleToLongBits(d));
    }

    public static byte[] toBytes(byte b)
    {
        return new byte[]{b};
    }

    public static byte[] toBytes(char c)
    {
        byte[] bytes = new byte[2];
        append0(bytes, 0, c);
        return bytes;
    }

    public static byte[] toBytes(short s)
    {
        byte[] bytes = new byte[2];
        append0(bytes, 0, s);
        return bytes;
    }

    public static byte[] toBytes(int i)
    {
        byte[] bytes = new byte[4];
        append0(bytes, 0, i);
        return bytes;
    }

    public static byte[] toBytes(long l)
    {
        byte[] bytes = new byte[8];
        append0(bytes, 0, l);
        return bytes;
    }

    public static byte[] toBytes(float f)
    {
        return toBytes(Float.floatToIntBits(f));
    }

    public static byte[] toBytes(double d)
    {
        return toBytes(Double.doubleToLongBits(d));
    }

    public static byte[] toBytes(String str)
    {
        if (str == null) {
            return null;
        }
        return toBytes0(str, 0, str.length());
    }

    public static byte[] EMPTY_BYTE_ARRAY = new byte[0];

    public static byte[] toBytes(String str, int off, int len)
    {
        if (str == null || len < 0) {
            return null;
        }
        else if (str.length() == 0 || len == 0) {
            return EMPTY_BYTE_ARRAY;
        }
        else if (off < 0 || off + len >= str.length()) {
            throw new IllegalArgumentException("Invalid offset or length, off=" + off + " len=" + len);
        }
// 这个判断有错误，
//        else if (len % 2 != 0) {
//            throw new IllegalArgumentException("Invalid length:" + len);
//        }

        return toBytes0(str, off, len);
    }

    private static byte[] toBytes0(String str, int off, int len)
    {
        byte[] bytes = new byte[len * 2];
        int v;
        int j = 0;
        for (int i = off, end = off + len; i < end; i++) {
            v = str.charAt(i);
            bytes[j++] = (byte)(v >> 8);
            bytes[j++] = (byte)(v);
        }
        return bytes;
    }

    public static byte[] toBytes(char[] chars)
    {
        if (chars == null) {
            return null;
        }
        return toBytes0(chars, 0, chars.length);
    }

    public static byte[] toBytes(char[] chars, int off, int len)
    {
        if (chars == null || len < 0) {
            return null;
        }
        else if (chars.length == 0 || len == 0) {
            return new byte[0];
        }
        else if (off < 0 || off + len >= chars.length) {
            throw new IllegalArgumentException("Invalid offset or length, off=" + off + " len=" + len);
        }
// 这个判断有错误
//        else if (len % 2 != 0) {
//            throw new IllegalArgumentException("Invalid length:" + len);
//        }
        return toBytes0(chars, off, len);
    }

    private static byte[] toBytes0(char[] chars, int off, int len)
    {
        byte[] bytes = new byte[len * 2];
        int v;
        int j = 0;
        int end = off + len;
        for (int i = off; i < end; i++) {
            v = chars[i];
            bytes[j++] = (byte)(v >> 8);
            bytes[j++] = (byte)(v);
        }
        return bytes;
    }

    /**
     * 取两个字节
     */
    public static byte[] toShortBytes(int i)
    {
        byte[] bytes = new byte[2];
        append1(bytes, 0, i);
        return bytes;
    }

    public static String toString(byte[] bytes)
    {
        if (bytes == null) {
            return null;
        }
        else if (bytes.length == 0) {
            return "";
        }
        else if (bytes.length % 2 != 0) {
            throw new IllegalArgumentException("Invalid length:" + bytes.length);
        }
        return toString0(bytes, 0, bytes.length);
    }

    public static String toString(byte[] bytes, int off, int len)
    {
        if (bytes == null || len < 0) {
            return null;
        }
        else if (bytes.length == 0 || len == 0) {
            return "";
        }
        else if (off < 0 || off + len > bytes.length) {
            throw new IllegalArgumentException("Invalid offset or length, off=" + off + " len=" + len);
        }
        else if (len % 2 != 0) {
            throw new IllegalArgumentException("Invalid length:" + len);
        }

        return toString0(bytes, off, len);
    }

    private static String toString0(byte[] bytes, int off, int len)
    {
        int l = len / 2;
        int ch1;
        int ch2;
        StringBuilder sb = new StringBuilder(l);
        int index = off;
        for (int i = 0; i < l; i++) {
            ch1 = bytes[index++] & 0XFF;
            ch2 = bytes[index++] & 0XFF;
            sb.append((char)((ch1 << 8) + (ch2)));
        }
        return sb.toString();
    }

    public static char[] toChars(byte[] bytes)
    {
        if (bytes == null) {
            return null;
        }
        else if (bytes.length == 0) {
            return new char[0];
        }
        else if (bytes.length % 2 != 0) {
            throw new IllegalArgumentException("Invalid length:" + bytes.length);
        }
        return toChars0(bytes, 0, bytes.length);
    }

    public static char[] toChars(byte[] bytes, int off, int len)
    {
        if (bytes == null || len < 0) {
            return null;
        }
        else if (bytes.length == 0 || len == 0) {
            return new char[0];
        }
        else if (off < 0 || off + len > bytes.length) {
            throw new IllegalArgumentException("Invalid offset or length, off=" + off + " len=" + len);
        }
        else if (len % 2 != 0) {
            throw new IllegalArgumentException("Invalid length:" + len);
        }

        return toChars0(bytes, off, len);
    }

    private static char[] toChars0(byte[] bytes, int off, int len)
    {
        int l = len / 2;
        int ch1;
        int ch2;

        char[] chars = new char[l];
        int index = off;
        for (int i = 0; i < l; i++) {
            ch1 = bytes[index++] & 0XFF;
            ch2 = bytes[index++] & 0XFF;
            chars[i] = (char)((ch1 << 8) + (ch2));
        }
        return chars;
    }


    public static byte toByte(byte[] bytes, int off)
    {
        check(off, 0, bytes.length);
        return bytes[off];
    }

    public static byte toByte(byte[] bytes)
    {
        if (bytes.length == 0) {
            throw new IllegalArgumentException("Illegal length: 0");
        }
        return bytes[0];
    }

    public static int toUnsignedByte(byte[] bytes, int off)
    {
        check(off, 0, bytes.length);
        return (int)bytes[off] & 0XFF;
    }

    public static int toUnsignedByte(byte[] bytes)
    {
        if (bytes.length == 0) {
            throw new IllegalArgumentException("Illegal length: 0");
        }
        return (int)bytes[0] & 0XFF;
    }

    /**
     * 还原以Binary形式存储的Char
     */
    public static char toChar(byte[] bytes, int off)
    {
        check(off, 0, bytes.length - 1);
        int ch1 = (int)bytes[off] & 0xFF;
        int ch2 = (int)bytes[off + 1] & 0xFF;
        return (char)((ch1 << 8) + (ch2));
    }

    public static char toChar(byte[] bytes)
    {
        return toChar(bytes, 0);
    }

    /**
     * 还原以Binary形式存储的Short
     */
    public static short toShort(byte[] bytes, int off)
    {
        check(off, 0, bytes.length - 1);
        int ch1 = (int)bytes[off] & 0xFF;
        int ch2 = (int)bytes[off + 1] & 0xFF;
        return (short)((ch1 << 8) + (ch2));
    }

    public static short toShort(byte[] bytes)
    {
        return toShort(bytes, 0);
    }

    public static int toUnsignedShort(byte[] bytes, int off)
    {
        check(off, 0, bytes.length - 1);
        int ch1 = (int)bytes[off] & 0xFF;
        int ch2 = (int)bytes[off + 1] & 0xFF;
        return (ch1 << 8) + (ch2);
    }

    public static int toUnsignedShort(byte[] bytes)
    {
        return toUnsignedShort(bytes, 0);
    }

    /**
     * 还原以Binary形式存储的Integer
     */
    public static int toInt(byte[] bytes, int off)
    {
        check(off, 0, bytes.length - 3);
        int i = off;
        int ch1 = (int)bytes[i++] & 0xFF;
        int ch2 = (int)bytes[i++] & 0xFF;
        int ch3 = (int)bytes[i++] & 0xFF;
        int ch4 = (int)bytes[i] & 0xFF;
        return ((ch1 << 24) + (ch2 << 16) + (ch3 << 8) + (ch4));
    }

    public static int toInt(byte[] bytes)
    {
        return toInt(bytes, 0);
    }

    public static long toLong(byte[] bytes, int off)
    {
        //check(off, 0, bytes.length-7);
        return ((long)toInt(bytes, off) << 32)
               | ((long)toInt(bytes, off + 4) & 0xFFFFFFFFL);
    }

    public static long toLong(byte[] bytes)
    {
        return toLong(bytes, 0);
    }

    public static float toFloat(byte[] bytes, int off)
    {
        return Float.intBitsToFloat(toInt(bytes, off));
    }

    public static float toFloat(byte[] bytes)
    {
        return toFloat(bytes, 0);
    }

    public static double toDouble(byte[] bytes, int off)
    {
        return Double.longBitsToDouble(toLong(bytes, off));
    }

    public static double toDouble(byte[] bytes)
    {
        return toDouble(bytes, 0);
    }

    /**
     * 对bytes数组进行异或操作
     *
     * @param bytes Bytes数组
     * @return
     */
    public static byte xor(byte[] bytes)
    {
        if (bytes == null) {
            throw new IllegalArgumentException("Null bytes");
        }
        return xor(bytes, 0, bytes.length);
    }

    /**
     * 对bytes数组进行异或操作
     *
     * @param bytes Bytes数组
     * @param off   起始位置
     * @param len   长度
     * @return
     */
    public static byte xor(byte[] bytes, int off, int len)
    {
        if (bytes == null) {
            throw new IllegalArgumentException("Null bytes");
        }

        byte result = 0;
        for (int i = off, end = off + len; i < end; i++) {
            result = (byte)(bytes[i] ^ result);
        }
        return result;
    }

    /**
     * 对字符各个字节进行异或操作
     *
     * @param ch 字符
     * @return
     */
    public static byte xor(char ch)
    {
        return xor(toBytes(ch));
    }

    /**
     * 对短整数各个字节进行异或操作
     *
     * @param s 短整数
     * @return
     */
    public static byte xor(short s)
    {
        return xor(toBytes(s));
    }

    /**
     * 对整数各个字节进行异或操作
     *
     * @param i 整数
     * @return
     */
    public static byte xor(int i)
    {
        return xor(toBytes(i));
    }

    /**
     * 对长整数各个字节进行异或操作
     *
     * @param l 长整数
     * @return
     */
    public static byte xor(long l)
    {
        return xor(toBytes(l));
    }

    /**
     * 对单精度浮点数各个字节进行异或操作
     *
     * @param f 单精度浮点数
     * @return
     */
    public static byte xor(float f)
    {
        return xor(toBytes(f));
    }

    /**
     * 对双精度浮点数各个字节进行异或操作
     *
     * @param d 双精度浮点数
     * @return
     */
    public static byte xor(double d)
    {
        return xor(toBytes(d));
    }
}
