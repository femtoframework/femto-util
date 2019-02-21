package org.femtoframework.util.status;

import java.util.ArrayList;
import java.util.List;

/**
 * Status Change Support
 *
 * @author fengyun
 * @version 1.00 2005-5-6 13:50:28
 */
public class StatusChangeSupport implements StatusChangeListener, StatusChangeSensor
{
    private List<StatusChangeListener> list;

    /**
     * Constructor
     */
    public StatusChangeSupport()
    {
        list = new ArrayList<>(2);
    }

    /**
     * Constructs from one listener
     *
     * @param listener StatusChangeListener
     */
    public StatusChangeSupport(StatusChangeListener listener)
    {
        list = new ArrayList<>(2);
        list.add(listener);
    }

    /**
     * On status changed
     */
    public void changed(StatusEvent event) {
        for (StatusChangeListener listener : list) {
            listener.changed(event);
        }
    }

    /**
     * Add status change listener
     *
     * @param listener StatusChangeListener
     */
    @Override
    public void addStatusChangeListener(StatusChangeListener listener) {
        list.add(listener);
    }

    /**
     * Remove status change listener
     *
     * @param listener StatusChangeListener
     */
    @Override
    public void removeStatusChangeListener(StatusChangeListener listener) {
        list.remove(listener);
    }
}
