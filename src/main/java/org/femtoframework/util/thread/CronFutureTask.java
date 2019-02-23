package org.femtoframework.util.thread;

import org.femtoframework.util.timer.CronEntry;

import java.util.concurrent.Callable;

class CronFutureTask<V> extends ScheduledFutureTask<V>
{
    /**
     * CrontabEntry
     */
    private CronEntry entry = null;

    /**
     * Creates a one-shot action with given nanoTime-based trigger time
     */
    CronFutureTask(Runnable task)
    {
        super(task);
    }

    /**
     * Creates a periodic action with given nano time and period
     */
    CronFutureTask(Runnable task, V result)
    {
        super(task, result);
    }

    /**
     * Creates a one-shot action with given nanoTime-based trigger
     */
    CronFutureTask(Callable<V> callable)
    {
        super(callable);
    }

    /**
     * Sets cron expression
     *
     * @param cron Cron Expression
     */
    void setCron(String cron)
    {
        entry = CronEntry.parse(cron);
    }

    /**
     * Calculate new execution time
     *
     * @param now Current time
     */
    public long nextExecutionTime(long now)
    {
        return entry.nextRunningTime(now);
    }
}
