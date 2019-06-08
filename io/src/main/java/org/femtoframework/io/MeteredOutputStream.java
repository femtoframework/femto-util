package org.femtoframework.io;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * 可以用来记录已经往给定的输出流写了多少数据的输出流
 *
 * @author fengyun
 * @version 1.00
 */
public class MeteredOutputStream
    extends FilterOutputStream
{
    private int length;
    private int written;

    /**
     * 构造
     *
     * @param out OutputStream
     */
    public MeteredOutputStream(OutputStream out)
    {
        this(out, 0);
    }

    /**
     * 构造
     * <p/>
     * 已经写了多少数据
     *
     * @param out    [OutputStream] 输出流
     * @param length [int] 长度
     */
    public MeteredOutputStream(OutputStream out, int length)
    {
        super(out);
        this.length = length;
        this.written = 0;
    }

    /**
     * Writes the specified <code>byte</code> to this output stream.
     * <p/>
     * The <code>write</code> method of <code>FilterOutputStream</code>
     * calls the <code>write</code> method of its underlying output stream,
     * that is, it performs <tt>out.write(b)</tt>.
     * <p/>
     * Implements the abstract <tt>write</tt> method of <tt>OutputStream</tt>.
     *
     * @param b the <code>byte</code>.
     * @throws IOException if an I/O error occurs.
     */
    public void write(int b) throws IOException
    {
        out.write(b);
        written++;
        length ++;
    }

    /**
     * Writes <code>len</code> bytes from the specified
     * <code>byte</code> array starting at offset <code>off</code> to
     * this output stream.
     * <p/>
     * The <code>write</code> method of <code>FilterOutputStream</code>
     * calls the <code>write</code> method of one argument on each
     * <code>byte</code> to output.
     * <p/>
     * Note that this method does not call the <code>write</code> method
     * of its underlying input stream with the same arguments. Subclasses
     * of <code>FilterOutputStream</code> should provide a more efficient
     * implementation of this method.
     *
     * @param b   the data.
     * @param off the start offset in the data.
     * @param len the number of bytes to write.
     * @throws IOException if an I/O error occurs.
     * @see FilterOutputStream#write(int)
     */
    public void write(byte b[], int off, int len) throws IOException
    {
        out.write(b, off, len);
        written += len;
        length += len;
    }


    public int length()
    {
        return length;
    }

    /**
     * 返回写出去的长度
     *
     * @return [int] 长度
     */
    public int getWritten()
    {
        return written;
    }
}
