package org.femtoframework.io;

import org.junit.Test;

import java.io.ByteArrayInputStream;

import static org.junit.Assert.assertNull;

/**
 * 测试SwitchInputStream
 *
 * @author fengyun
 * @version 1.00 2005-1-19 19:58:19
 */

public class SwitchInputStreamTest
{
    /**
     * 测试空的构造方法
     *
     * @throws Exception
     */
    @Test
    public void testSwitchInputStream0() throws Exception
    {
        SwitchInputStream sis = new SwitchInputStream();
        assertNull(sis.getInput());
    }

    /**
     * 测试构造
     *
     * @throws Exception
     */
    @Test
    public void testSwitchInputStream1() throws Exception
    {
        ByteArrayInputStream bais = new ByteArrayInputStream(new byte[2]);
        SwitchInputStream sis = new SwitchInputStream(bais);
        NutletBean.assertEquals(bais, sis.getInput());
    }

    /**
     * 测试设置输入流
     *
     * @throws Exception
     */
    @Test
    public void testSetInput() throws Exception
    {
        SwitchInputStream sis = new SwitchInputStream();
        assertNull(sis.getInput());
        ByteArrayInputStream bais = new ByteArrayInputStream(new byte[2]);
        sis.setInput(bais);
        NutletBean.assertEquals(bais, sis.getInput());
    }

    /**
     * 测试清除输入流
     *
     * @throws Exception
     */
    @Test
    public void testClearInput() throws Exception
    {
        SwitchInputStream sis = new SwitchInputStream();
        assertNull(sis.getInput());
        ByteArrayInputStream bais = new ByteArrayInputStream(new byte[2]);
        sis.setInput(bais);
        NutletBean.assertEquals(bais, sis.getInput());

        sis.clearInput();
        assertNull(sis.getInput());
    }

    /**
     * 测试返回输入流
     *
     * @throws Exception
     */
    @Test
    public void testGetInput() throws Exception
    {
        SwitchInputStream sis = new SwitchInputStream();
        assertNull(sis.getInput());
        ByteArrayInputStream bais = new ByteArrayInputStream(new byte[2]);
        sis.setInput(bais);
        NutletBean.assertEquals(bais, sis.getInput());
        sis.setInput(null);
        assertNull(sis.getInput());
    }
}