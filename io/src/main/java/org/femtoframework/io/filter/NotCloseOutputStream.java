package org.femtoframework.io.filter;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * 在关闭外层流的时候不关闭被嵌套的流
 *
 * @author fengyun
 */
public class NotCloseOutputStream extends FilterOutputStream
{
    /**
     * Creates an output stream filter built on top of the specified
     * underlying output stream.
     *
     * @param out the underlying output stream to be assigned to
     *            the field <tt>this.out</tt> for later use, or
     *            <code>null</code> if this instance is to be
     *            created without an underlying stream.
     */
    public NotCloseOutputStream(OutputStream out)
    {
        super(out);
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
    }

    /**
     * Closes this output stream and releases any system resources
     * associated with the stream.
     * <p/>
     * The <code>close</code> method of <code>FilterOutputStream</code>
     * calls its <code>flush</code> method, and then calls the
     * <code>close</code> method of its underlying output stream.
     *
     * @throws IOException if an I/O error occurs.
     * @see FilterOutputStream#flush()
     * @see FilterOutputStream#out
     */
    public void close() throws IOException
    {
        try {
            flush();
        }
        catch (IOException ignored) {
        }
        //不关闭
    }
}
