package com.example.napes.clients;

import android.util.Log;

import com.example.napes.MainActivity;
import com.example.napes.config.Config;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.io.FileOutputStream;

public class MqttCallbackImpl implements MqttCallback {
    private static final String TAG = "TAG" ;
    MainActivity mainActivity;
    MqttClient client;

    public MainActivity getMainActivity() {
        return mainActivity;
    }

    public void setMainActivity(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public MqttCallbackImpl(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public void setParams() {
     //   this.mainActivity = mainActivity;
        try {
             client = new MqttClient("tcp://"+ Config.ipAddress +":"+Config.mqttPort+"",
                    "AndroidThingSub", new MemoryPersistence());
            MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
            mqttConnectOptions.setUserName("12qw34er");
            mqttConnectOptions.setPassword("12qw34er".toCharArray());
            client.setCallback(this);
            client.connect(mqttConnectOptions);
           // String topic = "test/";
            client.subscribe(Config.mqttTopic);

        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void clientPublish(String message){
        try {
            client.publish(Config.mqttTopic,new MqttMessage(message.getBytes()));
           // mainActivity.setText("\nMQTT/Message sent!");
        } catch (MqttException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        String payload = new String(message.getPayload());
        System.out.println(payload);
      //  Config.fos.write(payload.getBytes());
        //textView.append("\n"+payload);
        mainActivity.setText("\nMQTT/M: "+payload);
        Log.d(TAG, payload);
    }

    @Override
    public void connectionLost(Throwable cause) {
        Log.d(TAG, "connectionLost");
        System.out.println(cause);
    }



    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        Log.d(TAG, "deliveryComplete");
    }

}
