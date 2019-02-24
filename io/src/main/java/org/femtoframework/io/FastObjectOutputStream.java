package org.femtoframework.io;


import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.OutputStream;

/**
 * 快速对象输出流<br>
 * <p/>
 * 与<code>ObjectOutputStream</code>的区别在于它不传Class信息。<br>
 * <p/>
 * 注意：不能在JDK1.3与较低的版本间互用。<br>
 * 如果出现上述情况，请设置系统变量： serialization=normal<br>
 *
 * @see ObjectOutputStream
 */

public class FastObjectOutputStream extends ObjectOutputStream {

    public FastObjectOutputStream(OutputStream output)
        throws IOException {
        super(output);
    }

    protected void writeStreamHeader() {
        //Do nothing
    }

    /**
     * 只传类名和Serial版本号
     */
    protected void writeClassDescriptor(ObjectStreamClass desc)
        throws IOException {
        CodecUtil.writeSingle(this, desc.getName());
    }

    /**
     * Primitive data write of this String in
     * <a href="DataInput.html#modified-utf-8">modified UTF-8</a>
     * format.  Note that there is a
     * significant difference between writing a String into the stream as
     * primitive data or as an Object. A String instance written by writeObject
     * is written into the stream as a String initially. Future writeObject()
     * calls write references to the string into the stream.
     *
     * @param    str the String to be written
     * @throws IOException if I/O errors occur while writing to the underlying
     * stream
     */
    public void writeUTF(String str) throws IOException {
        CodecUtil.writeString(this, str);
    }
}
