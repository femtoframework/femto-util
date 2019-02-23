package org.femtoframework.util.thread;

import org.femtoframework.bean.BeanPhase;
import org.femtoframework.bean.Destroyable;
import org.femtoframework.bean.annotation.Ignore;
import org.femtoframework.pattern.Loggable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executor;

/**
 * Thread Controller
 *
 * @author fengyun
 * @version 1.5 2005-2-19 11:59:16
 */
public class ThreadController extends LifecycleThread implements Executor, Loggable
{
    /**
     * ThreadContainer
     *
     * @see ThreadContainer
     */
    @Ignore
    private ThreadContainer container;

    /**
     * The method that is executed in this thread
     */
    protected Runnable task;

    /**
     * Activate the execution of the action
     */
    protected boolean shouldRun;

    /**
     * Logger
     */
    @Ignore
    protected Logger logger = LoggerFactory.getLogger(ThreadController.class);

    /**
     * 构造，只能通过继承的形式来构造
     */
    protected ThreadController()
    {
        task = null;
        shouldRun = false;
    }

    /**
     * 返回所在的线程容器
     *
     * @return
     */
    public ThreadContainer getContainer()
    {
        return container;
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

    private static class InternalControlledThread extends Thread implements ControlledThread
    {
        private ThreadController controller;

        public InternalControlledThread(Runnable target)
        {
            super(target);
        }

        public InternalControlledThread(Runnable target, String name)
        {
            super(target, name);
        }

        public ThreadController getController()
        {
            return controller;
        }

        public void setController(ThreadController controller)
        {
            this.controller = controller;
        }
    }

    protected Thread createThread()
    {
        String name = getName();
        InternalControlledThread thread = null;
        if (name == null) {
            thread = new InternalControlledThread(this);
            setName(thread.getName());
        }
        else {
            thread = new InternalControlledThread(this, name);
        }
        thread.setController(this);
        return thread;
    }

    protected void doRun() throws InterruptedException
    {
        synchronized (this) {
            if (!shouldRun && isRunning()) {
                this.wait();
            }
        }
        if (!isRunning()) {
            if (logger.isDebugEnabled()) {
                logger.debug("Controller stopped");
            }
            return;
        }
        if (task == null) {
            if (logger.isDebugEnabled()) {
                logger.debug("No task ???");
            }
            return;
        }
        /* Check if should execute a runnable.  */
        try {
            if (shouldRun) {
                task.run();
            }
        }
        catch (Throwable t) {
            if (logger.isDebugEnabled()) {
                logger.debug("Caught exception executing "
                        + task + ", terminating thread", t);
            }

            setRunning(false);
            shouldRun = false;
            container.terminate(this);
        }
        finally {
            if (task != null) {
                if (task instanceof Destroyable) {
                    ((Destroyable)task).destroy();
                }
                task = null;
            }
            if (shouldRun) {
                shouldRun = false;
                container.recycle(this);
            }
        }
    }

    protected boolean handleError(Throwable t)
    {
        logger.error("Error in #doRun", t);
        return false;
    }

    protected boolean handleException(Exception e)
    {
        if (e instanceof InterruptedException) {
            logger.debug("Thread interrupted", e);
            return true;
        }
        else {
            logger.debug("#doRun Exception", e);
        }

        return false;
    }

    public void setLogger(Logger logger)
    {
        this.logger = logger;
    }

    public Logger getLogger()
    {
        return logger;
    }

    /**
     * 设置所在的线程容器
     *
     * @param container 线程容器
     */
    public void setContainer(ThreadContainer container)
    {
        this.container = container;
    }

    /**
     * 执行Runnable，可能直接调用run方法，也可能创建一个新的线程来执行，<br>
     * 或者采用线程池来执行等。
     *
     * @param task 可执行程序单元
     * @throws java.util.concurrent.RejectedExecutionException
     *                              当任务无法被执行的时候抛出这个异常
     * @throws NullPointerException 当runnable是Null的时候
     */
    public synchronized void execute(Runnable task)
    {
        if (task == null) {
            throw new NullPointerException("Null Runnable");
        }
        this.task = task;
        this.shouldRun = true;
        this.notify();
    }
}
