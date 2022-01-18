/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.seas.daoImpl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.univaq.seas.dao.ZoneDao;

import static it.univaq.seas.daoImpl.InfluxdbConnection.urlLocalhost;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.influxdb.dto.QueryResult.Result;
import org.influxdb.dto.QueryResult.Series;

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

    @Override
    public List<String> getAllZoneNameWithTankLevelUpTo(int tank_level) {


        InfluxDB influxDBConnection = InfluxDBFactory.connect(serverURL, username, password);

        String command = "SELECT last(\"tankFillLevel\") FROM zone WHERE \"tankFillLevel\" >= "
                + String.valueOf(tank_level) + " AND \"active\" = 1 GROUP BY  \"topic\" ";

        System.out.println(command);
        Query query = new Query(command, "telegraf");

        List<String> topics = new ArrayList<>();

        QueryResult queryResult = influxDBConnection.query(query);

        if (!queryResult.getResults().isEmpty()) {
            System.out.println("C'è un result");
            System.out.println(queryResult.toString());
            List<Result> series = queryResult.getResults();
            // TODO che topic = null per vedere se ho un risultato
            for (Result result : series) {
                if (result.getSeries() != null) {
                    if (!result.getSeries().isEmpty()) {
                        System.out.println("C'è una series");
                        for (Series serie : result.getSeries()) {
                            if (serie.getTags().containsKey("topic")) {
                                topics.add(serie.getTags().get("topic"));
                                System.out.println(serie.getTags().get("topic"));
                            }
                            if (!serie.getValues().isEmpty()) {
                                serie.getValues().forEach(value -> {
                                    System.out.println(value.toString());
                                });

                            }
                        }

                    }
                }
            }

        }
        Runtime.getRuntime().addShutdownHook(new Thread(influxDBConnection::close));
        return topics;

    }

    @Override
    public List<String> getAllZoneNameWithTankLevelDownTo(int tank_level) {

        InfluxDB influxDBConnection = InfluxDBFactory.connect(serverURL, username, password);

        String command = "SELECT last(\"tankFillLevel\") FROM zone WHERE \"tankFillLevel\" <= "
                + String.valueOf(tank_level) + " AND \"active\" = 1 GROUP BY  \"topic\" ";


        Query query = new Query(command, "telegraf");

        List<String> topics = new ArrayList<>();
        QueryResult queryResult = influxDBConnection.query(query);
        if (!queryResult.getResults().isEmpty()) {
            System.out.println("C'è un result");
            List<Result> series = queryResult.getResults();
            for (Result result : series) {
                if (result.getSeries() != null) {
                    if (!result.getSeries().isEmpty()) {
                        System.out.println("C'è una series");
                        for (Series serie : result.getSeries()) {
                            if (serie.getTags().containsKey("topic")) {
                                topics.add(serie.getTags().get("topic"));
                                System.out.println(serie.getTags().get("topic"));
                            }
                            if (!serie.getValues().isEmpty()) {
                                serie.getValues().forEach(value -> {
                                    System.out.println(value.toString());
                                });

                            }
                        }

                    }
                }
            }

        }
        Runtime.getRuntime().addShutdownHook(new Thread(influxDBConnection::close));
        return topics;
    }

    @Override
    public List<String> getAllZoneNameThatAreSendInformatioUpto(Date date) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int checkConsumptionAdaptation() {
        InfluxDB influxDBConnection = InfluxDBFactory.connect(serverURL, username, password);

        String command = "SELECT zoneId, last(totalDemand), tankOutput FROM zone WHERE active = 1 GROUP BY topic";

        List<String> topics = new ArrayList<>();
        QueryResult queryResult = influxDBConnection.query(new Query(command, "telegraf"));

        System.out.println("Checking consumption adaptation");

        if (!queryResult.getResults().isEmpty()) {
            //System.out.println("C'è un result");
            List<Result> series = queryResult.getResults();
            for (Result result : series) {
                if (result.getSeries() != null) {
                    if (!result.getSeries().isEmpty()) {
                        //System.out.println("C'è una series");
                        // Get the main
                        Integer mainOutput = null;
                        Map<Integer, Integer> demands;
                        List<QueryResult.Series> res = result.getSeries();
                        List<Object> toRemove = null;
                        boolean found = false;

                        for(QueryResult.Series serie : res) {
                            //System.out.println(intcast(tuple.get(1)));
                            List<Object> tuple = serie.getValues().get(0);
                            if ((intcast(tuple.get(1))) == 0) {
                                // System.out.println(tuple.get(3));
                                // found
                                found = true;
                                mainOutput = intcast(tuple.get(3));
                                toRemove = tuple;
                                break;
                            }
                        }
//
                        if (found) {

                            //System.out.println("In" + toRemove.toString());


                            Integer finalMainOutput = mainOutput;

                            //System.out.println(res.size());
                            for(QueryResult.Series serie : res) {
                                //System.out.println(intcast(tuple.get(1)));
                                List<Object> tuple = serie.getValues().get(0);
                                //System.out.println(tuple.toString());
                                Integer zoneId = intcast(tuple.get(1));
                                Integer demand = intcast(tuple.get(2));

                                if (zoneId != 0 && demand >= (finalMainOutput / (res.size() - 1))) {
                                    return finalMainOutput;
                                }
                            }
                        }
                        else return 0;



                    }
                }
            }
        }
        return 0;
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


}
