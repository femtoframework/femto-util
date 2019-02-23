package org.femtoframework.util.timer;


public enum TimerState {

    /**
     * This task has not yet been scheduled.
     */
    VIRGIN,

    /**
     * This task is scheduled for execution.  If it is a non-repeating task,
     * it has not yet been executed.
     */
    SCHEDULED,

    /**
     * This non-repeating task has already executed (or is currently
     * executing) and has not been cancelled.
     */
    EXECUTED,

    /**
     * This task has been cancelled (with a call to TimerTask.cancel).
     */
    CANCELLED;
}
