package org.femtoframework.io;

import org.femtoframework.util.TimeWorker;
import org.femtoframework.util.nutlet.NutletUtil;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * @author fengyun
 * @version 1.00 2011-08-23 19:54
 */
public class FastObjectOutputStreamTest {
    @Test
    public void testWriteUTF() throws Exception {

        String str = NutletUtil.getString(256);
        int times = 100000;
        for (int i = 0; i < 100; i++) {
            fastSerialize(str);
        }
        for (int i = 0; i < 100; i++) {
            serialize(str);
        }
        TimeWorker timer = new TimeWorker();
        timer.addTimePoint("Serializing");
        for (int i = 0; i < times; i++) {
            serialize(str);
        }
        timer.addTimePoint("Serializing JRMP");
        for (int i = 0; i < times; i++) {
            serializeAsObject(str);
        }
        timer.addTimePoint("Serializing ASOBJ");
        for (int i = 0; i < times; i++) {
            fastSerialize(str);
        }
        timer.addTimePoint("Serializing FAST");

        timer.printResult();

        str = NutletUtil.getAscii(256);
        timer = new TimeWorker();
        timer.addTimePoint("Serializing ASCII");
        for (int i = 0; i < times; i++) {
            serialize(str);
        }
        timer.addTimePoint("Serializing JRMP");
        for (int i = 0; i < times; i++) {
            serializeAsObject(str);
        }
        timer.addTimePoint("Serializing ASOBJ");
        for (int i = 0; i < times; i++) {
            fastSerialize(str);
        }
        timer.addTimePoint("Serializing FAST");
        timer.printResult();
    }

    @Test
    public void testWriteUTF2() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject((String)null);
        oos.writeObject("TEST");

        InputStream input = baos.getInputStream();
        ObjectInputStream ois = new ObjectInputStream(input);

        String NULL = (String)ois.readObject();
        String test = (String)ois.readObject();
        assertNull(NULL);
        assertEquals(test, "TEST");
    }


    @Test
    public void testSerialize() throws Exception {

        NutletBean bean2 = new NutletBean();
        NutletBean bean1 = new NutletBean(bean2);

        for (int i = 0; i < 100; i++) {
            serialize(bean1);
        }
        for (int i = 0; i < 100; i++) {
            serialize(bean2);
        }

        int times = 10000;
        TimeWorker timer = new TimeWorker();
        timer.addTimePoint("Serialization");
        for (int i = 0; i < times; i++) {
            serialize(bean2);
        }
        timer.addTimePoint("Serializable");

        for (int i = 0; i < times; i++) {
            serialize(bean1);
        }
        timer.addTimePoint("Externalizable");
        for (int i = 0; i < times; i++) {
            fastSerialize(bean1);
        }
        timer.addTimePoint("FAST");
        timer.printResult();

        timer = new TimeWorker();
        timer.addTimePoint("Serialization");
        for (int i = 0; i < times; i++) {
            serialize(bean2);
        }
        timer.addTimePoint("Serializable");
        for (int i = 0; i < times; i++) {
            serialize(bean1);
        }
        timer.addTimePoint("Externalizable");
        for (int i = 0; i < times; i++) {
            fastSerialize(bean1);
        }
        timer.addTimePoint("FAST");
        timer.printResult();
    }

    /**
     * 将对象串行化到ByteArrayOutputStream中，然后读回来
     *
     * @param obj 需要串行化的对象
     * @return
     * @throws IOException
     */
    public static String serialize(String obj)
        throws IOException, ClassNotFoundException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeUTF(obj);
        oos.flush();

        ObjectInputStream ois = new ObjectInputStream(baos.getInputStream());
        String newObj = ois.readUTF();
        ois.close();
        oos.close();

        return newObj;
    }

    /**
     * 将对象串行化到ByteArrayOutputStream中，然后读回来
     *
     * @param obj 需要串行化的对象
     * @return
     * @throws IOException
     */
    public static String serializeAsObject(String obj)
        throws IOException, ClassNotFoundException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(obj);
        oos.flush();

        ObjectInputStream ois = new ObjectInputStream(baos.getInputStream());
        String newObj = (String)ois.readObject();
        ois.close();
        oos.close();
        return newObj;
    }


    /**
     * 将对象串行化到ByteArrayOutputStream中，然后读回来
     *
     * @param obj 需要串行化的对象
     * @return
     * @throws IOException
     */
    public static String fastSerialize(String obj)
        throws IOException, ClassNotFoundException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new FastObjectOutputStream(baos);
        oos.writeUTF(obj);
        oos.flush();

        ObjectInputStream ois = new FastObjectInputStream(baos.getInputStream());
        String newObj = ois.readUTF();
        ois.close();
        oos.close();

        return newObj;
    }


    /**
     * 将对象串行化到ByteArrayOutputStream中，然后读回来
     *
     * @param obj 需要串行化的对象
     * @return
     * @throws IOException
     */
    public static NutletBean serialize(NutletBean obj)
        throws IOException, ClassNotFoundException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(obj);
        oos.flush();

        ObjectInputStream ois = new ObjectInputStream(baos.getInputStream());
        NutletBean newObj = (NutletBean)ois.readObject();
        ois.close();
        oos.close();

        return newObj;
    }

    /**
     * 将对象串行化到ByteArrayOutputStream中，然后读回来
     *
     * @param obj 需要串行化的对象
     * @return
     * @throws IOException
     */
    public static NutletBean fastSerialize(NutletBean obj)
        throws IOException, ClassNotFoundException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new FastObjectOutputStream(baos);
        oos.writeObject(obj);
        oos.flush();

        ObjectInputStream ois = new FastObjectInputStream(baos.getInputStream());
        NutletBean newObj = (NutletBean)ois.readObject();
        ois.close();
        oos.close();

        return newObj;
    }

//    /**
//     * 将对象串行化到ByteArrayOutputStream中，然后读回来
//     *
//     * @param obj 需要串行化的对象
//     * @return
//     * @throws IOException
//     */
//    public static org.bolango.tools.nutlet.NutletBean serialize(org.bolango.tools.nutlet.NutletBean obj)
//        throws IOException, ClassNotFoundException {
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        ObjectOutputStream oos = new ObjectOutputStream(baos);
//        oos.writeObject(obj);
//        oos.flush();
//
//        ObjectInputStream ois = new ObjectInputStream(baos.getInputStream());
//        org.bolango.tools.nutlet.NutletBean newObj = (org.bolango.tools.nutlet.NutletBean)ois.readObject();
//        ois.close();
//        oos.close();
//        return newObj;
//    }


}
