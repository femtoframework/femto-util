package org.femtoframework.util.thread;

import org.junit.Ignore;
import org.junit.Test;

import java.util.concurrent.ScheduledFuture;

import static org.junit.Assert.assertTrue;

/**
 * 测试SingleThreadScheduler
 *
 * @author fengyun
 * @version 1.00 2005-6-8 11:17:35
 */
public class SingleThreadSchedulerTest
{
    @Test
    public void testScheduleWithFixedDelay() throws Exception
    {
        SingleThreadScheduler scheduler = new SingleThreadScheduler();
        TestRunnable tr = new TestRunnable();
        ScheduledFuture future = scheduler.scheduleWithFixedDelay(tr, 1000, 1000);
        Thread.sleep(5000);
        assertTrue(tr.getCount() > 0);
        future.cancel(true);
    }

    public static class TestRunnable implements Runnable
    {
        private int count = 0;

        /**
         * When an object implementing interface <code>Runnable</code> is used
         * to create a thread, starting the thread causes the object's
         * <code>run</code> method to be called in that separately executing
         * thread.
         * <p/>
         * The general contract of the method <code>run</code> is that it may
         * take any action whatsoever.
         *
         * @see Thread#run()
         */
        public void run()
        {
            count ++;
        }

        public int getCount()
        {
            return count;
        }
    }

    @Test
    public void testCron() throws Exception
    {
        SingleThreadScheduler scheduler = new SingleThreadScheduler();
        TestRunnable tr = new TestRunnable();
        ScheduledFuture future = scheduler.schedule(tr,"60/2 * *");
        Thread.sleep(7*1000);
        assertTrue(tr.getCount() >= 3);
        future.cancel(true);
        //todo test Cron
    }
}