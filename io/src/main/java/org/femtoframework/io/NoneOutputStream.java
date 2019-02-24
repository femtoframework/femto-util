package org.femtoframework.io;

import java.io.IOException;
import java.io.OutputStream;

/**
 * 不作任何输出的输出流
 *
 * @author fengyun
 * @version 1.00 Jan 24, 2002 10:37:57 PM
 */

public class NoneOutputStream
    extends OutputStream
{
    public void write(int b) throws IOException
    {
        //Do nothing
    }

    public void write(byte[] bytes, int off, int len)
        throws IOException
    {
        //Do nothing
    }
}
