package org.femtoframework.util.i18n;

import org.femtoframework.util.nutlet.NutletUtil;
import org.junit.Assert;
import org.junit.Test;

import java.io.CharArrayWriter;
import java.io.OutputStream;
import java.io.Writer;

import static org.junit.Assert.assertEquals;

/**
 * 测试StringResource
 *
 * @author fengyun
 * @version 1.00 Apr 2, 2004 6:31:25 PM
 */
public class StringResourceTest
{
    @Test
    public void testStringResource() throws Exception
    {
        StringResource sr = new StringResource("Invalid length:{0}, valid:[{1},{2}]");
        String result = sr.formatString(new String[]{"2", "3", "40"});
        assertEquals(result, "Invalid length:2, valid:[3,40]");
    }

    /**
     * 测试Format（边界）
     *
     * @throws Exception
     */
    @Test
    public void testFormat0() throws Exception
    {
        String str = "Invalid length:{0}";
        StringResource sr = new StringResource(str);
        
        StringBuilder sb = new StringBuilder();
        sr.format(sb);
        assertEquals(str, sb.toString());
    }

    /**
     * 测试Format
     *
     * @throws Exception
     */
    @Test
    public void testFormat1() throws Exception
    {
        String str = "Invalid length:{0}";
        StringResource sr = new StringResource(str);
        String arg = NutletUtil.getAscii();
        assertEquals(("Invalid length:" + arg), sr.format(new StringBuilder(), arg).toString());
    }

    /**
     * 测试Format
     *
     * @throws Exception
     */
    @Test
    public void testFormat2() throws Exception
    {
        String str = "Invalid length:{0}";
        StringResource sr = new StringResource(str);
        String arg = NutletUtil.getAscii();
        assertEquals(("Invalid length:" + arg), sr.format(new StringBuilder(), arg).toString());
    }

    /**
     * 测试Format
     *
     * @throws Exception
     */
    @Test
    public void testFormat3() throws Exception
    {
        String str = "Invalid length:{0}";
        StringResource sr = new StringResource(str);
        String arg = NutletUtil.getAscii();
        StringBuilder sb = new StringBuilder();
        sr.format(sb, arg);
        assertEquals("Invalid length:" + arg, sb.toString());
    }

    /**
     * 测试Format
     *
     * @throws Exception
     */
    @Test
    public void testFormat4() throws Exception
    {
        String str = "Invalid length:{0}";
        StringResource sr = new StringResource(str);
        String arg = NutletUtil.getAscii();
        StringBuilder sb = new StringBuilder();
        sr.format(sb, arg);
        assertEquals("Invalid length:" + arg, sb.toString());
    }

    /**
     * 测试FormatString
     *
     * @throws Exception
     */
    public void testFormatString() throws Exception
    {
        String str = "Invalid length:{0}";
        StringResource sr = new StringResource(str);
        String arg = NutletUtil.getAscii();
        String result = sr.formatString(arg);
        assertEquals("Invalid length:" + arg, result);
    }

    /**
     * 测试输出
     *
     * @throws Exception
     */
    @Test
    public void testWriteTo3() throws Exception
    {
        String str = "Invalid length:{0}";
        StringResource sr = new StringResource(str);
        String arg = NutletUtil.getAscii();
        CharArrayWriter caw = new CharArrayWriter();
        sr.writeTo((Writer) caw, arg);
        assertEquals("Invalid length:" + arg, caw.toString());
    }

    /**
     * 测试是否是不带参数的字符串
     *
     * @throws Exception
     */
    @Test
    public void testIsSimple() throws Exception
    {
        StringResource sr = new StringResource("Invalid length");
        Assert.assertTrue(sr.isSimple());
        sr = new StringResource("Invalid length:{0}, valid:[{1},{2}]");
        Assert.assertFalse(sr.isSimple());
    }

    /**
     * 测试toString方法
     *
     * @throws Exception
     */
    @Test
    public void testToString0() throws Exception
    {
        String str = "Invalid length:";
        StringResource sr = new StringResource(str);
        assertEquals(str, sr.toString());
    }

    /**
     * 测试toString方法
     *
     * @throws Exception
     */
    @Test
    public void testToString1() throws Exception
    {
        String str = "Invalid length:{0}";
        StringResource sr = new StringResource(str);
        assertEquals(str, sr.toString());
    }

    /**
     * 测试HashCode
     *
     * @throws Exception
     */
    @Test
    public void testHashCode() throws Exception
    {
        String str = "Invalid length:";
        StringResource sr = new StringResource(str);
        assertEquals(str.hashCode(), sr.hashCode());
    }

    /**
     * 测试是否相等
     *
     * @throws Exception
     */
    @Test
    public void testEquals() throws Exception
    {
        String str = "Invalid length:{0}";
        StringResource sr1 = new StringResource(str);
        assertEquals(str, sr1.toString());

        StringResource sr2 = new StringResource(str);
        assertEquals(sr1, sr2);
    }
}