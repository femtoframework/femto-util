package org.femtoframework.util.selector;

import org.femtoframework.parameters.Parameters;

import java.util.List;

/**
 * 对象选取器
 *
 * @author fengyun
 * @version 1.00 2004-3-14 18:45:44
 */
public interface ListSelector
{
    /**
     * 从列表中选取
     *
     * @param list 列表，要从外部保证LIST数据的不变性，在调用这个方法的时候只能单线程访问该参数<br>
     * @return 如果无满足条件的对象，返回<code>null</code>
     */
    default <V> V select(List<V> list) {
        return select(list, null);
    }

    /**
     * 从列表中选取
     *
     * @param list       列表，要从外部保证LIST数据的不变性，在调用这个方法的时候只能单线程访问该参数<br>
     * @param parameters 选取参数，用于给不同的选取器提供选取的基准
     * @return 如果无满足条件的对象，返回<code>null</code>
     */
    <V> V select(List<V> list, Parameters parameters);


    /**
     * 从列表中选取
     *
     * @param array 数组，要从外部保证LIST数据的不变性，在调用这个方法的时候只能单线程访问该参数<br>
     * @return 如果无满足条件的对象，返回<code>null</code>
     */
    default <V> V select(V[] array) {
        return select(array, null);
    }

    /**
     * 从列表中选取
     *
     * @param array      数组，要从外部保证LIST数据的不变性，在调用这个方法的时候只能单线程访问该参数<br>
     * @param parameters 选取参数，用于给不同的选取器提供选取的基准
     * @return 如果无满足条件的对象，返回<code>null</code>
     */
    <V> V select(V[] array, Parameters parameters);

}
