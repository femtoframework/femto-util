package org.femtoframework.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 对象串行化接口
 *
 * @author fengyun
 * @version 1.00 2006-8-9 14:26:52
 */
public interface Serializer
{
    /**
     * 从输入流中读取一个对象
     *
     * @param input 输入流
     * @return
     * @throws java.io.IOException
     */
    public Object readObject(InputStream input)
        throws IOException, ClassNotFoundException;

    /**
     * 将对象写出去
     *
     * @param output 输出流
     * @param obj    对象
     * @throws IOException
     */
    public void writeObject(OutputStream output, Object obj)
        throws IOException;
}
