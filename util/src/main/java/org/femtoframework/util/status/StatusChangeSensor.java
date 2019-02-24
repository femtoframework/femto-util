package org.femtoframework.util.status;

/**
 * Status change sensor
 *
 * @author fengyun
 * @version 1.00 2005-7-25 17:53:08
 */
public interface StatusChangeSensor
{
    /**
     * Add status change listener
     *
     * @param listener StatusChangeListener
     */
    void addStatusChangeListener(StatusChangeListener listener);

    /**
     * Remove status change listener
     *
     * @param listener StatusChangeListener
     */
    void removeStatusChangeListener(StatusChangeListener listener);
}
