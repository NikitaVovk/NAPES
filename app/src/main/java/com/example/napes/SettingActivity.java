package com.example.napes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.napes.clients.MqttCallbackImpl;
import com.example.napes.clients.StaticClients;
import com.example.napes.clients.UdpClient;
import com.example.napes.config.Config;

public class SettingActivity extends AppCompatActivity {

    private Button connectButton;
    private EditText ipText, mqttPortText, mqttTopicText, udpPortText,tcpPortText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        addListenerOnButton();
        if (!Config.ipAddress.isEmpty())
            ipText.setText(Config.ipAddress);

        if (Config.mqttPort!=-1)
            mqttPortText.setText(String.valueOf(Config.mqttPort));
        if (!Config.mqttTopic.isEmpty())
            mqttTopicText.setText(Config.mqttTopic);

        if (Config.udpPort!=-1)
            udpPortText.setText(String.valueOf(Config.udpPort));

        if (Config.tcpPort!=-1)
            tcpPortText.setText(String.valueOf(Config.tcpPort));

    }

    public void addListenerOnButton(){
        connectButton = (Button) findViewById(R.id.buttonConnect);
        ipText = (EditText) findViewById(R.id.ipAddressText);
        mqttPortText = (EditText) findViewById(R.id.mqttPortText);
        mqttTopicText = (EditText) findViewById(R.id.mqttTopicText);
        udpPortText =(EditText) findViewById(R.id.udpPortText);
        tcpPortText =(EditText) findViewById(R.id.tcpPortText);



        connectButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Config.ipAddress = ipText.getText().toString();

                        Config.mqttPort =Integer.parseInt( mqttPortText.getText().toString() );
                        Config.mqttTopic = mqttTopicText.getText().toString();

                        Config.udpPort = Integer.parseInt( udpPortText.getText().toString() );

                        Config.tcpPort = Integer.parseInt( tcpPortText.getText().toString() );

                        StaticClients.getMqttCallback().setParams();

                       // StaticClients.getUdpClient().setParams("Test message");
                       // StaticClients.getUdpClient().start();
                    }
                }
        );
    }
}