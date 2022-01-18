/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.disim.seas.watersystemplanner.controller;

import it.univaq.disim.seas.watersystemplanner.model.ZoneData;
import it.univaq.disim.seas.watersystemplanner.model.ZoneUpdate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.print.attribute.HashAttributeSet;

/**
 *
 * @author valerio
 */
public class WaterService {

    public static List<ZoneUpdate> waterConsumptionPolicy(List<ZoneData> zones, int mainBaseWater) {

        Map<ZoneData, Integer> ottimaOutput = new HashMap<>();
        int CurrentTotalOutput = 0;
        for (ZoneData zone : zones) {
            ottimaOutput.put(zone, zone.getDemand() + (zone.getDemand() / 100 * 20));
            CurrentTotalOutput += zone.getDemand() + (zone.getDemand() / 100 * 20);
            System.out.println(CurrentTotalOutput);
        }

        System.out.println("CIAOCIAO " + CurrentTotalOutput + " " + mainBaseWater);
        System.out.println(ottimaOutput.values().toString());
        System.out.println(ottimaOutput.size());

        while (CurrentTotalOutput >= mainBaseWater) {
            for (ZoneData zone : zones) {
                int requireOutput = ottimaOutput.get(zone);
                System.out.println(requireOutput);
                int tolgo = 10;
                ottimaOutput.put(zone, requireOutput - tolgo);
                CurrentTotalOutput -= tolgo;
                System.out.println(requireOutput + " " + tolgo + " " + CurrentTotalOutput);
                if(CurrentTotalOutput >= mainBaseWater){
                    break;
                }
            }
        }


        System.out.println(ottimaOutput.values().toString());
        System.out.println(ottimaOutput.size());
        
        for (ZoneData zone : zones) {
           zone.setDemand(ottimaOutput.get(zone));
        }


        List<ZoneUpdate> results = new ArrayList<ZoneUpdate>();
        for (ZoneData zone : ottimaOutput.keySet()) {
            ZoneUpdate local = new ZoneUpdate();
            local.setNewTankOutput(ottimaOutput.get(zone));
            local.setNewTankInput(0);
            local.setZoneId(zone.getId());
            results.add(local);
        }
        return results;

    }

}
