package org.femtoframework.util.timeout;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * The timeout factory.
 * <p/>
 * This is written with performance in mind. In case of <code>n</code>
 * active timeouts, creating, cancelling and firing timeouts all operate
 * in time <code>O(log(n))</code>.
 * <p/>
 * If a timeout is cancelled, the timeout is not discarded. Instead the
 * timeout is saved to be reused for another timeout. This means that if
 * no timeouts are fired, this class will eventually operate without
 * allocating anything on the heap.
 */
public class SimpleTimeoutFactory implements TimeoutFactory
{
    private ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    private static Logger log = LoggerFactory.getLogger(SimpleTimeoutFactory.class);

    /**
     * Our private Timeout implementation.
     */
    private class TimeoutImpl implements Timeout, Runnable
    {
        TimeoutTarget target; // target to fire at

        ScheduledFuture future;

        public void cancel()
        {
            future.cancel(false);
        }

        public void run()
        {
            timeout();
        }

        public void timeout()
        {
            try {
                target.timeout(this);
            }
            catch (Throwable t) {
                log.warn("Timed out exception", t);
            }
        }
    }

    /**
     * Create a new timeout.
     */
    private Timeout newTimeout(int timeout, TimeUnit unit, TimeoutTarget target)
    {
        TimeoutImpl impl = new TimeoutImpl();
        impl.target = target;
        impl.future = scheduler.schedule(impl, timeout, unit);
        return impl;
    }

    /**
     * Create a timeout
     *
     * @param timeout Timeout
     * @param unit    Unit of the time
     * @param target  TimeoutTarget
     */
    @Override
    public Timeout createTimeout(int timeout, TimeUnit unit, TimeoutTarget target) {
        if (timeout <= 0) {
            throw new IllegalArgumentException("Time not positive");
        }
        if (target == null) {
            throw new IllegalArgumentException("Null target");
        }

        return newTimeout(timeout, unit, target);
    }
}