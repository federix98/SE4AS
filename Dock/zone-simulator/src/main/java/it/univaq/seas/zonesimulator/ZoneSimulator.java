package it.univaq.seas.zonesimulator;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import org.eclipse.paho.client.mqttv3.*;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import sun.rmi.runtime.Log;

public class ZoneSimulator implements Runnable, MqttCallback {

	private String DockerURL = "tcp://mosquitto:1883";
	private String LocalURL = "tcp://localhost:1883";
	private String serverURI;

	// Tank Data
	private Integer tankOutput = null;
	private Integer tankInput = null;
	private Integer tankFillLevel = null;
	private Integer tankCapacity = null;
	
	// House Data
	private Integer numHouse = null;
	private Integer squareMeters = null;
	private Integer totalDemand = null;
	
	// Thread Data
	private String zoneName = "";
	private Integer zoneId = null;
	private boolean stop = false;
	private Integer interval = 10000;
	private Integer active = null;

	Map<String, Method> reflectionMap = null;
	
	MqttClient sensingClient, actingClient;

	public Integer getZoneId() {
		return zoneId;
	}

	public void setZoneId(Integer zoneId) {
		this.zoneId = zoneId;
	}

	public Integer getActive() {
		return active;
	}

	public void setActive(Integer active) {
		this.active = active;
	}

	public Integer getTotalDemand() {
		return totalDemand;
	}

	public void setTotalDemand(Integer totalDemand) {
		this.totalDemand = totalDemand;
	}

	public ZoneSimulator(Integer zoneId, String zoneName, Integer tankInput, Integer tankOutput, Integer tankFillLevel, Integer tankCapacity,
						 Integer numHouse, Integer squareMeters) {
		super();
		this.zoneId = zoneId;
		this.setZoneName(zoneName);
		this.tankOutput = tankOutput;
		this.tankInput = tankInput;
		this.tankFillLevel = tankFillLevel;
		this.tankCapacity = tankCapacity;
		this.numHouse = numHouse;
		this.squareMeters = squareMeters;
		this.active = 1;

		try {
			this.reflectionMap = buildReflectionMap();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}

		//this.serverURI = DockerURL;
		this.serverURI = LocalURL;

		this.totalDemand = calculateDemand();

		connectAndSubscribe();
	}

	private int calculateDemand() {
		if (this.numHouse == null) {
			return 0;
		}
		return this.numHouse * (new Random().nextInt(5) + 10);
	}

	private void selectMethod(String field, Integer value) {
		switch (field) {
			case "tankInput":
				this.setTankInput(value);
				break;
			case "tankOutput":
				this.setTankOutput(value);
				break;
			case "tankFillLevel":
				this.setTankFillLevel(value);
				break;
			case "active":
				this.setActive(value);
				break;
			case "tankCapacity":
				this.setTankCapacity(value);
				break;
			default:
				System.out.println("Error on selecting method" + field);
				break;
		}
	}

	private Map<String, Method> buildReflectionMap() throws NoSuchMethodException {

		Map<String, Method> toRet = new HashMap<String, Method>();
		toRet.put("tankInput", this.getClass().getMethod("setTankInput", Integer.class));
		toRet.put("tankOutput", this.getClass().getMethod("setTankOutput", Integer.class));
		toRet.put("tankFillLevel", this.getClass().getMethod("setTankFillLevel", Integer.class));
		toRet.put("tankCapacity", this.getClass().getMethod("setTankCapacity", Integer.class));
		toRet.put("numHouse", this.getClass().getMethod("setNumHouse", Integer.class));
		toRet.put("active", this.getClass().getMethod("setActive", Integer.class));

		return toRet;
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


	public void connectAndSubscribe() {
		try {
			actingClient = new MqttClient(this.serverURI, "zone" + this.zoneId + "_acting_client");

			actingClient.setCallback(this);
			actingClient.connect();
			// client.subscribe("home/outsidetemperature");
			// client.subscribe(new String[]{"home/outsidetemperature","home/livingroomtemperature"});
			actingClient.subscribe("home/acting/zone" + this.zoneId);
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		while(!this.stop) {
			//int randomNum = ThreadLocalRandom.current().nextInt(0, 100);
			JSONObject data = generateData(this);
			System.out.println(data);
			//String topub = "tank_level value=".concat(data.get("tankOutput").toString());
			publish(data.toString());

			long current = System.currentTimeMillis();
			long start = current;
			long now = System.currentTimeMillis();

			while ((now - start) < interval) {
				now = System.currentTimeMillis();
			}
			/*try {
				Thread.sleep(interval);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
			this.totalDemand = calculateDemand();
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
	
	public void publish(String data) {
	    try {
			sensingClient = new MqttClient(this.serverURI, "zone" + this.zoneId + "_sensing_client");
			sensingClient.connect();
			MqttMessage message = new MqttMessage();
			message.setPayload(data.getBytes());
			sensingClient.publish("home/sensing/zone" + this.zoneId, message);// + this.zoneName, message);
			sensingClient.disconnect();
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

	@Override
	public void connectionLost(Throwable throwable) {

	}

	@Override
	public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
		System.out.println(this.zoneName + " ==> New adaptation message on topic: " + mqttMessage);

		JSONObject adaptationData = new JSONObject(mqttMessage.toString());

		//System.out.println(adaptationData);

		String parameter = (String) adaptationData.get("parameter");
		Integer value = (Integer) adaptationData.get("value");

		if (reflectionMap.containsKey(parameter)) {
			//System.out.println("Param " + parameter + " val " + value);
			//this.getClass().getDeclaredMethod("setTankInput", Integer.class).invoke(value);
			selectMethod(parameter, value);
			//System.out.println(this.getTankInput());
			//reflectionMap.get(parameter).invoke("tankInput()");
		} else {
			System.out.println("Error: parameter not found - " + parameter);
		}
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

	}
}
