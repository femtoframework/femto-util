package org.femtoframework.nio;

import org.femtoframework.io.IOUtil;
import org.femtoframework.util.nutlet.NutletUtil;
import org.junit.Test;

import java.io.IOException;
import java.nio.ByteBuffer;

import static org.femtoframework.io.NutletBean.assertMatches;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * @author fengyun
 * @version 1.00 2004-12-27 14:51:22
 */

public class ByteBufferAsOutputStreamTest
{
    /**
     * 测试构造
     *
     * @throws Exception
     */
    @Test
    public void testByteBufferAsOutputStream0() throws Exception
    {
        try {
            new ByteBufferAsOutputStream(null);
            fail("IllegalArgument");
        }
        catch (IllegalArgumentException iae) {
        }
    }

    /**
     * 测试构造
     *
     * @throws Exception
     */
    public void testByteBufferAsOutputStream1() throws Exception
    {
        ByteBuffer buffer = ByteBuffer.wrap(new byte[1024]);
        new ByteBufferAsOutputStream(buffer);
    }

    /**
     * 测试写出
     *
     * @throws Exception
     */
    public void testWrite1() throws Exception
    {
        ByteBuffer buffer = ByteBuffer.wrap(new byte[1024]);
        ByteBufferAsOutputStream bbaos = new ByteBufferAsOutputStream(buffer);
        bbaos.write('a');
        byte[] bytes = NutletUtil.getBytes(NutletUtil.getInt(512));
        bbaos.write(bytes);
        IOUtil.close(bbaos);

        buffer.flip();
        byte[] data = new byte[buffer.remaining()];
        buffer.get(data);
        assertEquals('a', data[0]);
        assertMatches(bytes, 0, data, 1, bytes.length);
    }


    /**
     * 测试Buffer溢出
     *
     * @throws Exception
     */
    public void testWrite2() throws Exception
    {
        ByteBuffer buffer = ByteBuffer.wrap(new byte[1024]);
        ByteBufferAsOutputStream bbaos = new ByteBufferAsOutputStream(buffer);
        bbaos.write('a');
        byte[] bytes = NutletUtil.getBytes(1024);
        try {
            bbaos.write(bytes);
            fail("IOException");
        }
        catch (IOException ioe) {
        }
    }
}