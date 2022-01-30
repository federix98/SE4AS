/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.disim.seas.watersystemplanner;

import it.univaq.disim.seas.watersystemplanner.controller.WaterService;
import it.univaq.disim.seas.watersystemplanner.daoImpl.ZoneDaoImpl;
import it.univaq.disim.seas.watersystemplanner.dto.AdaptationMessageDTO;
import it.univaq.disim.seas.watersystemplanner.model.ZoneData;
import it.univaq.disim.seas.watersystemplanner.model.ZoneDataRegression;
import it.univaq.disim.seas.watersystemplanner.model.ZoneParameter;
import it.univaq.disim.seas.watersystemplanner.model.ZoneUpdate;
import it.univaq.disim.seas.watersystemplanner.utils.Utils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author valerio
 * @author federico
 *
 */
@org.springframework.web.bind.annotation.RestController
public class RestController {

    private boolean DOCKERIZE = true;

    @GetMapping("/")
    public String greeting() {
        return "Root";
    }

    @PostMapping("/message")
    public String message(@RequestBody String body) {
        System.out.println(body);
        return body;
    }

    @PostMapping("/consumptionAdaptation")
    public String consumptionAdaptation(@RequestBody AdaptationMessageDTO message) {

        // Retrieve zone data
        List<ZoneData> zones = new ZoneDaoImpl().getZoneData();
        int mainBaseWater = message.getAlertValue();

        System.out.println(mainBaseWater);

        List<ZoneData> results = WaterService.waterConsumptionPolicy(zones, mainBaseWater);

        List<ZoneData> toWrite = new ArrayList<ZoneData>();

        for (ZoneData res : results) {
            ZoneData local = new ZoneData();
            local.setId(res.getId());
            local.setTankOutput(res.getTankOutput());
            local.setTopic(res.getTopic());
            toWrite.add(local);
        }

        String jsonMessage = Utils.convertMessageToJSONString(toWrite);

        System.out.println("WATER CONSUMPTION POLICY" + jsonMessage);

        Utils.mqttPublish(jsonMessage, DOCKERIZE);

        return "Consumption Adaptation planned";
    }

    @PostMapping("/maintenance")
    public String maintenanceAdaptation(@RequestBody AdaptationMessageDTO message) {
        // Retrieve zone data
        List<ZoneData> zones = new ZoneDaoImpl().getZoneData();


        List<ZoneData> results = WaterService.waterMaintainancePolicy(zones, (List<String>) message.getZones());

        List<ZoneData> toWrite = new ArrayList<ZoneData>();

        for (ZoneData res : results) {
            ZoneData local = new ZoneData();
            local.setId(res.getId());
            local.setActive(res.getActive());
            local.setTankInput((res.getTankInput()));
            local.setTopic(res.getTopic());
            toWrite.add(local);
        }

        String jsonMessage = Utils.convertMessageToJSONString(toWrite);

        System.out.println(jsonMessage);

        Utils.mqttPublish(jsonMessage, DOCKERIZE);
        return "Maintenance adaptation planned";
    }

    @PostMapping("/maintenancepred")
    public String maintenancePredAdaptation(@RequestBody AdaptationMessageDTO message) {

        // Retrieve zone data
        List<ZoneDataRegression> zones = new ZoneDaoImpl().getZoneDataRegression();
        int mainBaseWater = message.getAlertValue();

        System.out.println(mainBaseWater);

        List<ZoneData> results = WaterService.waterConsumptionPredPolicy(zones, mainBaseWater);

        List<ZoneData> toWrite = new ArrayList<ZoneData>();

        for (ZoneData res : results) {
            ZoneData local = new ZoneData();
            local.setId(res.getId());
            local.setTankOutput(res.getTankOutput());
            local.setTopic(res.getTopic());
            toWrite.add(local);
        }

        String jsonMessage = Utils.convertMessageToJSONString(toWrite);

        System.out.println("WATER CONSUMPTION POLICY" + jsonMessage);

        Utils.mqttPublish(jsonMessage, DOCKERIZE);

        return "Consumption Adaptation planned";
    }




}
