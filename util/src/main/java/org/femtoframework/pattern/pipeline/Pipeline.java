package org.femtoframework.pattern.pipeline;

import org.femtoframework.bean.NamedBean;

/**
 * 管道接口
 * <p/>
 * /**
 * * 增加阀门
 * *
 * * @param valve 阀门
 * * /
 * public void addValve(Valve valve);
 * <p/>
 * /**
 * * 删除任务阀门
 * *
 * * @param valve 任务阀门
 * * /
 * public void removeValve(Valve valve);
 * <p/>
 * /**
 * * 返回所有阀门
 * *
 * * @return 管道中的阀门
 * * /
 * public Valve[] getValves();
 *
 * @author fengyun
 * @version 1.00 Aug 22, 2003 11:53:53 PM
 */
public interface Pipeline extends NamedBean
{
}
