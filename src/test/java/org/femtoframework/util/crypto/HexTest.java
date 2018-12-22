package org.femtoframework.util.crypto;


import org.femtoframework.util.nutlet.NutletUtil;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * 测试Hex
 *
 * @author fengyun
 * @version 1.00 2005-2-7 8:01:09
 */

public class HexTest
{
    /**
     * 测试decode
     */
    @Test
    public void testDecode0() throws Exception
    {
        String str = NutletUtil.getString(256);
        String encode = Hex.encode(str, "UTF16");
        String decoded = Hex.decode(encode, "UTF16");
        assertEquals(str, decoded);
    }

    @Test
    public void testDecode1() throws Exception
    {
        String str = NutletUtil.getString();
        String encode = Hex.encode(str);
        assertEquals(str, Hex.decode(encode));
    }


    @Test
    public void testAppendInt() throws Exception
    {
        StringBuilder sb = new StringBuilder();
        Hex.append(sb, 0x1A11);
        assertEquals("00001A11", sb.toString());
    }

    @Test
    public void testAppendLong() throws Exception
    {
        StringBuilder sb = new StringBuilder();
        Hex.append(sb, 0x1234567891A11L);
        assertEquals("0001234567891A11", sb.toString());
    }

    @Test
    public void testAppendChar() throws Exception
    {
        StringBuilder sb = new StringBuilder();
        Hex.append(sb, (char) 0x1A11);
        assertEquals("1A11", sb.toString());
    }

    @Test
    public void testAppendByte() throws Exception
    {
        StringBuilder sb = new StringBuilder();
        Hex.append(sb, (byte) 0x1A);
        assertEquals("1A", sb.toString());
    }

    @Test
    public void testToByte() throws Exception
    {
        byte[] bytes = new byte[]{'B', '9'};
        assertEquals((byte) 0xB9, Hex.toByte(bytes));
    }

}