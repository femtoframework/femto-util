package org.femtoframework.util.status;

import java.util.EventListener;

/**
 * Status change listener
 *
 * @author fengyun
 * @version 1.00 Mar 21, 2002 4:56:48 PM
 * @see StatusEvent
 */
public interface StatusChangeListener extends EventListener
{
    /**
     * On status changed
     *
     * @param event StatusEvent
     */
    void changed(StatusEvent event);
}
