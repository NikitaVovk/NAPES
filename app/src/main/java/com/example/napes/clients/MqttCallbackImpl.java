package com.example.napes.clients;

import android.graphics.Color;
import android.util.Log;

import com.example.napes.MainActivity;
import com.example.napes.config.Config;
import com.example.napes.runtime.domains.events.Event;
import com.example.napes.runtime.service.EventService;
import com.example.napes.runtime.service.Service;
import com.example.napes.runtime.service.ServiceStates;
import com.example.napes.runtime.service.payload.Colors;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.ListIterator;

public class MqttCallbackImpl implements MqttCallback {
    private static final String TAG = "TAG";
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

    public void setEventService(EventService eventService) {
        this.eventService = eventService;
    }

    public void setParams() {
        //   this.mainActivity = mainActivity;
        try {
            client = new MqttClient("tcp://" + Config.ipAddressBroker + ":" + Config.mqttPort
                    + "", MqttAsyncClient.generateClientId(), new MemoryPersistence());
            MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();

            mqttConnectOptions.setUserName("12qw34er");
            mqttConnectOptions.setPassword("12qw34er".toCharArray());

            client.setCallback(this);
            client.connect(mqttConnectOptions);
            // String topic = "test/";
            for (Event event : mainActivity.getComponent().getEventList().getEvents()) {
                if (event.getMqtt_eName() != null) {//&&!event.geteType().equals("o")){
                    System.out.println("SUBBING: " + event.getMqtt_eName());
                    client.subscribe(event.getMqtt_eName());
                }
            }

            System.out.println("EVENT SERVICE: " + eventService);


            System.out.println("MQTT IS RUNNING");
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public boolean disConnect() {
        try {

            client.disconnect();


            return true;
        } catch (MqttException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void clientPublish(String message) {
        try {
            client.publish(Config.mqttTopic, new MqttMessage(message.getBytes()));
            mainActivity.setText("MQTT/Message sent!\n", Colors.mqttColor);
        } catch (MqttException e) {
            e.printStackTrace();
        }

    }

    // przechwytywanie wchodzących wiadomości MQTT
    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        // tekst wiadomości
        String payload = new String(message.getPayload());

        // blok synchronized w tym przypadku nie pozwala na dodawanie
        // nowych zdarzeń wielu wątkom jednocześnie
        synchronized (eventService) {
            // wyszukiwanie obiektu zdarzenia za nazwą 'topic'
            // oraz jego umieszczenie do kolejki
            eventService.getArrivedQueueEvents()
                    .add(ServiceStates.searchEventByTopic(mainActivity.getComponent().getEventList(), topic));

            // zmiana wartości flagi, która wskazuje o dodaniu nowego zdarzenia do kolejki
            eventService.setChanged(true);

            // powiadomienie o konieczności przetwarzania
            // dla innych wątków, które znajdują się w stanie oczekującym
            eventService.notify();
        }


        //  Config.fos.write(payload.getBytes());
        //textView.append("\n"+payload);
        String currentTime = (Long.toString(System.currentTimeMillis()));
        String end = (Long.toString(System.currentTimeMillis() + 1000L));
        mainActivity.addLog("{\"pid\":\"Node1\",\"tid\":\"fsm1\",\"ts\":" + currentTime +
                ",\"ph\":\"n\",\"cat\":\"service_events\",\"name\":\"" + "MQTT: " + topic
                + "\",\"id\": 1,\"args\":{}},", mainActivity);
//        mainActivity.addLog("{\"pid\":\"Node1\",\"tid\":\"fsm1\",\"ts\":"+end+
//                ",\"ph\":\"e\",\"cat\":\"service_events\",\"name\":\""+"MQTT: "+topic
//                +"\",\"id\": 1,\"args\":{}},",mainActivity);
        mainActivity.setText("MQTT message:\n@     Topic   >>>   " + topic + "\n@     Payload   >>>   " + payload + "\n\n", Colors.mqttColor);
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
