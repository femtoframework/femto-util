package org.femtoframework.util.thread;

import org.femtoframework.bean.BeanPhase;
import org.femtoframework.bean.annotation.Ignore;
import org.femtoframework.util.timer.RunnableTask;
import org.femtoframework.util.timer.Timer;

import java.util.concurrent.*;

/**
 * Simple Thread Scheduler
 *
 * @author Sheldon Shao
 * @version 2.0
 */
public class SingleThreadScheduler extends AbstractExecutorService implements LifecycleExecutorService, ScheduleService {

    @Ignore
    private Timer timer;

    private boolean daemon = false;

    public boolean isDaemon()
    {
        return daemon;
    }

    public void setDaemon(boolean daemon)
    {
        this.daemon = daemon;
    }

    public void _doStart() {
        timer = new Timer(isDaemon());
    }

    public void _doStop() {
        if (timer != null) {
            timer.cancel();
        }
    }

    public void _doDestroy() {
        timer = null;
    }

    /**
     * Executes the given command at some time in the future.  The command
     * may execute in a new thread, in a pooled thread, or in the calling
     * thread, at the discretion of the {@code Executor} implementation.
     *
     * @param command the runnable task
     * @throws RejectedExecutionException if this task cannot be
     *                                    accepted for execution
     * @throws NullPointerException       if command is null
     */
    @Override
    public void execute(Runnable command) {
        ensureStarted();
        timer.schedule(new RunnableTask(command), 0);
    }

    protected void ensureStarted() {
        if (getBeanPhase().isBefore(BeanPhase.STARTING)) {
            start();
        }
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
    public ScheduledFuture schedule(Runnable runnable, long delay)
    {
        ensureStarted();

        ScheduledFutureTask task = new ScheduledFutureTask(runnable);
        timer.schedule(task, delay);
        return task;
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
    public <V> ScheduledFuture<V> schedule(Callable<V> callable, long delay)
    {
        ensureStarted();
        ScheduledFutureTask<V> task = new ScheduledFutureTask<V>(callable);
        timer.schedule(task, delay);
        return task;
    }

    /**
     * Schedules runnable by cron expression
     *
     * @param runnable the task to execute.
     * @param cron     Cron Expression
     * @return a Future representing pending completion of the task,
     * and whose <tt>get()</tt> method will return <tt>null</tt>
     * upon completion.
     * @throws java.util.concurrent.RejectedExecutionException if task cannot be scheduled
     *                                                         for execution.
     * @throws NullPointerException                            if runnable is null
     * @see "https://www.freeformatter.com/cron-expression-generator-quartz.html"
     */
    public ScheduledFuture schedule(Runnable runnable, String cron)
    {
        ensureStarted();
        CronFutureTask task = new CronFutureTask(runnable);
        task.setCron(cron);
        long now = System.currentTimeMillis();
        long next = task.nextExecutionTime(now);
        long period = next - now;
        if (period <= 0) {
            period = 1000;
        }
        timer.schedule(task, period, period);
        return task;
    }

    /**
     * Schedules callable by cron expression
     *
     * @param callable the function to execute.
     * @param cron     Cron Expression
     * @return a ScheduledFuture that can be used to extract result or cancel.
     * @throws java.util.concurrent.RejectedExecutionException if task cannot be scheduled
     *                                                         for execution.
     * @throws NullPointerException                            if callable is null
     */
    public <V> ScheduledFuture<V> schedule(Callable<V> callable, String cron)
    {
        ensureStarted();
        CronFutureTask<V> task = new CronFutureTask<V>(callable);
        task.setCron(cron);
        long now = System.currentTimeMillis();
        long next = task.nextExecutionTime(now);
        timer.schedule(task, next - now);
        return task;
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
    public ScheduledFuture scheduleAtFixedRate(Runnable runnable, long initialDelay, long period)
    {
        ensureStarted();
        ScheduledFutureTask task = new ScheduledFutureTask(runnable);
        timer.scheduleAtFixedRate(task, initialDelay, period);
        return task;
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
    public ScheduledFuture scheduleWithFixedDelay(Runnable runnable, long initialDelay, long delay)
    {
        ensureStarted();
        ScheduledFutureTask task = new ScheduledFutureTask(runnable);
        timer.schedule(task, initialDelay, delay);
        return task;
    }
}
