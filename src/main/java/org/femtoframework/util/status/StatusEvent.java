package org.femtoframework.util.status;

import java.util.EventObject;


/**
 * Status change event
 *
 * @author fengyun
 * @version 1.00 Mar 21, 2002 4:52:59 PM
 */
public class StatusEvent extends EventObject
{
    /**
     * Status
     */
    private int status;

    /**
     * Status code
     *
     * @param status Status Code
     * @param src Object source
     */
    public StatusEvent(int status, Object src)
    {
        super(src);
        this.status = status;
    }

    /**
     * Returns status
     *
     * @return status
     */
    public int getStatus()
    {
        return status;
    }

    /**
     * Dispatch listener
     *
     * @param changeListener StatusChangeListener
     */
    public void dispatch(StatusChangeListener changeListener)
    {
        if (changeListener != null) {
            changeListener.changed(this);
        }
    }
}
