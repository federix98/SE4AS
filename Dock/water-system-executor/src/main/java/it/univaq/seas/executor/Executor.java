package it.univaq.seas.executor;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONObject;

public class Executor implements Runnable, MqttCallback {

    private MqttClient client;
    private static Executor ExecutorInstance = null;
    private Thread myThread = null;
    private boolean stop;

    public static Executor getInstance() {

        if (ExecutorInstance == null) {
            ExecutorInstance = new Executor();
        }
        return ExecutorInstance;
    }

    private Executor() {
        myThread = new Thread(this);
        myThread.start();
    }



    private void publish(String data) {
        try {
            client = new MqttClient("tcp://mosquitto:1883", "water-system-executor-client");
            client.connect();
            MqttMessage message = new MqttMessage();
            message.setPayload(data.getBytes());
            client.publish("home/executor", message);// + this.zoneName, message);
            client.disconnect();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void subscribe() {
        try {
            client = new MqttClient("tcp://localhost:1883", "water-system-executor-client");

            client.setCallback(this);
            client.connect();
            // client.subscribe("home/outsidetemperature");
            // client.subscribe(new String[]{"home/outsidetemperature","home/livingroomtemperature"});
            client.subscribe("home/executor_listening_topic");
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {

        while(!this.stop) {
            ActuatorData toSend = new ActuatorData();
            toSend.insertData("testfield1", "10");
            toSend.insertData("testfield2", "34");
            toSend.insertData("testfield3", "re34");
            JSONObject data = new JSONObject(this);
            System.out.println(data.toString());
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

    @Override
    public void connectionLost(Throwable throwable) {

    }

    @Override
    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
        System.out.println("A new message arrived from the topic: \"" + s + "\". The payload of the message is " + mqttMessage.toString());
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

    }
}
