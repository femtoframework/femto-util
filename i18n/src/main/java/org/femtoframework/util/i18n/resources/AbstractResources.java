package org.femtoframework.util.i18n.resources;

import java.util.Locale;

/**
 * 抽象资源
 *
 * @author fengyun
 * @version 1.00 2004-7-28 15:18:15
 */
public abstract class AbstractResources
    implements Resources
{
    private String name;
    private boolean returnNull = true;
    private Locale locale;

    /**
     * 构造抽象的资源
     *
     * @param name   资源名称
     * @param locale MessageLocale
     */
    public AbstractResources(String name, Locale locale)
    {
        this.name = name;
        this.locale = locale;
    }

    /**
     * 资源名称
     *
     * @return
     */
    public String getName()
    {
        return name;
    }

    /**
     * 资源所在的Locale
     *
     * @return
     */
    public Locale getLocale()
    {
        return locale;
    }

    /**
     * 是否用返回null代替资源找不到的异常，默认是<code>true</code>
     *
     * @return
     */
    public boolean isReturnNull()
    {
        return returnNull;
    }

    /**
     * 设置是否用返回null代替资源找不到的异常
     *
     * @param returnNull
     */
    public void setReturnNull(boolean returnNull)
    {
        this.returnNull = returnNull;
    }
}
