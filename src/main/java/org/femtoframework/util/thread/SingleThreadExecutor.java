package org.femtoframework.util.thread;


import org.femtoframework.bean.BeanPhase;
import org.femtoframework.bean.annotation.Property;

import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.Executor;

/**
 * 单线程执行器
 *
 * @author fengyun
 * @version 1.00 2005-3-19 2:07:08
 */
public class SingleThreadExecutor extends AbstractExecutorService implements LifecycleExecutorService
{
    /**
     * 单线程执行
     */
    static class SingleThread extends LifecycleThread implements Executor
    {
        private Runnable runnable;

        private Object lock = new Object();

        /**
         * 实际要执行的任务方法。<br>
         * 通过这个方法，来执行实际的程序<br>
         * 如果出现异常，ErrorHandler的错误处理返回<code>true</code>，<br>
         * 那么该循环线程就终止循环。
         *
         * @throws Exception 各类执行异常
         * @see #run()
         */
        protected void doRun() throws Exception
        {
            if (runnable == null) {
                synchronized (lock) {
                    lock.wait();
                }
            }
            if (runnable != null) {
                runnable.run();
                runnable = null;
            }
            synchronized (lock) {
                lock.notify();
            }
        }

        /**
         * 执行Runnable，可能直接调用run方法，也可能创建一个新的线程来执行，<br>
         * 或者采用线程池来执行等。
         *
         * @param runnable 可执行程序单元
         * @throws java.util.concurrent.RejectedExecutionException 当任务无法被执行的时候抛出这个异常
         * @throws NullPointerException       当runnable是Null的时候
         */
        public void execute(Runnable runnable)
        {
            if (runnable == null) {
                throw new IllegalArgumentException("Null runnable");
            }
            while (this.runnable != null) {
                synchronized (lock) {
                    try {
                        lock.wait();
                    }
                    catch (InterruptedException e) {
                    }
                }
                if (this.runnable == null) {
                    break;
                }
            }
            this.runnable = runnable;
            synchronized (lock) {
                lock.notify();
            }
        }
    }

    private SingleThread thread;

    /**
     * 是否采用Daemon形式的线程
     */
    @Property("daemon")
    private boolean daemon = false;

    /**
     * 是否采用Daemon的模式
     *
     * @return [boolean]
     */
    public boolean isDaemon()
    {
        return daemon;
    }

    /**
     * 设置Daemon模式
     *
     * @param daemon 是否Daemon
     */
    public void setDaemon(boolean daemon)
    {
        this.daemon = daemon;
    }


    private BeanPhase beanPhase = BeanPhase.DISABLED;

    /**
     * Implement method of getPhase
     *
     * @return BeanPhase
     */
    @Override
    public BeanPhase _doGetPhase() {
        return beanPhase;
    }

    /**
     * Phase setter for internal
     *
     * @param phase BeanPhase
     */
    @Override
    public void _doSetPhase(BeanPhase phase) {
        this.beanPhase = phase;
    }

    /**
     * 初始化实现
     */
    public void _doInitialize()
    {
        thread = new SingleThread();
        thread.setDaemon(daemon);
    }

    /**
     * 实际启动实现
     */
    public void _doStart()
    {
        thread.start();
    }

    /**
     * 实际停止实现
     */
    public void _doStop()
    {
        thread.stop();
    }

    /**
     * 实际销毁实现
     */
    public void _doDestroy()
    {
        thread.destroy();
        thread = null;
    }

    /**
     * 执行Runnable，可能直接调用run方法，也可能创建一个新的线程来执行，<br>
     * 或者采用线程池来执行等。
     *
     * @param runnable 可执行程序单元
     * @throws java.util.concurrent.RejectedExecutionException 当任务无法被执行的时候抛出这个异常
     * @throws NullPointerException       当runnable是Null的时候
     */
    public void execute(Runnable runnable)
    {
        if (getBeanPhase().isBefore(BeanPhase.STARTING)) {
            start();
        }
        thread.execute(runnable);
    }
}
