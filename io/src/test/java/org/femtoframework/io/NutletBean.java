package org.femtoframework.io;


import junit.framework.AssertionFailedError;
import junit.framework.TestCase;
import org.femtoframework.util.ArrayUtil;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.*;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * 用于测试的实例Bean
 *
 * @author fengyun
 * @version 1.00 2004-3-19 13:35:56
 */
public class NutletBean implements Cloneable, Externalizable {

    private static transient Random random = new Random();

    public String field1;
    public int field2;
    public byte field3;
    public char field4;
    public long field5;
    public float field6;
    public double field7;
    public boolean field8;


    public String[] field9;
    public int[] field10;
    public byte[] field11;
    public char[] field12;
    public long[] field13;
    public float[] field14;
    public double[] field15;
    public boolean[] field16;

    public NutletBean(NutletBean bean) {
        field1 = bean.field1;
        field2 = bean.field2;
        field3 = bean.field3;
        field4 = bean.field4;
        field5 = bean.field5;
        field6 = bean.field6;
        field7 = bean.field7;
        field8 = bean.field8;

        field9 = bean.field9;
        field10 = bean.field10;

        field11 = bean.field11;

        field12 = bean.field12;

        field13 = bean.field13;

        field14 = bean.field14;

        field15 = bean.field15;
        field16 = bean.field16;
    }

    public NutletBean() {
        field1 = String.valueOf(random.nextLong());
        field2 = random.nextInt();
        field3 = (byte)random.nextInt();
        field4 = (char)random.nextInt();
        field5 = random.nextLong();
        field6 = random.nextFloat();
        field7 = random.nextDouble();
        field8 = random.nextBoolean();

        field9 = new String[random.nextInt(10) + 2];
        for (int i = 0; i < field9.length; i++) {
            field9[i] = String.valueOf(random.nextLong());
        }

        field10 = new int[random.nextInt(10) + 2];
        for (int i = 0; i < field10.length; i++) {
            field10[i] = random.nextInt();
        }

        field11 = new byte[random.nextInt(10) + 2];
        for (int i = 0; i < field11.length; i++) {
            field11[i] = (byte)random.nextInt();
        }

        field12 = new char[random.nextInt(10) + 2];
        for (int i = 0; i < field12.length; i++) {
            field12[i] = (char)random.nextInt();
        }

        field13 = new long[random.nextInt(10) + 2];
        for (int i = 0; i < field13.length; i++) {
            field13[i] = random.nextLong();
        }

        field14 = new float[random.nextInt(10) + 2];
        for (int i = 0; i < field14.length; i++) {
            field14[i] = random.nextFloat();
        }

        field15 = new double[random.nextInt(10) + 2];
        for (int i = 0; i < field15.length; i++) {
            field15[i] = random.nextDouble();
        }

        field16 = new boolean[random.nextInt(10) + 2];
        for (int i = 0; i < field16.length; i++) {
            field16[i] = random.nextBoolean();
        }
    }

    /**
     * Clone
     *
     * @return
     */
    public Object clone() throws CloneNotSupportedException {
        NutletBean bean = (NutletBean)super.clone();
        bean.field1 = field1;
        bean.field9 = field9;
        bean.field10 = field10;
        bean.field11 = field11;
        bean.field12 = field12;
        bean.field13 = field13;
        bean.field14 = field14;
        bean.field15 = field15;
        bean.field16 = field16;
        return bean;
    }


    /**
     * 判断两个对象是否相等
     *
     * @param obj 对象
     * @return
     */
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof NutletBean) {
            NutletBean bean = (NutletBean)obj;
            if (!field1.equals(bean.field1)) {
                return false;
            }
            if (field2 != bean.field2) {
                return false;
            }
            if (field3 != bean.field3) {
                return false;
            }
            if (field4 != bean.field4) {
                return false;
            }
            if (field5 != bean.field5) {
                return false;
            }
            if (field6 != bean.field6) {
                return false;
            }
            if (field7 != bean.field7) {
                return false;
            }
            if (field8 != bean.field8) {
                return false;
            }
            if (!Arrays.equals(field9, bean.field9)) {
                return false;
            }
            if (!Arrays.equals(field10, bean.field10)) {
                return false;
            }
            if (!Arrays.equals(field11, bean.field11)) {
                return false;
            }
            if (!Arrays.equals(field12, bean.field12)) {
                return false;
            }
            if (!Arrays.equals(field13, bean.field13)) {
                return false;
            }
            if (!Arrays.equals(field14, bean.field14)) {
                return false;
            }
            if (!Arrays.equals(field15, bean.field15)) {
                return false;
            }
            return true;
        }
        return false;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(64);
        sb.append("Field1=").append(field1).append(';');
        sb.append("Field2=").append(field2).append(';');
        sb.append("Field3=").append(field3).append(';');
        sb.append("Field4=").append(field4).append(';');
        sb.append("Field5=").append(field5).append(';');
        sb.append("Field6=").append(field6).append(';');
        sb.append("Field7=").append(field7).append(';');
        sb.append("Field8=").append(field8).append(';');

        return sb.toString();
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeUTF(field1);
        out.writeInt(field2);
        out.writeByte(field3);
        out.writeChar(field4);
        out.writeLong(field5);
        out.writeFloat(field6);
        out.writeDouble(field7);
        out.writeBoolean(field8);

        out.writeObject(field9);
        out.writeObject(field10);
        out.writeObject(field11);
        out.writeObject(field12);
        out.writeObject(field13);
        out.writeObject(field14);
        out.writeObject(field15);
        out.writeObject(field16);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        field1 = in.readUTF();
        field2 = in.readInt();
        field3 = in.readByte();
        field4 = in.readChar();
        field5 = in.readLong();
        field6 = in.readFloat();
        field7 = in.readDouble();
        field8 = in.readBoolean();

        field9 = (String[])in.readObject();
        field10 = (int[])in.readObject();
        field11 = (byte[])in.readObject();
        field12 = (char[])in.readObject();
        field13 = (long[])in.readObject();
        field14 = (float[])in.readObject();
        field15 = (double[])in.readObject();
        field16 = (boolean[])in.readObject();
    }



    /**
     * 判断指定的对象是否属于某一类
     */
    public static void assertClass(Object obj, Class clazz)
    {
        assertNotNull(obj);
        assertNotNull(clazz);
        Class clazz2 = obj.getClass();
        assertEquals(clazz2, clazz);
    }

    /**
     * 判断指定的对象是否属于某一类
     */
    public static void assertClass(Object obj, String className)
    {
        assertNotNull(obj);
        Class clazz = obj.getClass();
        assertEquals(clazz.getName(), className);
    }

    /**
     * 判断指定的对象是否属于相同的类
     */
    public static void assertClass(Object obj1, Object obj2)
    {
        assertNotNull(obj2);
        assertClass(obj1, obj2.getClass());
    }

    /**
     * Asserts that two objects are equal. If they are not
     * an AssertionFailedError is thrown.
     */
    public static void assertEquals(Object expected, Object actual)
    {
        assertEquals((String)null, expected, actual);
    }

    /**
     * Asserts that two objects are equal. If they are not
     * an AssertionFailedError is thrown with the given message.
     */
    public static void assertEquals(String message, Object expected, Object actual)
    {
        if (expected == null && actual == null) {
            return;
        }
        else if (expected == null || actual == null) {
            TestCase.assertEquals(message, expected, actual);
            return;
        }

        boolean isArray1 = expected.getClass().isArray();
        boolean isArray2 = actual.getClass().isArray();
        if (isArray1 && isArray2) {
            if (expected instanceof Object[] && actual instanceof Object[]) {
                if (message != null) {
                    assertEquals(message, (Object[])expected, (Object[])actual);
                }
                else {
                    assertEquals((Object[])expected, (Object[])actual);
                }
            }
            else {
                assertClass(expected, actual);
                if (expected instanceof boolean[]) {
                    assertEquals((boolean[])expected, (boolean[])actual);
                }
                else if (expected instanceof byte[]) {
                    assertEquals((byte[])expected, (byte[])actual);
                }
                else if (expected instanceof int[]) {
                    assertEquals((int[])expected, (int[])actual);
                }
                else if (expected instanceof char[]) {
                    assertEquals((char[])expected, (char[])actual);
                }
                else if (expected instanceof long[]) {
                    assertEquals((long[])expected, (long[])actual);
                }
                else if (expected instanceof float[]) {
                    assertEquals((float[])expected, (float[])actual);
                }
                else if (expected instanceof double[]) {
                    assertEquals((double[])expected, (double[])actual);
                }
            }
        }
        else {
            TestCase.assertEquals(message, expected, actual);
        }
    }

    /**
     * 判断两个数组中的数组大小和每个元素是否相等<br>
     * （为了提高速度，没有检查位置和长度，呵呵）
     *
     * @param a1 数组1
     * @param a2 数组2
     */
    public static void assertEquals(boolean[] a1, boolean[] a2)
    {
        assertTrue(Arrays.equals(a1, a2));
    }

//    /**
//     * 判断两个数组中的数组大小和每个元素是否相等<br>
//     * （为了提高速度，没有检查位置和长度，呵呵）
//     *
//     * @param a1     数组1
//     * @param off1   起始位置1
//     * @param a2     数组2
//     * @param off2   起始位置2
//     * @param length 比较长度
//     */
//    public static void assertMatches(boolean[] a1, int off1,
//                                     boolean[] a2, int off2,
//                                     int length)
//    {
//        assertTrue(ArrayUtil.matches(a1, off1, a2, off2, length));
//    }

    /**
     * 判断两个数组中的数组大小和每个元素是否相等<br>
     * （为了提高速度，没有检查位置和长度，呵呵）
     *
     * @param a1 数组1
     * @param a2 数组2
     */
    public static void assertEquals(byte[] a1, byte[] a2)
    {
        assertTrue(Arrays.equals(a1, a2));
    }

    /**
     * 判断两个数组中的数组大小和每个元素是否相等<br>
     * （为了提高速度，没有检查位置和长度，呵呵）
     *
     * @param a1     数组1
     * @param off1   起始位置1
     * @param a2     数组2
     * @param off2   起始位置2
     * @param length 比较长度
     */
    public static void assertMatches(byte[] a1, int off1,
                                     byte[] a2, int off2,
                                     int length)
    {
        assertTrue(ArrayUtil.matches(a1, off1, a2, off2, length));
    }

    /**
     * 判断两个数组中的数组大小和每个元素是否相等<br>
     * （为了提高速度，没有检查位置和长度，呵呵）
     *
     * @param a1 数组1
     * @param a2 数组2
     */
    public static void assertEquals(char[] a1, char[] a2)
    {
        assertTrue(Arrays.equals(a1, a2));
    }

//    /**
//     * 判断两个数组中的数组大小和每个元素是否相等<br>
//     * （为了提高速度，没有检查位置和长度，呵呵）
//     *
//     * @param a1     数组1
//     * @param off1   起始位置1
//     * @param a2     数组2
//     * @param off2   起始位置2
//     * @param length 比较长度
//     */
//    public static void assertMatches(char[] a1, int off1,
//                                     char[] a2, int off2,
//                                     int length)
//    {
//        assertTrue(ArrayUtil.matches(a1, off1, a2, off2, length));
//    }

    /**
     * 判断两个数组中的数组大小和每个元素是否相等<br>
     * （为了提高速度，没有检查位置和长度，呵呵）
     *
     * @param a1 数组1
     * @param a2 数组2
     */
    public static void assertEquals(int[] a1, int[] a2)
    {
        assertTrue(Arrays.equals(a1, a2));
    }

//    /**
//     * 判断两个数组中的数组大小和每个元素是否相等<br>
//     * （为了提高速度，没有检查位置和长度，呵呵）
//     *
//     * @param a1     数组1
//     * @param off1   起始位置1
//     * @param a2     数组2
//     * @param off2   起始位置2
//     * @param length 比较长度
//     */
//    public static void assertMatches(int[] a1, int off1,
//                                     int[] a2, int off2,
//                                     int length)
//    {
//        assertTrue(ArrayUtil.matches(a1, off1, a2, off2, length));
//    }

    /**
     * 判断两个数组中的数组大小和每个元素是否相等<br>
     * （为了提高速度，没有检查位置和长度，呵呵）
     *
     * @param a1 数组1
     * @param a2 数组2
     */
    public static void assertEquals(long[] a1, long[] a2)
    {
        assertTrue(Arrays.equals(a1, a2));
    }

//    /**
//     * 判断两个数组中的数组大小和每个元素是否相等<br>
//     * （为了提高速度，没有检查位置和长度，呵呵）
//     *
//     * @param a1     数组1
//     * @param off1   起始位置1
//     * @param a2     数组2
//     * @param off2   起始位置2
//     * @param length 比较长度
//     */
//    public static void assertMatches(long[] a1, int off1,
//                                     long[] a2, int off2,
//                                     int length)
//    {
//        assertTrue(ArrayUtil.matches(a1, off1, a2, off2, length));
//    }

    /**
     * 判断两个数组中的数组大小和每个元素是否相等<br>
     * （为了提高速度，没有检查位置和长度，呵呵）
     *
     * @param a1 数组1
     * @param a2 数组2
     */
    public static void assertEquals(float[] a1, float[] a2)
    {
        assertTrue(Arrays.equals(a1, a2));
    }

//    /**
//     * 判断两个数组中的数组大小和每个元素是否相等<br>
//     * （为了提高速度，没有检查位置和长度，呵呵）
//     *
//     * @param a1     数组1
//     * @param off1   起始位置1
//     * @param a2     数组2
//     * @param off2   起始位置2
//     * @param length 比较长度
//     */
//    public static void assertMatches(float[] a1, int off1,
//                                     float[] a2, int off2,
//                                     int length)
//    {
//        assertTrue(ArrayUtil.matches(a1, off1, a2, off2, length));
//    }

    /**
     * 判断两个数组中的数组大小和每个元素是否相等<br>
     * （为了提高速度，没有检查位置和长度，呵呵）
     *
     * @param a1 数组1
     * @param a2 数组2
     */
    public static void assertEquals(double[] a1, double[] a2)
    {
        assertTrue(Arrays.equals(a1, a2));
    }

//    /**
//     * 判断两个数组中的数组大小和每个元素是否相等<br>
//     * （为了提高速度，没有检查位置和长度，呵呵）
//     *
//     * @param a1     数组1
//     * @param off1   起始位置1
//     * @param a2     数组2
//     * @param off2   起始位置2
//     * @param length 比较长度
//     */
//    public static void assertMatches(double[] a1, int off1,
//                                     double[] a2, int off2,
//                                     int length)
//    {
//        assertTrue(ArrayUtil.matches(a1, off1, a2, off2, length));
//    }

    /**
     * 判断两个数组中的数组大小和每个元素是否相等<br>
     * （为了提高速度，没有检查位置和长度，呵呵）
     *
     * @param a1 数组1
     * @param a2 数组2
     */
    public static void assertEquals(Object[] a1, Object[] a2)
    {
        assertTrue(Arrays.equals(a1, a2));
    }

    /**
     * 判断两个数组中的数组大小和每个元素是否相等<br>
     * （为了提高速度，没有检查位置和长度，呵呵）
     *
     * @param a1 数组1
     * @param a2 数组2
     */
    public static void assertEquals(String message, Object[] a1, Object[] a2)
    {
        assertTrue(message, Arrays.equals(a1, a2));
    }

//    /**
//     * 判断两个数组中的数组大小和每个元素是否相等<br>
//     * （为了提高速度，没有检查位置和长度，呵呵）
//     *
//     * @param a1     数组1
//     * @param off1   起始位置1
//     * @param a2     数组2
//     * @param off2   起始位置2
//     * @param length 比较长度
//     */
//    public static void assertMatches(Object[] a1, int off1,
//                                     Object[] a2, int off2,
//                                     int length)
//    {
//        assertTrue(ArrayUtil.matches(a1, off1, a2, off2, length));
//    }

    public static void assertEquals(Map map, Object key, Object value)
    {
        assertNotNull(map);
        Object value2 = map.get(key);
        assertEquals(value2, value);
    }

    public static void assertContains(Map map, Object key)
    {
        assertNotNull(map);
        assertNotNull(map.get(key));
    }

    public static void assertEquals(List list, int index, Object value)
    {
        assertContains(list, index);
        Object value2 = list.get(index);
        assertEquals(value2, value);
    }

    /**
     * 鍒ゆ柇鎸囧畾鐨凩ist涓槸鍚﹀瓨鍦ㄧ浉搴擨ndex鐨勫€�
     */
    public static void assertContains(List list, int index)
    {
        assertNotNull(list);
        assertTrue(index >= 0 && index < list.size());
    }

    public static void assertEquals(Enumeration enum1, Enumeration enum2)
    {
        if (enum1 == enum2) {
            return;
        }
        if (enum1 == null || enum2 == null) {
            throw new AssertionFailedError("One of the enumeration is null, enum1:"
                    + enum1 + " enum2:" + enum2);
        }
        while (enum1.hasMoreElements() && enum2.hasMoreElements()) {
            assertEquals(enum1.nextElement(), enum2.nextElement());
        }
        assertFalse(enum1.hasMoreElements());
        assertFalse(enum2.hasMoreElements());
    }

    /**
     * 鍒ゆ柇涓や釜Iterator涓殑鍏冪礌鏄惁鐩哥瓑
     */
    public static void assertEquals(Iterator it1, Iterator it2)
    {
        if (it1 == it2) {
            return;
        }
        if (it1 == null || it2 == null) {
            throw new AssertionFailedError("One of the iterator is null, it1:"
                    + it1 + " it2:" + it2);
        }
        while (it1.hasNext() && it2.hasNext()) {
            assertEquals(it1.next(), it2.next());
        }
        assertFalse(it1.hasNext());
        assertFalse(it2.hasNext());
    }

    /**
     * 鍒ゆ柇涓や釜Iterator涓殑鍏冪礌鏄惁鐩哥瓑
     */
    public static void assertEquals(Iterator it1, Enumeration enum2)
    {
        if (it1 == enum2) {
            return;
        }
        if (it1 == null || enum2 == null) {
            throw new AssertionFailedError("One of the iterator is null, it1:"
                    + it1 + " enum2:" + enum2);
        }
        while (it1.hasNext() && enum2.hasMoreElements()) {
            assertEquals(it1.next(), enum2.nextElement());
        }
        assertFalse(it1.hasNext());
        assertFalse(enum2.hasMoreElements());
    }
}