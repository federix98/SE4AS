/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.seas.model;

/**
 *
 * @author valerio
 * @author federico
 */
public class ZoneData {
    private int id;
    private int demand;
    private int numHouse;
    private String topic;
    private int tankInput;
    private int tankOutput;

    public int getTankOutput() {
        return tankOutput;
    }

    public void setTankOutput(int tankOutput) {
        this.tankOutput = tankOutput;
    }

    public int getTankInput() {
        return tankInput;
    }

    public void setTankInput(int tankInput) {
        this.tankInput = tankInput;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDemand() {
        return demand;
    }

    public void setDemand(int demand) {
        this.demand = demand;
    }

    public int getNumHouse() {
        return numHouse;
    }

    public void setNumHouse(int numHouse) {
        this.numHouse = numHouse;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public int getTank_level() {
        return tank_level;
    }

    public void setTank_level(int tank_level) {
        this.tank_level = tank_level;
    }
    private int active;
    private int tank_level;
}
