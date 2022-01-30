package it.univaq.disim.seas.watersystemplanner.model;

import java.util.List;

public class ZoneDataRegression extends ZoneData {

    private List<Integer> historicDemandValues;

    public List<Integer> getHistoricDemandValues() {
        return historicDemandValues;
    }

    public void setHistoricDemandValues(List<Integer> historicDemandValues) {
        this.historicDemandValues = historicDemandValues;
    }
}
