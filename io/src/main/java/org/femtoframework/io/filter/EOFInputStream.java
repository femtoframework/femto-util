package org.femtoframework.io.filter;

import java.io.EOFException;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 当读取结束时会抛出EOFException的输入流
 *
 * @author fengyun
 */
public class EOFInputStream
    extends FilterInputStream
{
    /**
     * 构造
     *
     * @param in
     */
    public EOFInputStream(InputStream in)
    {
        super(in);
    }

    /**
     * Reads the next byte of data from this input stream. The value
     * byte is returned as an <code>int</code> in the range
     * <code>0</code> to <code>255</code>. If no byte is available
     * because the end of the stream has been reached, the value
     * <code>-1</code> is returned. This method blocks until input data
     * is available, the end of the stream is detected, or an exception
     * is thrown.
     * <p/>
     * This method
     * simply performs <code>in.read()</code> and returns the result.
     *
     * @return the next byte of data, or <code>-1</code> if the end of the
     *         stream is reached.
     * @throws IOException if an I/O error occurs.
     * @see FilterInputStream#in
     */
    public int read() throws IOException
    {
        int read = in.read();
        if (read == -1) {
            throw new EOFException();
        }
        return read;
    }

    /**
     * Reads up to <code>byte.length</code> bytes of data from this
     * input stream into an array of bytes. This method blocks until some
     * input is available.
     * <p/>
     * This method simply performs the call
     * <code>read(b, 0, b.length)</code> and returns
     * the  result. It is important that it does
     * <i>not</i> do <code>in.read(b)</code> instead;
     * certain subclasses of  <code>FilterInputStream</code>
     * depend on the implementation strategy actually
     * used.
     *
     * @param b the buffer into which the data is read.
     * @return the total number of bytes read into the buffer, or
     *         <code>-1</code> if there is no more data because the end of
     *         the stream has been reached.
     * @throws IOException if an I/O error occurs.
     * @see FilterInputStream#read(byte[], int, int)
     */
    public int read(byte b[]) throws IOException
    {
        return read(b, 0, b.length);
    }

    /**
     * Reads up to <code>len</code> bytes of data from this input stream
     * into an array of bytes. This method blocks until some input is
     * available.
     * <p/>
     * This method simply performs <code>in.read(b, off, len)</code>
     * and returns the result.
     *
     * @param b   the buffer into which the data is read.
     * @param off the start offset of the data.
     * @param len the maximum number of bytes read.
     * @return the total number of bytes read into the buffer, or
     *         <code>-1</code> if there is no more data because the end of
     *         the stream has been reached.
     * @throws IOException if an I/O error occurs.
     * @see FilterInputStream#in
     */
    public int read(byte b[], int off, int len) throws IOException
    {
        int read = in.read(b, off, len);
        if (read == -1) {
            throw new EOFException();
        }
        return read;
    }
}
