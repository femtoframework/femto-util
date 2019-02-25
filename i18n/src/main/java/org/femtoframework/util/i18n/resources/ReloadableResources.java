package org.femtoframework.util.i18n.resources;

/**
 * 可以重新装载的资源Resources
 *
 * @author fengyun
 * @version 1.00 2004-8-5 15:57:15
 */
public interface ReloadableResources
    extends Resources
{
    /**
     * 设置检查间隔时间
     *
     * @param interval 间隔时间
     */
    void setInterval(int interval);

    /**
     * 返回检查间隔时间
     */
    int getInterval();

    /**
     * 返回是否已经被更新
     */
    boolean isModified();

    /**
     * 判断指定key的资源是否更新
     */
    boolean isModified(String key);

    /**
     * 设置是否进行检查
     *
     * @param reloadable 是否检查是否更新
     */
    void setReloadable(boolean reloadable);

    /**
     * 是否检查已经更新
     *
     * @return 是否检查更新
     */
    boolean isReloadable();

    /**
     * 重新装载
     */
    void reload();
}
