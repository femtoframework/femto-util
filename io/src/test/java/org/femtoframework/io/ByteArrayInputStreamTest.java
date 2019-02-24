package org.femtoframework.io;

import org.femtoframework.util.nutlet.NutletUtil;
import org.junit.Test;

import java.io.ByteArrayInputStream;

import static org.femtoframework.io.NutletBean.assertEquals;
import static org.femtoframework.io.NutletBean.assertMatches;

/**
 * 测试ByteArrayInputStream
 *
 * @author fengyun
 * @version 1.00 2005-2-8 20:51:01
 */
public class ByteArrayInputStreamTest
{
    /**
     * 测试构造
     */
    @Test
    public void testByteArrayInputStream0() throws Exception
    {
        byte[] bytes = NutletUtil.getBytes();
        new ByteArrayInputStream(bytes);
    }


    /**
     * 测试构造
     */
    @Test
    public void testByteArrayInputStream1() throws Exception
    {
        byte[] bytes = NutletUtil.getBytes();
        int len = NutletUtil.getInt(bytes.length);
        int off = NutletUtil.getInt(bytes.length - len);
        new ByteArrayInputStream(bytes, off, len);
    }

//    /**
//     * 测试setBytes
//     */
//    public void testSetBytes() throws Exception
//    {
//        byte[] bytes = NutletUtil.getBytes();
//        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
//        byte[] bytes2 = NutletUtil.getBytes();
//        bais.setBytes(bytes2);
//        bais.setCount(bytes2.length);
//        byte[] check = new byte[NutletUtil.getInt(bytes2.length)];
//        IOUtil.readFully(bais, check);
//        assertMatches(check, 0, bytes2, 0, check.length);
//    }
//
//    /**
//     * 测试setCount
//     */
//    public void testSetCount() throws Exception
//    {
//        byte[] bytes = NutletUtil.getBytes();
//        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
//        int count = NutletUtil.getInt(bytes.length);
//        bais.setCount(count);
//        byte[] check = new byte[bytes.length];
//        assertEquals(count, IOUtil.readFully(bais, check));
//        assertMatches(check, 0, bytes, 0, count);
//    }
}