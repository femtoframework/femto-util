package org.femtoframework.util.thread;

import org.femtoframework.util.timer.TimerState;
import org.femtoframework.util.timer.TimerTask;

import java.util.concurrent.*;

class ScheduledFutureTask<V> extends FutureTask<V> implements ScheduledFuture<V>, TimerTask
{
    /**
     * Creates a one-shot action with given nanoTime-based trigger time
     */
    ScheduledFutureTask(Runnable task)
    {
        this(task, null);
    }

    /**
     * Creates a periodic action with given nano time and period
     */
    ScheduledFutureTask(Runnable task, V result)
    {
        super(task, result);
    }

    /**
     * Creates a one-shot action with given nanoTime-based trigger
     */
    ScheduledFutureTask(Callable<V> callable)
    {
        super(callable);
    }

    /**
     * Attempts to cancel execution of this task.  This attempt will
     * fail if the task has already completed, already been cancelled,
     * or could not be cancelled for some other reason. If successful,
     * and this task has not started when <tt>cancel</tt> is called,
     * this task should never run.  If the task has already started,
     * then the <tt>mayInterruptIfRunning</tt> parameter determines
     * whether the thread executing this task should be interrupted in
     * an attempt to stop the task.
     *
     * @param mayInterruptIfRunning <tt>true</tt> if the thread executing this
     *                              task should be interrupted; otherwise, in-progress tasks are allowed
     *                              to complete
     * @return <tt>false</tt> if the task could not be cancelled,
     *         typically because it has already completed normally;
     *         <tt>true</tt> otherwise
     */
    public boolean cancel(boolean mayInterruptIfRunning)
    {
        super.cancel(mayInterruptIfRunning);
        return cancel();
    }

    public long getDelay()
    {
        return nextExecutionTime - System.currentTimeMillis();
    }

    public int compareTo(Delayed x)
    {
        if (x == this) {
            return 0;
        }
        ScheduledFutureTask other = (ScheduledFutureTask)x;
        long diff = nextExecutionTime - other.nextExecutionTime;
        if (diff < 0) {
            return -1;
        }
        else if (diff > 0) {
            return 1;
        }
        else {
            return 1;
        }
    }

    //TimerTask
    /**
     * The state of this task, chosen from the constants below.
     */
    private TimerState state = TimerState.VIRGIN;

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
    private long period = 0;

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
            boolean result = (state == TimerState.SCHEDULED);
            state = TimerState.CANCELLED;
            return result;
        }
    }

    /**
     * Sets this Future to the result of computation unless
     * it has been cancelled.
     */
    public void run()
    {
        if (isPeriodic()) {
            runAndReset();
        }
        else {
            super.run();
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
            long t;
            if (period < 0) {
                t = nextExecutionTime + period;
            }
            else {
                t = nextExecutionTime - period;
            }
            return t;
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

    /**
     * Returns true if this is a periodic (not a one-shot) action.
     *
     * @return true if periodic
     */
    boolean isPeriodic()
    {
        return period != 0;
    }

    /**
     * Returns the remaining delay associated with this object, in the
     * given time unit.
     *
     * @param unit the time unit
     * @return the remaining delay; zero or negative values indicate
     *         that the delay has already elapsed
     */
    public long getDelay(TimeUnit unit)
    {
        return unit.convert(nextExecutionTime - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
    }
}
