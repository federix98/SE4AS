package it.univaq.disim.seas.watersystemplanner.model;

/**
 * @author federico
 */
public class ZoneUpdate {

    private int newTankInput;
    private int newTankOutput;
    private int zoneId;
    private String topic;

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

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
