package org.femtoframework.util.timer;

/**
 * Crontab Runnable Task
 *
 * @author fengyun
 * @version 1.00 Jun 15, 2003 1:20:55 PM
 */
public class CronRunnableTask extends CronTimerTask
{
    private Runnable runnable;

    public CronRunnableTask(String crontab, Runnable runnable)
    {
        super(crontab);
        if (runnable == null) {
            throw new IllegalArgumentException("Null runnable");
        }
        this.runnable = runnable;
    }

    /**
     * The action to be performed by this timer task.
     */
    public void run()
    {
        runnable.run();
    }
}
