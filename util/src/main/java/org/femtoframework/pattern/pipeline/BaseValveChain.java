package org.femtoframework.pattern.pipeline;

import java.util.List;

/**
 * 基础的ValveChain
 * <p/>
 * <p/>
 * public void handleNext(SmsContext context) throws Exception
 * {
 * if (index < size) {
 * valves.get(index++).handle(context, this);
 * }
 * }
 *
 * @author fengyun
 * @version 1.00 2006-3-11 10:19:23
 */
public class BaseValveChain<V>
{
    protected List<V> valves = null;

    protected int index = 0;

    protected int size;

    public BaseValveChain(List<V> valves)
    {
        this.valves = valves;
        this.size = valves.size();
    }

    public BaseValveChain(List<V> valves, int index)
    {
        this(valves);
        this.index = index;
    }
}
