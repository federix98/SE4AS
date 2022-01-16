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
import it.univaq.seas.service.ZoneService;
import it.univaq.seas.utils.Utils;
import java.util.List;

/**
 *
 * @author valerio
 */
public class ZoneServiceImpl implements ZoneService {

    private ZoneDao dao = new ZoneDaoImpl();

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

}
