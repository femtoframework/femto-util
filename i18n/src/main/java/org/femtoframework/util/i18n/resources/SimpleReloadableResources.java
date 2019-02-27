package org.femtoframework.util.i18n.resources;

import java.util.Locale;
import java.util.Set;

/**
 * 可以被重新装载的模块实现
 *
 * @author yuanyou(yuanyou@femtoframework.cn)
 */
public class SimpleReloadableResources implements ReloadableResources
{
    private Resources resources;
    private long lastModified;
    private int interval = 1000;
    private boolean reloadable = true;
    private AbstractResourcesFactory factory = null;

    /**
     * 构造抽象的资源
     *
     * @param factory   工厂
     * @param resources 资源
     */
    protected SimpleReloadableResources(AbstractResourcesFactory factory, Resources resources)
    {
        this.resources = resources;
        this.factory = factory;
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
     * 返回最后更新时间
     *
     * @return 最后更新时间
     */
    public long lastModified()
    {
        return resources.lastModified();
    }


    /**
     * 返回是否已经被更新
     *
     * @return
     */
    public boolean isModified()
    {
        long time = lastModified();
        if (time > lastModified) {
            lastModified = time;
            return true;
        }
        return false;
    }

    /**
     * 判断指定key的资源是否更新
     *
     * @return
     */
    public boolean isModified(String key)
    {
        return isModified();
    }

    private void check()
    {
        if (reloadable) {
            if (isModified()) {
                reload();
            }
        }
    }

    /**
     * 资源名称
     *
     * @return
     */
    @Override
    public String getName() {
        return resources.getName();
    }

    /**
     * 资源所在的Locale
     */
    @Override
    public Locale getLocale() {
        return resources.getLocale();
    }

    /**
     * 返回所有的key
     *
     * @return 所有的key
     */
    public Set<String> keySet()
    {
        check();
        return resources.keySet();
    }

    /**
     * 是否存在资源
     *
     * @param key 资源名称
     */
    @Override
    public boolean hasResource(String key) {
        return false;
    }


    /**
     * 以字符串的形式返回key对应的资源
     *
     * @param key key
     */
    public String getString(String key)
    {
        check();
        return resources.getString(key);
    }

    /**
     * 设置是否进行检查
     *
     * @param reloadable 是否检查是否更新
     */
    public void setReloadable(boolean reloadable)
    {
        this.reloadable = reloadable;
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

    /**
     * 重新装载
     */
    public void reload()
    {
        resources = factory.doCreate(getName(), getLocale(), true);
    }
}
