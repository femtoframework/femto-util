package org.femtoframework.util.thread;

/**
 * An interface to allow the code running within the thread to access the Controller instance.
 */
public interface ControlledThread {

    /**
     * Return current ThreadController
     *
     * @return current ThreadController
     */
    ThreadController getController();

}
