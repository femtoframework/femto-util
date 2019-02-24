package org.femtoframework.util.selector;

import org.femtoframework.parameters.Parameters;

import java.util.List;
import java.util.Random;

/**
 * 随机选取
 *
 * @author fengyun
 * @version 1.00 2004-3-14 21:26:12
 */
public class RandomSelector extends AbstractListSelector
{
    //随机分配
    private Random random = new Random();

    /**
     * 从列表中选取
     *
     * @param list       列表
     * @param parameters 选取参数，用于给不同的选取器提供选取的基准
     * @return 如果无满足条件的对象，返回<code>null</code>
     */
    protected <V> V doSelect(List<V> list, Parameters parameters, int size)
    {
        int r = random.nextInt(size);
        return peek(r, list, size);
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
        int r = random.nextInt(size);
        return peek(r, array, size);
    }

}
