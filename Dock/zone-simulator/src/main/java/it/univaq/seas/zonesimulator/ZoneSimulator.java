package it.univaq.seas.zonesimulator;

import java.util.concurrent.ThreadLocalRandom;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONObject;

public class ZoneSimulator implements Runnable {
	
	// Tank Data
	private Integer tankOutput = null;
	private Integer tankInput = null;
	private Integer tankFillLevel = null;
	private Integer tankCapacity = null;
	
	// House Data
	private Integer numHouse = null;
	private Integer squareMeters = null;
	private Double totalDemand = null;
	
	// Thread Data
	private String zoneName = "";
	private Integer zoneId = null;
	private boolean stop = false;
	private Integer interval = 10000;
	
	MqttClient client;
	
	public ZoneSimulator(Integer zoneId, String zoneName, Integer tankInput, Integer tankFillLevel, Integer tankCapacity,
			Integer numHouse, Integer squareMeters) {
		super();
		this.zoneId = zoneId;
		this.setZoneName(zoneName);
		this.tankInput = tankInput;
		this.tankFillLevel = tankFillLevel;
		this.tankCapacity = tankCapacity;
		this.numHouse = numHouse;
		this.squareMeters = squareMeters;
	}
	
	


	public Integer getTankOutput() {
		return tankOutput;
	}




	public void setTankOutput(Integer tankOutput) {
		this.tankOutput = tankOutput;
	}




	public Integer getTankInput() {
		return tankInput;
	}




	public void setTankInput(Integer tankInput) {
		this.tankInput = tankInput;
	}




	public Integer getTankFillLevel() {
		return tankFillLevel;
	}




	public void setTankFillLevel(Integer tankFillLevel) {
		this.tankFillLevel = tankFillLevel;
	}




	public Integer getTankCapacity() {
		return tankCapacity;
	}




	public void setTankCapacity(Integer tankCapacity) {
		this.tankCapacity = tankCapacity;
	}




	public Integer getNumHouse() {
		return numHouse;
	}




	public void setNumHouse(Integer numHouse) {
		this.numHouse = numHouse;
	}




	public Integer getSquareMeters() {
		return squareMeters;
	}




	public void setSquareMeters(Integer squareMeters) {
		this.squareMeters = squareMeters;
	}




	public Double getTotalDemand() {
		return totalDemand;
	}




	public void setTotalDemand(Double totalDemand) {
		this.totalDemand = totalDemand;
	}




	public boolean isStop() {
		return stop;
	}




	public void setStop(boolean stop) {
		this.stop = stop;
	}




	public Integer getInterval() {
		return interval;
	}




	public void setInterval(Integer interval) {
		this.interval = interval;
	}




	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		while(!this.stop) {
			//int randomNum = ThreadLocalRandom.current().nextInt(0, 100);
			JSONObject data = generateData(this);
			System.out.println(data);
			//String topub = "tank_level value=".concat(data.get("tankOutput").toString());
			publish1(data.toString());
			try {
				Thread.sleep(interval);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	private JSONObject generateData(ZoneSimulator obj) {
		JSONObject data = new JSONObject(obj);
		
		//data.put("tankOutput", tankOutput);
		return data;
		
	}
	
	public void stop() {
		this.stop = true;
	}
	
	public void publish1(String data) {
	    try {
		  client = new MqttClient("tcp://mosquitto:1883", "pahomqtt" + this.zoneName);
		  client.connect();
		  MqttMessage message = new MqttMessage();
		  message.setPayload(data.getBytes());
		  client.publish("home/zone" + this.zoneId, message);// + this.zoneName, message);
		  client.disconnect();
	    } catch (MqttException e) {
	      e.printStackTrace();
	    }
	  }




	public String getZoneName() {
		return zoneName;
	}




	public void setZoneName(String zoneName) {
		this.zoneName = zoneName;
	}

}
