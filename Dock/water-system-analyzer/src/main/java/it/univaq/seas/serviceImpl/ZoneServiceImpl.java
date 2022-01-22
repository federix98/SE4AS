/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.seas.serviceImpl;

import it.univaq.seas.dao.ZoneDao;
import it.univaq.seas.daoImpl.ZoneDaoImpl;
import it.univaq.seas.httpConnection.SimpleHttpConnection;
import it.univaq.seas.model.SymptomId;
import it.univaq.seas.model.SymptomsMessage;
import it.univaq.seas.model.ZoneData;
import it.univaq.seas.service.ZoneService;
import it.univaq.seas.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author federico
 * @author valerio
 */
public class ZoneServiceImpl implements ZoneService {

    private ZoneDao dao = new ZoneDaoImpl();
    private String DOCKERURL = "http://water-system-planner:8081/";
    private String LOCALURL = "http://localhost:8081/";
    private Boolean DOCKERIZE = false;

    @Override
    public void checkTankLevelIsUpTo(int tankLevel) {
        //make call
        //che if response is empty
        //Send http post message
        List<String> topics = dao.getAllZoneNameWithTankLevelUpTo(tankLevel);

        if (!topics.isEmpty()) {
            SymptomsMessage message = new SymptomsMessage();
            message.setSymptomId(SymptomId.TANK_UP_TO_WARNING_LEVEL);
            message.setAlertValue(tankLevel);
            message.setZones(topics);

            String jsonMessage = Utils.convertMessageToJSONString(message);

            SimpleHttpConnection.invoke(jsonMessage);
        }

    }

    @Override
    public void checkTankLevelIsDownTo(int tankLevel) {
        
        List<String> topics = dao.getAllZoneNameWithTankLevelDownTo(tankLevel);

        if (!topics.isEmpty()) {
            SymptomsMessage message = new SymptomsMessage();
            message.setSymptomId(SymptomId.TANK_DOWN_WARNING_LEVEL);
            message.setAlertValue(tankLevel);
            message.setZones(topics);

            String jsonMessage = Utils.convertMessageToJSONString(message);

            SimpleHttpConnection.invoke(jsonMessage);
        }
    }

    @Override
    public void consumptionAdaptation() {

        /**
         * Si attiva solo quando la totaldemand di qualcuno supera il tankOutput
         */

        String endpoint = "consumptionAdaptation";
        String Url = (DOCKERIZE) ? (DOCKERURL + endpoint) : (LOCALURL + endpoint);

        int val = dao.checkConsumptionAdaptation();
        if (val != 0) {
            System.out.println("Need to adapt the consumption of the zones");
            SymptomsMessage message = new SymptomsMessage();
            message.setSymptomId(SymptomId.CONSUMPTION_ADAPTATION_REQURIED);
            message.setAlertValue(val);
            message.setZones(null);


            String jsonMessage = Utils.convertMessageToJSONString(message);

            SimpleHttpConnection.invoke(jsonMessage, Url);
        }
        else {
            System.out.println("No need for adaptation of the consumption");
        }

    }

    @Override
    public void setMaintainance() {

        String endpoint = "maintenance";
        String Url = (DOCKERIZE) ? (DOCKERURL + endpoint) : (LOCALURL + endpoint);
        List<ZoneData> zones = dao.getZoneData();
        List<ZoneData> to_adapt = new ArrayList<ZoneData>();

        for (ZoneData zone : zones) {
            if ((zone.getDemand() >= (zone.getNumHouse() * 30)) && zone.getActive() == 1) {
                System.out.println("Option 1 " + zone.getTopic() + " " + zone.getDemand() + " " + zone.getNumHouse());
                to_adapt.add(zone);
            }
            else if (zone.getTank_level() == 0 && zone.getActive() == 1) {
                System.out.println("Option 2 " + zone.getTopic() + " " + zone.getTank_level());
                to_adapt.add(zone);
            }
        }

        if (!to_adapt.isEmpty()) {
            SymptomsMessage msg = new SymptomsMessage();

            List<String> topics = new ArrayList<String>();
            for (ZoneData zone : to_adapt) {
                topics.add(zone.getTopic());
            }

            msg.setZones(topics);
            msg.setSymptomId(SymptomId.MAINTENANCE);
            msg.setAlertValue(0);

            System.out.println("Maintainance adaptation " + msg.getZones().toString());

            String jsonMessage = Utils.convertMessageToJSONString(msg);

            SimpleHttpConnection.invoke(jsonMessage, Url);
        }




    }


}
