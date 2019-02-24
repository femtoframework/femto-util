package org.femtoframework.io;


import org.femtoframework.lang.reflect.Reflection;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;

/**
 * 快速对象输入流<br>
 * <p/>
 * 与<code>ObjectInputStream</code>的区别在于它不传Class信息<br>
 * <p/>
 * 注意：不能在JDK1.3与较低的版本间互用<br>
 * 如果出现上述情况，请设置系统变量： serialization=normal<br>
 *
 * @see ObjectInputStream
 */
public class FastObjectInputStream extends ObjectInputStream {
    /**
     * 构造快速串行化输入流
     *
     * @param input 输入流
     * @throws IOException 发生I/O异常时抛出
     */
    public FastObjectInputStream(InputStream input)
        throws IOException {
        super(input);
    }

    protected void readStreamHeader() {
        //Do nothing
    }

    protected ObjectStreamClass readClassDescriptor()
        throws IOException, ClassNotFoundException {
        String className = CodecUtil.readSingle(this);
        Class clazz = resolveClass(className);
        return ObjectStreamClass.lookup(clazz);
    }

    protected Class resolveClass(ObjectStreamClass osc)
        throws IOException, ClassNotFoundException {
        Class clazz = osc.forClass();
        if (clazz == null) {
            String className = osc.getName();
            clazz = resolveClass(className);
        }
        return clazz;
    }

    protected Class resolveClass(String className)
        throws IOException, ClassNotFoundException {
        return Class.forName(className, true, Reflection.getClassLoader());
    }

    /**
     * Reads a String in
     * <a href="DataInput.html#modified-utf-8">modified UTF-8</a>
     * format.
     *
     * @throws java.io.UTFDataFormatException if read bytes do not represent a valid
     *                                        modified UTF-8 encoding of a string
     * @return the String.
     * @throws IOException if there are I/O errors while reading from the
     * underlying <code>InputStream</code>
     */
    public String readUTF() throws IOException {
        return CodecUtil.readString(this);
    }
}

