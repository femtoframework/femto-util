package org.femtoframework.util.thread;

import java.util.concurrent.Executor;

/**
 * Indicates an object needs to inject Executor
 *
 */
public interface ExecutorAware {

    /**
     * Executor
     *
     * @param executor Executor
     */
    void setExecutor(Executor executor);
}
