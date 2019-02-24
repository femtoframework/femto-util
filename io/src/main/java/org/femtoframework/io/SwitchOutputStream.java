package org.femtoframework.io;

import java.io.FilterOutputStream;
import java.io.OutputStream;

/**
 * 可以替换输出流的过滤输出流
 *
 * @author fengyun
 * @version Dec 25, 2002 10:17:33 PM
 */
public class SwitchOutputStream
    extends FilterOutputStream
{
    /**
     * 构造
     *
     * @param out 输出流
     */
    public SwitchOutputStream(OutputStream out)
    {
        super(out);
    }

    /**
     * 构造
     */
    public SwitchOutputStream()
    {
        super(null);
    }

    /**
     * 设置新的输出流
     *
     * @param out
     */
    public void setOutput(OutputStream out)
    {
        this.out = out;
    }

    /**
     * 返回输出流
     *
     * @return
     */
    public OutputStream getOutput()
    {
        return out;
    }

    /**
     * 清除输出流
     */
    public void clearOutput()
    {
        this.out = null;
    }
}
