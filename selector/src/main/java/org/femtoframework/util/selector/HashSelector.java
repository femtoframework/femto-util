package org.femtoframework.util.selector;

import org.femtoframework.parameters.Parameters;

import java.util.List;

/**
 * 采用Hash算法的选取器
 *
 * @author fengyun
 * @version 1.00 2004-3-14 21:29:39
 */
public class HashSelector extends RandomSelector
{
    /**
     * 哈希字段名
     */
    private String parameter = null;

    public HashSelector(String parameter) {
        this.parameter = parameter;
    }

    /**
     * 从列表中选取
     *
     * @param list       列表
     * @param parameters 选取参数，用于给不同的选取器提供选取的基准
     * @return 如果无满足条件的对象，返回<code>null</code>
     */
    protected <V> V doSelect(List<V> list, Parameters parameters, int size)
    {
        if (parameters == null || parameter == null) {
            //如果相应的参数集合或者参数为空，采用随机选取
            return super.doSelect(list, parameters, size);
        }

        Object obj = parameters.get(parameter);
        if (obj == null) {
            //如果没有对应的参数值，同样采用随机选取
            return super.doSelect(list, parameters, size);
        }
        else {
            int hashCode = obj.hashCode();
            int index = hashCode % size;
            return super.peek(index, list, size);
        }
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
        if (parameters == null || parameter == null) {
            //如果相应的参数集合或者参数为空，采用随机选取
            return super.doSelect(array, parameters, size);
        }

        Object obj = parameters.get(parameter);
        if (obj == null) {
            //如果没有对应的参数值，同样采用随机选取
            return super.doSelect(array, parameters, size);
        }
        else {
            int hashCode = obj.hashCode();
            int index = hashCode % size;
            return super.peek(index, array, size);
        }
    }

    /**
     * 返回哈希字段名
     */
    public String getParameter()
    {
        return parameter;
    }

    /**
     * 设置哈希字段名
     *
     * @param parameter 字段名称
     */
    public void setParameter(String parameter)
    {
        this.parameter = parameter;
    }

}
