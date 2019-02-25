package org.femtoframework.util.i18n;

import org.femtoframework.util.i18n.resources.ReloadableResources;
import org.femtoframework.util.i18n.resources.Resources;

import java.util.*;

/**
 * 简单消息实现
 *
 * @author fengyun
 * @version 1.00 2004-8-13 22:05:49
 */
public class SimpleMessages extends AbstractMessages
{
    /**
     * 资源
     */
    private Resources resources;

    /**
     * 消息
     */
    private Map messages;

    /**
     * 是否检查更新
     */
    private boolean reloadable = false;

    /**
     * 构造
     *
     * @param resources 资源集合
     */
    public SimpleMessages(Resources resources)
    {
        this.resources = resources;
        if (resources instanceof ReloadableResources) {
            reloadable = ((ReloadableResources)resources).isReloadable();
        }
        this.messages = new HashMap();
    }

    /**
     * 返回对象
     *
     * @param key key
     * @return
     */
    protected Object doGetObject(String key)
    {
        Object obj = messages.get(key);
        if (obj == null) {
            obj = createObject(key);
        }
        else if (reloadable) {
            //如果重新装载需要检查资源中返回的字符串是否已经更新
            ReloadableResources rr = (ReloadableResources)resources;

            boolean isModified = rr.isModified();
            if (isModified) {
                messages.clear();
                obj = createObject(key);
            }
            else {
                isModified = rr.isModified(key);
                if (isModified) {
                    obj = createObject(key);
                }
            }
        }
        return obj;
    }

    private Object createObject(String key)
    {
        Object obj = null;
        String str = resources.getString(key);
        if (str != null) {
            obj = new StringResource(str);
            messages.put(key, obj);
        }
        return obj;
    }

    /**
     * 消息集合名称，跟资源集合名称相同
     */
    public String getName()
    {
        return resources.getName();
    }

    /**
     * 消息集合所在的Locale
     */
    public Locale getLocale()
    {
        return resources.getLocale();
    }

    /**
     * 返回所有的key
     *
     * @return 所有的key
     */
    public Set<String> keySet()
    {
        return resources.keySet();
    }

    /**
     * 是否存在消息
     *
     * @param key 消息名称
     */
    public boolean hasMessage(String key)
    {
        return resources.hasResource(key);
    }

    /**
     * 设置是否进行检查
     *
     * @param reloadable 是否检查是否更新
     */
    public void setReloadable(boolean reloadable)
    {
        this.reloadable = reloadable;
        if (resources instanceof ReloadableResources) {
            ((ReloadableResources)resources).setReloadable(reloadable);
        }
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
