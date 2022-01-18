package it.univaq.disim.seas.watersystemplanner.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.univaq.disim.seas.watersystemplanner.model.ZoneUpdate;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.List;

public class Utils {

    public static String convertMessageToJSONString(List<ZoneUpdate> zones){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(zones);
        } catch (JsonProcessingException ex) {
            // Do notting
        }
        return null;
    }

    public static void mqttPublish(String data) {
        try {
            MqttClient sensingClient = new MqttClient("tcp://localhost:1883", "water-system-planner");
            sensingClient.connect();
            MqttMessage message = new MqttMessage();
            message.setPayload(data.getBytes());
            sensingClient.publish("home/planner_executor", message);// + this.zoneName, message);
            sensingClient.disconnect();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

}
