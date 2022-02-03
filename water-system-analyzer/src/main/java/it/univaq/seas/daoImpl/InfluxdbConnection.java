/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.seas.daoImpl;

import it.univaq.seas.utils.Utils;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;

/**
 * @author valerio
 */
public class InfluxdbConnection {


    public static final String urlDocker = "http://influxdb:8086";

    public static final String urlLocalhost = "http://localhost:8086";

    protected InfluxDB influxDBConnection;

    public InfluxdbConnection() {
        String serverURL = (Utils.dockerized) ? urlDocker : urlLocalhost, username = "telegraf", password = "secretpassword";
        this.influxDBConnection = InfluxDBFactory.connect(serverURL, username, password);
    }


    @Override
    protected void finalize() throws Throwable {
        try {
            Runtime.getRuntime().addShutdownHook(new Thread(this.influxDBConnection::close));
        } finally {
            super.finalize();
        }
    }


//    QueryResult queryResult = influxDB.query(new Query("SELECT last(\"tank_level\") FROM zone WHERE \"tank_level\" >= 60 GROUP BY  \"topic\"  ","telegraf"));
//
//    //System.out.println(queryResult);
//    if(!queryResult.getResults().isEmpty()){
//        System.out.println("C'è un result");
//        List<Result> series = queryResult.getResults();
//        for(Result result : series){
//            if(!result.getSeries().isEmpty()){
//                System.out.println("C'è una series");
//                for(Series serie : result.getSeries()){
//                    System.out.println(serie.toString());
//                    if(serie.getTags().containsKey("topic")){
//                        System.out.println(serie.getTags().get("topic"));
//                    }
//                    if(!serie.getValues().isEmpty()){
//                       serie.getValues().forEach(value -> {
//                           System.out.println(value.toString());
//                       });
//                       
//                    }
//                }
//                
//            }
//        }
//    }
    //queryResult.getResults().forEach(query -> {query.getSeries().});


}
