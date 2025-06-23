package com.pcb.ecosystem.core.board_hierarchy;

import com.pcb.ecosystem.core.abstract_classes.PCBBoard;

public class GatewayBoard extends PCBBoard {
    
    public GatewayBoard() {
        super();
    }
    
    @Override
    public String getBoardType() {
        return "GatewayBoard";
    }
}