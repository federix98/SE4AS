/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.seas.daoImpl;

import it.univaq.seas.dao.ZoneDao;
import static it.univaq.seas.daoImpl.InfluxdbConnection.urlLocalhost;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.influxdb.dto.QueryResult.Result;
import org.influxdb.dto.QueryResult.Series;

/**
 *
 * @author valerio
 */
public class ZoneDaoImpl implements ZoneDao {


    
    @Override
    public List<String> getAllZoneNameWithTankLevelUpTo(int tank_level) {
        String serverURL = "http://localhost:8086", username = "telegraf", password = "secretpassword";
        
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
            for (Result result : series) {
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
        Runtime.getRuntime().addShutdownHook(new Thread(influxDBConnection::close));
        return topics;
    }

    @Override
    public List<String> getAllZoneNameWithTankLevelDownTo(int tank_level) {
        String serverURL = "http://localhost:8086", username = "telegraf", password = "secretpassword";
        
        InfluxDB influxDBConnection = InfluxDBFactory.connect(serverURL, username, password);
        
        String command = "SELECT last(\"tank_level\") FROM zone WHERE \"tank_level\" <= " 
                + String.valueOf(tank_level) + " GROUP BY  \"topic\" ";
        
        System.out.println(command);
        
        Query query = new Query(command, "telegraf");
       
        List<String> topics = new ArrayList<>();
        QueryResult queryResult = influxDBConnection.query(query);
        
        if (!queryResult.getResults().isEmpty()) {
            System.out.println("C'è un result");
            List<Result> series = queryResult.getResults();
            for (Result result : series) {
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
        Runtime.getRuntime().addShutdownHook(new Thread(influxDBConnection::close));
        return topics;
    
    }

    @Override
    public List<String> getAllZoneNameThatAreSendInformatioUpto(Date date) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
