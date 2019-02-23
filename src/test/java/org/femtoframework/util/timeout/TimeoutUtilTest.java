package org.femtoframework.util.timeout;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * 测试TimeoutUtil
 *
 * @author fengyun
 * @version 1.00 2005-5-20 23:06:38
 */
public class TimeoutUtilTest
{
    /**
     * 测试createTimeout
     */
    @Test
    public void testCreateTimeout() throws Exception
    {
        int timeout = 1000;
        final int[] count = new int[1];
        TimeoutUtil.createTimeout(timeout, new TimeoutTarget()
        {
            /**
             * 超时时调用的方法
             *
             * @param timeout 超时
             */
            public void timeout(Timeout timeout)
            {
                count[0]++;
            }
        });
        Thread.sleep(3000);
        assertEquals(1, count[0]);
    }
}