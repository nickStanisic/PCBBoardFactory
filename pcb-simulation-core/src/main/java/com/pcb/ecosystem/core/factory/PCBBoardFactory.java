package com.pcb.ecosystem.core.factory;

import com.pcb.ecosystem.core.abstract_classes.PCBBoard;
import com.pcb.ecosystem.core.board_hierarchy.TestBoard;
import com.pcb.ecosystem.core.board_hierarchy.SensorBoard;
import com.pcb.ecosystem.core.board_hierarchy.GatewayBoard;

/**
 * Simple Factory for creating PCB boards
 */
public class PCBBoardFactory {
    
    public static PCBBoard createTestBoard() {
        return new TestBoard();
    }
    
    public static PCBBoard createSensorBoard() {
        return new SensorBoard();
    }
    
    public static PCBBoard createGatewayBoard() {
        return new GatewayBoard();
    }
}