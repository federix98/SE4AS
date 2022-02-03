/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.disim.seas.watersystemplanner.controller;

import it.univaq.disim.seas.watersystemplanner.model.ZoneData;
import it.univaq.disim.seas.watersystemplanner.model.ZoneDataRegression;
import it.univaq.disim.seas.watersystemplanner.model.ZoneUpdate;
import it.univaq.disim.seas.watersystemplanner.utils.LinearRegressor;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.IntStream;
import javax.print.attribute.HashAttributeSet;

/**
 *
 * @author valerio
 * @author federico
 */
public class WaterService {


    public static List<ZoneData> waterConsumptionPredPolicy(List<ZoneDataRegression> zones, int mainBaseWater) {
        System.out.println("In prediction method");

        Map<ZoneData, Integer> ottimaOutput = new HashMap<>();
        int CurrentTotalOutput = 0;
        for (ZoneDataRegression zone : zones) {
            if (zone.getId() != 0) {
                List<Integer> x = new ArrayList<Integer>();
                for (int i : IntStream.range(0, zone.getHistoricDemandValues().size()).toArray()) x.add(i);
                Integer predictedValue = (LinearRegressor.predictForValue(x, zone.getHistoricDemandValues(), zone.getHistoricDemandValues().size())).intValue();
                System.out.println("Predicted value from zone " + zone.getTopic() + " by values " + zone.getHistoricDemandValues() + " is ==> " + predictedValue);
                ottimaOutput.put((ZoneData) zone, predictedValue + (predictedValue / 100 * 20));
                CurrentTotalOutput += predictedValue + (predictedValue / 100 * 20);
                System.out.println(CurrentTotalOutput);
            }

        }

        System.out.println("CIAOCIAO " + CurrentTotalOutput + " " + mainBaseWater);
        System.out.println(ottimaOutput.values().toString());
        System.out.println(ottimaOutput.size());

        while (CurrentTotalOutput >= mainBaseWater) {
            for (ZoneData zone : zones) {
                if (zone.getId() != 0) {
                    int requireOutput = ottimaOutput.get(zone);
                    //System.out.println(requireOutput);
                    int tolgo = 10;

                    if (requireOutput > 10) {
                        ottimaOutput.put(zone, requireOutput - tolgo);
                        CurrentTotalOutput -= tolgo;

                    }

                    // TO FIX
                    CurrentTotalOutput -= tolgo;

                    System.out.println(requireOutput + " " + tolgo + " " + CurrentTotalOutput);

                    if(CurrentTotalOutput <= mainBaseWater){
                        break;
                    }
                }
            }
        }


        //System.out.println(ottimaOutput.values().toString());
        //System.out.println(ottimaOutput.size());

        for (ZoneData zone : zones) {
            zone.setDemand(ottimaOutput.get(zone));
        }


        List<ZoneData> results = new ArrayList<ZoneData>();

        /*
        for (ZoneData zone : ottimaOutput.keySet()) {
            ZoneUpdate local = new ZoneUpdate();
            local.setNewTankOutput(ottimaOutput.get(zone));
            local.setNewTankInput(0);
            local.setZoneId(zone.getId());
            local.setTopic(zone.getTopic());
            results.add(local);
        }*/

        for (ZoneData zone : ottimaOutput.keySet()) {
            //ZoneDataRegression local = (ZoneDataRegression) cloneObject(zone);
            ZoneData local = new ZoneData();
            local.setTopic(zone.getTopic());
            local.setId(zone.getId());
            local.setDemand(zone.getDemand());
            local.setActive(zone.getActive());
            local.setTankInput(zone.getTankInput());
            local.setNumHouse(zone.getNumHouse());
            local.setTankOutput(ottimaOutput.get(zone));
            results.add(local);
            System.out.println("OUTPUT TO SET " + local.getTankOutput());
        }

        System.out.println(ottimaOutput.values().toString());
        System.out.println(ottimaOutput.size());
        return results;
    }

    public static List<ZoneData> waterConsumptionPolicy(List<ZoneData> zones, int mainBaseWater) {

        Map<ZoneData, Integer> ottimaOutput = new HashMap<>();
        int CurrentTotalOutput = 0;
        for (ZoneData zone : zones) {
            if (zone.getId() != 0) {
                ottimaOutput.put(zone, zone.getDemand() + (zone.getDemand() / 100 * 20));
                CurrentTotalOutput += zone.getDemand() + (zone.getDemand() / 100 * 20);
                System.out.println(CurrentTotalOutput);
            }

        }

        System.out.println("CIAOCIAO " + CurrentTotalOutput + " " + mainBaseWater);
        System.out.println(ottimaOutput.values().toString());
        System.out.println(ottimaOutput.size());

        while (CurrentTotalOutput >= mainBaseWater) {
            for (ZoneData zone : zones) {
                if (zone.getId() != 0) {
                    int requireOutput = ottimaOutput.get(zone);
                    //System.out.println(requireOutput);
                    int tolgo = 10;

                    if (requireOutput > 10) {
                        ottimaOutput.put(zone, requireOutput - tolgo);
                        CurrentTotalOutput -= tolgo;

                    }

                    // TO FIX
                    CurrentTotalOutput -= tolgo;

                    System.out.println(requireOutput + " " + tolgo + " " + CurrentTotalOutput);

                    if(CurrentTotalOutput <= mainBaseWater){
                        break;
                    }
                }
            }
        }


        //System.out.println(ottimaOutput.values().toString());
        //System.out.println(ottimaOutput.size());
        
        for (ZoneData zone : zones) {
           zone.setDemand(ottimaOutput.get(zone));
        }


        List<ZoneData> results = new ArrayList<ZoneData>();

        /*
        for (ZoneData zone : ottimaOutput.keySet()) {
            ZoneUpdate local = new ZoneUpdate();
            local.setNewTankOutput(ottimaOutput.get(zone));
            local.setNewTankInput(0);
            local.setZoneId(zone.getId());
            local.setTopic(zone.getTopic());
            results.add(local);
        }*/

        for (ZoneData zone : ottimaOutput.keySet()) {
            ZoneData local = (ZoneData) cloneObject(zone);
            local.setTankOutput(ottimaOutput.get(zone));
            results.add(local);
            System.out.println("OUTPUT TO SET " + local.getTankOutput());
        }

        System.out.println(ottimaOutput.values().toString());
        System.out.println(ottimaOutput.size());
        return results;

    }

    public static List<ZoneData> waterMaintainancePolicy(List<ZoneData> zones, List<String> topics) {
        List<ZoneData> results = new ArrayList<ZoneData>();
        for (ZoneData zone : zones) {
            if(topics.contains(zone.getTopic())) {
                ZoneData local = (ZoneData) cloneObject(zone);
                System.out.println(local.getActive());
                local.setActive(0);
                local.setTankInput(0);
                System.out.println(local.getActive());
                results.add(local);
            }
        }
        return results;
    }

    /*
    private static ZoneData cloneZone(ZoneData toClone) {
        ZoneData toFill = new ZoneData();
        toFill.setNumHouse(toClone.getNumHouse());
        toFill.setActive(toClone.getActive());
        toFill.setDemand(toClone.getDemand());
        toFill.setId(to);
    }

     */

    private static Object cloneObject(Object obj){
        try{
            Object clone = obj.getClass().newInstance();
            for (Field field : obj.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                if(field.get(obj) == null || Modifier.isFinal(field.getModifiers())){
                    continue;
                }
                if(field.getType().isPrimitive() || field.getType().equals(String.class)
                        || field.getType().getSuperclass().equals(Number.class)
                        || field.getType().equals(Boolean.class)){
                    field.set(clone, field.get(obj));
                }else{
                    Object childObj = field.get(obj);
                    if(childObj == obj){
                        field.set(clone, clone);
                    }else{
                        field.set(clone, cloneObject(field.get(obj)));
                    }
                }
            }
            return clone;
        }catch(Exception e){
            return null;
        }
    }

}
