package com.pcb.ecosystem.core.station_hierarchy.non_defect_stations;

import com.pcb.ecosystem.core.abstract_classes.Station;
import com.pcb.ecosystem.core.abstract_classes.PCBBoard;

public class CleaningStation extends Station {
    
    public CleaningStation() {
        super("Cleaning", false); 
        this.next = new DepanelizationStation();
    }
    
}