package org.femtoframework.pattern.pipeline;

import org.femtoframework.bean.NamedBean;

/**
 * 阀门
 * <p/>
 * 根据具体应用的不同采用具体的调用方法，一般是跟下面类似的方法定义：
 * <p/>
 * <p/>
 * /**
 * * 处理上下文
 * *
 * * @param context 上下文
 * * @param chain 管道链
 * * /
 * public void handle(ValveContext context, ValveChain chain);
 *
 * @author fengyun
 * @version 1.00 Aug 22, 2003 11:52:44 PM
 */
public interface Valve extends NamedBean
{
}

