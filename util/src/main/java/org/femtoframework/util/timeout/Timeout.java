package org.femtoframework.util.timeout;

/**
 * 超时控制接口<br>
 * <p/>
 * Timeout用于TimeoutTarget提交后的控制<br>
 */
public interface Timeout
{
    /**
     * Cancel this timeout.
     * <p/>
     * It is guaranteed that on return from this method this timer is
     * no longer active. This means that either it has been cancelled and
     * the timeout will not happen, or (in case of late cancel) the
     * timeout has happened and the timeout callback function has returned.
     * <p/>
     * On return from this method this instance should no longer be
     * used. The reason for this is that an implementation may reuse
     * cancelled timeouts, and at return the instance may already be
     * in use for another timeout.
     */
    void cancel();
}
