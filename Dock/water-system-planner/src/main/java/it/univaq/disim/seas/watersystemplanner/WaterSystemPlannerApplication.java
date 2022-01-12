package it.univaq.disim.seas.watersystemplanner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;

@SpringBootApplication
public class WaterSystemPlannerApplication {
    
	public static void main(String[] args) {
		SpringApplication.run(WaterSystemPlannerApplication.class, args);
	}

}
