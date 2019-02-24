package org.femtoframework.io.helper;

import org.femtoframework.io.*;

import java.io.*;
import java.lang.ref.SoftReference;

/**
 * 串行化工具
 * <p/>
 *
 * @author fengyun
 * @version Dec 26, 2002 12:30:13 PM
 */

public class MarshalHelper implements ObjectCodec {
    private ObjectInputStream ois = null;
    private ObjectOutputStream oos = null;
    private SwitchInputStream sis = null;
    private SwitchOutputStream sos = null;


    private void initInput(InputStream in) throws IOException {
        if (sis == null || ois == null) {
            sis = new SwitchInputStream(in);
        }
        else {
            sis.setInput(in);
        }

        if (ois == null) {
            ois = new FastObjectInputStream(sis);
        }
    }

    private void initOutput(OutputStream out)
        throws IOException {
        if (sos == null || oos == null) {
            sos = new SwitchOutputStream(out);
        }
        else {
            sos.setOutput(out);
        }

        if (oos == null) {
            oos = new FastObjectOutputStream(sos);
        }
    }


    public void writeObject(OutputStream out, Object obj)
        throws IOException {
        initOutput(out);
        try {
            oos.reset();
            CodecUtil.writeObject(oos, obj);
            oos.flush();
        }
        catch (IOException ioe) {
            oos = null;
            throw ioe;
        }
        catch (Throwable t) {
            oos = null;
            throw new IOException("Error:" + t.getMessage(), t);
        }
        finally {
            sos.clearOutput();
        }
    }

    /**
     * 读取对象
     *
     * @param in
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public Object readObject(InputStream in)
        throws IOException, ClassNotFoundException {
        initInput(in);

        try {
            return CodecUtil.readObject(ois);
        }
        catch (IOException ioe) {
            ois = null;
            throw ioe;
        }
        catch (ClassNotFoundException cnfe) {
            ois = null;
            throw cnfe;
        }
        catch (Throwable t) {
            ois = null;
            throw new IOException("Error:" + t.getMessage(), t);
        }
        finally {
            sis.clearInput();
        }
    }

    /**
     * 根据输入流创建对象输入流
     *
     * @param input 输入流
     */
    public ObjectInputStream getObjectInput(InputStream input)
        throws IOException {
        return new FastObjectInputStream(input);
    }

    /**
     * 根据输出流创建对象输出流
     *
     * @param output 输出流
     */
    public ObjectOutputStream getObjectOutput(OutputStream output)
        throws IOException {
        return new FastObjectOutputStream(output);
    }

    public void writeExternalizable(OutputStream out, Externalizable obj)
        throws IOException {
        initOutput(out);

        try {
            oos.reset();
            obj.writeExternal(oos);
            oos.flush();
        }
        catch (IOException ioe) {
            oos = null;
            throw ioe;
        }
        catch (Throwable t) {
            oos = null;
            throw new IOException("Error:" + t.getMessage());
        }
        finally {
            sos.clearOutput();
        }
    }

    public void readExternalizable(InputStream in, Externalizable obj)
        throws IOException, ClassNotFoundException {
        initInput(in);
        try {
            obj.readExternal(ois);
        }
        catch (IOException ioe) {
            ois = null;
            throw ioe;
        }
        catch (ClassNotFoundException cnfe) {
            ois = null;
            throw cnfe;
        }
        catch (Throwable t) {
            ois = null;
            throw new IOException("Error:" + t.getMessage());
        }
        finally {
            sis.clearInput();
        }
    }

    private static ThreadLocal<SoftReference<MarshalHelper>> local = new ThreadLocal<SoftReference<MarshalHelper>>();

    public static MarshalHelper getHelper() {
        SoftReference<MarshalHelper> sr = local.get();
        MarshalHelper mh = null;
        if (sr != null) {
            mh = sr.get();
        }
        if (mh == null) {
            mh = new MarshalHelper();
            sr = new SoftReference<>(mh);
            local.set(sr);
        }
        return mh;
    }
}
