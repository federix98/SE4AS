/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.disim.seas.watersystemplanner.dao;

import it.univaq.disim.seas.watersystemplanner.model.ZoneData;

import java.util.Date;
import java.util.List;

/**
 *
 * @author valerio
 */
public interface ZoneDao {

    public List<ZoneData> getZoneData();
    
}
