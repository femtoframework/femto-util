package org.femtoframework.io;

import org.junit.Test;

import static org.junit.Assert.assertNull;

/**
 * @author fengyun
 * @version 1.00 2005-1-19 19:58:35
 */

public class SwitchOutputStreamTest
{
    /**
     * 测试空的构造方法
     *
     * @throws Exception
     */
    @Test
    public void testSwitchOutputStream0() throws Exception
    {
        SwitchOutputStream sis = new SwitchOutputStream();
        assertNull(sis.getOutput());
    }

    /**
     * 测试构造
     *
     * @throws Exception
     */
    @Test
    public void testSwitchOutputStream1() throws Exception
    {
        ByteArrayOutputStream bais = new ByteArrayOutputStream();
        SwitchOutputStream sis = new SwitchOutputStream(bais);
        NutletBean.assertEquals(bais, sis.getOutput());
    }

    /**
     * 测试设置输入流
     *
     * @throws Exception
     */
    @Test
    public void testSetOutput() throws Exception
    {
        SwitchOutputStream sis = new SwitchOutputStream();
        assertNull(sis.getOutput());
        ByteArrayOutputStream bais = new ByteArrayOutputStream();
        sis.setOutput(bais);
        NutletBean.assertEquals(bais, sis.getOutput());
    }

    /**
     * 测试清除输入流
     *
     * @throws Exception
     */
    @Test
    public void testClearOutput() throws Exception
    {
        SwitchOutputStream sis = new SwitchOutputStream();
        assertNull(sis.getOutput());
        ByteArrayOutputStream bais = new ByteArrayOutputStream();
        sis.setOutput(bais);
        NutletBean.assertEquals(bais, sis.getOutput());

        sis.clearOutput();
        assertNull(sis.getOutput());
    }

    /**
     * 测试返回输入流
     *
     * @throws Exception
     */
    @Test
    public void testGetOutput() throws Exception
    {
        SwitchOutputStream sis = new SwitchOutputStream();
        assertNull(sis.getOutput());
        ByteArrayOutputStream bais = new ByteArrayOutputStream();
        sis.setOutput(bais);
        NutletBean.assertEquals(bais, sis.getOutput());
        sis.setOutput(null);
        assertNull(sis.getOutput());
    }
}