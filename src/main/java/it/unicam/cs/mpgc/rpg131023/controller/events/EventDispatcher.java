package it.unicam.cs.mpgc.rpg131023.controller.events;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EventDispatcher {
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);
    private final List<String> eventLog = new ArrayList<>();

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }

    public void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        support.firePropertyChange(propertyName, oldValue, newValue);
    }

    public void logEvent(String message) {
        String log = "· " + message;
        this.eventLog.add(log);
        support.firePropertyChange("eventLogAdded", null, log);
    }

    public List<String> getEventLog() {
        return Collections.unmodifiableList(this.eventLog);
    }
}
