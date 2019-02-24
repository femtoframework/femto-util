package org.femtoframework.io;


import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import static org.junit.Assert.*;

/**
 * 测试ByteArrayOutputStream
 *
 * @author fengyun
 * @version 1.00 2005-2-8 21:01:29
 */
public class ByteArrayOutputStreamTest
{
    /**
     * 测试默认的构造方法
     *
     * @throws Exception
     */
    @Test
    public void testByteArrayOutputStream1() throws Exception
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        assertCapacity(1024, baos);
    }

    /**
     * 如果指定的大小小有1024按照1024构造，如果小于2048 4096 8192<br>
     * 按后者的空间构造，如果超过8192，按照指定的大小构造。
     *
     * @throws Exception
     */
    @Test
    public void testByteArrayOutputStream2() throws Exception
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(888);
        assertCapacity(1024, baos);
        baos = new ByteArrayOutputStream(1025);
        assertCapacity(2048, baos);
        baos = new ByteArrayOutputStream(4000);
        assertCapacity(4096, baos);
        baos = new ByteArrayOutputStream(7555);
        assertCapacity(8192, baos);
        baos = new ByteArrayOutputStream(18575);
        assertCapacity(18575, baos);
    }

    private void assertCapacity(int capacity, ByteArrayOutputStream baos)
    {
        byte[] bytes = baos.getBytes();
        assertNotNull(bytes);
        assertEquals(capacity, bytes.length);
    }

    /**
     * 无法构造小于0的流
     *
     * @throws Exception
     */
    @Test
    public void testByteArrayOutputStream3() throws Exception
    {
        try {
            new ByteArrayOutputStream(-1);
            fail("Illegal argument");
        }
        catch (IllegalArgumentException e) {
        }
    }

    /**
     * 测试变成字符串信息（默认字符集）
     *
     * @throws Exception
     */
    @Test
    public void testToString1() throws Exception
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write("String".getBytes("GBK"));
        baos.write(" ".getBytes());
        baos.write("helloworld".getBytes());

        assertEquals("String helloworld", baos.toString());
    }

    /**
     * 测试变成字符串信息（指定字符集）
     *
     * @throws Exception
     */
    @Test
    public void testToString2() throws Exception
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write("String".getBytes());
        baos.write(" ".getBytes());
        baos.write("helloworld".getBytes());

        assertEquals("String helloworld", baos.toString("GBK"));
    }

    /**
     * 简单的写出（测试三个方法）
     *
     * @throws Exception
     */
    @Test
    public void testWrite1() throws Exception
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write("String".getBytes());
        baos.write(' ');
        byte[] bytes = "helloworld".getBytes();
        baos.write(bytes, 5, 5);

        assertEquals("String world", baos.toString("GBK"));
    }

    /**
     * 超过长度的输出（各种长度增长情况）
     *
     * @throws Exception
     */
    @Test
    public void testWrite2() throws Exception
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        assertCapacity(1024, baos);
        //输出1025的数据
        baos.write(new byte[1025]);
        assertCapacity(2048, baos);

        //输出2048的数据
        baos = new ByteArrayOutputStream();
        baos.write(new byte[2048]);
        assertCapacity(2048, baos);

        //两次输出小于4096的数据
        baos = new ByteArrayOutputStream();
        baos.write(new byte[2047]);
        assertCapacity(2048, baos);
        baos.write(new byte[1025]);
        assertCapacity(4096, baos);
        assertEquals(1025 + 2047, baos.size());

        //两次输出大于8192的数据
        baos = new ByteArrayOutputStream(8192);
        baos.write(new byte[15 * 1024]);
        assertCapacity(8192 * 2, baos);

        baos = new ByteArrayOutputStream();
        baos.write(new byte[8193]);
        assertCapacity(16384, baos);
    }

    /**
     * 无效的数据（边界条件）
     *
     * @throws Exception
     */
    @Test
    public void testWrite3() throws Exception
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] bytes = "helloworld".getBytes();
        try {
            baos.write(bytes, 5, 6);
            fail("Index out of bound");
        }
        catch (IndexOutOfBoundsException exception) {

        }

        try {
            baos.write(bytes, 11, 0);
            fail("Index out of bound");
        }
        catch (IndexOutOfBoundsException exception) {

        }

        try {
            baos.write(bytes, -1, 5);
            fail("Index out of bound");
        }
        catch (IndexOutOfBoundsException exception) {

        }

        try {
            baos.write(bytes, 0, -1);
            fail("Index out of bound");
        }
        catch (IndexOutOfBoundsException exception) {

        }

    }

    /**
     * 关闭之后的输出
     *
     * @throws Exception
     */
    @Test
    public void testWrite4() throws Exception
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] bytes = "helloworld".getBytes();
        baos.write(bytes);
        baos.close();
        try {
            baos.write(bytes);
            fail("Closed");
        }
        catch (IllegalStateException ioe) {
        }
    }

    /**
     * 释放之后应该无法再写入和输出
     *
     * @throws Exception
     */
    @Test
    public void testRelease() throws Exception
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        assertCapacity(1024, baos);
        baos.close();
        assertNull(baos.getBytes());
        assertEquals(-1, baos.getCount());

        baos = new ByteArrayOutputStream();
        baos.write("fengyun".getBytes());
        baos.close();
        assertNull(baos.getBytes());
        assertEquals(-1, baos.getCount());
    }

    /**
     * 重置
     *
     * @throws Exception
     */
    @Test
    public void testReset1() throws Exception
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        assertCapacity(1024, baos);
        assertEquals(0, baos.getCount());
        baos.reset();
        assertCapacity(1024, baos);
        assertEquals(0, baos.getCount());

        baos = new ByteArrayOutputStream();
        baos.write("fengyun".getBytes());
        int c = baos.getCount();
        assertEquals(7, baos.toByteArray().length);
        assertEquals(7, c);
        baos.reset();
        assertEquals(0, baos.getCount());
        assertEquals(0, baos.toByteArray().length);
    }

    /**
     * 关闭之后无法重置
     *
     * @throws Exception
     */
    @Test
    public void testReset2() throws Exception
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write("fengyun".getBytes());
        baos.close();
        try {
            baos.reset();
            fail("Closed");
        }
        catch (IllegalStateException ioe) {
        }
    }

    /**
     * 测试数据大小
     *
     * @throws Exception
     */
    @Test
    public void testSize() throws Exception
    {
        //一次写入，固定1024的容积
        for (int i = 0; i < 11; i++) {
            byte[] bytes = getData();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            baos.write(bytes);
            assertEquals(bytes.length, baos.size());
        }

        //一次写入，随机容积
        for (int i = 0; i < 11; i++) {
            int capacity = random.nextInt(16 * 1024);
            int size = random.nextInt(16 * 1024);
            ByteArrayOutputStream baos = new ByteArrayOutputStream(capacity);
            baos.write(new byte[size]);
            assertEquals(size, baos.size());
        }

        //多次写入，随机容积
        for (int i = 0; i < 11; i++) {
            int capacity = random.nextInt(16 * 1024);
            ByteArrayOutputStream baos = new ByteArrayOutputStream(capacity);
            int times = random.nextInt(16);
            int total = 0;
            for (int j = 0; j < times; j++) {
                int size = random.nextInt(16 * 1024);
                baos.write(new byte[size]);
                total += size;
            }
            assertEquals(total, baos.size());
        }
    }

    /**
     * TO Byte Array
     *
     * @throws Exception
     */
    @Test
    public void testToByteArray() throws Exception
    {
        //一次写入，固定1024的容积
        for (int i = 0; i < 11; i++) {
            int size = random.nextInt(16 * 1024);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            baos.write(new byte[size]);
            byte[] result = baos.toByteArray();
            assertNotNull(result);
            assertEquals(size, result.length);
        }

        //一次写入，随机容积
        for (int i = 0; i < 11; i++) {
            int capacity = random.nextInt(16 * 1024);
            int size = random.nextInt(16 * 1024);
            ByteArrayOutputStream baos = new ByteArrayOutputStream(capacity);
            baos.write(new byte[size]);
            byte[] result = baos.toByteArray();
            assertNotNull(result);
            assertEquals(size, result.length);
        }

        //多次写入，随机容积
        for (int i = 0; i < 11; i++) {
            int capacity = random.nextInt(16 * 1024);
            ByteArrayOutputStream baos = new ByteArrayOutputStream(capacity);
            int times = random.nextInt(16);
            int total = 0;
            for (int j = 0; j < times; j++) {
                int size = random.nextInt(16 * 1024);
                baos.write(new byte[size]);
                total += size;
            }
            byte[] result = baos.toByteArray();
            assertNotNull(result);
            assertEquals(total, result.length);
        }
    }

    private Random random = new Random();

    private byte[] getData()
    {
        int size = random.nextInt(64 * 1024);
        byte[] data = new byte[size];
        random.nextBytes(data);
        return data;
    }

    /**
     * 输出ByteArray内容
     *
     * @throws Exception
     */
    @Test
    public void testWriteTo() throws Exception
    {
        java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream();

        //一次写入，固定1024的容积
        for (int i = 0; i < 11; i++) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] data = getData();
            baos.write(data);
            baos.writeTo(out);
            NutletBean.assertEquals(data, out.toByteArray());
            out.reset();
        }

        //一次写入，随机容积
        for (int i = 0; i < 11; i++) {
            int capacity = random.nextInt(16 * 1024);
            ByteArrayOutputStream baos = new ByteArrayOutputStream(capacity);

            byte[] data = getData();
            baos.write(data);
            baos.writeTo(out);
            NutletBean.assertEquals(data, out.toByteArray());
            out.reset();
        }

        //多次写入，随机容积
        for (int i = 0; i < 11; i++) {
            int capacity = random.nextInt(16 * 1024);
            ByteArrayOutputStream baos = new ByteArrayOutputStream(capacity);
            java.io.ByteArrayOutputStream base = new java.io.ByteArrayOutputStream(capacity);
            int times = random.nextInt(16);
            for (int j = 0; j < times; j++) {
                byte[] data = getData();
                baos.write(data);
                base.write(data);
            }
            baos.writeTo(out);
            //参考两个java.io.ByteArrayOutputStream的数据
            NutletBean.assertEquals(base.toByteArray(), out.toByteArray());
            out.reset();
        }
    }

    /**
     * 比较输入和输出
     *
     * @param input
     * @param data
     */
    private void compare(InputStream input, java.io.ByteArrayOutputStream data)
        throws IOException
    {
        java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream();
        IOUtil.copy(input, out);
        NutletBean.assertEquals(out.toByteArray(), data.toByteArray());
    }

    /**
     * 比较输入和输出
     *
     * @param input
     * @param data
     */
    private void compare(InputStream input, byte[] data)
        throws IOException
    {
        java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream();
        out.write(data);
        compare(input, out);
    }

    /**
     * 获取共享输入流
     *
     * @throws Exception
     */
    @Test
    public void testGetInputStream1() throws Exception
    {
        //一次写入，固定1024的容积
        for (int i = 0; i < 11; i++) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] data = getData();
            baos.write(data);
            compare(baos.getInputStream(), data);
        }

        //一次写入，随机容积
        for (int i = 0; i < 11; i++) {
            int capacity = random.nextInt(16 * 1024);
            ByteArrayOutputStream baos = new ByteArrayOutputStream(capacity);

            byte[] data = getData();
            baos.write(data);
            compare(baos.getInputStream(), data);
        }

        //多次写入，随机容积
        for (int i = 0; i < 11; i++) {
            int capacity = random.nextInt(16 * 1024);
            ByteArrayOutputStream baos = new ByteArrayOutputStream(capacity);
            java.io.ByteArrayOutputStream base = new java.io.ByteArrayOutputStream(capacity);
            int times = random.nextInt(16);
            for (int j = 0; j < times; j++) {
                byte[] data = getData();
                baos.write(data);
                base.write(data);
            }
            //参考两个java.io.ByteArrayOutputStream的数据
            compare(baos.getInputStream(), base);
        }
    }

    /**
     * 获取共享输入流（无法在关闭之后调用，或者多次调用）
     *
     * @throws Exception
     */
    @Test
    public void testGetInputStream2() throws Exception
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.close();
        try {
            baos.getInputStream();
            fail("Illegal state");
        }
        catch (IllegalStateException ex) {
        }

        baos = new ByteArrayOutputStream();
        baos.getInputStream();
        baos.getInputStream();

        baos = new ByteArrayOutputStream();
        baos.write(getData());
        baos.close();
        try {
            baos.getInputStream();
            fail("Illegal state");
        }
        catch (Exception ex) {
        }

        baos = new ByteArrayOutputStream();
        baos.write(getData());
        baos.getInputStream();
        baos.getInputStream();
    }
}