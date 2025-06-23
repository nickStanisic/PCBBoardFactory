package com.pcb.ecosystem.core.board_hierarchy;

import com.pcb.ecosystem.core.abstract_classes.PCBBoard;

public class TestBoard extends PCBBoard {
    
    public TestBoard() {
        super();
    }
    
    @Override
    public String getBoardType() {
        return "TestBoard";
    }  
}
