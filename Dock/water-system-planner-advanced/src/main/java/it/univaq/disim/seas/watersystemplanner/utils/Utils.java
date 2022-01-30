package it.univaq.disim.seas.watersystemplanner.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.univaq.disim.seas.watersystemplanner.model.ZoneData;
import it.univaq.disim.seas.watersystemplanner.model.ZoneUpdate;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.List;

/**
 * @author federico
 */
public class Utils {

    public static boolean dockerized = true;

    public static String convertMessageToJSONString(List<ZoneData> zones){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(zones);
        } catch (JsonProcessingException ex) {
            // Do notting
        }
        return null;
    }

    public static void mqttPublish(String data, boolean DOCKERIZE) {
        try {
            String serverURI = (DOCKERIZE) ? "tcp://mosquitto:1883" : "tcp://localhost:1883";
            MqttClient sensingClient = new MqttClient(serverURI, "water-system-planner");
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
