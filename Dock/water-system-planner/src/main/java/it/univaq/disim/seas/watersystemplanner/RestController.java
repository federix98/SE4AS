/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.disim.seas.watersystemplanner;

import it.univaq.disim.seas.watersystemplanner.controller.WaterService;
import it.univaq.disim.seas.watersystemplanner.daoImpl.ZoneDaoImpl;
import it.univaq.disim.seas.watersystemplanner.dto.ConsumptionAdaptationMessageDTO;
import it.univaq.disim.seas.watersystemplanner.model.ZoneData;
import it.univaq.disim.seas.watersystemplanner.model.ZoneUpdate;
import it.univaq.disim.seas.watersystemplanner.utils.Utils;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 *
 * @author valerio
 * @author federico
 *
 */
@org.springframework.web.bind.annotation.RestController
public class RestController {

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
    public String consumptionAdaptation(@RequestBody ConsumptionAdaptationMessageDTO message) {

        boolean DOCKERIZE = true;

        // Retrieve zone data
        List<ZoneData> zones = new ZoneDaoImpl().getZoneData();
        int mainBaseWater = message.getAlertValue();

        System.out.println(mainBaseWater);

        List<ZoneUpdate> results = WaterService.waterConsumptionPolicy(zones, mainBaseWater);


        String jsonMessage = Utils.convertMessageToJSONString(results);

        System.out.println(jsonMessage);

        Utils.mqttPublish(jsonMessage, DOCKERIZE);

        return "Adaptation planned";
    }





}
