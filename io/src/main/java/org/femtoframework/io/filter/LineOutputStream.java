package org.femtoframework.io.filter;

import org.femtoframework.util.CharUtil;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;


/**
 * This class is to support writing out Strings as a sequence of bytes
 * terminated by a CRLF sequence. The String must contain only US-ASCII
 * characters.<p>
 * <p/>
 * The expected use is to write out RFC822 style headers to an output
 * stream. <p>
 */

public class LineOutputStream extends FilterOutputStream
{
    private static final byte[] NEWLINE = CharUtil.CRLF_BYTES;

    private Charset enc;

    public LineOutputStream(OutputStream out)
    {
        this(out, null);
    }

    public LineOutputStream(OutputStream out, Charset enc)
    {
        super(out);
        this.enc = enc;
    }

    public void writeln(String s) throws IOException
    {
        writeln(s, enc);
    }

    public void write(String s) throws IOException
    {
        write(s, enc);
    }

    /**
     * 输出一行的数据
     *
     * @param bytes
     * @throws IOException
     */
    public void writeln(byte[] bytes) throws IOException
    {
        writeln(bytes, 0, bytes.length);
    }

    /**
     * 输出一行的数据
     *
     * @param bytes
     * @throws IOException
     */
    public void writeln(byte[] bytes, int off, int len) throws IOException
    {
        write(bytes, off, len);
        writeln();
    }

    public void writeln(String s, Charset charset) throws IOException
    {
        writeln(s.getBytes(charset));
    }

    public void write(String s, Charset charset) throws IOException
    {
        write(s.getBytes(charset));
    }

    public void writeln() throws IOException
    {
        out.write(NEWLINE);
    }
}
