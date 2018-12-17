/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.femtoframework.io;

import java.io.*;

/**
 * Couple differences with java.io.ByteArrayOutputStream
 * 1. It is able to return the raw byte array instead of making a copy when you need to access the entire array.
 * 2. Able to create an InputStream to read from it.
 * 3. Start initial capacity with 256 bytes
 */
public class ByteArrayOutputStream extends OutputStream implements ByteData
{

    /**
     * The buffer where data is stored.
     */
    protected byte buf[];

    /**
     * The number of valid bytes in the buffer.
     */
    protected int count;

    /**
     * Check to make sure that the stream has not been closed
     */
    private void ensureOpen()
    {
        if (buf == null) {
            throw new IllegalStateException("Byte array output stream closed");
        }
    }

    /**
     * Creates a new byte array output stream. The buffer capacity is
     * initially 256 bytes, though its size increases if necessary.
     */
    public ByteArrayOutputStream()
    {
        this(256);
    }

    /**
     * Creates a new byte array output stream by given size
     */
    public ByteArrayOutputStream(int size)
    {
        if (size < 0) {
            throw new IllegalArgumentException("Negative initial size: " + size);
        }
        this.buf = new byte[size];
    }

    public ByteArrayOutputStream(byte[] buf) {
        this.buf = buf;
    }

    private void ensureCapacity(int newCount)
    {
        byte newBuf[];
        int newSize = buf.length << 1;
        while (newSize < newCount) {
            newSize = newSize << 1;
        }

        newBuf = new byte[newSize];

        if (count > 0) {
            System.arraycopy(buf, 0, newBuf, 0, count);
        }
        buf = newBuf;
    }

    /**
     * Writes the specified byte to this byte array output stream.
     *
     * @param b the byte to be written.
     */
    public void write(int b)
    {
        ensureOpen();
        int newcount = count + 1;
        if (newcount > buf.length) {
            ensureCapacity(newcount);
        }
        buf[count] = (byte)b;
        count = newcount;
    }

    /**
     * Writes <code>len</code> bytes from the specified byte array
     * starting at offset <code>off</code> to this byte array output stream.
     *
     * @param b   the data.
     * @param off the start offset in the data.
     * @param len the number of bytes to write.
     */
    public void write(byte b[], int off, int len)
    {
        ensureOpen();
        if ((off < 0) || (off > b.length) || (len < 0) ||
                ((off + len) > b.length) || ((off + len) < 0)) {
            throw new IndexOutOfBoundsException("Length of bytes:" + b.length + " off:" + off + " len:" + len);
        }
        else if (len == 0) {
            return;
        }
        int newcount = count + len;
        if (newcount > buf.length) {
            ensureCapacity(newcount);
        }
        System.arraycopy(b, off, buf, count, len);
        count = newcount;
    }

    /**
     * Writes the complete contents of this byte array output stream to
     * the specified output stream argument, as if by calling the output
     * stream's write method using <code>out.write(buf, 0, count)</code>.
     *
     * @param out the output stream to which to write the data.
     * @throws java.io.IOException if an I/O error occurs.
     */
    public void writeTo(OutputStream out)
            throws IOException
    {
        if (count > 0) {
            out.write(buf, 0, count);
        }
    }

    /**
     * Resets the <code>count</code> field of this byte array output
     * stream to zero, so that all currently accumulated output in the
     * ouput stream is discarded. The output stream can be used again,
     * reusing the already allocated buffer space.
     *
     * @see java.io.ByteArrayInputStream#count
     */
    public void reset()
    {
        ensureOpen();
        count = 0;
    }

    /**
     * Creates a newly allocated byte array. Its size is the current
     * size of this output stream and the valid contents of the buffer
     * have been copied into it.
     *
     * @return the current contents of this output stream, as a byte array.
     * @see java.io.ByteArrayOutputStream#size()
     */
    public byte[] toByteArray()
    {
        if (count >= 0) {
            byte newbuf[] = new byte[count];
            System.arraycopy(buf, 0, newbuf, 0, count);
            return newbuf;
        }
        else {
            throw new IllegalStateException("Closed");
        }
    }

    /**
     * Returns the current size of the buffer.
     *
     * @return the value of the <code>count</code> field, which is the number
     *         of valid bytes in this output stream.
     * @see java.io.ByteArrayOutputStream#count
     */
    public int size()
    {
        return count;
    }

    /**
     * Converts the buffer's contents into a string, translating bytes into
     * characters according to the platform's default character encoding.
     *
     * @return String translated from the buffer's contents.
     * @since JDK1.1
     */
    public String toString()
    {
        return new String(buf, 0, count);
    }

    /**
     * Converts the buffer's contents into a string, translating bytes into
     * characters according to the specified character encoding.
     *
     * @param enc a character-encoding name.
     * @return String translated from the buffer's contents.
     * @throws java.io.UnsupportedEncodingException
     *          If the named encoding is not supported.
     * @since JDK1.1
     */
    public String toString(String enc) throws UnsupportedEncodingException
    {
        return new String(buf, 0, count, enc);
    }

    /**
     * Closes this output stream and releases any system resources
     * associated with this stream. A closed stream cannot perform
     * output operations and cannot be reopened.
     * <p/>
     */
    public void close()
    {
        buf = null;
        count = -1;
    }

    public InputStream getInputStream() throws IOException
    {
        ensureOpen();
        return new ByteArrayInputStream(buf, 0, count);
    }

    /**
     * Return current bytes
     *
     * @return
     */
    public byte[] getBytes()
    {
        return buf;
    }

    /**
     * Return current count
     *
     * @return
     */
    public int getCount()
    {
        return size();
    }
}
