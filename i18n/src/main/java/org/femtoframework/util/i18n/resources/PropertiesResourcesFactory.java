package org.femtoframework.util.i18n.resources;

import java.util.Locale;

/**
 * 消息资源工厂
 *
 * @author fengyun
 * @version 1.00 2004-7-23 11:37:36
 */
public class PropertiesResourcesFactory extends AbstractResourcesFactory
{
    /**
     * 正在创建
     *
     * @param name
     * @param locale
     * @return
     */
    protected Resources doCreate(String name, Locale locale)
    {
        PropertiesResources resources = new PropertiesResources(name, locale);
        if (resources.isReloadable()) {
            return new SimpleReloadableResources(this, resources);
        }
        return resources;
    }
}
