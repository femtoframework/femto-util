package org.femtoframework.util.timer;


import static org.femtoframework.util.timer.TimerState.CANCELLED;
import static org.femtoframework.util.timer.TimerState.SCHEDULED;

/**
 * Abstract Timer Task
 *
 * @author fengyun
 * @version 1.00
 */

public abstract class AbstractTimerTask implements TimerTask
{
    /**
     * The state of this task, chosen from the constants below.
     */
    protected TimerState state = TimerState.VIRGIN;

    /**
     * Next execution time for this task in the format returned by
     * System.currentTimeMillis, assuming this task is schedule for execution.
     * For repeating tasks, this field is updated prior to each task execution.
     */
    protected long nextExecutionTime;

    /**
     * Period in milliseconds for repeating tasks.  A positive value indicates
     * fixed-rate execution.  A negative value indicates fixed-delay execution.
     * A value of 0 indicates a non-repeating task.
     */
    protected long period = 0;

    /**
     * Creates a new timer task.
     */
    protected AbstractTimerTask()
    {
    }

    /**
     * The action to be performed by this timer task.
     */
    public abstract void run();

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
    public boolean cancel()
    {
        synchronized (this) {
            boolean result = (state == SCHEDULED);
            state = CANCELLED;
            return result;
        }
    }

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
    public long scheduledExecutionTime()
    {
        synchronized (this) {
            return (period < 0 ? nextExecutionTime + period
                    : nextExecutionTime - period);
        }
    }

    /**
     * TimerState
     */
    public TimerState getState() {
        return state;
    }

    /**
     * Set TimerState
     *
     * @param state TimerState
     */
    public void setState(TimerState state) {
        this.state = state;
    }

    /**
     * Set next execution time
     *
     * @param next Next execution time
     */
    public void setNextExecutionTime(long next) {
        this.nextExecutionTime = next;
    }

    /**
     * Returns next execution time
     */
    public long getNextExecutionTime() {
        return nextExecutionTime;
    }

    /**
     * Returns period
     */
    public long getPeriod() {
        return period;
    }

    /**
     * Sets period
     */
    public void setPeriod(long period) {
        this.period = period;
    }

    /**
     * Calculate new execution time
     *
     * @param now Current time
     */
    public long nextExecutionTime(long now)
    {
        long p = period;
        return p == 0 ? -1L : p < 0 ? now - p : nextExecutionTime + p;
    }
}
