package org.femtoframework.util.thread;

public interface ErrorHandler {

    /**
     * Handle exception
     *
     * @param e Exception
     * @return Whether continue this job or not
     */
    boolean handleException(Exception e);

    /**
     * Handle Throwable
     *
     * @param t Error
     * @return Whether continue this job or not
     */
    boolean handleError(Throwable t);
}
