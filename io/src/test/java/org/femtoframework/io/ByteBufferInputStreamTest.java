package org.femtoframework.io;

import org.femtoframework.nio.ByteBufferPool;
import org.junit.Test;

import java.io.IOException;
import java.nio.ByteBuffer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

/**
 * @version 1.00 2005-2-18 15:45:35
 */
public class ByteBufferInputStreamTest
{
    /**
     * 测试构造函数    public ByteBufferInputStream(InputStream input, int size)
     */
    @Test
    public void testConstructor1()
        throws Exception
    {
        ByteBufferInputStream bbis = new ByteBufferInputStream(null, 0);
        assertNotNull(bbis);
        assertEquals(0, bbis.size());
    }


    /**
     * 测试构造函数       ByteBufferInputStream(ByteBuffer[] buffer, int count, int size)
     */
    @Test
    public void testConstructor2()
        throws Exception
    {
        ByteBufferInputStream bbis = new ByteBufferInputStream(null, 1, 100);
        assertNotNull(bbis);
        assertEquals(100, bbis.size());
    }

    /**
     * 测试 read() ,单个ByteBuffer的情况
     *
     * @throws Exception
     */
    @Test
    public void testReadSingle1() throws Exception
    {
        ByteBuffer[] bbs = getSingleByteBuffer();

        ByteBufferInputStream bbis = new ByteBufferInputStream(bbs, 1, SIZE_SINGLE);

        for (int i = 0; i < SIZE_SINGLE; i++) {
            byte b = (byte) bbis.read();
            assertEquals(b, i);
        }
    }

    /**
     * 测试 read() ,多个ByteBuffer的情况
     *
     * @throws Exception
     */
    @Test
    public void testReadSingle2() throws Exception
    {
        int count = 3;
        int UNIT_SIZE = 1024;
        int size = UNIT_SIZE * 2 + 3;
        //组建 ByteBuffer数组,填充之
        ByteBuffer[] bb = getMulitByteBuffer();

        ByteBufferInputStream bbis = new ByteBufferInputStream(bb, count, size);

        for (int i = 0; i < size; i++) {
            byte v = (byte) (i % 256);
            byte b = (byte) bbis.read();
            assertEquals(b, v);
        }
    }

    int SIZE_SINGLE = 100;

    /**
     * 测试 read(byte[] b, int off, int len) ,单个ByteBuffer的情况
     *
     * @throws Exception
     */
    @Test
    public void testReadBlock1() throws Exception
    {
        ByteBuffer[] bbs = getSingleByteBuffer();


        ByteBufferInputStream bbis = new ByteBufferInputStream(bbs, 1, SIZE_SINGLE);

        byte[] bytes = new byte[SIZE_SINGLE];
        int read = bbis.read(bytes, 0, SIZE_SINGLE);
        assertEquals(read, SIZE_SINGLE);

        for (int i = 0; i < SIZE_SINGLE; i++) {
            assertEquals(bytes[i], i);
        }
    }

    /**
     * 测试  read(byte[] b, int off, int len) ,多个ByteBuffer的情况
     *
     * @throws Exception
     */
    @Test
    public void testReadBlock2() throws Exception
    {
        int count = 3;
        int UNIT_SIZE = 1024;
        int size = UNIT_SIZE * 2 + 3;
        //组建 ByteBuffer数组,填充之
        ByteBuffer[] bb = getMulitByteBuffer();

        ByteBufferInputStream bbis = new ByteBufferInputStream(bb, count, size);

        byte[] bytes = new byte[size];
        int read = bbis.read(bytes, 0, size);
        assertEquals(read, size);

        for (int i = 0; i < size; i++) {
            byte v = (byte) (i % 256);
            assertEquals(bytes[i], v);
        }
    }

    private ByteBuffer[] getMulitByteBuffer()
    {
        int count = 3;
        int size = 1024 * 2 + 3;
        //组建 ByteBuffer数组,填充之
        ByteBuffer[] bb = new ByteBuffer[count];
        for (int i = 0; i < count; i++) {
            bb[i] = ByteBufferPool.allocate();
        }
        for (int i = 0; i < size; i++) {
            byte v = (byte) (i % 256);
            if (i < 1024) {
                bb[0].put(v);
            }
            else if (i < 2048) {
                bb[1].put(v);
            }
            else {
                bb[2].put(v);
            }
        }

        for (int i = 0; i < count; i++) {
            bb[i].flip();
        }
        return bb;
    }

    private ByteBuffer[] getSingleByteBuffer()
    {
        ByteBuffer[] bbs = {ByteBufferPool.allocate()};
        for (int i = 0; i < SIZE_SINGLE; i++) {
            bbs[0].put((byte) i);
        }
        bbs[0].flip();
        return bbs;
    }

    /**
     * 测试close以后再读时会抛出一个IO异常
     *
     * @throws Exception
     */
    @Test
    public void testClose()
        throws Exception
    {
        ByteBuffer[] bbs = getSingleByteBuffer();
        ByteBufferInputStream bbis = new ByteBufferInputStream(bbs, 1, SIZE_SINGLE);
        bbis.close();

        try {
            bbis.read();
            fail("InputStream should be closed!");
        }
        catch (IOException e) {

        }
    }

    /**
     * 测试mark和reset
     * 1.单个ByteBuffer的情况
     * 2.先读5个byte,mark,再读3(这个数是随意的,只要小于remaining)个byte
     *
     * @throws Exception
     */
    @Test
    public void testMark1() throws Exception
    {
        ByteBuffer[] bbs = getSingleByteBuffer();
        ByteBufferInputStream bbis = new ByteBufferInputStream(bbs, 1, SIZE_SINGLE);
        int readFirst = 5;
        int readAhead = 3;
        for (int i = 0; i < readFirst; i++) {
            bbis.read();
        }
        bbis.mark(readAhead);
        for (int i = 0; i < readAhead; i++) {
            bbis.read();
        }

        bbis.reset();
        assertEquals(bbis.available(), SIZE_SINGLE - readFirst);
        int b = bbis.read();
        assertEquals(b, readFirst);
    }

    /**
     * 测试mark和reset
     * 1.多个ByteBuffer的情况(ByteBuffer数目为3)
     * 2.先读完1个ByteBuffer和第二个的5个byte,mark,再读3(这个数是随意的,只要小于remaining)个byte
     *
     * @throws Exception
     */
    @Test
    public void testMark2() throws Exception
    {
        int count = 3;
        int UNIT_SIZE = 1024;
        int size = UNIT_SIZE * 2 + 3;
        //组建 ByteBuffer数组,填充之
        ByteBuffer[] bbs = getMulitByteBuffer();

        ByteBufferInputStream bbis = new ByteBufferInputStream(bbs, count, size);
        int readFirst = UNIT_SIZE + 5;
        int readAhead = 3;
        for (int i = 0; i < readFirst; i++) {
            bbis.read();
        }
        bbis.mark(readAhead);
        for (int i = 0; i < readAhead + UNIT_SIZE; i++) {
            bbis.read();
        }

        bbis.reset();
        assertEquals(bbis.available(), size - readFirst);
        int b = bbis.read();
        assertEquals(b, readFirst % 256);
    }


    /**
     * 测试mark和reset
     * 1.多个ByteBuffer的情况(ByteBuffer数目为3)
     * 2.先读完1个ByteBuffer和第二个的5个byte,mark,再读3(这个数是随意的,只要小于remaining)个byte
     *
     * 注: 和上面的case不同之处: read是按块读的
     *
     * @throws Exception
     */
    @Test
    public void testMark3() throws Exception
    {
        int count = 3;
        int UNIT_SIZE = 1024;
        int size = UNIT_SIZE * 2 + 3;
        //组建 ByteBuffer数组,填充之
        ByteBuffer[] bbs = getMulitByteBuffer();

        ByteBufferInputStream bbis = new ByteBufferInputStream(bbs, count, size);
        int readFirst = UNIT_SIZE + 5;
        int readAhead = 3;

        byte[] bytes = new byte[size];
        bbis.read(bytes,0,readFirst);
        bbis.mark(readAhead);
        for (int i = 0; i < readAhead + UNIT_SIZE; i++) {
            bbis.read();
        }

        bbis.reset();
        assertEquals(bbis.available(), size - readFirst);
        bbis.read(bytes,readFirst,size - readFirst);

        for (int i = 0; i < size; i++) {
            byte v = (byte) (i % 256);
            assertEquals(bytes[i], v);
        }
    }
}
