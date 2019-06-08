package org.femtoframework.io;

import java.io.IOException;
import java.io.Writer;

/**
 * 可以用来记录已经往给定的输出流写了多少数据的输出流
 *
 * @author fengyun
 * @version 1.00
 */
public class MeteredWriter
    extends Writer
{
    private Writer out;
    private int length;
    private int written;

    /**
     * 构造
     *
     * @param out Writer
     */
    public MeteredWriter(Writer out)
    {
        this(out, 0);
    }

    /**
     * 构造
     * <p/>
     * 已经写了多少数据
     *
     * @param out    [Writer] 输出流
     * @param length [int] 长度
     */
    public MeteredWriter(Writer out, int length)
    {
        this.out = out;
        this.length = length;
        this.written = 0;
    }

    public void write(int b) throws IOException
    {
        out.write(b);
        length++;
        written++;
    }

    public void write(char chars[]) throws IOException
    {
        this.write(chars, 0, chars.length);
    }

    public void write(char chars[], int off, int len)
        throws IOException
    {
        out.write(chars, off, len);
        length += len;
        written += len;
    }

    public void flush() throws IOException
    {
        out.flush();
    }

    public void close() throws IOException
    {
        out.close();
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
