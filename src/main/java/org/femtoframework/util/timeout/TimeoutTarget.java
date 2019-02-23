package org.femtoframework.util.timeout;

/**
 * Timeout Target who needs to receive timeout callback
 *
 * @author fengyun
 * @version 1.00
 */
public interface TimeoutTarget
{
    /**
     * Timeout callback method
     *
     * @param timeout The timeout inteface
     */
    void timeout(Timeout timeout);
}
