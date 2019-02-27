package org.femtoframework.util.i18n.resources;

import org.femtoframework.util.i18n.LocaleUtil;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 抽象资源工厂
 *
 * @author fengyun
 * @version 1.00
 */
public abstract class AbstractResourcesFactory
    implements ResourcesFactory
{
    private static final Map<String, Map<Locale, Resources>> cache = new ConcurrentHashMap<>();

    /**
     * 返回资源集合
     *
     * @param name
     * @return
     */
    public Resources getResources(String name)
    {
        return getResources(name, Locale.getDefault());
    }

    /**
     * 返回资源集合
     *
     * @param name Name
     * @param locale Locale
     */
    public Resources getResources(String name, Locale locale)
    {
        if (locale == null) {
            locale = Locale.getDefault();
        }
        if (name == null) {
            return null;
        }

        Map<Locale, Resources> obj = cache.computeIfAbsent(name, k -> new HashMap<>());

        Resources res = obj.get(locale);
        if (res == null) {
            try {
                res = doCreate(name, locale, false);
            }
            catch (MissingResourceException mre) {
                //如果资源没有找到
                Locale defaultLocale = LocaleUtil.getLocaleByLanguage(locale.getLanguage());
                if (defaultLocale != null) {
                    res = doCreate(name, defaultLocale, false);
                }
            }
            if (res != null) {
                obj.put(locale, res);
            }
        }
        return res;
    }

    protected abstract Resources doCreate(String name, Locale locale, boolean internal);

    /**
     * 只创建资源，不进行Cache
     *
     * @param name
     * @param locale
     * @return
     */
    public Resources createResources(String name, Locale locale)
    {
        return doCreate(name, locale, false);
    }

    /**
     * 判断资源集合是否可以被重新装载
     *
     * @param name
     * @param locale
     * @return
     */
    public boolean isReloadable(String name, Locale locale)
    {
        Resources resources = getResources(name, locale);
        if (resources == null) {
            return false;
        }
        if (resources instanceof ReloadableResources) {
            return ((ReloadableResources) resources).isReloadable();
        }
        return false;
    }

    /**
     * 判断资源集合是否可以被重新装载
     *
     * @param name
     * @param locale
     * @param isReloadable 是否可以重新装载
     */
    public void setReloadable(String name, Locale locale, boolean isReloadable)
    {
        Resources resources = getResources(name, locale);
        if (resources == null) {
            return;
        }
        if (resources instanceof ReloadableResources) {
            ((ReloadableResources) resources).setReloadable(isReloadable);
        }
        else {
            SimpleReloadableResources wrapper = new SimpleReloadableResources(this, resources);
            Map<Locale, Resources> obj = cache.computeIfAbsent(name, k -> new HashMap<>());
            obj.put(locale, wrapper);
        }
    }
}
