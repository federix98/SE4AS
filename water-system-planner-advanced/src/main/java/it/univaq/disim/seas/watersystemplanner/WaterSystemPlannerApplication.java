package it.univaq.disim.seas.watersystemplanner;

import it.univaq.disim.seas.watersystemplanner.controller.WaterService;
import it.univaq.disim.seas.watersystemplanner.dao.ZoneDao;
import it.univaq.disim.seas.watersystemplanner.daoImpl.ZoneDaoImpl;
import it.univaq.disim.seas.watersystemplanner.model.ZoneDataRegression;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;

@SpringBootApplication
public class WaterSystemPlannerApplication {
    
	public static void main(String[] args) {
		SpringApplication.run(WaterSystemPlannerApplication.class, args);
		//ZoneDao dao = new ZoneDaoImpl();
		//System.out.println(dao.getZoneData().get(0).getId());
		//ZoneDao dao = new ZoneDaoImpl();
		//System.out.println(WaterService.waterConsumptionPredPolicy(dao.getZoneDataRegression(), 5000));

	}

}
