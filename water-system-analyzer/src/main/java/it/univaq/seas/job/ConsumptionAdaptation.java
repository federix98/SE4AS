package it.univaq.seas.job;

import it.univaq.seas.service.ZoneService;
import it.univaq.seas.serviceImpl.ZoneServiceImpl;

import java.util.TimerTask;

public class ConsumptionAdaptation extends TimerTask {

    @Override
    public void run() {
        ZoneService service = new ZoneServiceImpl();
        service.consumptionAdaptation();
        service.setMaintainance();
    }

}
