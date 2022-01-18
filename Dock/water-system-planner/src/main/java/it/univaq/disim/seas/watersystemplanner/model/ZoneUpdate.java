package it.univaq.disim.seas.watersystemplanner.model;

public class ZoneUpdate {

    private int newTankInput;
    private int newTankOutput;
    private int zoneId;

    public int getZoneId() {
        return zoneId;
    }

    public void setZoneId(int zoneId) {
        this.zoneId = zoneId;
    }

    public ZoneUpdate() {

    }

    public int getNewTankInput() {
        return newTankInput;
    }

    public void setNewTankInput(int newTankInput) {
        this.newTankInput = newTankInput;
    }

    public int getNewTankOutput() {
        return newTankOutput;
    }

    public void setNewTankOutput(int newTankOutput) {
        this.newTankOutput = newTankOutput;
    }
}
