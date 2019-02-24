package org.femtoframework.io;

import org.femtoframework.util.TimeWorker;
import org.junit.Test;

import java.io.*;

/**
 * 测试CodecUtil
 *
 * @author fengyun
 * @version 1.00 2005-2-15 11:52:46
 */
public class CodecUtilTest {
    public static String STR1 = "apdfapfiafajdfanfnjzv;zcvznvafqerqruqerqprerijeqkadfajdfd;" +
                                "adjdfadjdffnadfifereqrqerqeriaidfadifadfjadfd"
                                + "apdfapfiafajdfanfnjzv;zcvznvafqerqruqerqprerijeqkadfajdfd;" +
                                "adjdfadjdffnadfifereqrqerqeriaidfadifadfjadfd"
                                + "apdfapfiafajdfanfnjzv;zcvznvafqerqruqerqprerijeqkadfajdfd;" +
                                "adjdfadjdffnadfifereqrqerqeriaidfadifadfjadfd"
                                + "apdfapfiafajdfanfnjzv;zcvznvafqerqruqerqprerijeqkadfajdfd;" +
                                "adjdfadjdffnadfifereqrqerqeriaidfadifadfjadfd";

    public static final String STR2 = "apdfapfiafajdfanf";

    public static final String STR3 = "apdfap";

    @Test
    public void testReadUnsignedInt() throws Exception {
        assertEquals(0xFFFFFFFFL);
        assertEquals(0x12345678L);
        assertEquals(0x98765432L);
        assertEquals(0x0L);
    }

    private void assertEquals(long l)
        throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        CodecUtil.writeUnsignedInt(baos, l);
        InputStream input = baos.getInputStream();
        long l2 = CodecUtil.readUnsignedInt(input);
        NutletBean.assertEquals(l, l2);
    }

    @Test
    public void testWriteUnsignedInt() throws Exception {
        long l = 0xFFFFFFFFL;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        CodecUtil.writeUnsignedInt(baos, l);
        byte[] bytes = baos.toByteArray();
        NutletBean.assertEquals(bytes, new byte[]{(byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF});
    }

    @Test
    public void testWriteString1() throws Exception {
        TimeWorker timer = new TimeWorker(3);
        timer.addTimePoint("WriteString1");
        System.out.println(STR1.length());
        for (int i = 0; i < 10000; i++) {
            writeString1(STR1);
            writeString1(STR2);
            writeString1(STR3);
        }
        timer.addTimePoint("WriteString1End");
        timer.printResult();
    }

    @Test
    public void testWriteString2() throws Exception {
        TimeWorker timer = new TimeWorker(3);
        timer.addTimePoint("WriteString2");
        for (int i = 0; i < 10000; i++) {
            writeString2(STR1);
            writeString2(STR2);
            writeString2(STR3);
        }
        timer.addTimePoint("WriteString2End");
        timer.printResult();
    }

    private static void writeString1(String str)
        throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        writeString1(baos, str, str.length(), byteBuffer);
    }

    private static void writeString2(String str)
        throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        writeString2(baos, str, str.length(), byteBuffer);
    }

    private static final byte[] byteBuffer = new byte[2048];
    private static final char[] charBuffer = new char[1024];

    private static void writeString1(OutputStream out, String str,
                                     int len, byte[] buf)
        throws IOException {
        int j = 0;
        int v;

        for (int i = 0; i < len; i++) {
            v = str.charAt(i);
            buf[j++] = (byte)((v >>> 8) & 0xFF);
            buf[j++] = (byte)((v >>> 0) & 0xFF);
            if (j == buf.length) { //Full
                out.write(buf, 0, j);
                j = 0;
            }
        }
        if (j > 0) {
            out.write(buf, 0, j);
        }
    }

    private static void writeString2(OutputStream out, String str,
                                     int len, byte[] buf)
        throws IOException {
        int off = 0;
        int batch = charBuffer.length;
        int read;
        int v;
        int j = 0;
        while (len > 0) {
            read = len > batch ? batch : len;
            str.getChars(off, off + read, charBuffer, 0);
            for (int i = 0; i < read; i++) {
                v = charBuffer[i];
                buf[j++] = (byte)((v >>> 8) & 0xFF);
                buf[j++] = (byte)((v >>> 0) & 0xFF);
            }

            out.write(buf, 0, j);
            j = 0;
            off += read;
            len -= read;
        }
    }

    @Test
    public void testWriteUnsignedShort() throws Exception {
        assertEquals(0xFFFF);
        assertEquals(0x5678);
        assertEquals(0x9876);
        assertEquals(0x0);
    }


    private void assertEquals(int i)
        throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        CodecUtil.writeUnsignedShort(baos, i);
        InputStream input = baos.getInputStream();
        int i2 = CodecUtil.readUnsignedShort(input);
        NutletBean.assertEquals(i, i2);
    }

    private static class MarshalBean implements Serializable {
        private String name;
        private boolean defaultConstructor = false;

        public MarshalBean() {
            defaultConstructor = true;
        }

        public boolean isDefaultConstructor() {
            return defaultConstructor;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void marshal(ObjectOutputStream oos) throws IOException {
            CodecUtil.writeSingle(oos, name);
            oos.writeBoolean(defaultConstructor);
        }

        public void demarshal(ObjectInputStream ois) throws IOException, ClassNotFoundException {
            name = CodecUtil.readSingle(ois);
            defaultConstructor = ois.readBoolean();
        }
    }

//    /**
//     * 测试newInstance
//     */
//    public void testNewInstance0() throws Exception {
//        MarshalBean bean = (MarshalBean)CodecUtil.newInstance(MarshalBean.class);
//        assertNotNull(bean);
//        assertFalse(bean.isDefaultConstructor());
//    }
//
//    /**
//     * 测试newInstance
//     */
//    public void testNewInstance1() throws Exception {
//        MarshalBean bean = (MarshalBean)CodecUtil.newInstance("org.bolango.io.CodecUtilTest$MarshalBean");
//        assertNotNull(bean);
//        assertFalse(bean.isDefaultConstructor());
//    }
}