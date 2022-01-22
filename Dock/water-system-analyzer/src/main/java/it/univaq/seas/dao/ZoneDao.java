/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.seas.dao;

import it.univaq.seas.model.ZoneData;

import java.util.Date;
import java.util.List;

/**
 *
 * @author valerio
 */
public interface ZoneDao {
    /**
     * Funzione che ritorna la lista di zone che hanno l'ultimo dato del 
     * livello di riempimento del serbatoio maggiore di a tank_level (tutte le zone)
     * Lo possiamo usare per trovare le zone che hanno il serbatoio pieno e 
     * @param tank_level
     * @return 
     */
    
    public List<String> getAllZoneNameWithTankLevelUpTo(int tank_level);
    
     /**
     * Funzione che ritorna la lista di zone che hanno l'ultimo dato del 
     * livello di riempimento del serbatoio al di sotto di tank_level (tutte le zone)
     * Lo possiamo usare per trovare le zone che hanno il serbatoio vuoto
     * @param tank_level
     * @return 
     */
    
    public List<String> getAllZoneNameWithTankLevelDownTo(int tank_level);
    
    /**
     * Ritorna la lista di zone che hanno inviato un dato entro il time stamp 
     * programmato
     * @param date
     * @return 
     */
    
    public List<String> getAllZoneNameThatAreSendInformatioUpto(Date date);
    
    public int checkConsumptionAdaptation();

    public List<ZoneData> getZoneData();

}
