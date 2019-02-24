package org.femtoframework.util.selector;

import org.femtoframework.annotation.ImplementedBy;

/**
 * List选取器工厂
 *
 * @author fengyun
 * @version 1.00 2005-2-19 0:45:39
 */
@ImplementedBy("org.femtoframework.util.selector.SimpleListSelectorFactory")
public interface ListSelectorFactory
{
    /**
     * 返回默认的选取器
     */
    ListSelector createDefaultSelector();

    /**
     * 根据选取器类型和过滤器名称返回选取器（选取器必需是单例）
     *
     * @param type 类型
     * @param arguments Arguments for constructor
     */
    ListSelector createSelector(String type, Object... arguments);
}
