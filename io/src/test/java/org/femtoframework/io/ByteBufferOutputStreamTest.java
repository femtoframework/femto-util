package org.femtoframework.io;


import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * @author <a href="mailto:yqchen@naesasoft.com">yqchen</a>
 * @version 1.00 2005-2-21 10:20:28
 */
public class ByteBufferOutputStreamTest
{
    /**
     * 测试构造函数,不带参数的
     */
    @Test
    public void testConstructor1()
    {
        ByteBufferOutputStream bbos = new ByteBufferOutputStream();
        assertNotNull(bbos);
        NutletBean.assertEquals(bbos.count(), 1);
    }

    /**
     * 测试构造函数,带参数的
     */
    @Test
    public void testConstructor2()
    {
        int count = 3;
        ByteBufferOutputStream bbos = new ByteBufferOutputStream(count);
        assertNotNull(bbos);
        NutletBean.assertEquals(bbos.count(), count);
    }

    /**
     * test write(int b);
     *
     * @throws Exception
     */
    @Test
    public void testWriteSingle1() throws Exception
    {
        ByteBufferOutputStream bbos = new ByteBufferOutputStream();
        byte b = 65;
        bbos.write(b);
        NutletBean.assertEquals(bbos.size(), 1);
    }

    /**
     * test write(byte[] bytes);
     *
     * @throws Exception
     */
    @Test
    public void testWriteBlock1() throws Exception
    {
        ByteBufferOutputStream bbos = new ByteBufferOutputStream();
        byte[] bytes = {(byte) 1, (byte) 2};
        bbos.write(bytes);
        NutletBean.assertEquals(bbos.size(), bytes.length);
    }

    /**
     * 测试getInputStream
     *
     * @throws Exception
     */
    @Test
    public void testGetInputStream() throws Exception
    {
        ByteBufferOutputStream bbos = new ByteBufferOutputStream();
        byte[] bytes = {(byte) 1, (byte) 2};
        bbos.write(bytes);

        ByteBufferInputStream bbis = bbos.getInputStream();
        assertNotNull(bbis);
        NutletBean.assertEquals(bbis.count, 1);

        byte[] bytesForRead = new byte[1024];
        int read = bbis.read(bytesForRead);
        NutletBean.assertEquals(read, 2);
        NutletBean.assertEquals(bytesForRead[0], (byte)1);
        NutletBean.assertEquals(bytesForRead[1], (byte)2);
    }
}
