package org.femtoframework.util.selector;

import org.femtoframework.implement.ImplementUtil;

/**
 * @author fengyun
 * @version 1.00 2005-2-19 0:45:06
 */
public class ListSelectorUtil
{
    private static ListSelectorFactory factory = ImplementUtil.getInstance(ListSelectorFactory.class);

    /**
     * 返回ListSelectorFactory
     */
    public static ListSelectorFactory getFactory()
    {
        return factory;
    }

    /**
     * 返回默认的选取器
     *
     * @return 默认的选取器
     */
    public static ListSelector createDefaultSelector()
    {
        return factory.createDefaultSelector();
    }

    /**
     * 根据选取器类型和过滤器名称返回选取器（选取器必需是单例）
     *
     * @param type 类型
     * @return 选取器
     */
    public static ListSelector createSelector(String type)
    {
        return factory.createSelector(type);
    }
}
