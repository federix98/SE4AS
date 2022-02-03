/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.seas.model;

import java.util.List;

/**
 *
 * @author valerio
 */
public class SymptomsMessage {
    private SymptomId symptomId;
    private int alertValue;
    private List<String> zones;

    public SymptomsMessage(SymptomId symptomId, int alertValue, List<String> zones) {
        this.symptomId = symptomId;
        this.alertValue = alertValue;
        this.zones = zones;
    }

    public SymptomsMessage() {
    }

    public SymptomId getSymptomId() {
        return symptomId;
    }

    public void setSymptomId(SymptomId symptomId) {
        this.symptomId = symptomId;
    }

    public int getAlertValue() {
        return alertValue;
    }

    public void setAlertValue(int alertValue) {
        this.alertValue = alertValue;
    }

    public List<String> getZones() {
        return zones;
    }

    public void setZones(List<String> zones) {
        this.zones = zones;
    }
    
    
}
