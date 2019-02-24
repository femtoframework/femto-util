package org.femtoframework.io;


import org.femtoframework.nio.ByteBufferPool;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

public class ByteBufferOutputStream
    extends OutputStream
    implements ByteData
{
    /**
     * The buffer where data is stored.
     */
    protected ByteBuffer buf[];

    /**
     * 写了多少个byte?
     */
    protected int size = 0;

    /**
     * ByteBuffer的数量
     */
    protected int count;


    /**
     * ByteBuffer的大小
     */
    protected static final int BUFF_SIZE = ByteBufferPool.DEFAULT_BUFFER_SIZE;

    /**
     * 当前所在ByteBuffer的索引
     */

    protected int index = 0;

//    private boolean isClosed = false;

    public ByteBufferOutputStream()
    {
        this(1);
    }

    /**
     * @param count ByteBuffer的数量
     *              <p/>
     *              ByteBuffer的大小 默认是1K
     */
    public ByteBufferOutputStream(int count)
    {
        if (count < 0) {
            throw new IllegalArgumentException("Negative initial size: "
                                               + count);
        }
        buf = new ByteBuffer[count];
        for (int i = 0; i < count; i++) {
            buf[i] = ByteBufferPool.allocate();
        }
        this.count = count;
    }

    public void write(int b)
        throws IOException
    {
//        ensureOpen();

        int newsize = size + 1;
        int free = newsize - capacity();
        if (free > 0) {
            //扩容
            expandCapacity(count + (free / BUFF_SIZE) + 1);
        }

        ByteBuffer bb = buf[index];
        if (bb.remaining() <= 0) {
            bb = buf[++index];
        }

        bb.put((byte)(b & 0XFF));
        size++;
    }

    public void write(byte b[], int off, int len)
        throws IOException
    {
//        ensureOpen();

        if (b == null) {
            //should throw a exception?
            return;
        }
        if ((off < 0) || (off > b.length) || (len < 0) ||
            ((off + len) > b.length) || ((off + len) < 0)) {
            throw new IndexOutOfBoundsException("off=" + off + ", len=" + len);
        }
        else if (len == 0) {
            return;
        }

        int newsize = size + len;
        int free = newsize - capacity();
        if (free > 0) {
            //扩容
            expandCapacity(count + (free / BUFF_SIZE) + 1);
        }
        //填充
        int n = off;
        int left = len;
        while (left > 0) {
            ByteBuffer bb = buf[index];
            int available = bb.remaining();
            int written = Math.min(available, left);
            bb.put(b, n, written);
            left -= written;
            n += written;
            if (bb.remaining() <= 0) {
                index++;
            }
        }
        size = newsize;
    }

    /**
     * 扩容
     */
    protected void expandCapacity(int newcount)
    {
        ByteBuffer[] newbuf = new ByteBuffer[newcount];
        System.arraycopy(buf, 0, newbuf, 0, count);
        for (int i = count; i < newcount; i++) {
            newbuf[i] = ByteBufferPool.allocate();
        }
        buf = newbuf;
        count = newcount;
    }

    public void writeTo(OutputStream out)
        throws IOException
    {
//        ensureOpen();
        int max = index < count ? index : count - 1;
        for (int i = 0; i <= max; i++) {
            ByteBuffer bb = buf[i];
            byte[] array = bb.array();
            int size = bb.position();
            out.write(array, 0, size);
        }
        out.flush();
    }

    public void reset()
    {
        int max = index < count ? index : count - 1;
        for (int i = 0; i <= max; i++) {
            ByteBuffer bb = buf[i];
            bb.clear();
        }
        index = 0;
        size = 0;
    }

    /**
     * 注意ByteBuffer[].length 并不与实际的ByteBuffer相同
     */
    public ByteBuffer[] toByteBuffer()
    {
        return buf;
    }

    /**
     * 当前的容量
     */
    protected int capacity()
    {
        return count * BUFF_SIZE;
    }

    /**
     * 返回ByteBuffer的数量
     */
    public int count()
    {
        return count;
    }

    /**
     * 返回字节的数目
     */
    public int size()
    {
        return size;
    }

    /**
     * 返回byte数
     */
    public String toString()
    {
        return String.valueOf(size);
    }

    public void close() throws IOException
    {
        for (int i = 0; i < count; i++) {
            ByteBufferPool.recycle(buf[i]);
            buf[i] = null;
        }
        size = 0;
        count = 0;
    }


    private ByteBufferInputStream input = null;
    private boolean shared = false;

    public ByteBufferInputStream getInputStream() throws IOException
    {
        if (shared) {
            throw new IOException("Only one input stream is supported");
        }
        for (int i = 0; i < count; i++) {
            ByteBuffer bb = buf[i];
            bb.flip();
        }
        input = new ByteBufferInputStream(buf, count, size);
        return input;
    }
}
