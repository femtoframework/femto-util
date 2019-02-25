package org.femtoframework.util.i18n.resources;

import java.util.Locale;
import java.util.Set;

/**
 * 资源集合
 *
 * @author fengyun
 * @version 1.00
 */
public interface Resources
{
    /**
     * 资源名称
     */
    String getName();

    /**
     * 资源所在的Locale
     */
    Locale getLocale();

    /**
     * 返回所有的key
     *
     * @return 所有的key
     */
    Set<String> keySet();

    /**
     * 是否存在资源
     *
     * @param key 资源名称
     */
    boolean hasResource(String key);

    /**
     * 以字符串的形式返回key对应的资源
     *
     * @param key key
     */
    String getString(String key);

    /**
     * 返回最后更新时间
     *
     * @return 最后更新时间
     */
    long lastModified();

}
