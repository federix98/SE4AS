package it.univaq.seas;

import it.univaq.seas.dao.ZoneDao;
import it.univaq.seas.daoImpl.ZoneDaoImpl;
import it.univaq.seas.service.ZoneService;
import it.univaq.seas.serviceImpl.ZoneServiceImpl;

/**
 * Hello world!
 *
 */
public class App {

    public static void main(String[] args) {
        System.out.println("Hello World!");
        ZoneService service = new ZoneServiceImpl();
        service.checkTankLevelIsDownTo(200);

    }
}
