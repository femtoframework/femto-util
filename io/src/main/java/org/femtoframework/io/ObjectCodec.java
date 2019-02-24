package org.femtoframework.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

/**
 * 对象解码编码器
 *
 * @author fengyun
 */
public interface ObjectCodec extends Serializer
{
    /**
     * 根据输入流创建对象输入流
     *
     * @param input 输入流
     */
    public ObjectInputStream getObjectInput(InputStream input)
        throws IOException;

    /**
     * 根据输出流创建对象输出流
     *
     * @param output 输出流
     */
    public ObjectOutputStream getObjectOutput(OutputStream output)
        throws IOException;
}
