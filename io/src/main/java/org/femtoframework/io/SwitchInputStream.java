package org.femtoframework.io;

import java.io.FilterInputStream;
import java.io.InputStream;

/**
 * 可以替换输入流的过滤输入流
 *
 * @author fengyun
 * @version Dec 25, 2002 10:14:48 PM
 */

public class SwitchInputStream
    extends FilterInputStream
{

    /**
     * 构造
     */
    public SwitchInputStream()
    {
        super(null);
    }

    /**
     * 构造
     *
     * @param in
     */
    public SwitchInputStream(InputStream in)
    {
        super(in);
    }

    /**
     * 设置新的输入流
     *
     * @param in 新的输入流
     */
    public void setInput(InputStream in)
    {
        //Close Old one
        this.in = in;
    }

    /**
     * 清除输入流
     */
    public void clearInput()
    {
        this.in = null;
    }

    /**
     * 返回输入流
     *
     * @return
     */
    public InputStream getInput()
    {
        return in;
    }
}
