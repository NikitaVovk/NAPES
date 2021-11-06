package com.example.napes.clients;

import android.graphics.Color;
import android.util.Log;

import com.example.napes.MainActivity;
import com.example.napes.config.Config;
import com.example.napes.runtime.domains.events.Event;
import com.example.napes.runtime.service.EventService;
import com.example.napes.runtime.service.Service;
import com.example.napes.runtime.service.ServiceStates;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.io.FileOutputStream;
import java.util.ListIterator;

public class MqttCallbackImpl implements MqttCallback {
    private static final String TAG = "TAG" ;
    MainActivity mainActivity;
    MqttClient client;
    EventService eventService;

    public MainActivity getMainActivity() {
        return mainActivity;
    }

    public void setMainActivity(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public MqttCallbackImpl(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public void  setEventService(EventService eventService){
        this.eventService = eventService;
    }
    public void setParams() {
     //   this.mainActivity = mainActivity;
        try {
             client = new MqttClient("tcp://"+ Config.ipAddressBroker +":"+Config.mqttPort+"",
                     MqttAsyncClient.generateClientId(), new MemoryPersistence());
            MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();

            mqttConnectOptions.setUserName("12qw34er");
            mqttConnectOptions.setPassword("12qw34er".toCharArray());

            client.setCallback(this);
            client.connect(mqttConnectOptions);
           // String topic = "test/";
            for (Event event : mainActivity.getComponent().getEventList().getEvents()){
            if (event.getMqtt_eName()!=null){//&&!event.geteType().equals("o")){
                System.out.println("SUBBING: "+event.getMqtt_eName() );
            client.subscribe(event.getMqtt_eName());}
            }

            System.out.println("EVENT SERVICE: "+eventService);


            System.out.println("MQTT IS RUNNING");
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }


    public void clientPublish(String message){
        try {
            client.publish(Config.mqttTopic,new MqttMessage(message.getBytes()));
            mainActivity.setText("MQTT/Message sent!\n", Color.rgb(205,108,108));
        } catch (MqttException e) {
            e.printStackTrace();
        }

    }

    @Override
    public  void messageArrived(String topic, MqttMessage message) throws Exception {
        String payload = new String(message.getPayload());
        System.out.println(payload);
//        while (ServiceStates.isRunning){
//
//            System.out.println("isRunning");
//        }
        synchronized (eventService){
            //eventService.setArrivedEvent( ServiceStates.searchEventByTopic(mainActivity.getComponent().getEventList(),topic));
          //  eventService.getArrivedQueueEvents().add(ServiceStates.searchEventByTopic(mainActivity.getComponent().getEventList(),topic));

//            ListIterator<Event> eventListIterator = eventService.getArrivedQueueEvents().listIterator();
//            eventListIterator.add(ServiceStates.searchEventByTopic(mainActivity.getComponent().getEventList(),topic));
            eventService.getArrivedQueueEvents().add(ServiceStates.searchEventByTopic(mainActivity.getComponent().getEventList(),topic));

            eventService.setChanged(true);
           // System.out.println("EVENT Arrived:"+ eventService.getArrivedEvent());
        eventService.notify();
        }



      //  Config.fos.write(payload.getBytes());
        //textView.append("\n"+payload);
        mainActivity.setText("MQTT message on topic: " + topic +" ... "+payload+"\n\n", Color.rgb(108,205,192));
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
