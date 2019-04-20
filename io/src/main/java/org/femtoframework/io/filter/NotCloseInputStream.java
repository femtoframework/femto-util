package org.femtoframework.io.filter;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 在关闭外层流的时候不关闭被嵌套的流
 *
 * @author fengyun
 */
public class NotCloseInputStream extends FilterInputStream
{
    /**
     * Creates a <code>FilterInputStream</code>
     * by assigning the  argument <code>in</code>
     * to the field <code>this.in</code> so as
     * to remember it for later use.
     *
     * @param in the underlying input stream, or <code>null</code> if
     *           this instance is to be created without an underlying stream.
     */
    public NotCloseInputStream(InputStream in)
    {
        super(in);
    }

    /**
     * Closes this input stream and releases any system resources
     * associated with the stream.
     * This
     * method simply performs <code>in.close()</code>.
     *
     * @throws IOException if an I/O error occurs.
     * @see FilterInputStream#in
     */
    public void close() throws IOException
    {
    }
}
