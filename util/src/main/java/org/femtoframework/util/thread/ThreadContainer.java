package org.femtoframework.util.thread;

/**
 * Thread Container is an abstract interface for ThreadPool
 */
public interface ThreadContainer {

    /**
     * Wait until gets a ThreadController
     */
    ThreadController compete();

    /**
     * Wait given time and try to get a ThreadController
     *
     * @param timeout Timeout
     * @return ThreadController
     */
    ThreadController compete(int timeout);

    /**
     * Allocate a ThreadController, if no ThreadController available, return null
     */
    ThreadController allocate();

    /**
     * Terminate controller
     *
     * @param controller ThreadController
     */
    void terminate(ThreadController controller);

    /**
     * Recycle ThreadController
     *
     * @param controller ThreadController
     */
    void recycle(ThreadController controller);
}
