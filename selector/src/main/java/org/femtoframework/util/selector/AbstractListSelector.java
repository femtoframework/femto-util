package org.femtoframework.util.selector;

import org.femtoframework.parameters.Parameters;

import java.util.List;


/**
 * 抽象列表对象选取器
 *
 * @author fengyun
 * @version 1.00 2004-3-14 18:52:12
 */
public abstract class AbstractListSelector
    implements ListSelector
{

    /**
     * 从列表中选取
     *
     * @param list       列表
     * @param parameters 选取参数，用于给不同的选取器提供选取的基准
     * @return 如果无满足条件的对象，返回<code>null</code>
     */
    public <V> V select(List<V> list, Parameters parameters)
    {
        if (list == null) {
            return null;
        }
        int size = list.size();
        if (size == 0) {
            return null;
        }
        return doSelect(list, parameters, size);
    }

    /**
     * 从列表中选取
     *
     * @param list       列表
     * @param parameters 选取参数，用于给不同的选取器提供选取的基准
     * @return 如果无满足条件的对象，返回<code>null</code>
     */
    protected abstract <V> V doSelect(List<V> list, Parameters parameters, int size);

    /**
     * 从数组中选取
     *
     * @param array      数组
     * @param parameters 选取参数，用于给不同的选取器提供选取的基准
     * @return 如果无满足条件的对象，返回<code>null</code>
     */
    public <V> V select(V[] array, Parameters parameters)
    {
        if (array == null) {
            return null;
        }
        int size = array.length;
        if (size == 0) {
            return null;
        }
        return doSelect(array, parameters, size);
    }

    /**
     * 从数组中选取
     *
     * @param array      数组
     * @param parameters 选取参数，用于给不同的选取器提供选取的基准
     * @return 如果无满足条件的对象，返回<code>null</code>
     */
    protected abstract <V> V doSelect(V[] array, Parameters parameters, int size);

    /**
     * 在列表的指定位置选取
     *
     * @param i    索引，必需满足 [0, size)
     * @param list 列表
     * @param size 大小
     */
    protected <V> V peek(int i, List<V> list, int size)
    {
        int r = i;
        V obj = null;
        do {
            obj = list.get(i);
            i = (i + 1) % size;
        } while (i != r);
        return obj;
    }

    /**
     * 在列表的指定位置选取
     *
     * @param i     索引，必需满足 [0, size)
     * @param array 数组
     * @param size  大小
     */
    protected <V> V peek(int i, V[] array, int size)
    {
        int r = i;
        V obj = null;
        do {
            obj = array[i];
            i = (i + 1) % size;
        } while (i != r);
        return obj;
    }
}
