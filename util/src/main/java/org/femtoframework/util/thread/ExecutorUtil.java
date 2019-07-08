package org.femtoframework.util.thread;


import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;


/**
 * Executor工具类
 *
 * @author fengyun
 * @since 5.0
 */
public class ExecutorUtil
{
    /**
     * 创建一个线程池，用给定的各项参数进行参数注入，并且自动初始化和启动
     *
     * @return the newly created thread pool
     */
    public static ExecutorService newThreadPool(String name, int minSpareThreads, int maxSpareThreads, int maxThreads)
    {
        ThreadPool pool = new ThreadPool();
        pool.setName(name);
        pool.setMinSpareThreads(minSpareThreads);
        pool.setMaxSpareThreads(maxSpareThreads);
        pool.setMaxThreads(maxThreads);
        pool.start();
        return pool;
    }

    /**
     * 创建一个线程池，用给定的各项参数进行参数注入，并且自动初始化和启动
     *
     * @return the newly created thread pool
     */
    public static ExecutorService newThreadPool(String name)
    {
        ThreadPool pool = new ThreadPool();
        pool.setName(name);
        pool.start();
        return pool;
    }

    /**
     * 需要外部调用#start方法
     * <p/>
     * Creates an Executor that uses a single worker thread operating
     * off an unbounded queue. (Note however that if this single
     * thread terminates due to a failure during execution prior to
     * shutdown, a new one will take its place if needed to execute
     * subsequent tasks.)  Tasks are guaranteed to execute
     * sequentially, and no more than one task will be active at any
     * given time. Unlike the otherwise equivalent
     * <tt>newFixedThreadPool(1)</tt> the returned executor is
     * guaranteed not to be reconfigurable to use additional threads.
     *
     * @return the newly created single-threaded Executor
     */
    public static ExecutorService newSingleThreadExecutor()
    {
        return new SingleThreadExecutor();
    }

    /**
     * 需要外部调用#start方法
     * <p/>
     * Creates a single-threaded executor that can schedule commands
     * to run after a given delay, or to execute periodically.
     * (Note however that if this single
     * thread terminates due to a failure during execution prior to
     * shutdown, a new one will take its place if needed to execute
     * subsequent tasks.)  Tasks are guaranteed to execute
     * sequentially, and no more than one task will be active at any
     * given time. Unlike the otherwise equivalent
     * <tt>newScheduledThreadPool(1)</tt> the returned executor is
     * guaranteed not to be reconfigurable to use additional threads.
     *
     * @return the newly created scheduled executor
     */
    public static ScheduleService newSingleThreadScheduler()
    {
        return new SingleThreadScheduler();
    }


    /**
     * Cannot instantiate.
     */
    private ExecutorUtil()
    {
    }

    /**
     * 简单单线程调度器
     */
    private static SingleThreadScheduler scheduler = new SingleThreadScheduler();

    /**
     * Creates and executes a one-shot action that becomes enabled
     * after the given delay.
     *
     * @param runnable the task to execute.
     * @param delay    the time from now to delay execution.
     * @return a Future representing pending completion of the task,
     *         and whose <tt>get()</tt> method will return <tt>null</tt>
     *         upon completion.
     * @throws java.util.concurrent.RejectedExecutionException
     *                              if task cannot be scheduled
     *                              for execution.
     * @throws NullPointerException if runnable is null
     */
    public static ScheduledFuture schedule(Runnable runnable, long delay)
    {
        return scheduler.schedule(runnable, delay, TimeUnit.MILLISECONDS);
    }

    /**
     * Creates and executes a ScheduledFuture that becomes enabled after the
     * given delay.
     *
     * @param callable the function to execute.
     * @param delay    the time from now to delay execution.
     * @return a ScheduledFuture that can be used to extract result or cancel.
     * @throws java.util.concurrent.RejectedExecutionException
     *                              if task cannot be scheduled
     *                              for execution.
     * @throws NullPointerException if callable is null
     */
    public static <V> ScheduledFuture<V> schedule(Callable<V> callable, long delay)
    {
        return scheduler.schedule(callable, delay, TimeUnit.MILLISECONDS);
    }


    /**
     * 根据时间表达式来提交任务
     *
     * @param runnable the task to execute.
     * @param cron     时间表达式
     * @return a Future representing pending completion of the task,
     *         and whose <tt>get()</tt> method will return <tt>null</tt>
     *         upon completion.
     * @throws java.util.concurrent.RejectedExecutionException
     *                              if task cannot be scheduled
     *                              for execution.
     * @throws NullPointerException if runnable is null
     */
    public static ScheduledFuture schedule(Runnable runnable, String cron)
    {
        return scheduler.schedule(runnable, cron);
    }

    /**
     * Creates and executes a ScheduledFuture that becomes enabled after the
     * given delay.
     *
     * @param callable the function to execute.
     * @param cron     时间表达式
     * @return a ScheduledFuture that can be used to extract result or cancel.
     * @throws java.util.concurrent.RejectedExecutionException
     *                              if task cannot be scheduled
     *                              for execution.
     * @throws NullPointerException if callable is null
     */
    public static <V> ScheduledFuture<V> schedule(Callable<V> callable, String cron)
    {
        return scheduler.schedule(callable, cron);
    }

    /**
     * Creates and executes a periodic action that becomes enabled first
     * after the given initial delay, and subsequently with the given
     * period; that is executions will commence after
     * <tt>initialDelay</tt> then <tt>initialDelay+period</tt>, then
     * <tt>initialDelay + 2 * period</tt>, and so on.
     * If any execution of the task
     * encounters an exception, subsequent executions are suppressed.
     * Otherwise, the task will only terminate via cancellation or
     * termination of the executor.
     *
     * @param runnable     the task to execute.
     * @param initialDelay the time to delay first execution.
     * @param period       the period between successive executions.
     * @return a Future representing pending completion of the task,
     *         and whose <tt>get()</tt> method will throw an exception upon
     *         cancellation.
     * @throws java.util.concurrent.RejectedExecutionException
     *                                  if task cannot be scheduled
     *                                  for execution.
     * @throws NullPointerException     if runnable is null
     * @throws IllegalArgumentException if period less than or equal to zero.
     */
    public static ScheduledFuture scheduleAtFixedRate(Runnable runnable, long initialDelay, long period)
    {
        return scheduler.scheduleAtFixedRate(runnable, initialDelay, period);
    }

    /**
     * Creates and executes a periodic action that becomes enabled first
     * after the given initial delay, and subsequently with the
     * given delay between the termination of one execution and the
     * commencement of the next. If any execution of the task
     * encounters an exception, subsequent executions are suppressed.
     * Otherwise, the task will only terminate via cancellation or
     * termination of the executor.
     *
     * @param runnable     the task to execute.
     * @param initialDelay the time to delay first execution.
     * @param delay        the delay between the termination of one
     *                     execution and the commencement of the next.
     * @return a Future representing pending completion of the task,
     *         and whose <tt>get()</tt> method will throw an exception upon
     *         cancellation.
     * @throws java.util.concurrent.RejectedExecutionException
     *                                  if task cannot be scheduled
     *                                  for execution.
     * @throws NullPointerException     if runnable is null
     * @throws IllegalArgumentException if delay less than or equal to zero.
     */
    public static ScheduledFuture scheduleWithFixedDelay(Runnable runnable, long initialDelay, long delay)
    {
        return scheduler.scheduleWithFixedDelay(runnable, initialDelay, delay);
    }

}
