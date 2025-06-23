package com.pcb.ecosystem.core.board_hierarchy;

import com.pcb.ecosystem.core.abstract_classes.PCBBoard;

public class SensorBoard extends PCBBoard {
    
    public SensorBoard() {
        super();
    }
    
    @Override
    public String getBoardType() {
        return "SensorBoard";
    }
}