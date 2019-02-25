package org.femtoframework.util.i18n.resources;

import java.util.Locale;

/**
 * 抽象可重新装载的Resources
 *
 * @author fengyun
 * @version 1.00 2004-8-12 22:21:31
 */
public abstract class AbstractReloadableResources
    extends AbstractResources
    implements ReloadableResources
{
    private long lastModified;
    private long lastCheck;
    private boolean lastResult = false;
    private int interval = 1000;
    private boolean reloadable = true;

    /**
     * 构造
     *
     * @param name
     * @param locale
     */
    public AbstractReloadableResources(String name, Locale locale)
    {
        super(name, locale);
    }

    /**
     * 设置检查间隔时间
     *
     * @param interval 间隔时间
     */
    public void setInterval(int interval)
    {
        this.interval = interval;
    }

    /**
     * 返回检查间隔时间
     *
     * @return
     */
    public int getInterval()
    {
        return interval;
    }

    /**
     * 返回是否已经被更新
     *
     * @return
     */
    public boolean isModified()
    {
        long time = System.currentTimeMillis();
        if (time > lastCheck + interval) {
            lastCheck = time;
            lastResult = isModified0();
        }
        return lastResult;
    }

    /**
     * 是否确实已经修改
     *
     * @return
     */
    protected boolean isModified0()
    {
        long time = lastModified();
        return time > lastModified;
    }

    /**
     * 检查是否需要重新装载
     */
    protected final void check()
    {
        if (isReloadable()) {
            if (isModified()) {
                reload();
                lastModified = lastModified();
            }
        }
    }

    /**
     * 设置是否进行检查
     *
     * @param checkModified 是否检查是否更新
     */
    public void setReloadable(boolean checkModified)
    {
        this.reloadable = checkModified;
    }

    /**
     * 是否检查已经更新
     *
     * @return 是否检查更新
     */
    public boolean isReloadable()
    {
        return reloadable;
    }
}
