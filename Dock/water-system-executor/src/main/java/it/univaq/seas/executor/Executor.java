package it.univaq.seas.executor;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Map;

/**
 * @author federico
 */
public class Executor implements MqttCallback {

    private MqttClient plannerClient = null;
    private MqttClient systemClient = null;
    private static Executor ExecutorInstance = null;
    private Thread myThread = null;
    private boolean stop;
    private String url;
    private boolean dockerized;

    public static Executor getInstance(Boolean dockerize) {

        if (ExecutorInstance == null) {
            try {
                ExecutorInstance = new Executor(dockerize);
                ExecutorInstance.dockerized = dockerize;
            } catch (MqttException e) {
                e.printStackTrace();
            }

        }
        return ExecutorInstance;
    }

    private Executor(Boolean dockerize) throws MqttException {

        /**
         * Set the url. The broker is the same deployment node
         * for both listening and publishing.
         */
        if (dockerize) {
            this.url = "tcp://mosquitto:1883";
        }
        else {
            this.url = "tcp://localhost:1883";
        }

        stop = false;

        this.systemClient = new MqttClient(this.url, "Executor_SystemClient");
        this.plannerClient = new MqttClient(this.url, "Executor_PlannerClient");


        systemClient.connect();
        connectAndSubscribe();
    }




    private void publish(String topic, String data) {
        try {
            MqttMessage message = new MqttMessage();
            message.setPayload(data.getBytes());
            systemClient.publish(topic, message);// + this.zoneName, message);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void connectAndSubscribe() {
        try {
            this.plannerClient.setCallback(this);
            this.plannerClient.connect();
            // client.subscribe("home/outsidetemperature");
            // client.subscribe(new String[]{"home/outsidetemperature","home/livingroomtemperature"});
            this.plannerClient.subscribe("home/planner_executor");
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    private void publishParameterValues(JSONObject jsonObj, String parameter, String value) {

        System.out.println(parameter + jsonObj.get(parameter));
        Object parame = jsonObj.get(parameter);

        System.out.println(parame.toString());

        if(parame.toString().equals("null")) return;

        if (parame != null) {
            System.out.println("IN");
            ActingData toSend = new ActingData();
            toSend.setParameter(parameter);
            toSend.setValue(jsonObj.getInt(value));
            //toPub = new JSONObject(toSend).toString();
            //toPub = "{\"parameter\": " + jsonobj.getInt("newTankInput") + ", value: " + jsonobj.getInt(\"newTankInput\") + \"}";
            String toPub = new JSONObject(toSend).toString();
            publish(jsonObj.getString("topic").replace("sensing", "acting"), toPub);
            System.out.println("PUBLISHED " + toPub);
        }

        System.out.println(parameter + jsonObj.get(parameter));

    }

    private JSONObject execute(String s) {

        //System.out.println("SXXX");


        //JSONObject json = new JSONObject(s);
        JSONArray arrJSON = new JSONArray(s);

        for (Object obj : arrJSON) {

            JSONObject jsonobj = (JSONObject) obj;
            //System.out.println(jsonobj.getString("topic"));

            publishParameterValues(jsonobj, "tankInput", "tankInput");
            publishParameterValues(jsonobj, "tankOutput", "tankOutput");
            publishParameterValues(jsonobj, "active", "active");

        }

        //System.out.println(s.toString());

        //ActuatorData toSend = new ActuatorData();
        //toSend.insertData("testfield1", "10");
        //toSend.insertData("testfield2", "34");
        //toSend.insertData("testfield3", "re34");
        //JSONObject data = new JSONObject(toSend);
        return null;
    }

    public void stop() {

        this.stop = true;
    }

    @Override
    public void connectionLost(Throwable throwable) {

    }

    @Override
    public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
        System.out.println("EXECUTOR: A new message arrived from the topic: \"" + topic + "\". The payload of the message is " + mqttMessage.toString());

        execute(mqttMessage.toString());
        //JSONObject toWrite = execute(s);
        //System.out.println("Execution parameters computed: " + toWrite.toString());
        //publish(toWrite.toString());

    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

    }
}
