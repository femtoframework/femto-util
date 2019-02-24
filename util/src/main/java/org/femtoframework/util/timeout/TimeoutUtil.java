package org.femtoframework.util.timeout;


import org.femtoframework.implement.ImplementUtil;

import java.util.concurrent.TimeUnit;

/**
 * Timeout Utility
 *
 * @author fengyun
 * @version 1.00 Sep 16, 2003 11:16:31 AM
 */
public class TimeoutUtil
{
    private static TimeoutFactory factory = ImplementUtil.getInstance(TimeoutFactory.class);

    /**
     * Create a timeout
     *
     * @param timeout 超时时间
     * @param target  目标对象
     */
    public static Timeout createTimeout(int timeout, TimeoutTarget target)
    {
        return factory.createTimeout(timeout, target);
    }

    /**
     * Create a timeout
     *
     * @param timeout Timeout
     * @param unit Unit of the time
     * @param target  TimeoutTarget
     */
    public static Timeout createTimeout(int timeout, TimeUnit unit, TimeoutTarget target) {
        return factory.createTimeout(timeout, unit, target);
    }
}
