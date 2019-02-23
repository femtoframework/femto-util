package org.femtoframework.util.timer;

/**
 * 采用Crontab的定时任务
 *
 * @author fengyun
 * @version 1.00 Jun 15, 2003 12:52:14 PM
 */
public abstract class CronTimerTask
    extends AbstractTimerTask
{
    /**
     * CronEntry
     */
    private CronEntry entry = null;

    public CronTimerTask(String crontab)
    {
        super();

        entry = CronEntry.parse(crontab);
    }


    /**
     * 根据当前时间返回下一次需要执行的时间
     *
     * @param now 当前时间
     * @return
     */
    public long nextExecutionTime(long now)
    {
        if (Math.abs(nextExecutionTime - now) < 1000) {
            now = Math.max(nextExecutionTime, now) + 1000;
        }
        return entry.nextRunningTime(now);
    }
}
