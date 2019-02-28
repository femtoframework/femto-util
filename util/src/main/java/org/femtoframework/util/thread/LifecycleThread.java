package org.femtoframework.util.thread;

import org.femtoframework.bean.BeanPhase;
import org.femtoframework.bean.LifecycleMBean;
import org.femtoframework.bean.Nameable;
import org.femtoframework.bean.NamedBean;
import org.femtoframework.bean.annotation.Ignore;
import org.femtoframework.bean.exception.StopException;

/**
 * An thread + lifecycle adapter
 */
public abstract class LifecycleThread implements LifecycleMBean, Runnable, NamedBean, Nameable {

    private String name;

    private boolean running;

    private boolean daemon;

    /**
     * Thread
     */
    @Ignore
    private Thread thread;

    /**
     * Error Handler
     */
    protected ErrorHandler handler = null;

    private BeanPhase beanPhase = BeanPhase.DISABLED;

    public boolean isDaemon()
    {
        return daemon;
    }

    public boolean isRunning() {
        return running;
    }

    protected void setRunning(boolean running) {
        this.running = running;
    }

    public void setDaemon(boolean daemon)
    {
        this.daemon = daemon;
    }

    /**
     * Implement method of getPhase
     *
     * @return BeanPhase
     */
    public BeanPhase _doGetPhase() {
        return beanPhase;
    }

    /**
     * Phase setter for internal
     *
     * @param phase BeanPhase
     */
    public void _doSetPhase(BeanPhase phase) {
        this.beanPhase = phase;
    }

    public void _doInit()
    {
        thread = createThread();
        thread.setDaemon(daemon);
    }

    protected Thread createThread()
    {
        String name = getName();
        Thread thread = null;
        if (name == null) {
            thread = new Thread(this);
            setName(thread.getName());
        }
        else {
            thread = new Thread(this, name);
        }
        return thread;
    }

    public void _doStart()
    {
        running = true;
        thread.start();
    }

    public synchronized void _doStop()
    {
        running = false;
        this.notify();

        if (thread != Thread.currentThread()) {
            try {
                //Sleep a second
                Thread.sleep(100);

                //Thread thread should be interrupted
                thread.interrupt();
            }
            catch (InterruptedException ie) {
                throw new StopException("Interrupt thread exception", ie);
            }
        }
    }

    /**
     * 实现销毁
     */
    public void _doDestroy()
    {
        if (running) {
            thread.interrupt();
            Thread.yield();
            thread = null;
        }
    }

    /**
     * Runnable
     *
     * @see java.lang.Runnable
     */
    public void run()
    {
        while (running) {
            try {
                doRun();
            }
            catch (Exception ex) {
                if (handleException(ex)) {
                    break;
                }
            }
            catch (Throwable t) {
                if (handleError(t)) {
                    break;
                }
            }
        }
        running = false;
    }


    /**
     * 设置错误处理
     *
     * @param handler 错误处理器
     * @see ErrorHandler
     */
    public void setErrorHandler(ErrorHandler handler)
    {
        this.handler = handler;
    }

    /**
     * 返回错误处理器
     *
     * @return 错误处理器
     */
    public ErrorHandler getErrorHandler()
    {
        return handler;
    }

    /**
     * 处理执行异常
     *
     * @param e 异常
     * @return [true|false] 是否需要结束循环
     */
    protected boolean handleException(Exception e)
    {
        return handler != null && handler.handleException(e);
    }


    /**
     * 处理执行错误
     *
     * @param t 错误
     * @return [true|false] 是否需要结束循环
     */
    protected boolean handleError(Throwable t)
    {
        return handler != null && handler.handleError(t);
    }

    /**
     * The real logic
     *
     * @throws Exception Exception
     * @see #run()
     */
    protected abstract void doRun() throws Exception;


    /**
     * Current thread
     *
     * @return Current thread
     */
    public Thread getThread()
    {
        return thread;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }
}
