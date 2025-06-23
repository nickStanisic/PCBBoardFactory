package com.pcb.ecosystem.core.abstract_classes;

/**
 * Abstract base class for all PCB boards
 */
public abstract class PCBBoard {
    protected String boardId;
    
    public PCBBoard() {
    }
    
    public abstract String getBoardType();
    
    @Override
    public String toString() {
        return getBoardType();
    }
}