import java.util.Scanner;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.eclipse.paho.client.mqttv3.*;

public class MQTT_Test{
    private final static String ADAFRUIT_USERNAME = "pimonteiro";

    private final static String ADAFRUIT_AIO_KEY  = "aio_RJsH02PYI6XYcS6zceIyrA1GEyz3";

    public static void main(String[] args) {
        String topic = ADAFRUIT_USERNAME + "/feeds/subscriber";
        int qos= 1;     //QoS: 0 -at most once, 1 -at least once, 2 -exactly once
        String broker = "tcp://io.adafruit.com:1883"; //Adafruit IO broker
        String client_id = "JavaSample";
        MemoryPersistence persistence = new MemoryPersistence();

        try{
            MqttClient mqtt_client = new MqttClient(broker, client_id, persistence);

            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            connOpts.setUserName(ADAFRUIT_USERNAME);    
            connOpts.setPassword(ADAFRUIT_AIO_KEY.toCharArray());

            System.out.println("Connection to broker: " + broker);
            mqtt_client.connect(connOpts);
            mqtt_client.subscribe(topic,1);
            mqtt_client.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable throwable) {
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    System.out.println(String.format("\nMessage Received: [%s] %s", topic, new String(message.getPayload())));
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
                }
            });

            String text = "";
            do{
                Scanner sn = new Scanner(System.in);
                System.out.print("Text to send: ");
                text = sn.nextLine();
                System.out.println("Publishing message: " + text);
                MqttMessage message = new MqttMessage(text.getBytes());
                message.setQos(qos);
                mqtt_client.publish(topic, message);
                System.out.println("Message published");
            } while(!text.equals("exit"));   

            mqtt_client.disconnect();
            System.out.println("Disconnected");
            System.exit(0);
        }
        catch(MqttException me) {
            System.out.println("reason: " + me.getReasonCode());
            System.out.println("msg: " + me.getMessage());
            System.out.println("loc: " + me.getLocalizedMessage());
            System.out.println("cause: " + me.getCause());
            System.out.println("excep: " + me);
            me.printStackTrace();
        }
    }
}