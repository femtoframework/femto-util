package org.femtoframework.util.timeout;

import org.femtoframework.annotation.ImplementedBy;

import java.util.concurrent.TimeUnit;

/**
 * Timeout Factory
 *
 * @author fengyun
 * @version 1.00 2005-5-20 22:56:33
 */
@ImplementedBy("org.femtoframework.util.timeout.SimpleTimeoutFactory")
public interface TimeoutFactory
{
    /**
     * Create a timeout
     *
     * @param timeout Timeout in milliseconds
     * @param target  TimeoutTarget
     */
    default Timeout createTimeout(int timeout, TimeoutTarget target) {
        return createTimeout(timeout, TimeUnit.MILLISECONDS, target);
    }

    /**
     * Create a timeout
     *
     * @param timeout Timeout
     * @param unit Unit of the time
     * @param target  TimeoutTarget
     */
    Timeout createTimeout(int timeout, TimeUnit unit, TimeoutTarget target);
}
