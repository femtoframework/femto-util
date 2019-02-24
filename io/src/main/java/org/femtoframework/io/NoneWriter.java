package org.femtoframework.io;

import java.io.IOException;
import java.io.Writer;

/**
 * 不写出任何东西
 *
 * @author fengyun
 * @version 1.00 Apr 25, 2002 12:30:09 AM
 */
public class NoneWriter
    extends Writer
{
    public void write(int b) throws IOException
    {
        //Do nothing
    }

    public void write(char[] chars, int off, int len)
        throws IOException
    {
        //Do nothing
    }

    public void flush()
    {
    }

    public void close()
    {
    }
}
