package org.femtoframework.io.filter;


import org.femtoframework.util.CharUtil;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * 用CRLF方式输出
 *
 * @author fengyun
 */
public class CRLFOutputStream extends FilterOutputStream
{
    protected int lastByte = -1;

    private static final byte[] NEWLINE = CharUtil.CRLF_BYTES;

    public CRLFOutputStream(OutputStream os)
    {
        super(os);
    }

    public void write(int b) throws IOException
    {
        if (b == '\r') {
            out.write(NEWLINE);
        }
        else if (b == '\n') {
            if (lastByte != '\r') {
                out.write(NEWLINE);
            }
        }
        else {
            out.write(b);
        }
        lastByte = b;
    }

    public void write(byte b[], int off, int len)
        throws IOException
    {
        int start = off;
        len += off;
        for (int i = start; i < len; i++) {
            if (b[i] == '\r') {
                out.write(b, start, i - start);
                out.write(NEWLINE);
                start = i + 1;
            }
            else if (b[i] == '\n') {
                if (lastByte != '\r') {
                    out.write(b, start, i - start);
                    out.write(NEWLINE);
                }
                start = i + 1;
            }
            lastByte = b[i];
        }
        if ((len - start) > 0) {
            out.write(b, start, len - start);
        }
    }

    /*
     * Just write out a new line, something similar to out.println()
     */
    public void writeln() throws IOException
    {
        out.write(NEWLINE);
    }

    /*
     * Just write out a new line, something similar to out.println()
     */
    public void writeln(byte[] bytes) throws IOException
    {
        writeln(bytes, 0, bytes.length);
    }

    /*
     * Just write out a new line, something similar to out.println()
     */
    public void writeln(byte[] bytes, int off, int len) throws IOException
    {
        out.write(bytes, off, len);
        out.write(NEWLINE);
    }

    /*
     * Just write out a new line, something similar to out.println()
     */
    public void println()
    {
        try {
            out.write(NEWLINE);
        }
        catch (IOException ioe) {
        }
    }

    /*
     * Just write out a new line, something similar to out.println()
     */
    public void print(byte[] bytes)
    {
        print(bytes, 0, bytes.length);
    }

    /*
     * Just write out a new line, something similar to out.println()
     */
    public void print(byte[] bytes, int off, int len)
    {
        try {
            out.write(bytes, off, len);
        }
        catch (IOException e) {
        }
    }

    /*
     * Just write out a new line, something similar to out.println()
     */
    public void println(byte[] bytes)
    {
        println(bytes, 0, bytes.length);
    }

    /*
     * Just write out a new line, something similar to out.println()
     */
    public void println(byte[] bytes, int off, int len)
    {
        try {
            out.write(bytes, off, len);
            out.write(NEWLINE);
        }
        catch (IOException e) {
        }
    }

    /*
     * Just write out a new line, something similar to out.println()
     */
    public void println(String str)
    {
        try {
            out.write(str.getBytes());
            out.write(NEWLINE);
        }
        catch (IOException e) {
        }
    }

    /*
     * Just write out a new line, something similar to out.println()
     */
    public void print(String str)
    {
        try {
            out.write(str.getBytes());
        }
        catch (IOException e) {
        }
    }
}
