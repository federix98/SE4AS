/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.disim.seas.watersystemplanner.model;

/**
 *
 * @author valerio
 */
public class ZoneData {
    private Integer id;
    private Integer demand;
    private Integer numHouse;
    private String topic;
    private Integer tankInput;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDemand() {
        return demand;
    }

    public void setDemand(Integer demand) {
        this.demand = demand;
    }

    public Integer getNumHouse() {
        return numHouse;
    }

    public void setNumHouse(Integer numHouse) {
        this.numHouse = numHouse;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public Integer getTankInput() {
        return tankInput;
    }

    public void setTankInput(Integer tankInput) {
        this.tankInput = tankInput;
    }

    public Integer getTankOutput() {
        return tankOutput;
    }

    public void setTankOutput(Integer tankOutput) {
        this.tankOutput = tankOutput;
    }

    public Integer getActive() {
        return active;
    }

    public void setActive(Integer active) {
        this.active = active;
    }

    public Integer getTank_level() {
        return tank_level;
    }

    public void setTank_level(Integer tank_level) {
        this.tank_level = tank_level;
    }

    private Integer tankOutput;
    private Integer active;
    private Integer tank_level;
}
