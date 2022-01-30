package it.univaq.seas.zonesimulator;

import it.univaq.seas.zonesimulator.ZoneSimulator;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

    public static void main( String[] args) {
        Map<String, ZoneSimulator> zones = new HashMap<String, ZoneSimulator>();
        try {
            zones.put("MainPipe", new ZoneSimulator(0, "MainPipe", 5000, 5000, null, null, null, null, 1));
            zones.put("Zone1", new ZoneSimulator(1, "Zone1", 50, 45, 0, 100, 7, 50, 1));
            zones.put("Zone2", new ZoneSimulator(2, "Zone2", 60, 55, 0, 100, 4, 70, 1));
            zones.put("Zone3", new ZoneSimulator(3, "Zone3", 30, 35, 100, 100, 67, 400, 1));
            zones.put("Zone4", new ZoneSimulator(4, "Zone4", 60, 55, 50, 100, 450, 500, 1));
            zones.put("Zone5", new ZoneSimulator(5, "Zone5", 70, 65, 70, 100, 23, 400, 1));
            //zones.put("Zone6", new ZoneSimulator(6, "Zone6", 50, 95, 87, 100, 23, 400, 1));
            //zones.put("Zone7", new ZoneSimulator(7, "Zone7", 50, 95, 32, 100, 23, 400, 1));

        } catch (MqttException e) {
            e.printStackTrace();
        }


        List<Thread> threads = new ArrayList<Thread>();
        for (String key : zones.keySet()) {
            threads.add(new Thread(zones.get(key)));
        }

        for(Thread t : threads) {
            t.start();
        }

        //Thread.sleep(10000);

        /*
         * for (String key : zones.keySet()) { zones.get(key).stop(); }
         */
    }
}
