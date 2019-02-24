package org.femtoframework.util.crypto;


import org.femtoframework.io.ByteArrayOutputStream;
import org.femtoframework.io.IOUtil;
import org.femtoframework.lang.reflect.Reflection;
import org.junit.Test;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static org.junit.Assert.assertEquals;

/**
 * 测试MD5
 *
 * @author fengyun
 * @version 1.00 2005-2-7 7:59:54
 */
public class MD5Test
{
    @Test
    public void testMD5() throws Exception
    {
        InputStream input = Reflection.getResourceAsStream("org/femtoframework/util/crypto/MD5Test.class");
        String rs1 = digest(input);
        input = Reflection.getResourceAsStream("org/femtoframework/util/crypto/MD5Test.class");
        String rs2 = digest1(input);
        assertEquals(rs1, rs2);
        input = Reflection.getResourceAsStream("org/femtoframework/util/crypto/MD5Test.class");
        rs1 = digest(input);
        input = Reflection.getResourceAsStream("org/femtoframework/util/crypto/MD5Test.class");
        rs2 = digest1(input);
        assertEquals(rs1, rs2);
    }

    @Test
    public void testMD5_Performance() throws Exception
    {
        InputStream input = Reflection.getResourceAsStream("org/femtoframework/util/crypto/MD5Test.class");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        IOUtil.copy(input, baos);
        byte[] data = baos.toByteArray();

        for(int i = 0; i < 10; i ++) {
            digest(data);
        }
        for(int i = 0; i < 10; i ++) {
            digest1(data);
        }

        int times = 100000;
        long start = System.currentTimeMillis();
        for(int i = 0; i < times; i ++) {
            digest1(data);
        }
        System.out.println("MD5(Java):" + (System.currentTimeMillis() - start));
        start = System.currentTimeMillis();
        for(int i = 0; i < times; i ++) {
            digest(data);
        }
        System.out.println("MD5(Ours):" + (System.currentTimeMillis() - start));

    }

    private static String digest(InputStream input)
        throws Exception
    {
        BufferedInputStream bis = new BufferedInputStream(input);
        byte[] buf = new byte[1024];
        MD5 md5 = new MD5();
        int read = 0;
        while ((read = bis.read(buf)) > 0) {
            md5.update(buf, 0, read);
        }
        byte[] result = md5.digest();
        IOUtil.close(bis);
        return Hex.encode(result);
    }

    private static byte[] digest(byte[] input)
        throws Exception
    {
        return MD5.encrypt(input);
    }

    private static String digest1(InputStream input)
        throws Exception
    {
        BufferedInputStream bis = new BufferedInputStream(input);
        byte[] buf = new byte[1024];
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        int read = 0;
        while ((read = bis.read(buf)) > 0) {
            md5.update(buf, 0, read);
        }
        byte[] result = md5.digest();
        IOUtil.close(bis);
        return Hex.encode(result);
    }

    private static byte[] digest1(byte[] input)
        throws Exception
    {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
            digest.update(input);
            return digest.digest();
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }
}