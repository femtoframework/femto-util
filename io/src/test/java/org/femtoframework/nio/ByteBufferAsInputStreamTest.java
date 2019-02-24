package org.femtoframework.nio;

import org.femtoframework.io.IOUtil;
import org.femtoframework.util.nutlet.NutletUtil;
import org.junit.Test;

import java.nio.ByteBuffer;

import static org.femtoframework.io.NutletBean.assertMatches;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * @author fengyun
 * @version 1.00 2004-12-27 15:11:48
 */

public class ByteBufferAsInputStreamTest
{
    /**
     * 测试构造
     *
     * @throws Exception
     */
    @Test
    public void testByteBufferAsInputStream0() throws Exception
    {
        try {
            new ByteBufferAsInputStream(null);
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
    @Test
    public void testByteBufferAsInputStream1() throws Exception
    {
        ByteBuffer buffer = ByteBuffer.wrap(new byte[1024]);
        new ByteBufferAsInputStream(buffer);
    }

    @Test
    public void testRead() throws Exception
    {
        ByteBuffer buffer = ByteBuffer.wrap(new byte[1024]);
        ByteBufferAsOutputStream bbaos = new ByteBufferAsOutputStream(buffer);
        bbaos.write('a');
        byte[] bytes = NutletUtil.getBytes(NutletUtil.getInt(512));
        bbaos.write(bytes);
        IOUtil.close(bbaos);

        ByteBufferAsInputStream bbais = new ByteBufferAsInputStream(buffer);
        byte[] data = new byte[1024];
        int read = bbais.read(data);
        assertEquals(bytes.length + 1, read);
        assertEquals('a', data[0]);
        assertMatches(bytes, 0, data, 1, bytes.length);
    }
}