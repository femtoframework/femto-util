package org.femtoframework.io;

import org.femtoframework.lang.Binary;
import org.femtoframework.lang.reflect.Reflection;
import org.femtoframework.util.DataUtil;

import java.io.*;
import java.lang.ref.SoftReference;
import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;


/**
 * 编码解码工具类，为了能够在非ceno-base环境中依然能够使用相关的方法Serialize方法
 *
 * @author fengyun
 */
public class CodecUtil implements CodecConstants {

    /**
     * 写出一个<code>null</code>对象
     *
     * @param out 输出流
     * @throws java.io.IOException 发生I/O异常时抛出
     */
    public static void writeNull(OutputStream out)
        throws IOException {
        writeType(out, TYPE_NULL);
    }

    /**
     * 写出对象传输中的对象类型
     *
     * @param out  输出流
     * @param type 对象在<code>FastSerialize</code>中的类型
     * @throws IOException 发生I/O异常时抛出
     */
    public static void writeType(OutputStream out, int type)
        throws IOException {
        out.write(type);
    }

    /**
     * 读取对象传输中的对象类型
     *
     * @param in 输出流
     * @return type 对象在<code>FastSerialize</code>中的类型
     * @throws IOException 发生I/O异常时抛出
     */
    public static int readType(InputStream in)
        throws IOException {
        return in.read();
    }

    /**
     * 写出一个简单的字节
     *
     * @param out 输出流
     * @param b   字节
     * @throws IOException 发生I/O异常时抛出
     */
    public static void writeByte(OutputStream out, byte b)
        throws IOException {
        out.write(b);
    }

    /**
     * 写出一个布尔类型
     *
     * @param out 输出流
     * @param b   布尔值
     * @throws IOException 发生I/O异常时抛出
     */
    public static void writeBoolean(OutputStream out, boolean b)
        throws IOException {
        out.write(b ? 1 : 0);
    }

    /**
     * 写出一个字符
     *
     * @param out 输出流
     * @param c   字符
     * @throws IOException 发生I/O异常时抛出
     */
    public static void writeChar(OutputStream out, char c)
        throws IOException {
        out.write((c >>> 8) & 0xFF);
        out.write((c) & 0xFF);
    }

    /**
     * 写出一个短整型数字
     *
     * @param out 输出流
     * @param s   短整数
     * @throws IOException 发生I/O异常时抛出
     */
    public static void writeShort(OutputStream out, short s)
        throws IOException {
        out.write((s >>> 8) & 0xFF);
        out.write((s) & 0xFF);
    }

    public static void writeUnsignedShort(OutputStream out, int i)
        throws IOException {
//        out.write(Binary.toShortBytes(i));
        out.write((i >>> 8) & 0xFF);
        out.write((i) & 0xFF);
    }

    public static void writeUnsignedByte(OutputStream out, int i)
        throws IOException {
        out.write(i);
    }

    public static int readInt(InputStream in)
            throws IOException
    {
        int ch1 = in.read();
        int ch2 = in.read();
        int ch3 = in.read();
        int ch4 = in.read();
        if ((ch1 | ch2 | ch3 | ch4) < 0) {
            throw new EOFException();
        }
        return ((ch1 << 24) + (ch2 << 16) + (ch3 << 8) + (ch4));
    }

    public static void writeInt(OutputStream out, int i)
            throws IOException
    {
        out.write((i >>> 24) & 0xFF);
        out.write((i >>> 16) & 0xFF);
        out.write((i >>> 8) & 0xFF);
        out.write((i) & 0xFF);
    }

    /**
     * 输出无符号整数
     *
     * @param out
     * @param l
     * @throws IOException
     */
    public static void writeUnsignedInt(OutputStream out, long l)
        throws IOException {
        out.write((int)(l >>> 24) & 0xFF);
        out.write((int)(l >>> 16) & 0xFF);
        out.write((int)(l >>> 8) & 0xFF);
        out.write((int)(l) & 0xFF);
    }

    public static void writeLong(OutputStream out, long l)
        throws IOException {
        out.write((int)(l >>> 56) & 0xFF);
        out.write((int)(l >>> 48) & 0xFF);
        out.write((int)(l >>> 40) & 0xFF);
        out.write((int)(l >>> 32) & 0xFF);
        out.write((int)(l >>> 24) & 0xFF);
        out.write((int)(l >>> 16) & 0xFF);
        out.write((int)(l >>> 8) & 0xFF);
        out.write((int)(l) & 0xFF);
    }

    public static void writeFloat(OutputStream out, float f)
        throws IOException {
        writeInt(out, Float.floatToIntBits(f));
    }

    public static void writeDouble(OutputStream out, double d)
        throws IOException {
        writeLong(out, Double.doubleToLongBits(d));
    }

    public static void writeBytes(OutputStream out, byte[] bytes)
        throws IOException {
        int len = -1;
        if (bytes != null) {
            len = bytes.length;
        }
        writeInt(out, len);
        if (bytes != null) {
            out.write(bytes);
        }
    }

    public static byte readByte(InputStream in)
        throws IOException {
        return (byte)(in.read() & 0xFF);
    }

    public static boolean readBoolean(InputStream in)
        throws IOException {
        int ch = in.read();
        return ch != 0;
    }

    /**
     * 注意：有可能产生数据没有读到的情况（对于BufferedInputStream或ByteArrayInputStream
     * 之类的就不会有问题），为了速度牺牲它吧！～～ 下同
     */
    public static char readChar(InputStream in)
        throws IOException {
        int ch1 = in.read();
        int ch2 = in.read();
        if ((ch1 | ch2) < 0) {
            throw new EOFException();
        }
        return (char)((ch1 << 8) + (ch2));
    }

    public static int readUnsignedByte(InputStream in)
        throws IOException {
        int ch = in.read();
        if (ch < 0) {
            throw new EOFException();
        }
        return ch;
    }

    public static short readShort(InputStream in)
        throws IOException {
        int ch1 = in.read();
        int ch2 = in.read();
        if ((ch1 | ch2) < 0) {
            throw new EOFException();
        }
        return (short)((ch1 << 8) + (ch2));
    }

    public static int readUnsignedShort(InputStream in)
        throws IOException {
        int ch1 = in.read();
        int ch2 = in.read();
        if ((ch1 | ch2) < 0) {
            throw new EOFException();
        }
        return (ch1 << 8) + (ch2);
    }

    /**
     * 读入无符号整数
     *
     * @param in
     * @return
     * @throws IOException
     */
    public static long readUnsignedInt(InputStream in)
        throws IOException {
        long ch1 = in.read();
        int ch2 = in.read();
        int ch3 = in.read();
        int ch4 = in.read();
        if ((ch1 | ch2 | ch3 | ch4) < 0) {
            throw new EOFException();
        }
        return ((ch1 << 24) + (ch2 << 16) + (ch3 << 8) + (ch4));
    }

    public static long readLong(InputStream in)
        throws IOException {
        return ((long)(readInt(in)) << 32) +
               (readInt(in) & 0xFFFFFFFFL);
    }

    public static float readFloat(InputStream in)
        throws IOException {
        return Float.intBitsToFloat(readInt(in));
    }

    public static double readDouble(InputStream in)
        throws IOException {
        return Double.longBitsToDouble(readLong(in));
    }

    public static byte[] readBytes(InputStream in)
        throws IOException {
        int len = readInt(in);
        if (len < 0) {
            return null;
        }

        byte[] bytes = new byte[len];
        IOUtil.readFully(in, bytes);
        return bytes;
    }

    //UTF-8

    /**
     * The format is different with DataOutputStream.writeUTF
     * It has both string length(int) and utf8 bytes size(int) for non-empty strings
     * Also it is able to write NULL
     */
    public static String readUTFX(InputStream in)
            throws IOException {
        int strlen = readInt(in);
        if (strlen == -1) {
            return null;
        }
        else if (strlen == 0) {
            return DataUtil.EMPTY_STRING;
        }

        int utflen = readInt(in);
        StringBuilder sb = new StringBuilder(strlen);
        ByteBuffer buffer;
        byte[] bytes;
        if (utflen <= BYTE_BUFFER_SIZE) {
            buffer = IOUtil.getByteBuffer();
            bytes = buffer.array();
        }
        else {
            bytes = new byte[utflen];
        }

        IOUtil.readFully(in, bytes, 0, utflen);

        fromUTFX(bytes, utflen, sb);

        return sb.toString();
    }

    protected static void fromUTFX(byte[] bytes, int utflen, StringBuilder sb)
            throws IOException {
        int c, char2, char3;
        int count = 0;

        while (count < utflen) {
            c = (int)bytes[count] & 0xff;
            switch (c >> 4) {
                case 0:
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                    /* 0xxxxxxx*/
                    count++;
                    sb.append((char)c);
                    break;
                case 12:
                case 13:
                    /* 110x xxxx   10xx xxxx*/
                    count += 2;
                    if (count > utflen) {
                        throw new UTFDataFormatException();
                    }
                    char2 = (int)bytes[count - 1];
                    if ((char2 & 0xC0) != 0x80) {
                        throw new UTFDataFormatException();
                    }
                    sb.append((char)(((c & 0x1F) << 6) | (char2 & 0x3F)));
                    break;
                case 14:
                    /* 1110 xxxx  10xx xxxx  10xx xxxx */
                    count += 3;
                    if (count > utflen) {
                        throw new UTFDataFormatException();
                    }
                    char2 = (int)bytes[count - 2];
                    char3 = (int)bytes[count - 1];
                    if (((char2 & 0xC0) != 0x80) || ((char3 & 0xC0) != 0x80)) {
                        throw new UTFDataFormatException();
                    }
                    sb.append((char)(((c & 0x0F) << 12) |
                            ((char2 & 0x3F) << 6) |
                            ((char3 & 0x3F))));
                    break;
                default:
                    /* 10xx xxxx,  1111 xxxx */
                    throw new UTFDataFormatException();
            }
        }
    }


    /**
     *
     * The format is different with DataOutputStream.writeUTF
     * It has both string length and utf8 bytes size for non-empty strings
     * Also it is able to write NULL
     */
    public static int writeUTFX(OutputStream out, String str)
            throws IOException {
        if (str == null) {
            writeInt(out, -1);
            return -1;
        }
        else if (str.length() == 0) {
            writeInt(out, 0);
            return 0;
        }
        ByteBuffer buf = toUTFX(str);
        int size = buf.limit();
        byte[] bytes = buf.array();
        out.write(bytes, 0, size);
        return size;
    }

    static ByteBuffer toUTFX(String str) throws IOException {
        int strlen = str.length();
        int utflen = 0;
        CharBuffer charBuffer;
        char[] chars;
        if (strlen <= CHAR_BUFFER_SIZE) {
            charBuffer = IOUtil.getCharBuffer();
            chars = charBuffer.array();
        }
        else {
            chars = new char[strlen];
        }

        int c, count = 0;

        str.getChars(0, strlen, chars, 0);

        for (int i = 0; i < strlen; i++) {
            c = chars[i];
            if ((c >= 0x0001) && (c <= 0x007F)) {
                utflen++;
            }
            else if (c > 0x07FF) {
                utflen += 3;
            }
            else {
                utflen += 2;
            }
        }

        if (utflen > 65535) {
            throw new UTFDataFormatException();
        }

        int size = utflen + 8;
        ByteBuffer byteBuffer = null;
        byte[] bytes;
        if (size <= BYTE_BUFFER_SIZE) {
            byteBuffer = IOUtil.getByteBuffer();
            bytes = byteBuffer.array();
            byteBuffer.limit(size);
        }
        else {
            bytes = new byte[size];
        }

        Binary.append(bytes, count, strlen);
        count += 4;
        Binary.append(bytes, count, utflen);
        count += 4;
        for (int i = 0; i < strlen; i++) {
            c = chars[i];
            if ((c >= 0x0001) && (c <= 0x007F)) {
                bytes[count++] = (byte)c;
            }
            else if (c > 0x07FF) {
                bytes[count++] = (byte)(0xE0 | ((c >> 12) & 0x0F));
                bytes[count++] = (byte)(0x80 | ((c >> 6) & 0x3F));
                bytes[count++] = (byte)(0x80 | ((c) & 0x3F));
            }
            else {
                bytes[count++] = (byte)(0xC0 | ((c >> 6) & 0x1F));
                bytes[count++] = (byte)(0x80 | ((c) & 0x3F));
            }
        }
        return byteBuffer == null ? ByteBuffer.wrap(bytes, 0, size) : byteBuffer;
    }


    /**
     * 测试读入字符数组
     *
     * @param in
     * @return
     * @throws IOException
     */
    public static char[] readChars(InputStream in)
        throws IOException {
        int len = readInt(in);
        if (len < 0) {
            return null;
        }
        else if (len == 0) {
            return new char[0];
        }
        else if (len > MAX_STRING_LENGTH) {
            throw new IOException("The length of chars is so big");
        }

        int ch1, ch2;

        byte[] bytes = getByteBuffer();
        int read;
        char[] chars = new char[len];
        int i = 0;
        while (len > 0) {
            read = bytes.length;
            read = read > len * 2 ? len * 2 : read;
            read = IOUtil.readFully(in, bytes, 0, read);
            if (read <= 0) {
                break;
            }
            for (int j = 0; j < read;) {
                ch1 = (bytes[j++] & 0xFF);
                ch2 = (bytes[j++] & 0xFF);
                chars[i++] = (char)((ch1 << 8) + (ch2));
            }
            len -= read / 2;
        }

        return chars;
    }

    /**
     * 采用byte缓冲机制的快速写字符
     */
    public static void writeChars(OutputStream out, char[] chars)
        throws IOException {
        int len = -1;
        if (chars != null) {
            len = chars.length;
        }
        int v;
        writeInt(out, len);
        if (len <= 0) {
            return;
        }
        else if (len < 128) { //字符比较少
            byte[] bytes = new byte[2];
            for (int i = 0; i < len; i++) {
                v = chars[i];
                bytes[0] = (byte)((v >>> 8) & 0xFF);
                bytes[1] = (byte)((v) & 0xFF);
                out.write(bytes);
            }
        }
        else { //字符比较多，采用Buffer来写
            byte[] bytes = getByteBuffer();
            int j = 0;
            for (int i = 0; i < len; i++) {
                v = chars[i];
                bytes[j++] = (byte)((v >>> 8) & 0xFF);
                bytes[j++] = (byte)((v) & 0xFF);
                if (j == bytes.length) { //Full
                    out.write(bytes, 0, j);
                    j = 0;
                }
            }
            if (j > 0) {
                out.write(bytes, 0, j);
            }
        }
    }


    /**
     * 采用byte缓冲机制的快速写字符
     */
    public static void writeString(OutputStream out, String str)
        throws IOException {
        int len = -1;
        if (str != null) {
            len = str.length();
        }
        writeInt(out, len);

        if (len > 0) {
            byte[] buf = getByteBuffer();
            writeString(out, str, len, buf);
        }
    }

    protected static void writeString(OutputStream out, String str,
                                      int len, byte[] buf)
        throws IOException {
        int j = 0;
        int v;

        for (int i = 0; i < len; i++) {
            v = str.charAt(i);
            buf[j++] = (byte)((v >>> 8) & 0xFF);
            buf[j++] = (byte)((v) & 0xFF);
            if (j == buf.length) { //Full
                out.write(buf, 0, j);
                j = 0;
            }
        }
        if (j > 0) {
            out.write(buf, 0, j);
        }
    }

    /**
     * 写单字节字符
     */
    public static void writeSingle(OutputStream out, String s)
        throws IOException {
        int len = -1;
        if (s != null) {
            len = s.length();
        }
        writeInt(out, len);

        int v;
        if (len <= 0) {
            return;
        }
        else {
            byte[] bytes = getByteBuffer();
            int j = 0;
            for (int i = 0; i < len; i++) {
                v = s.charAt(i);
                bytes[j++] = (byte)((v) & 0xFF);
                if (j == bytes.length) { //Full
                    out.write(bytes, 0, j);
                    j = 0;
                }
            }
            if (j > 0) {
                out.write(bytes, 0, j);
            }
        }
    }



    private static ThreadLocal<SoftReference<byte[]>> byteBuffer
        = ThreadLocal.withInitial(() -> new SoftReference<byte[]>(new byte[BYTE_BUFFER_SIZE]));

    private static ThreadLocal<SoftReference<char[]>> charBuffer
        = ThreadLocal.withInitial(() -> new SoftReference<>(new char[CHAR_BUFFER_SIZE]));


    protected static byte[] getByteBuffer() {
        SoftReference<byte[]> sr = byteBuffer.get();
        byte[] buf = null;
        if (sr != null) {
            buf = sr.get();
        }
        if (buf == null) {
            buf = new byte[BYTE_BUFFER_SIZE];
            sr = new SoftReference<byte[]>(buf);
            byteBuffer.set(sr);
        }
        return buf;
    }

    protected static char[] getCharBuffer() {
        SoftReference<char[]> sr = charBuffer.get();
        char[] buf = null;
        if (sr != null) {
            buf = (char[])sr.get();
        }
        if (buf == null) {
            buf = new char[CHAR_BUFFER_SIZE];
            sr = new SoftReference<char[]>(buf);
            charBuffer.set(sr);
        }
        return buf;
    }

    public static String readSingle(InputStream in)
        throws IOException {
        int len = readInt(in);

        if (len < 0) {
            return null;
        }
        else if (len == 0) {
            return "";
        }
        else if (len > MAX_STRING_LENGTH) {
            throw new IOException("The string length is so big");
        }

        byte[] bytes = getByteBuffer();
        int ch1;
        int read = 0;
        if (len <= CHAR_BUFFER_SIZE) {
            read = IOUtil.readFully(in, bytes, 0, len);
            if (read != len) {
                throw new IOException("No more data left:" + len + " read:" + read);
            }
            char[] chars = getCharBuffer();
            for (int i = 0, j = 0; j < read; i++) {
                ch1 = (bytes[j++] & 0xFF);
                chars[i] = (char)(ch1);
            }
            return new String(chars, 0, len);
        }
        else {
            StringBuilder sb = new StringBuilder(len);
            while (len > 0) {
                read = bytes.length > len ? len : bytes.length;
                read = IOUtil.readFully(in, bytes, 0, read);
                if (read <= 0) {
                    break;
                }
                for (int i = 0, j = 0; i < read; i++) {
                    ch1 = (bytes[j++] & 0xFF);
                    sb.append((char)(ch1));
                }
                len -= read;
            }
            return sb.toString();
        }
    }


    public static String readString(InputStream in)
        throws IOException {
        int len = readInt(in);

        if (len < 0) {
            return null;
        }
        else if (len == 0) {
            return "";
        }
        else if (len > MAX_STRING_LENGTH) {
            throw new IOException("The string length is so big");
        }

        byte[] buf = getByteBuffer();
        return readString(in, len, buf);
    }


    protected static String readString(InputStream in, int len, byte[] buf)
        throws IOException {
        int ch1, ch2;
        int read = 0;
        if (len <= CHAR_BUFFER_SIZE) {
            char[] chars = getCharBuffer();
            int k = 0;
            int l = len;
            while (l > 0) {
                read = buf.length > l * 2 ? l * 2 : buf.length;
                read = IOUtil.readFully(in, buf, 0, read);
                if (read <= 0) {
                    break;
                }
                for (int j = 0; j < read;) {
                    ch1 = (buf[j++] & 0xFF);
                    ch2 = (buf[j++] & 0xFF);
                    chars[k++] = (char)((ch1 << 8) + (ch2));
                }
                l -= (read / 2);
            }
            return new String(chars, 0, len);
        }
        else {
            StringBuilder sb = new StringBuilder(len);
            while (len > 0) {
                read = buf.length > len * 2 ? len * 2 : buf.length;
                read = IOUtil.readFully(in, buf, 0, read);
                if (read <= 0) {
                    break;
                }
                for (int j = 0; j < read;) {
                    ch1 = (buf[j++] & 0xFF);
                    ch2 = (buf[j++] & 0xFF);
                    sb.append((char)((ch1 << 8) + (ch2)));
                }
                len -= (read / 2);
            }
            return sb.toString();
        }
    }

    private static void writeString(OutputStream out,
                                    String str, byte[] buf)
        throws IOException {
        int len = -1;
        if (str != null) {
            len = str.length();
        }
        writeInt(out, len);

        if (len > 0) {
            writeString(out, str, len, buf);
        }
    }

    private static String readString(InputStream in, byte[] buf)
        throws IOException {
        int len = readInt(in);

        if (len < 0) {
            return null;
        }
        else if (len == 0) {
            return "";
        }
        else if (len > MAX_STRING_LENGTH) {
            throw new IOException("The string length is so big");
        }

        return readString(in, len, buf);
    }

    public static void writeStringArray(ObjectOutputStream oos, String[] array)
        throws IOException {
        writeStringArray((OutputStream)oos, array);
    }


    public static void writeStringArray(OutputStream oos, String[] array)
        throws IOException {
        if (array == null) {
            writeInt(oos, -1);
            return;
        }

        int len = array.length;
        writeInt(oos, len);
        byte[] buf = getByteBuffer();
        for (int i = 0; i < len; i++) {
            writeString(oos, array[i], buf);
        }
    }

    public static String[] readStringArray(ObjectInputStream ois)
        throws IOException {
        return readStringArray((InputStream)ois);
    }

    public static String[] readStringArray(InputStream ois)
        throws IOException {
        int len = readInt(ois);
        if (len == -1) {
            return null;
        }
        else if (len == 0) {
            return DataUtil.EMPTY_STRING_ARRAY;
        }
        else if (len > MAX_ARRAY_LENGTH) {
            throw new IOException("The length is so big");
        }

        String[] array = new String[len];
        byte[] buf = getByteBuffer();
        for (int i = 0; i < len; i++) {
            array[i] = readString(ois, buf);
        }

        return array;
    }

    //快速串行化相关的类

    static void writeClass(OutputStream out, Class clazz)
        throws IOException {
        writeSingle(out, clazz.getName());
    }

    public static void writeObject(ObjectOutputStream oos, Object object)
        throws IOException {
        if (object == null) {
            writeNull(oos);
            return;
        }

        if (object instanceof byte[]) {
            writeType(oos, TYPE_BYTE_ARRAY);
            writeBytes(oos, (byte[])object);
        }
        else if (object instanceof String) {
            writeType(oos, TYPE_STRING);
            writeString(oos, (String)object);
        }
        else if (object instanceof char[]) {
            writeType(oos, TYPE_CHAR_ARRAY);
            writeChars(oos, (char[])object);
        }
        else if (object instanceof Character) {
            writeType(oos, TYPE_CHARACTER);
            writeChar(oos, (Character)object);
        }
        else if (object instanceof Number) {
            if (object instanceof Integer) {
                writeType(oos, TYPE_INTEGER);
                writeInt(oos, (Integer)object);
            }
            else if (object instanceof Long) {
                writeType(oos, TYPE_LONG);
                writeLong(oos, (Long)object);
            }
            else if (object instanceof Byte) {
                writeType(oos, TYPE_BYTE);
                writeByte(oos, (Byte)object);
            }
            else if (object instanceof Float) {
                writeType(oos, TYPE_FLOAT);
                writeFloat(oos, (Float)object);
            }
            else if (object instanceof Double) {
                writeType(oos, TYPE_DOUBLE);
                writeDouble(oos, (Double)object);
            }
            else if (object instanceof Short) {
                writeType(oos, TYPE_SHORT);
                writeShort(oos, (Short)object);
            }
            else {
                writeType(oos, TYPE_OBJECT);
                oos.writeObject(object);
            }
        }
        else if (object instanceof Boolean) {
            writeType(oos, TYPE_BOOLEAN);
            writeBoolean(oos, (Boolean)object);
        }
        else if (object instanceof String[]) {
            writeType(oos, TYPE_STRING_ARRAY);
            writeStringArray(oos, (String[])object);
        }
        else if (object instanceof Class) {
            writeType(oos, TYPE_CLASS);
            writeClass(oos, (Class)object);
        }
        else if (object instanceof Streamable) {
            writeType(oos, TYPE_STREAMABLE);
            writeStreamable(oos, (Streamable)object);
        }
        else if (object instanceof Object[]) {
            writeType(oos, TYPE_OBJECT_ARRAY);
            writeObjectArray(oos, (Object[])object);
        }
        else {
            writeType(oos, TYPE_OBJECT);
            oos.writeObject(object);
        }
    }

    /**
     * 输出Streamable对象
     *
     * @param oos        OutputStream
     * @param streamable Streamable
     * @throws IOException
     */
    public static void writeStreamable(OutputStream oos, Streamable streamable)
        throws IOException {
        writeSingle(oos, streamable.getClass().getName());
        streamable.writeTo(oos);
    }

    /**
     * 读取Streamable
     *
     * @param ois InputStream
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static Object readStreamable(InputStream ois) throws IOException, ClassNotFoundException {
        String className = readSingle(ois);
        Streamable obj = (Streamable) Reflection.newInstance(className);
        obj.readFrom(ois);
        return obj;
    }


    public static void writeObjectArray(ObjectOutputStream oos,
                                        Object[] array)
        throws IOException {
        if (array == null) {
            writeInt(oos, -1);
            return;
        }

        int len = array.length;
        writeInt(oos, len);
        writeClass(oos, array.getClass().getComponentType());
        for (int i = 0; i < len; i++) {
            writeObject(oos, array[i]);
        }
    }

    public static Object[] readObjectArray(ObjectInputStream ois)
        throws IOException, ClassNotFoundException {
        int len = readInt(ois);
        if (len == -1) {
            return null;
        }
        else if (len > MAX_ARRAY_LENGTH) {
            throw new IOException("The length is so big");
        }

        Class clazz = readClass(ois);
        Object[] array = (Object[])Array.newInstance(clazz, len);
        for (int i = 0; i < len; i++) {
            array[i] = readObject(ois);
        }
        return array;
    }

    static Class readClass(InputStream in)
        throws IOException, ClassNotFoundException {
        String className = readSingle(in);
        return Reflection.getClass(className);
    }


    public static Object readObject(ObjectInputStream ois)
        throws ClassNotFoundException, IOException {
        int type = readType(ois);
        switch (type) {
            case TYPE_NULL:
                return null;
            case TYPE_BYTE_ARRAY:
                return readBytes(ois);
            case TYPE_CHAR_ARRAY:
                return readChars(ois);
            case TYPE_STRING:
                return readString(ois);
            case TYPE_INTEGER:
                return readInt(ois);
            case TYPE_LONG:
                return readLong(ois);
            case TYPE_STREAMABLE:
                return readStreamable(ois);
            case TYPE_BYTE:
                return readByte(ois);
            case TYPE_CHARACTER:
                return readChar(ois);
            case TYPE_BOOLEAN:
                return readBoolean(ois);
            case TYPE_SHORT:
                return readShort(ois);
            case TYPE_FLOAT:
                return readFloat(ois);
            case TYPE_DOUBLE:
                return readDouble(ois);
            case TYPE_OBJECT:
                return ois.readObject();
            case TYPE_CLASS:
                return readClass(ois);
            case TYPE_STRING_ARRAY:
                return readStringArray(ois);
            case TYPE_OBJECT_ARRAY:
                return readObjectArray(ois);
            default:
                throw new IOException("Invalid Type:" + type);
        }
    }
}
