package org.femtoframework.io;

import org.femtoframework.lang.reflect.Reflection;
import org.femtoframework.util.DataUtil;

import java.io.*;
import java.lang.reflect.Array;
import java.nio.ByteBuffer;

/**
 * Data Codec Util
 * 
 * @author fengyun
 * @version 1.00 11-8-3 下午10:29
 */
public class DataCodec implements CodecConstants {

    /**
     * 写出一个<code>null</code>对象
     *
     * @param out 输出流
     * @throws java.io.IOException 发生I/O异常时抛出
     */
    public static void writeNull(DataOutput out)
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
    public static void writeType(DataOutput out, int type)
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
    public static int readType(DataInput in)
        throws IOException {
        return (int)in.readByte();
    }

    /**
     * 采用byte缓冲机制的快速写字符
     */
    public static void writeString(DataOutput out, String str)
        throws IOException {
        int len = -1;
        if (str != null) {
            len = str.length();
        }
        out.writeInt(len);

        if (len > 0) {
            byte[] buf = CodecUtil.getByteBuffer();
            writeString(out, str, len, buf);
        }
    }

    /**
     * 写单字节字符
     */
    public static void writeSingle(DataOutput out, String s)
        throws IOException {
        int len = -1;
        if (s != null) {
            len = s.length();
        }
        out.writeInt(len);

        int v;
        if (len > 0) {
            byte[] bytes = CodecUtil.getByteBuffer();
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

    protected static void writeString(DataOutput out, String str, int len, byte[] buf)
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

    public static String readSingle(DataInput in)
        throws IOException {
        int len = in.readInt();

        if (len < 0) {
            return null;
        }
        else if (len == 0) {
            return "";
        }
        else if (len > MAX_STRING_LENGTH) {
            throw new IOException("The string length is so big");
        }

        byte[] bytes = CodecUtil.getByteBuffer();
        int ch1;
        int read;
        if (len <= CHAR_BUFFER_SIZE) {
            in.readFully(bytes, 0, len);
            char[] chars = CodecUtil.getCharBuffer();
            for (int i = 0, j = 0; j < len; i++) {
                ch1 = (bytes[j++] & 0xFF);
                chars[i] = (char)(ch1);
            }
            return new String(chars, 0, len);
        }
        else {
            StringBuilder sb = new StringBuilder(len);
            while (len > 0) {
                read = bytes.length > len ? len : bytes.length;
                in.readFully(bytes, 0, read);
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


    public static String readString(DataInput in)
        throws IOException {
        int len = in.readInt();

        if (len < 0) {
            return null;
        }
        else if (len == 0) {
            return "";
        }
        else if (len > MAX_STRING_LENGTH) {
            throw new IOException("The string length is so big");
        }

        byte[] buf = CodecUtil.getByteBuffer();
        return readString(in, len, buf);
    }


    protected static String readString(DataInput in, int len, byte[] buf)
        throws IOException {
        int ch1, ch2;
        int read;
        if (len <= CHAR_BUFFER_SIZE) {
            char[] chars = CodecUtil.getCharBuffer();
            int k = 0;
            int l = len;
            while (l > 0) {
                read = buf.length > l * 2 ? l * 2 : buf.length;
                in.readFully(buf, 0, read);
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
                in.readFully(buf, 0, read);
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

    public static void writeBytes(DataOutput out, byte[] bytes)
        throws IOException {
        int len = -1;
        if (bytes != null) {
            len = bytes.length;
        }
        out.writeInt(len);
        if (bytes != null) {
            out.write(bytes);
        }
    }

    public static byte[] readBytes(DataInput in)
        throws IOException {
        int len = in.readInt();
        if (len < 0) {
            return null;
        }

        byte[] bytes = new byte[len];
        in.readFully(bytes);
        return bytes;
    }


    private static void writeString(DataOutput out, String str, byte[] buf)
        throws IOException {
        int len = -1;
        if (str != null) {
            len = str.length();
        }
        out.writeInt(len);

        if (len > 0) {
            writeString(out, str, len, buf);
        }
    }

    private static String readString(DataInput in, byte[] buf)
        throws IOException {
        int len = in.readInt();

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

    /**
     * 测试读入字符数组
     *
     * @param in
     * @return
     * @throws IOException
     */
    public static char[] readChars(DataInput in)
        throws IOException {
        int len = in.readInt();
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

        byte[] bytes = CodecUtil.getByteBuffer();
        int read;
        char[] chars = new char[len];
        int i = 0;
        while (len > 0) {
            read = bytes.length;
            read = read > len * 2 ? len * 2 : read;
            in.readFully(bytes, 0, read);
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
    public static void writeChars(DataOutput out, char[] chars)
        throws IOException {
        int len = -1;
        if (chars != null) {
            len = chars.length;
        }
        int v;
        out.writeInt(len);
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
            byte[] bytes = CodecUtil.getByteBuffer();
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

    static void writeClass(ObjectOutput out, Class clazz)
        throws IOException {
        writeSingle(out, clazz.getName());
    }

    public static void writeObject(ObjectOutput oos, Object object)
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
            oos.writeChar((Character)object);
        }
        else if (object instanceof Number) {
            if (object instanceof Integer) {
                writeType(oos, TYPE_INTEGER);
                oos.writeInt((Integer)object);
            }
            else if (object instanceof Long) {
                writeType(oos, TYPE_LONG);
                oos.writeLong((Long)object);
            }
            else if (object instanceof Byte) {
                writeType(oos, TYPE_BYTE);
                oos.writeByte((Byte)object);
            }
            else if (object instanceof Float) {
                writeType(oos, TYPE_FLOAT);
                oos.writeFloat((Float)object);
            }
            else if (object instanceof Double) {
                writeType(oos, TYPE_DOUBLE);
                oos.writeDouble((Double)object);
            }
            else if (object instanceof Short) {
                writeType(oos, TYPE_SHORT);
                oos.writeShort((Short)object);
            }
            else {
                writeType(oos, TYPE_OBJECT);
                oos.writeObject(object);
            }
        }
        else if (object instanceof Boolean) {
            writeType(oos, TYPE_BOOLEAN);
            oos.writeBoolean((Boolean)object);
        }
        else if (object instanceof String[]) {
            writeType(oos, TYPE_STRING_ARRAY);
            writeStringArray(oos, (String[])object);
        }
        else if (object instanceof Class) {
            writeType(oos, TYPE_CLASS);
            writeClass(oos, (Class)object);
        }
        else if (object instanceof Object[]) {
            writeType(oos, TYPE_OBJECT_ARRAY);
            writeObjectArray(oos, (Object[])object);
        }
        else if (object instanceof Externalizable) {
            writeType(oos, TYPE_EXTERNALIZABLE);
            writeExternalizable(oos, (Externalizable)object);
        }
        else {
            writeType(oos, TYPE_OBJECT);
            oos.writeObject(object);
        }
    }


    /**
     * 写出<code>Externalizable</code>对象<br>
     * 写出类名以及对象<br>
     */
    public static void writeExternalizable(ObjectOutput oos, Externalizable able)
        throws IOException {
        writeSingle(oos, able.getClass().getName());
        able.writeExternal(oos);
    }

    /**
     * 读取<code>Externalizable</code>对象
     * 读取类名以及对象<br>
     */
    public static Object readExternalizable(ObjectInput ois)
        throws IOException, ClassNotFoundException {
        String className = readSingle(ois);
        Externalizable obj = (Externalizable)Reflection.newInstance(className);
        if (obj != null) {
            obj.readExternal(ois);
        }
        return obj;
    }

    static Class readClass(DataInput in) throws IOException, ClassNotFoundException {
        String className = readSingle(in);
        return Reflection.getClass(className);
    }

//    /**
//     * 装载类
//     *
//     * @param name 类名
//     * @return 类，找不到返回<code>null</code>
//     */
//    static Class<?> loadClass(String name)
//        throws ClassNotFoundException {
//        return Reflection.loadClass(name);
//    }


    public static void writeObjectArray(ObjectOutput oos, Object[] array)
        throws IOException {
        if (array == null) {
            oos.writeInt(-1);
            return;
        }

        int len = array.length;
        oos.writeInt(len);
        writeClass(oos, array.getClass().getComponentType());
        for (int i = 0; i < len; i++) {
            writeObject(oos, array[i]);
        }
    }

    public static Object[] readObjectArray(ObjectInput ois)
        throws IOException, ClassNotFoundException {
        int len = ois.readInt();
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


    public static Object readObject(ObjectInput ois)
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
                return ois.readInt();
            case TYPE_LONG:
                return ois.readLong();
            case TYPE_BYTE:
                return ois.readByte();
            case TYPE_CHARACTER:
                return ois.readChar();
            case TYPE_BOOLEAN:
                return ois.readBoolean();
            case TYPE_SHORT:
                return ois.readShort();
            case TYPE_FLOAT:
                return ois.readFloat();
            case TYPE_DOUBLE:
                return ois.readDouble();
            case TYPE_OBJECT:
                return ois.readObject();
            case TYPE_CLASS:
                return readClass(ois);
            case TYPE_STRING_ARRAY:
                return readStringArray(ois);
            case TYPE_OBJECT_ARRAY:
                return readObjectArray(ois);
            case TYPE_EXTERNALIZABLE:
                return readExternalizable(ois);
            default:
                throw new IOException("Invalid Type:" + type);
        }
    }



    public static void writeStringArray(DataOutput oos, String[] array)
        throws IOException {
        if (array == null) {
            oos.writeInt(-1);
            return;
        }

        int len = array.length;
        oos.writeInt(len);
        byte[] buf = CodecUtil.getByteBuffer();
        for (int i = 0; i < len; i++) {
            writeString(oos, array[i], buf);
        }
    }

    public static String[] readStringArray(DataInput ois)
        throws IOException {
        int len = ois.readInt();
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
        byte[] buf = CodecUtil.getByteBuffer();
        for (int i = 0; i < len; i++) {
            array[i] = readString(ois, buf);
        }

        return array;
    }

    /**
     * 以UTF8的形式输入，注意：与DataInputStream#readUTF格式不同
     */
    public static String readUTFX(DataInput in) throws IOException {
        int strlen = in.readInt();
        if (strlen == -1) {
            return null;
        }
        else if (strlen == 0) {
            return DataUtil.EMPTY_STRING;
        }

        int utflen = in.readInt();
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

        in.readFully(bytes, 0, utflen);
        CodecUtil.fromUTFX(bytes, utflen, sb);
        return sb.toString();
    }

    //UTF8
    public static int writeUTFX(DataOutput out, String str)
            throws IOException {
        if (str == null) {
            out.writeInt(-1);
            return -1;
        }
        else if (str.length() == 0) {
            out.writeInt(0);
            return 0;
        }
        ByteBuffer buf = CodecUtil.toUTFX(str);
        int size = buf.limit();
        byte[] bytes = buf.array();
        out.write(bytes, 0, size);
        return size;
    }
}
