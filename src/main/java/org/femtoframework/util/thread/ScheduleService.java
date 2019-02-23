package org.femtoframework.util.thread;

import org.femtoframework.bean.Lifecycle;

import java.util.concurrent.*;

/**
 * Schedule Service
 *
 * An extension of ScheduledExecutorService
 */
public interface ScheduleService extends ScheduledExecutorService, Lifecycle {

    /**
     * Creates and executes a one-shot action that becomes enabled
     * after the given delay.
     *
     * @param command the task to execute
     * @param delay   the time from now to delay execution
     * @param unit    the time unit of the delay parameter
     * @return a ScheduledFuture representing pending completion of
     * the task and whose {@code get()} method will return
     * {@code null} upon completion
     * @throws RejectedExecutionException if the task cannot be
     *                                    scheduled for execution
     * @throws NullPointerException       if command is null
     */
    default ScheduledFuture<?> schedule(Runnable command,
                                        long delay, TimeUnit unit) {
        return schedule(command, unit.toMillis(delay));
    }

    /**
     * Creates and executes a ScheduledFuture that becomes enabled after the
     * given delay.
     *
     * @param callable the function to execute
     * @param delay    the time from now to delay execution
     * @param unit     the time unit of the delay parameter
     * @param <V>      the type of the callable's result
     * @return a ScheduledFuture that can be used to extract result or cancel
     * @throws RejectedExecutionException if the task cannot be
     *                                    scheduled for execution
     * @throws NullPointerException       if callable is null
     */
    default <V> ScheduledFuture<V> schedule(Callable<V> callable,
                                            long delay, TimeUnit unit) {
        return schedule(callable, unit.toMillis(delay));
    }

    /**
     * Creates and executes a periodic action that becomes enabled first
     * after the given initial delay, and subsequently with the given
     * period; that is executions will commence after
     * {@code initialDelay} then {@code initialDelay+period}, then
     * {@code initialDelay + 2 * period}, and so on.
     * If any execution of the task
     * encounters an exception, subsequent executions are suppressed.
     * Otherwise, the task will only terminate via cancellation or
     * termination of the executor.  If any execution of this task
     * takes longer than its period, then subsequent executions
     * may start late, but will not concurrently execute.
     *
     * @param command      the task to execute
     * @param initialDelay the time to delay first execution
     * @param period       the period between successive executions
     * @param unit         the time unit of the initialDelay and period parameters
     * @return a ScheduledFuture representing pending completion of
     * the task, and whose {@code get()} method will throw an
     * exception upon cancellation
     * @throws RejectedExecutionException if the task cannot be
     *                                    scheduled for execution
     * @throws NullPointerException       if command is null
     * @throws IllegalArgumentException   if period less than or equal to zero
     */
    default ScheduledFuture<?> scheduleAtFixedRate(Runnable command,
                                                   long initialDelay,
                                                   long period,
                                                   TimeUnit unit) {
        return scheduleAtFixedRate(command, unit.toMillis(initialDelay), unit.toMillis(period));
    }

    /**
     * Creates and executes a periodic action that becomes enabled first
     * after the given initial delay, and subsequently with the
     * given delay between the termination of one execution and the
     * commencement of the next.  If any execution of the task
     * encounters an exception, subsequent executions are suppressed.
     * Otherwise, the task will only terminate via cancellation or
     * termination of the executor.
     *
     * @param command      the task to execute
     * @param initialDelay the time to delay first execution
     * @param delay        the delay between the termination of one
     *                     execution and the commencement of the next
     * @param unit         the time unit of the initialDelay and delay parameters
     * @return a ScheduledFuture representing pending completion of
     * the task, and whose {@code get()} method will throw an
     * exception upon cancellation
     * @throws RejectedExecutionException if the task cannot be
     *                                    scheduled for execution
     * @throws NullPointerException       if command is null
     * @throws IllegalArgumentException   if delay less than or equal to zero
     */
    default ScheduledFuture<?> scheduleWithFixedDelay(Runnable command,
                                                      long initialDelay,
                                                      long delay,
                                                      TimeUnit unit) {
        return scheduleWithFixedDelay(command, unit.toMillis(initialDelay), unit.toMillis(delay));
    }

    /**
     * Creates and executes a one-shot action that becomes enabled
     * after the given delay.
     *
     * @param runnable the task to execute.
     * @param delay    the time from now to delay execution.
     * @return a Future representing pending completion of the task,
     * and whose <tt>get()</tt> method will return <tt>null</tt>
     * upon completion.
     * @throws java.util.concurrent.RejectedExecutionException if task cannot be scheduled
     *                                                         for execution.
     * @throws NullPointerException                            if runnable is null
     */
    ScheduledFuture schedule(Runnable runnable, long delay);

    /**
     * Creates and executes a ScheduledFuture that becomes enabled after the
     * given delay.
     *
     * @param callable the function to execute.
     * @param delay    the time from now to delay execution.
     * @return a ScheduledFuture that can be used to extract result or cancel.
     * @throws java.util.concurrent.RejectedExecutionException if task cannot be scheduled
     *                                                         for execution.
     * @throws NullPointerException                            if callable is null
     */
    <V> ScheduledFuture<V> schedule(Callable<V> callable, long delay);


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
    ScheduledFuture schedule(Runnable runnable, String cron);

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
    <V> ScheduledFuture<V> schedule(Callable<V> callable, String cron);

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
     * and whose <tt>get()</tt> method will throw an exception upon
     * cancellation.
     * @throws java.util.concurrent.RejectedExecutionException if task cannot be scheduled
     *                                                         for execution.
     * @throws NullPointerException                            if runnable is null
     * @throws IllegalArgumentException                        if period less than or equal to zero.
     */
    ScheduledFuture scheduleAtFixedRate(Runnable runnable, long initialDelay, long period);

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
     * and whose <tt>get()</tt> method will throw an exception upon
     * cancellation.
     * @throws java.util.concurrent.RejectedExecutionException if task cannot be scheduled
     *                                                         for execution.
     * @throws NullPointerException                            if runnable is null
     * @throws IllegalArgumentException                        if delay less than or equal to zero.
     */
    ScheduledFuture scheduleWithFixedDelay(Runnable runnable, long initialDelay, long delay);
}