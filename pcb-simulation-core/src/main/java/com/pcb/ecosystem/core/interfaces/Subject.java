package com.pcb.ecosystem.core.interfaces;

/**
 * Subject interface for the Observer pattern
 * Objects implementing this interface can be observed by Observers
 */
public interface Subject {
    
    /**
     * Add an observer to this subject
     * @param observer The observer to add
     */
    void addObserver(Observer observer);
    
    /**
     * Remove an observer from this subject
     * @param observer The observer to remove
     */
    void removeObserver(Observer observer);
    
    /**
     * Notify all observers of an event
     * This is typically called by the concrete subject when state changes
     */
    void notifyObservers();
}