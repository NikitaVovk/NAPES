package com.example.napes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Layout;
import android.text.Spannable;
import android.text.method.ScrollingMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.napes.clients.MqttCallbackImpl;
import com.example.napes.clients.StaticClients;
import com.example.napes.clients.TcpClient;
import com.example.napes.clients.UdpClient;

import com.example.napes.config.Config;
import com.example.napes.runtime.domains.component.Component;
import com.example.napes.runtime.parser.MainParser;
import com.example.napes.runtime.service.Service;

import java.io.FileNotFoundException;

public class MainActivity extends AppCompatActivity  {

    private static final int READ_REQUEST_CODE = 42;
    private static final int PERMISSION_REQUEST_STORAGE = 1000;

    private  TextView textView;
    private EditText messageText;
    private Button settingButton, sendButton, startButton;
    RadioGroup radioGroup;
    RadioButton radioButton;

    private Service service;
    private Component component;

    public Service getService() {
        return service;
    }



    public Component getComponent() {
        return component;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //request permissions
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M &&
                checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},PERMISSION_REQUEST_STORAGE);
        }

        textView = (TextView)findViewById(R.id.textViewMqtt);
        textView.setMovementMethod(new ScrollingMovementMethod());
        //MqttCallbackImpl mqttCallback = new MqttCallbackImpl(this);
        addListenerOnButton();
    }



    public void addListenerOnButton(){
        System.out.println("COMING ON MAIN");
        settingButton = (Button) findViewById(R.id.settingButton);
       // radioGroup = (RadioGroup) findViewById(R.id.radioGroup);

      //  messageText = findViewById(R.id.messageText);
        sendButton = findViewById(R.id.sendButton);
        startButton = findViewById(R.id.startSimulation);



      //  if (StaticClients.getMqttCallback()==null)
//        StaticClients.setMqttCallback(new MqttCallbackImpl(this));

        StaticClients.setUdpClient(new UdpClient(this));

        startButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                            StaticClients.setUdpClient(new UdpClient(MainActivity.this));
//                            StaticClients.getUdpClient().setParams("HELLO");
//                            StaticClients.getUdpClient().start();
                         service = new Service(component,MainActivity.this);
                        //service.serviceServerPorts();
//                        StaticClients.getMqttCallback().setParams();
                        service.serviceMain();


                    }
                }
        );

        sendButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        int radioId = radioGroup.getCheckedRadioButtonId();
//                        radioButton = findViewById(radioId);
//                        if (radioButton.getText().equals("MQTT")){
//                            StaticClients.getMqttCallback().clientPublish(messageText.getText().toString());
//                        }
//                        if (radioButton.getText().equals("UDP")){
//                            StaticClients.setUdpClient(new UdpClient(MainActivity.this));
//                            StaticClients.getUdpClient().setParams(messageText.getText().toString());
//                            StaticClients.getUdpClient().start();
//                        }
//                        if (radioButton.getText().equals("TCP")){
//                            StaticClients.setTcpClient(new TcpClient(MainActivity.this));
//                            StaticClients.getTcpClient().setParams(messageText.getText().toString());
//                            StaticClients.getTcpClient().start();
//                        }

                    performFileSearch();


                    }
                }
        );

        settingButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(".SettingActivity");

                        startActivity(intent);


                    }
                }
        );
    }
    private void performFileSearch(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");


        startActivityForResult(intent, READ_REQUEST_CODE);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == READ_REQUEST_CODE)
        {
            if (data!=null){
                System.out.println("DATAAA: "+data.getDataString());
                Uri uri = data.getData();
                String path = uri.getPath();
                System.out.println("URI+: "+ uri.getEncodedAuthority()+"   PATH:"+ path);
                path = path.substring(path.indexOf(":")+1);

              // path= path.replaceAll("/storage/emulated/0","");


              //  String file = "/sdcard/system/temp/RCR/client0.rcr";
                MainParser mainParser = null;
                System.out.println("\n\n\n\nPATH!!!: "+path);
                try {
                    mainParser = new MainParser(path);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                component = mainParser.getComponent();
                System.out.println("\n\n\n");
                System.out.println(component);

                 setText("################################" +
                         "\n#################################\n#################################\n \n             Component name : " + component.getcName()+"\n\n",0xFF000000);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == PERMISSION_REQUEST_STORAGE){
            if (grantResults[0]== PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this,"Permission granted!!!",Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this,"Permission not granted!!!",Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @SuppressLint("ShowToast")
    public void checkButton(View v){
        int radioId = radioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(radioId);
        System.out.println("SELECTED: "+ radioButton.getText());
     //   Toast.makeText(this, "Selected: "+radioButton.getText(),Toast.LENGTH_SHORT);
    }

    public  void setText(final String value, int color){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                textView.setTextColor(color);
//                textView.append(value);
//
                int start = textView.getText().length();
                textView.append("#"+value);
                int end = textView.getText().length();
                Spannable spannableText = (Spannable) textView.getText();
                spannableText.setSpan(new ForegroundColorSpan(color), start, end, 0);
                textView.append("\n");


                Layout layout = textView.getLayout();
                if (layout != null) {
                    int lineTop = layout.getLineTop(textView.getLineCount());
//                    final int scrollAmount = lineTop + textView.getPaddingTop()
//                            + textView.getPaddingBottom() - textView.getBottom() + textView.getTop();
                    final int scrollAmount = textView.getLayout().getLineTop(textView.getLineCount()) - textView.getHeight();

                    if (scrollAmount > 0) {
//                        System.out.println("GETSCROLL Y "+scrollAmount );
                        textView.setGravity(Gravity.BOTTOM);
                      //  textView.scrollBy(0, scrollAmount);
                    } else {
                        textView.scrollTo(0, 0);
                    }

                    //textView.scrollTo(textView.getScrollX(),textView.getScrollY());
                }
            }
        });
    }



}