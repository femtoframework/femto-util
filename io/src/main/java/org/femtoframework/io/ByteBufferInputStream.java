package org.femtoframework.io;


import org.femtoframework.nio.ByteBufferPool;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class ByteBufferInputStream
    extends InputStream
{
    protected InputStream input;

    private static final int BUFFER_SIZE = ByteBufferPool.DEFAULT_BUFFER_SIZE;

    protected ByteBuffer[] buf;

    /**
     * ByteBuffer的数目
     */
    protected int count;

    /**
     * 在ByteBuffer[]中的Index
     */
    protected int index = 0;

    /**
     * ByteBuffer[Index]将要被读的位置
     */
    protected int pos = 0;

    /**
     * 总的大小
     */
    protected int size;

    /**
     * 剩下的byte数目
     */
    protected int available;

    protected int mark = -1;
    protected int markIndex = -1;
    protected int markPos = -1;

    protected ByteBufferInputStream(InputStream input)
    {
        this.input = input;
    }

    /**
     * 从input[InputStream]中读取size数量的byte
     */
    public ByteBufferInputStream(InputStream input, int size)
        throws IOException
    {
        this(input);
        init(size);
    }

    private boolean shared = false;

    /**
     * 用ByteBuffer数组构造
     *
     * @param buffer 数组
     * @param count  有效的Buffer总数
     * @param size   数据总大小
     */
    protected ByteBufferInputStream(ByteBuffer[] buffer, int count, int size)
    {
        this.buf = buffer;
        this.count = count;
        this.size = size;
        this.available = size;
        this.shared = true;
    }

    void resize(int size)
    {
        this.available += size - this.size;
        this.size = size;
    }

    protected void init(int size) throws IOException
    {
        this.size = size;
        this.count = (size / BUFFER_SIZE) + 1;
        buf = new ByteBuffer[count];
        for (int i = 0; i < count; i++) {
            buf[i] = ByteBufferPool.allocate();
        }
        fill();
    }

    /**
     * 全部读取
     */
    protected void fill() throws IOException
    {
        int left = size;
        int read = 0;
        for (int i = index; i < count && left > 0; i++) {
            ByteBuffer bb = buf[i];
            byte[] array = bb.array();
            read = Math.min(left, array.length);
            //Read fully
            IOUtil.readFully(input, array, 0, read);
            bb.position(read);
            bb.flip();
            left -= read;
        }
        available = size;
    }

    /**
     * 从Buffer中读取
     */
    public int read() throws IOException
    {
        ensureOpen();
        if (available <= 0) {
            return -1;
        }

        if (index >= count) {
            return -1;
        }

        ByteBuffer bb = buf[index];
        int limit = bb.limit();
        if (limit - pos <= 0) {
            return -1;
        }
        byte[] array = bb.array();
        int value = array[pos++] & 0xFF;

        if (limit - pos <= 0) {
            index++;
            pos = 0;
        }
//        ByteBuffer bb = buf[index];
//        if (bb.remaining() <= 0) {
//            return -1;
//        }
//        int value = bb.get() & 0xFF;
//        if (bb.remaining() <= 0) {
//            index++;
//        }
        available--;
        return value;
    }

    public int read(byte[] b, int off, int len)
        throws IOException
    {
        ensureOpen();
        if (b == null) {
            throw new NullPointerException();
        }
        else if ((off < 0) || (off > b.length) || (len < 0) ||
                 ((off + len) > b.length) || ((off + len) < 0)) {
            throw new IndexOutOfBoundsException("Offset=" + off + " Length=" + len);
        }

        if (index >= count) {
            return -1;
        }

        ByteBuffer bb = buf[index];

        int limit = bb.limit();
        int remaining = limit - pos;
        if (remaining <= 0) {
            return -1;
        }

        int read = Math.min(available, len);
        int left = read;
        int n = off;
        while (left > 0) {
            int r = Math.min(remaining, left);
            //           bb.get(b, n, r); //改为对ByteBuffer的byte数组直接操作
            byte[] array = bb.array();
            System.arraycopy(array, pos, b, n, r);
            left -= r;
            n += r;
            pos += r;
            remaining -= r;
            if (remaining <= 0) {
                index++;
                if (index < count) {
                    bb = buf[index];
                    pos = 0;
                    remaining = bb.limit();
                }
                else {
                    //数据读取完备
                    break;
                }
            }
        }
        available -= read;
        return read;
    }

    public int available()
    {
        return available;
    }

    public int size()
    {
        return size;
    }

    public boolean markSupported()
    {
        return true;
    }

    public void mark(int readAheadLimit)
    {
        mark = available;
        markIndex = index;
        markPos = pos;
    }

    public void reset()
        throws IOException
    {
        ensureOpen();
        if (mark != -1) {
            available = mark;
            index = markIndex;
            pos = markPos;
        }
    }

    /**
     * 恢复到初始状态
     */
    public void clear()
    {
        this.available = size;
    }

    public void close() throws IOException
    {
        isClosed = true;
        if (!shared) {
            for (int i = 0; i < count; i++) {
                ByteBufferPool.recycle(buf[i]);
                buf[i] = null;
            }
        }
        size = 0;
        count = 0;
        available = 0;
        pos = 0;
    }

    private boolean isClosed = false;

    private void ensureOpen() throws IOException
    {
        if (isClosed) {
            throw new IOException("The InputStream is closed.");
        }
    }
}
