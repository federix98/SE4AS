package it.univaq.seas.executor;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONObject;

import java.util.Map;

/**
 * @author federico
 */
public class Executor implements Runnable, MqttCallback {

    private MqttClient client = null;
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
        myThread = new Thread(this);
        myThread.start();

        connectAndSubscribe();
    }




    private void publish(String data) {
        try {
            MqttMessage message = new MqttMessage();
            message.setPayload(data.getBytes());
            client.publish("home/executor", message);// + this.zoneName, message);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void connectAndSubscribe() {
        try {
            client = new MqttClient(this.url, "water-system-executor-client");

            client.setCallback(this);
            client.connect();
            // client.subscribe("home/outsidetemperature");
            // client.subscribe(new String[]{"home/outsidetemperature","home/livingroomtemperature"});
            client.subscribe("home/executor_listening_topic");
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    private JSONObject execute(String s) {
        ActuatorData toSend = new ActuatorData();
        toSend.insertData("testfield1", "10");
        toSend.insertData("testfield2", "34");
        toSend.insertData("testfield3", "re34");
        JSONObject data = new JSONObject(toSend);
        return data;
    }

    @Override
    public void run() {

        while(!this.stop) {


            System.out.println("Executor listening");
            try {
                Thread.sleep(100000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

    public void stop() {

        this.stop = true;
    }

    @Override
    public void connectionLost(Throwable throwable) {

    }

    @Override
    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
        System.out.println("EXECUTOR: A new message arrived from the topic: \"" + s + "\". The payload of the message is " + mqttMessage.toString());

        JSONObject toWrite = execute(s);
        System.out.println("Execution parameters computed: " + toWrite.toString());
        publish(toWrite.toString());

    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

    }
}
