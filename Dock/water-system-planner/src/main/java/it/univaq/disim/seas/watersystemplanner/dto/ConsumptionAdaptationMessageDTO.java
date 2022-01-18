package it.univaq.disim.seas.watersystemplanner.dto;

public class ConsumptionAdaptationMessageDTO {

    private String symptomId;
    private int alertValue;
    private Object zones;

    public ConsumptionAdaptationMessageDTO() {

    }

    public String getSymptomId() {
        return symptomId;
    }

    public void setSymptomId(String symptomId) {
        this.symptomId = symptomId;
    }

    public int getAlertValue() {
        return alertValue;
    }

    public void setAlertValue(int alertValue) {
        this.alertValue = alertValue;
    }

    public Object getZones() {
        return zones;
    }

    public void setZones(Object zones) {
        this.zones = zones;
    }
}
