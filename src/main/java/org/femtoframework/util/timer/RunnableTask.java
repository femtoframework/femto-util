package org.femtoframework.util.timer;

/**
 * Adapter for runnable
 *
 * @author fengyun
 * @version 1.00 Jun 15, 2003 12:54:46 PM
 */
public class RunnableTask extends AbstractTimerTask
{
    private Runnable runnable;

    /**
     * Constructor
     *
     * @param runnable Runnable
     */
    public RunnableTask(Runnable runnable)
    {
        if (runnable == null) {
            throw new IllegalArgumentException("Null runnable");
        }

        this.runnable = runnable;
    }

    /**
     * The action to be performed by this timer task.
     */
    public void run()
    {
        runnable.run();
    }
}
