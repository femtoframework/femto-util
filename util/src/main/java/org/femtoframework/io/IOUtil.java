package org.femtoframework.io;

import java.io.*;
import java.lang.ref.SoftReference;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;

/**
 * IO Utilities
 */
public class IOUtil {

    public static final int BYTE_BUFFER_SIZE = 4096;
    public static final int CHAR_BUFFER_SIZE = 4096;

    protected IOUtil() {
    }

    private static ThreadLocal<SoftReference<ByteBuffer>> byteLocal = new ThreadLocal<SoftReference<ByteBuffer>>();
    private static ThreadLocal<SoftReference<CharBuffer>> charLocal = new ThreadLocal<SoftReference<CharBuffer>>();

    //Buffer for each thread
    protected static ByteBuffer getByteBuffer() {
        SoftReference<ByteBuffer> soft = byteLocal.get();
        ByteBuffer buffer;
        if (soft == null || (buffer = soft.get()) == null) {
            buffer = ByteBuffer.allocate(BYTE_BUFFER_SIZE);
            soft = new SoftReference<>(buffer);
            byteLocal.set(soft);
        }
        buffer.clear();
        return buffer;
    }

    //Buffer for each thread
    protected static CharBuffer getCharBuffer() {
        SoftReference<CharBuffer> soft = charLocal.get();
        CharBuffer buffer;
        if (soft == null || (buffer = soft.get()) == null) {
            buffer = CharBuffer.allocate(CHAR_BUFFER_SIZE);
            soft = new SoftReference<CharBuffer>(buffer);
            charLocal.set(soft);
        }
        buffer.clear();
        return buffer;
    }

    /**
     * Copy all data from input to output
     *
     * @param input InputStream
     * @param output OutputStream
     * @return How many bytes got copied.
     * @throws IOException Exception
     */
    public static int copy(InputStream input, OutputStream output) throws IOException {
        return copy(input, output, -1);
    }

    /**
     * Copy all data from input to output, reuse the given buffer
     *
     * @param input InputStream
     * @param output OutputStream
     * @param buf Given buffer
     * @return How many bytes got copied.
     * @throws IOException Exception
     */
    public static int copy(InputStream input, OutputStream output, byte[] buf) throws IOException {
        return copy(input, output, -1, buf);
    }

    /**
     * Copy limited size data from input to output
     *
     * @param input InputStream
     * @param output OutputStream
     * @param size Given size
     * @return How many bytes got copied.
     * @throws IOException Exception
     */
    public static int copy(InputStream input, OutputStream output, int size)
            throws IOException {
        if (size < 0) {
            size = Integer.MAX_VALUE;
        }
        if (size == 0) {
            return 0;
        }
        ByteBuffer buffer = getByteBuffer();
        byte[] buf = buffer.array();
        return copy0(input, output, size, buf);
    }

    /**
     * Copy limited size data from input to output, and reuse given buffer
     *
     * @param input InputStream
     * @param output OutputStream
     * @param size Given size
     * @return How many bytes got copied.
     * @throws IOException Exception
     */
    public static int copy(InputStream input,
                                 OutputStream output,
                                 int size,
                                 byte[] buf)
            throws IOException {
        if (size < 0) {
            size = Integer.MAX_VALUE;
        }
        if (size == 0) {
            return 0;
        }
        return copy0(input, output, size, buf);
    }

    private static int copy0(InputStream input,
                                   OutputStream output,
                                   int size,
                                   byte[] buf)
            throws IOException {
        int total = 0;
        int read;
        int len = buf.length;
        while (size > 0) {
            read = size > len ? len : size;
            read = input.read(buf, 0, read);
            if (read <= 0) {
                break;
            }
            output.write(buf, 0, read);
            total += read;
            if (size != Integer.MAX_VALUE) {
                size -= read;
            }
        }
        return total;
    }


    /**
     * Copy all data from reader to writer
     *
     * @param reader Reader
     * @param writer Writer
     * @return How many chars got copied.
     * @throws IOException Exception
     */
    public static int copy(Reader reader, Writer writer) throws IOException {
        return copy(reader, writer, -1);
    }

    /**
     * Copy all data from reader to output, reuse the given buffer
     *
     * @param reader Reader
     * @param writer Writer
     * @param buf Given buffer
     * @return How many chars got copied.
     * @throws IOException Exception
     */
    public static int copy(Reader reader, Writer writer, char[] buf) throws IOException {
        return copy(reader, writer, -1, buf);
    }

    /**
     * Copy limited size data from reader to output
     *
     * @param reader Reader
     * @param writer Writer
     * @param size Given size
     * @return How many chars got copied.
     * @throws IOException Exception
     */
    public static int copy(Reader reader, Writer writer, int size)
            throws IOException {
        if (size < 0) {
            size = Integer.MAX_VALUE;
        }
        if (size == 0) {
            return 0;
        }
        CharBuffer buffer = getCharBuffer();
        char[] buf = buffer.array();
        return copy0(reader, writer, size, buf);
    }

    /**
     * Copy limited size data from input to output, and reuse given buffer
     *
     * @param reader Reader
     * @param writer Writer
     * @param size Given size
     * @return How many chars got copied.
     * @throws IOException Exception
     */
    public static int copy(Reader reader,
                           Writer writer,
                           int size,
                           char[] buf)
            throws IOException {
        if (size < 0) {
            size = Integer.MAX_VALUE;
        }
        if (size == 0) {
            return 0;
        }
        return copy0(reader, writer, size, buf);
    }

    private static int copy0(Reader reader,
                             Writer writer,
                             int size,
                             char[] buf)
            throws IOException {
        int total = 0;
        int read;
        int len = buf.length;
        while (size > 0) {
            read = size > len ? len : size;
            read = reader.read(buf, 0, read);
            if (read <= 0) {
                break;
            }
            writer.write(buf, 0, read);
            total += read;
            if (size != Integer.MAX_VALUE) {
                size -= read;
            }
        }
        return total;
    }

    /**
     * Close the closeable quietly
     *
     * @param closeable Closeable
     */
    public static void close(Closeable closeable) {
        try {
            closeable.close();
        }
        catch(IOException ioe) {
            //
        }
    }

    /**
     * 从输入流中读取给定长度的数据
     *
     * @param input 输入流
     * @param bytes 要填充的数俎
     * @param off   起始位置
     * @param len   长度
     */
    public static int readFully(InputStream input,
                                byte[] bytes, int off, int len)
            throws IOException
    {
        if (len < 0) {
            throw new IndexOutOfBoundsException();
        }

        int n = 0;
        int read;
        while (n < len) {
            read = input.read(bytes, off + n, len - n);
            if (read <= 0) {
                break;
            }
            n += read;
        }
        return n;
    }

    /**
     * 从输入流中读取数据填充数组
     *
     * @param input 输入流
     * @param bytes 要填充的数俎
     */
    public static int readFully(InputStream input, byte[] bytes)
            throws IOException
    {
        return readFully(input, bytes, 0, bytes.length);
    }

    /**
     * 从Reader中读取给定长度的数据
     *
     * @param reader Reader
     * @param chars  要填充的数俎
     * @param off    起始位置
     * @param len    长度
     */
    public static int readFully(Reader reader,
                                char[] chars, int off, int len)
            throws IOException
    {
        if (len < 0) {
            throw new IndexOutOfBoundsException();
        }

        int n = 0;
        int read;
        while (n < len) {
            read = reader.read(chars, off + n, len - n);
            if (read <= 0) {
                break;
            }
            n += read;
        }
        return n;
    }

    /**
     * 从Reader中读取数据填充数组
     *
     * @param reader Reader
     * @param chars  要填充的数俎
     */
    public static int readFully(Reader reader, char[] chars)
            throws IOException
    {
        return readFully(reader, chars, 0, chars.length);
    }

    /**
     * 读取一行的数据
     *
     * @param input 输入流
     * @param bytes 数据
     * @param off   起始位置
     * @param len   长度
     */
    public static int readLine(InputStream input,
                               byte[] bytes, int off, int len)
            throws IOException {
        if (len <= 0) {
            return 0;
        }

        int count = 0, c;
        while ((c = input.read()) != -1) {
            bytes[off++] = (byte)c;
            count++;
            if (c == '\n' || count == len) {
                break;
            }
        }
        return count > 0 ? count : -1;
    }

    /**
     * 读取一行的数据
     *
     * @param reader 输入流
     * @param chars  数据
     * @param off    起始位置
     * @param len    长度
     */
    public static int readLine(Reader reader,
                               char[] chars, int off, int len)
            throws IOException {
        if (len <= 0) {
            return 0;
        }

        int count = 0, c;
        while ((c = reader.read()) != -1) {
            chars[off++] = (char)c;
            count++;
            if (c == '\n' || count == len) {
                break;
            }
        }
        return count > 0 ? count : -1;
    }

    /**
     * Skip given number of bytes
     *
     * @param is
     * @param n
     * @throws java.io.IOException
     */
    public static int skipFully(InputStream is, int n)
            throws IOException {
        //skip data
        ByteBuffer byteBuf = getByteBuffer();
        byte[] buf = byteBuf.array();
        int bufLen;
        int left = n;
        while (left > 0) {
            bufLen = is.read(buf, 0, Math.min(BYTE_BUFFER_SIZE, left));
            if (bufLen > 0) {
                left -= bufLen;
            }
            else {
                break;
            }
        }
        return n - left;
    }


    /**
     * Skip given number of bytes
     *
     * @param reader
     * @param n
     * @throws java.io.IOException
     */
    public static int skipFully(Reader reader, int n)
            throws IOException {
        //skip data
        CharBuffer charBuf = getCharBuffer();
        char[] buf = charBuf.array();
        int bufLen;
        int left = n;
        while (left > 0) {
            bufLen = reader.read(buf, 0, Math.min(CHAR_BUFFER_SIZE, left));
            if (bufLen > 0) {
                left -= bufLen;
            }
            else {
                break;
            }
        }
        return n - left;
    }
}
