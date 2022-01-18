/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.disim.seas.watersystemplanner.daoImpl;

import it.univaq.disim.seas.watersystemplanner.dao.ZoneDao;
import it.univaq.disim.seas.watersystemplanner.model.ZoneData;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.influxdb.dto.QueryResult.Result;
import org.influxdb.dto.QueryResult.Series;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author valerio
 */
public class ZoneDaoImpl implements ZoneDao {

    private String serverURL;
    private String username;
    private String password;

    public ZoneDaoImpl() {
        serverURL = "http://localhost:8086";
        username = "telegraf";
        password = "secretpassword";
    }

    private Integer intcast(Object var) {
        if (var instanceof Double) {
            return ( (Double) var).intValue();
        }
        if (var instanceof String) {
            return Integer.parseInt((String) var);
        }
        else return (Integer) var;
    }


    @Override
    public List<ZoneData> getZoneData() {

        InfluxDB influxDBConnection = InfluxDBFactory.connect(serverURL, username, password);

        String command = "SELECT last(*) FROM zone GROUP BY topic";

        System.out.println(command);
        Query query = new Query(command, "telegraf");

        List<String> topics = new ArrayList<>();

        QueryResult queryResult = influxDBConnection.query(query);

        List<ZoneData> zones = new ArrayList<ZoneData>();
        for(Result series : queryResult.getResults()) {
            for (Series serie : series.getSeries()) {
                List<Object> tuple = serie.getValues().get(0);
                System.out.println(tuple);
                ZoneData localZone = new ZoneData();

                int zoneId = intcast(tuple.get(10));
                if(zoneId != 0) {
                    localZone.setActive(intcast(tuple.get(2)));
                    localZone.setDemand(intcast(tuple.get(9)));
                    localZone.setId(zoneId);
                    localZone.setTopic(serie.getTags().get("topic"));
                    localZone.setTank_level(intcast(tuple.get(6)));
                    localZone.setTankInput(intcast(tuple.get(7)));
                    localZone.setTankOutput(intcast(tuple.get(8)));

                    zones.add(localZone);
                }






            }
        }

        return zones;

    }
}
