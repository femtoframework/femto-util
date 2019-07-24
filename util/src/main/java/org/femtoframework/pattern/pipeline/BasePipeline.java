package org.femtoframework.pattern.pipeline;


import org.femtoframework.bean.Nameable;

import java.util.ArrayList;
import java.util.List;

/**
 * 基础的Pipeline样板实现
 *
 * @author fengyun
 * @version 1.00 2006-3-11 10:09:25
 */
public class BasePipeline<V> implements Pipeline, Nameable
{
    private String name;
    private List<V> valves;

    public BasePipeline(int initSize)
    {
        valves = new ArrayList<V>(initSize);
    }

    public BasePipeline()
    {
        this(2);
    }

    /**
     * 增加阀门
     *
     * @param valve 阀门
     */
    public void addValve(V valve)
    {
        valves.add(valve);
    }

    /**
     * 删除任务阀门
     *
     * @param valve 任务阀门
     */
    public void removeValve(V valve)
    {
        valves.remove(valve);
    }

    /**
     * 返回所有阀门
     *
     * @return 管道中的阀门
     */
    public List<V> getValves()
    {
        return valves;
    }


    /**
     * Set valves
     *
     * @param valves Values
     */
    public void setValves(List<V> valves) {
        this.valves = valves;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
