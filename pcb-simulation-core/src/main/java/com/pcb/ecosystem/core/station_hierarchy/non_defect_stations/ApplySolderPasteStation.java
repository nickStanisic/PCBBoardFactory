package com.pcb.ecosystem.core.station_hierarchy.non_defect_stations;

import com.pcb.ecosystem.core.abstract_classes.Station;
import com.pcb.ecosystem.core.abstract_classes.PCBBoard;
import com.pcb.ecosystem.core.station_hierarchy.defect_detection_stations.PlaceComponentsStation;

public class ApplySolderPasteStation extends Station {
    
    public ApplySolderPasteStation() {
        super("Apply Solder Paste", false); 
        this.next = new PlaceComponentsStation();
    }
    
}