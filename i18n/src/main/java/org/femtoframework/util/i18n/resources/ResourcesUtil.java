package org.femtoframework.util.i18n.resources;

import org.femtoframework.implement.ImplementUtil;

import java.util.Locale;

/**
 * 获取资源工具类.
 *
 * @author yuanyou(yuanyou@femtoframework.cn)
 */
public class ResourcesUtil
{
    private static ResourcesFactory resourcesFactory = ImplementUtil.getInstance(ResourcesFactory.class);

    private ResourcesUtil()
    {
    }

    /**
     * 返回默认的资源工厂
     */
    public static ResourcesFactory getResourcesFactory()
    {
        return resourcesFactory;
    }

    /**
     * 返回资源集合
     *
     * @param name 资源名称
     */
    public static Resources getResources(String name)
    {
        return resourcesFactory.getResources(name);
    }

    /**
     * 返回资源集合
     *
     * @param name   资源名称
     * @param locale Locale
     */
    public static Resources getResources(String name, Locale locale)
    {
        return resourcesFactory.getResources(name, locale);
    }
}
