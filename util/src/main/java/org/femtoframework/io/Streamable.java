package org.femtoframework.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 基于输入输出流的对象流化处理接口
 *
 * @author fengyun
 * @version 1.00 2005-6-4 20:58:08
 */
public interface Streamable
{
    /**
     * 串行化
     *
     * @param oos 输出流
     * @throws java.io.IOException 当发生I/O异常时
     */
    public void writeTo(OutputStream oos)
        throws IOException;

    /**
     * 反串行化
     *
     * @param ois 输入
     * @throws IOException            当发生I/O异常时
     * @throws ClassNotFoundException
     */
    public void readFrom(InputStream ois)
        throws IOException, ClassNotFoundException;
}
