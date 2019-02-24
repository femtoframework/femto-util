package org.femtoframework.io;

import java.io.IOException;
import java.io.Reader;

/**
 * 空的Reader
 *
 * @author fengyun
 * @version 1.00 May 23, 2003 12:41:40 PM
 */
public class NoneReader
    extends Reader
{

    /**
     * Read a single character.  This method will block until a character is
     * available, an I/O error occurs, or the end of the stream is reached.
     * <p/>
     * <p> Subclasses that intend to support efficient single-character input
     * should override this method.
     *
     * @return The character read, as an integer in the range 0 to 65535
     *         (<tt>0x00-0xffff</tt>), or -1 if the end of the stream has
     *         been reached
     * @throws IOException If an I/O error occurs
     */
    public int read() throws IOException
    {
        return -1;
    }

    /**
     * Read characters into a portion of an array.  This method will block
     * until some input is available, an I/O error occurs, or the end of the
     * stream is reached.
     *
     * @param cbuf Destination buffer
     * @param off  Offset at which to start storing characters
     * @param len  Maximum number of characters to read
     * @return The number of characters read, or -1 if the end of the
     *         stream has been reached
     * @throws IOException If an I/O error occurs
     */
    public int read(char cbuf[], int off, int len) throws IOException
    {
        return -1;
    }

    /**
     * Close the stream.  Once a stream has been closed, further read(),
     * ready(), mark(), or reset() invocations will throw an IOException.
     * Closing a previously-closed stream, however, has no effect.
     *
     * @throws IOException If an I/O error occurs
     */
    public void close() throws IOException
    {
    }
}
