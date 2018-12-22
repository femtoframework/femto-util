package org.femtoframework.implement;

import org.junit.Test;

import java.util.Iterator;

import static org.junit.Assert.*;

/**
 * ImplementUtil
 *
 * @author fengyun
 * @version 1.00 2015-2-1 21:39:57
 */
public class ImplementUtilTest
{
    @Test
    public void testGetImplements0() throws Exception
    {
        Iterator it = ImplementUtil.getImplements(ServiceInterface.class);
        assertTrue(it.hasNext());
    }

    @Test
    public void testGetImplements1() throws Exception
    {
        Iterator it = ImplementUtil.getImplements(ServiceInterface.class,
            ClassLoader.getSystemClassLoader());
        assertTrue(it.hasNext());
    }

    @Test
    public void testGetImplements2() throws Exception
    {
        ServiceInterface2 si2 =  ImplementUtil.getInstance(ServiceInterface2.class,
            ClassLoader.getSystemClassLoader());
        assertNotNull(si2);
    }

    @Test
    public void testGetImplement0() throws Exception
    {
        Class clazz1 = ImplementUtil.getImplement(ServiceInterface.class);
        assertNotNull(clazz1);
        Class clazz2 = ImplementUtil.getImplement(ServiceInterface.class);
        assertNotNull(clazz2);
        assertEquals(clazz1, clazz2);
    }

    @Test
    public void testGetImplement1() throws Exception
    {
        Class clazz1 = ImplementUtil.getImplement(ServiceInterface.class,
            ClassLoader.getSystemClassLoader());
        assertNotNull(clazz1);
        Class clazz2 = ImplementUtil.getImplement(ServiceInterface.class,
            ClassLoader.getSystemClassLoader());
        assertNotNull(clazz2);
        assertEquals(clazz1, clazz2);
    }
}