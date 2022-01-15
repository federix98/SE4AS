package it.univaq.seas.run;

import it.univaq.seas.zonesimulator.ZoneSimulator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

    public static void main( String[] args) {
        Map<String, ZoneSimulator> zones = new HashMap<String, ZoneSimulator>();
        zones.put("MainPipe", new ZoneSimulator(0, "MainPipe", 5000, null, null, null, null));
        zones.put("Zone1", new ZoneSimulator(1, "Zone1", 50, 100, 100, 7, 50));
        zones.put("Zone2", new ZoneSimulator(2, "Zone2", 60, 100, 100, 4, 70));
        zones.put("Zone3", new ZoneSimulator(3, "Zone3", 30, 100, 100, 67, 400));
        zones.put("Zone4", new ZoneSimulator(4, "Zone4", 60, 100, 100, 100, 500));
        zones.put("Zone5", new ZoneSimulator(5, "Zone5", 70, 100, 100, 23, 400));


        List<Thread> threads = new ArrayList<Thread>();
        threads.add(new Thread(zones.get("MainPipe")));
        threads.add(new Thread(zones.get("Zone1")));
        threads.add(new Thread(zones.get("Zone2")));
        threads.add(new Thread(zones.get("Zone3")));
        threads.add(new Thread(zones.get("Zone4")));
        threads.add(new Thread(zones.get("Zone5")));

        for(Thread t : threads) {
            t.start();
        }

        //Thread.sleep(10000);

        /*
         * for (String key : zones.keySet()) { zones.get(key).stop(); }
         */
    }
}
