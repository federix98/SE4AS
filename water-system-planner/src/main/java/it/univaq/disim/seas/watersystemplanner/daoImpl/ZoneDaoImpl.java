/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.disim.seas.watersystemplanner.daoImpl;

import it.univaq.disim.seas.watersystemplanner.dao.ZoneDao;
import it.univaq.disim.seas.watersystemplanner.model.ZoneData;
import it.univaq.disim.seas.watersystemplanner.model.ZoneDataRegression;
import it.univaq.disim.seas.watersystemplanner.utils.Utils;
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
        serverURL = (Utils.dockerized) ? "http://influxdb:8086" : "http://localhost:8086";
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

        String command = "SELECT last(*) FROM zone WHERE time >= now() - 7d GROUP BY topic";

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
                    localZone.setActive(intcast(tuple.get(1)));
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

    @Override
    public List<ZoneDataRegression> getZoneDataRegression() {
        InfluxDB influxDBConnection = InfluxDBFactory.connect(serverURL, username, password);

        String command = "SELECT last(*) FROM zone WHERE time >= now() - 7d GROUP BY topic";

        System.out.println(command);
        Query query = new Query(command, "telegraf");

        List<String> topics = new ArrayList<>();

        QueryResult queryResult = influxDBConnection.query(query);

        List<ZoneDataRegression> zones = new ArrayList<ZoneDataRegression>();
        for(Result series : queryResult.getResults()) {
            for (Series serie : series.getSeries()) {
                List<Object> tuple = serie.getValues().get(0);
                System.out.println(tuple);
                ZoneDataRegression localZone = new ZoneDataRegression();

                int zoneId = intcast(tuple.get(10));
                if(zoneId != 0) {
                    localZone.setActive(intcast(tuple.get(1)));
                    localZone.setHistoricDemandValues(getDemandsFromZone(serie.getTags().get("topic"), influxDBConnection));
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

    private List<Integer> getDemandsFromZone(String topic, InfluxDB influxDBConnection) {
        String retrieveDemandCommand = "SELECT mean(totalDemand) FROM zone WHERE topic = '" + topic + "' GROUP BY time(10m)";
        QueryResult subresult = influxDBConnection.query(new Query(retrieveDemandCommand, "telegraf"));
        System.out.println(retrieveDemandCommand);

        List<Integer> demands = new ArrayList<Integer>();
        for(Result series : subresult.getResults()) {
            for (Series serie : series.getSeries()) {
                for (List<Object> tuple : serie.getValues()) {
                    //System.out.println("Getting demands for topic " + topic + ", : " + tuple);
                    demands.add(intcast(tuple.get(1)));
                }
            }
        }
        System.out.println("Returning " + demands.toString());
        return demands;
    }
}
