package org.femtoframework.util.selector;

import org.femtoframework.parameters.Parameters;

import java.util.List;

/**
 * 循环选取<br>
 *
 * @author fengyun
 * @version 1.00 2004-3-14 21:17:59
 */
public class RoundRobinSelector extends AbstractListSelector
{
    /**
     * 下一服务器标识
     */
    private int next = 0;

    /**
     * 从列表中选取，（考虑性能，不加锁）
     *
     * @param list       列表
     * @param parameters 选取参数，用于给不同的选取器提供选取的基准
     * @return 如果无满足条件的对象，返回<code>null</code>
     */
    protected <V> V doSelect(List<V> list, Parameters parameters, int size)
    {
        V obj = peek(next++, list, size);
        next %= size;
        return obj;
    }

    /**
     * 从数组中选取
     *
     * @param array      数组
     * @param parameters 选取参数，用于给不同的选取器提供选取的基准
     * @return 如果无满足条件的对象，返回<code>null</code>
     */
    protected <V> V doSelect(V[] array, Parameters parameters, int size)
    {
        V obj = peek(next++, array, size);
        next %= size;
        return obj;
    }
}
