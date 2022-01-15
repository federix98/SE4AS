package it.univaq.seas;

import it.univaq.seas.dao.ZoneDao;
import it.univaq.seas.daoImpl.InfluxdbConnection;
import it.univaq.seas.daoImpl.ZoneDaoImpl;
import it.univaq.seas.httpConnection.SimpleHttpConnection;
import it.univaq.seas.scheduler.AnalyzerScheduler;
import java.util.List;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        AnalyzerScheduler.scedulle();
    }
}
