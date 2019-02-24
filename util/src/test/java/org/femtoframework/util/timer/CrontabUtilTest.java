package org.femtoframework.util.timer;


import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * CrontabUtilTest
 *
 * @author renex
 * @version 2006-8-2 18:39:55
 */
public class CrontabUtilTest
{
    @Test
    public void testRange() throws Throwable
    {
        boolean[] bools = new boolean[8];
        CrontabUtil.parseToken("0-9/2", bools, false);
        assertTrue(bools[0]);
        assertFalse(bools[1]);
        assertTrue(bools[2]);
        assertFalse(bools[3]);
        assertTrue(bools[4]);
        assertFalse(bools[5]);
        assertTrue(bools[6]);
        assertFalse(bools[7]);

        CrontabUtil.parseToken("1-9/2", bools, true);
        assertTrue(bools[0]);
        assertFalse(bools[1]);
        assertTrue(bools[2]);
        assertFalse(bools[3]);
        assertTrue(bools[4]);
        assertFalse(bools[5]);
        assertTrue(bools[6]);
        assertFalse(bools[7]);

        bools = new boolean[8];
        CrontabUtil.parseToken("8/3", bools, false);
        assertTrue(bools[0]);
        assertFalse(bools[1]);
        assertFalse(bools[2]);
        assertTrue(bools[3]);
        assertFalse(bools[4]);
        assertFalse(bools[5]);
        assertTrue(bools[6]);
        assertFalse(bools[7]);

        bools = new boolean[8];
        CrontabUtil.parseToken("7-3/2", bools, false);
        assertTrue(bools[7]);
        assertTrue(bools[1]);
        assertTrue(bools[3]);
        assertFalse(bools[2]);
        assertFalse(bools[4]);
        assertFalse(bools[5]);
        assertFalse(bools[6]);
        assertFalse(bools[0]);
    }
}
