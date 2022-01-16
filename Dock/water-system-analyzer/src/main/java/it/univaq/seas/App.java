package it.univaq.seas;

import it.univaq.seas.dao.ZoneDao;
import it.univaq.seas.daoImpl.ZoneDaoImpl;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        ZoneDao dao = new ZoneDaoImpl();
        dao.getAllZoneNameWithTankLevelUpTo(90);
        
    }
}
