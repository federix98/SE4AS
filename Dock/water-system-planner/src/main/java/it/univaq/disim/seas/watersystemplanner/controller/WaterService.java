/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.disim.seas.watersystemplanner.controller;

import it.univaq.disim.seas.watersystemplanner.model.ZoneData;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.print.attribute.HashAttributeSet;

/**
 *
 * @author valerio
 */
public class WaterService {

    public static List<ZoneData> waterConsumptionPolicy(List<ZoneData> zones, int mainBaseWater) {

        Map<ZoneData, Integer> ottimaOutput = new HashMap<>();
        int CurrentTotalOutput = 0;
        for (ZoneData zone : zones) {
            ottimaOutput.put(zone, zone.getDemand() + (zone.getDemand() / 100 * 20));
            CurrentTotalOutput += zone.getDemand() + (zone.getDemand() / 100 * 20);
        }

        while (CurrentTotalOutput >= mainBaseWater) {
            for (ZoneData zone : zones) {
                int requireOutput = ottimaOutput.get(zone);
                int tolgo = requireOutput / 100*5;
                ottimaOutput.put(zone, requireOutput - tolgo);
                CurrentTotalOutput -= tolgo;
                if(CurrentTotalOutput >= mainBaseWater){
                    break;
                }
               
                
            }
        }
        
        for (ZoneData zone : zones) {
           zone.setDemand(ottimaOutput.get(zone));
        }
        return zones;

    }

}
