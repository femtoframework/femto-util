package org.femtoframework.util.timer;

/**
 * An interface for TimerTask
 *
 * @see java.util.TimerTask
 *
 * @author fengyun
 * @version 1.00
 */
public interface TimerTask extends Runnable
{
    /**
     * Cancels this timer task.  If the task has been scheduled for one-time
     * execution and has not yet run, or has not yet been scheduled, it will
     * never run.  If the task has been scheduled for repeated execution, it
     * will never run again.  (If the task is running when this call occurs,
     * the task will run to completion, but will never run again.)
     * <p/>
     * <p>Note that calling this method from within the <tt>run</tt> method of
     * a repeating timer task absolutely guarantees that the timer task will
     * not run again.
     * <p/>
     * <p>This method may be called repeatedly; the second and subsequent
     * calls have no effect.
     *
     * @return true if this task is scheduled for one-time execution and has
     *         not yet run, or this task is scheduled for repeated execution.
     *         Returns false if the task was scheduled for one-time execution
     *         and has already run, or if the task was never scheduled, or if
     *         the task was already cancelled.  (Loosely speaking, this method
     *         returns <tt>true</tt> if it prevents one or more scheduled
     *         executions from taking place.)
     */
    boolean cancel();

    /**
     * Returns the <i>scheduled</i> execution time of the most recent
     * <i>actual</i> execution of this task.  (If this method is invoked
     * while task execution is in progress, the return value is the scheduled
     * execution time of the ongoing task execution.)
     * <p/>
     * <p>This method is typically invoked from within a task's run method, to
     * determine whether the current execution of the task is sufficiently
     * timely to warrant performing the scheduled activity:
     * <pre>
     *   public void run() {
     *       if (System.currentTimeMillis() - scheduledExecutionTime() >=
     *           MAX_TARDINESS)
     *               return;  // Too late; skip this execution.
     *       // Perform the task
     *   }
     * </pre>
     * This method is typically <i>not</i> used in conjunction with
     * <i>fixed-delay execution</i> repeating tasks, as their scheduled
     * execution times are allowed to drift over time, and so are not terribly
     * significant.
     *
     * @return the time at which the most recent execution of this task was
     *         scheduled to occur, in the format returned by Date.getTime().
     *         The return value is undefined if the task has yet to commence
     *         its first execution.
     */
    long scheduledExecutionTime();

    /**
     * Calculate new execution time
     *
     * @param now Current time
     */
    long nextExecutionTime(long now);

    /**
     * TimerState
     */
    TimerState getState();

    /**
     * Set TimerState
     *
     * @param state TimerState
     */
    void setState(TimerState state);

    /**
     * Set next execution time
     *
     * @param next Next execution time
     */
    void setNextExecutionTime(long next);

    /**
     * Returns next execution time
     */
    long getNextExecutionTime();

    /**
     * Returns period
     */
    long getPeriod();

    /**
     * Sets period
     */
    void setPeriod(long period);
}
