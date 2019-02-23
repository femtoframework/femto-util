package org.femtoframework.pattern;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

public class EventListeners<L extends EventListener> implements EventListener {

    protected List<L> listeners;

    public EventListeners() {
         listeners = new ArrayList<>(2);
    }

    public EventListeners(L listener) {
        this();
        this.listeners.add(listener);
    }

    public EventListeners(List<L> listeners) {
        this.listeners = listeners;
    }

    public int getListenerCount() {
        return listeners.size();
    }

    public List<L> getListeners() {
        return listeners;
    }

    public void addListener(L listener) {
        if (listener instanceof EventListeners) {
            EventListeners<L> other = (EventListeners<L>)listener;
            List<L> array = other.getListeners();
            for (L l : array) {
                listeners.add(l);
            }
        }
        else {
            listeners.add(listener);
        }
    }

    public void removeListener(L listener) {
        listeners.remove(listener);
    }
}
