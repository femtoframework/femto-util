package org.femtoframework.util.i18n.resources;

import java.util.Locale;

/**
 * 资源工厂
 */
public interface ResourcesFactory
{
    /**
     * 返回资源集合
     *
     * @param name
     */
    Resources getResources(String name);

    /**
     * 返回资源集合
     *
     * @param name
     * @param locale
     * @return
     */
    Resources getResources(String name, Locale locale);

    /**
     * 判断资源集合是否可以被重新装载
     *
     * @param name
     * @param locale
     * @return
     */
    boolean isReloadable(String name, Locale locale);

    /**
     * 判断资源集合是否可以被重新装载
     *
     * @param name
     * @param locale
     * @param isReloadable 是否可以重新装载
     */
    void setReloadable(String name, Locale locale, boolean isReloadable);

    /**
     * 创建资源
     *
     * @param name
     * @param locale
     * @return
     */
    Resources createResources(String name, Locale locale);

}
