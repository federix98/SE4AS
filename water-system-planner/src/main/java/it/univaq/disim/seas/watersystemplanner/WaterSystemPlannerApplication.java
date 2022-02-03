package it.univaq.disim.seas.watersystemplanner;

import it.univaq.disim.seas.watersystemplanner.controller.WaterService;
import it.univaq.disim.seas.watersystemplanner.dao.ZoneDao;
import it.univaq.disim.seas.watersystemplanner.daoImpl.ZoneDaoImpl;
import it.univaq.disim.seas.watersystemplanner.model.ZoneDataRegression;
import it.univaq.disim.seas.watersystemplanner.utils.LinearRegressor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class WaterSystemPlannerApplication {
    
	public static void main(String[] args) {
		SpringApplication.run(WaterSystemPlannerApplication.class, args);
		//Integer[] x = {0, 1, 2, 3, 4, 5, 6, 7};
		//Integer[] y = {85, 83, 84, 82, 81, 77, 79, 76};
		//List<Integer> xl = new ArrayList<Integer>(Arrays.asList(x));
		//List<Integer> yl = new ArrayList<Integer>(Arrays.asList(y));
		//System.out.println(LinearRegressor.predictForValue(xl, yl, 8));
		//ZoneDao dao = new ZoneDaoImpl();
		//System.out.println(dao.getZoneData().get(0).getId());
		//ZoneDao dao = new ZoneDaoImpl();
		//System.out.println(WaterService.waterConsumptionPredPolicy(dao.getZoneDataRegression(), 5000));

	}

}
