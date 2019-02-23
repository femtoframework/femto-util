package org.femtoframework.util.thread;

import org.femtoframework.bean.BeanPhase;
import org.femtoframework.bean.Nameable;
import org.femtoframework.bean.NamedBean;
import org.femtoframework.bean.annotation.Description;
import org.femtoframework.bean.annotation.Ignore;
import org.femtoframework.bean.annotation.Property;
import org.femtoframework.pattern.Loggable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RejectedExecutionException;

/**
 * Thread Pool implementation
 *
 * @author fengyun
 * @version 2.0 Since 2005
 * @version 1.00 2005-2-19 15:07:34
 */
public class ThreadPool extends AbstractExecutorService implements ThreadContainer, LifecycleExecutorService, Loggable, NamedBean, Nameable {
    public static final int MAX_THREADS = 100;
    public static final int MAX_SPARE_THREADS = 50;
    public static final int MIN_SPARE_THREADS = 10;
    public static final int INC_THREADS = 10;

    private String name;

    protected ThreadController[] pool;

    @Property(value = "maxThreads", defaultValue ="100")
    @Description("Maximum threads")
    protected int maxThreads = MAX_THREADS;


    @Ignore
    private int mask = 0;

    @Property (value = "minSpareThreads", defaultValue = "10")
    @Description("Minimun spare threads")
    protected int minSpareThreads = MIN_SPARE_THREADS;

    @Property (value = "maxSpareThreads", defaultValue = "50")
    @Description("Maximum spare threads")
    protected int maxSpareThreads = MAX_SPARE_THREADS;

    @Property (value = "incThreads", defaultValue = "10")
    private int incThreads = INC_THREADS;

    @Property (value = "currentThreadCount", writable = false)
    @Description("Current count of threads")
    protected int currentThreadCount;
    /*
     * Flag that the pool should terminate all the threads and stop.
     */
    protected boolean stopThePool;

    @Ignore
    private final Object headLock = new Object();

    @Ignore
    private final Object tailLock = new Object();

    @Ignore
    private int head = 0;

    @Ignore
    private int tail = head;

    @Ignore
    protected Logger logger;

    public ThreadPool()
    {
        currentThreadCount = 0;
        stopThePool = false;
    }

    public void _doInitialize()
    {
        if (logger == null) {
            logger = LoggerFactory.getLogger(getName());
        }
        adjustLimits();
        this.mask = maxThreads + 1;
        this.pool = new ThreadController[mask];

        logger.info(getName() + "#init{ MaxThreads:" + maxThreads
                + ",MaxSpareThreads:" + maxSpareThreads
                + ",MinSpareThreads:" + minSpareThreads + " }");

        monitorThread = new LifecycleThread()
        {
            /**
             * 实际要执行的任务方法。<br>
             * 通过这个方法，来执行实际的程序<br>
             * 如果出现异常，ErrorHandler的错误处理返回<code>true</code>，<br>
             * 那么该循环线程就终止循环。
             *
             * @throws Exception 各类执行异常
             */
            protected void doRun() throws Exception
            {
                Thread.sleep(monitorPeriod);
                doMonitor();
            }
        };
        monitorThread.setDaemon(daemon);
    }

    public synchronized void _doStart()
    {
        openThreads(minSpareThreads);
    }

    public void setMaxThreads(int maxThreads)
    {
        this.maxThreads = maxThreads;
    }

    public int getMaxThreads()
    {
        return maxThreads;
    }

    public void setMinSpareThreads(int minSpareThreads)
    {
        this.minSpareThreads = minSpareThreads;
    }

    public int getMinSpareThreads()
    {
        return minSpareThreads;
    }

    public void setMaxSpareThreads(int maxSpareThreads)
    {
        this.maxSpareThreads = maxSpareThreads;
    }

    public int getMaxSpareThreads()
    {
        return maxSpareThreads;
    }

    public int getCurrentThreadCount()
    {
        return currentThreadCount;
    }

    @Property (value = "currentThreadBusy",writable = false)
    @Description("Current threads in running")
    public int getCurrentThreadBusy()
    {
        return mask == 0 ? 0 : currentThreadCount - ((mask + tail - head) % mask);
    }

    public ThreadController compete(int timeout)
    {
        ThreadController controller = allocate();
        if (controller != null) {
            return controller;
        }

        synchronized (headLock) {
            while (true) {
                int spare = (mask + tail - head) % mask;
                if (spare <= 0) {
                    if (currentThreadCount < maxThreads) {
                        controller = createThread();
                        break;
                    }
                    else {
                        competeNoThread();
                        long startTime = 0;
                        try {
                            if (timeout > 0) {
                                startTime = System.currentTimeMillis();
                                headLock.wait(timeout);
                            }
                            else {
                                headLock.wait(100);
                            }
                        }
                        catch (InterruptedException e) {
                            logger.error("Unexpected exception", e);
                        }
                        //如果已经超时
                        if (timeout > 0 && System.currentTimeMillis() - startTime > timeout) {
                            return null;
                        }
                    }
                }
                else {
                    controller = pool[head];
                    pool[head] = null;
                    head = ++head % mask;
                    break;
                }
                if (stopThePool) {
                    throw new RejectedExecutionException("Current thread count:" + currentThreadCount
                            + " stop the pool:" + stopThePool);
                }
            }
        }
        return controller;
    }

    public ThreadController compete()
    {
        return compete(-1);
    }

    public ThreadController allocate()
    {
        if (stopThePool) {
            throw new RejectedExecutionException("Current thread count:" + currentThreadCount
                    + " stop the pool:" + stopThePool);
        }

        ThreadController controller = null;
        synchronized (headLock) {
            int spare = (mask + tail - head) % mask;
            if (spare <= 0) {
                if (currentThreadCount < maxThreads) {
                    int toOpen = currentThreadCount + incThreads;
                    openThreads(toOpen);
                }
                else {
                    return allocateNoThread();
                }
            }
            // If we are here it means that there is a free thread. Take it.
            controller = pool[head];
            pool[head] = null;
            head = ++head % mask;
        }
        return controller;
    }

    protected void competeNoThread()
    {
//        log.debug("Pool exhausted with " + currentThreadCount
//                  + " threads.");
    }

    protected ThreadController allocateNoThread()
    {
        logger.debug("Pool exhausted with " + currentThreadCount
                + " threads.");
        return null;
    }

    /**
     * Stop the thread pool
     */
    public synchronized void _doStop()
    {
        if (!stopThePool) {
            stopThePool = true;
            ThreadController controller = null;
            for (int i = 0; i < maxThreads; i++) {
                controller = pool[i];
                if (controller != null) {
                    try {
                        controller.stop();
                    }
                    catch (Throwable t) {
                        logger.error("Ignored exception while shutting down thread pool", t);
                    }
                }
            }
            currentThreadCount = 0;
            notifyAll();

            if (logger.isDebugEnabled()) {
                logger.debug("ThreadPool stopped");
            }
        }
    }

    /**
     * Called by the monitor thread to harvest idel threads.
     */
    protected void checkSpareControllers()
    {
        if (stopThePool) {
            return;
        }

        //从头部逐渐回收
        int spare = (mask + tail - head) % mask;
        if (spare > maxSpareThreads) {
            int freed = 0;
            while (true) {
                ThreadController c = null;
                synchronized (headLock) {
                    spare = (mask + tail - head) % mask;
                    if (spare > maxSpareThreads) {
                        c = pool[head];
                        pool[head] = null;
                        head = ++head % mask;
                    }
                    else {
                        break;
                    }
                }
                if (c != null) {
                    destroyIt(c);
                    synchronized (this) {
                        currentThreadCount--;
                    }
                    c = null;
                    freed++;
                }

                if (freed >= incThreads) {
                    //每次减少增长的线程数
                    break;
                }
            }
        }
    }

    /**
     * Returns the thread to the pool.
     * Called by threads as they are becoming idel.
     */
    public void recycle(ThreadController controller)
    {
        if (stopThePool) {
            controller.stop();
        }
        else {
            // atomic
            int spare = 0;
            synchronized (tailLock) {
                pool[tail] = controller;
                tail = ++tail % mask;

                spare = (mask + tail - head) % mask;
            }

            //Notify Waiting Threads
            if (spare <= 1) {
                synchronized (headLock) {
                    spare = (mask + tail - head) % mask;
                    if (spare <= 1) {
                        headLock.notifyAll();
                    }
                }
            }
        }
    }

    /**
     * 真正实施监控的方法
     *
     * @throws Exception
     */
    protected void doMonitor() throws Exception
    {
        try {
            checkSpareControllers();
        }
        catch (Throwable t) {
            logger.error("Error", t);
        }
    }

    /**
     * Terminate controller
     *
     * @param controller ThreadController
     */
    public synchronized void terminate(ThreadController controller)
    {
        currentThreadCount--;
        //Destroy It
        destroyIt(controller);

        notify();
    }

    protected void destroyIt(ThreadController controller)
    {
        try {
            controller.destroy();
        }
        catch (Throwable t) {
        }
    }


    /*
     * Checks for problematic configuration and fix it.
     * The fix provides reasonable settings for a single CPU
     * with medium load.
     */
    protected void adjustLimits()
    {
        if (maxThreads <= 0) {
            maxThreads = MAX_THREADS;
        }
        if (maxSpareThreads >= maxThreads) {
            maxSpareThreads = maxThreads;
        }
        if (maxSpareThreads <= 0) {
            if (1 == maxThreads) {
                maxSpareThreads = 1;
            }
            else {
                maxSpareThreads = maxThreads / 2;
            }
        }
        if (minSpareThreads > maxSpareThreads) {
            minSpareThreads = maxSpareThreads;
        }
        if (minSpareThreads <= 0) {
            if (1 == maxSpareThreads) {
                minSpareThreads = 1;
            }
            else {
                minSpareThreads = maxSpareThreads / 2;
            }
        }
        if (maxThreads - minSpareThreads < incThreads) {
            incThreads = maxThreads - minSpareThreads;
        }
    }

    @Property
    private int monitorPeriod = 60 * 1000;

    @Ignore
    private LifecycleThread monitorThread = null;

    private boolean daemon;

    public boolean isDaemon()
    {
        return daemon;
    }

    public void setDaemon(boolean daemon)
    {
        this.daemon = daemon;
    }

    protected ThreadController newThread()
    {
        return new ThreadController();
    }

    protected ThreadController createThread()
    {
        ThreadController controller = newThread();
        start(controller);
        synchronized (this) {
            currentThreadCount++;
        }
        return controller;
    }

    private static volatile int poolId = 1;

    private volatile int threadId = 1;

    protected static String getNextPoolName(String prefix)
    {
        return prefix.concat(String.valueOf(poolId++));
    }

    protected String getNextThreadName()
    {
        return getName() + "_thread" + String.valueOf(threadId++);
    }

    protected void start(ThreadController controller)
    {
        controller.setContainer(this);
        if (controller.getName() == null) {
            controller.setName(getNextThreadName());
        }
        if (controller.getLogger() == null && this instanceof Loggable) {
            controller.setLogger(((Loggable)this).getLogger());
        }
        controller.setDaemon(isDaemon());
        controller.start();
    }

    protected void openThreads(int toOpen)
    {
        if (toOpen > maxThreads) {
            toOpen = maxThreads;
        }

        for (int i = currentThreadCount; i < toOpen; i++) {
            ThreadController controller = createThread();

            synchronized (tailLock) {
                pool[tail] = controller;
                tail = ++tail % mask;
            }
        }

        if (logger.isDebugEnabled()) {
            logger.debug("Open threads:" + toOpen);
        }
    }

    /**
     * Destroy all the threads
     */
    protected void doDestroy()
    {
        clearPool();
    }

    private void clearPool()
    {
        if (pool != null) {
            for (int i = 0; i < pool.length; i++) {
                ThreadController controller = pool[i];
                if (controller != null) {
                    controller.destroy();
                    pool[i] = null;
                }
            }
        }
    }

    public int getIncThreads()
    {
        return incThreads;
    }

    public void setIncThreads(int incThreads)
    {
        this.incThreads = incThreads;
    }

    /**
     * 执行Runnable，可能直接调用run方法，也可能创建一个新的线程来执行，<br>
     * 或者采用线程池来执行等。
     *
     * @param runnable 可执行程序单元
     * @throws RejectedExecutionException 当任务无法被执行的时候抛出这个异常
     * @throws NullPointerException       当runnable是Null的时候
     */
    public void execute(Runnable runnable)
    {
        if (runnable == null) {
            throw new NullPointerException("Null runnable");
        }
        if (runnable instanceof FutureTask) {
            FutureTask task = (FutureTask)runnable;
            while (true) {
                ThreadController controller = compete(1000);
                if (task.isCancelled()) {
                    if (controller != null) {
                        recycle(controller);
                    }
                    //如果已经取消，那么不在等待
                    return;
                }
                if (controller != null) {
                    controller.execute(task);
                    break;
                }
                else {
                    //判断当前线程是否被中断？
                    if (Thread.currentThread().isInterrupted()) {
                        return;
                    }
                }
            }
        }
        else {
            ThreadController controller = compete();
            controller.execute(runnable);
        }
    }

    public void setLogger(Logger logger)
    {
        this.logger = logger;
    }

    public Logger getLogger()
    {
        return logger;
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

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }
}
