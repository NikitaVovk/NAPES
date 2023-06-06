package com.example.napes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Layout;
import android.text.Spannable;
import android.text.method.ScrollingMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
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
import com.example.napes.runtime.payloads.FileUtils;

import com.example.napes.runtime.service.Service;
import com.example.napes.runtime.service.payload.Colors;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URISyntaxException;

public class MainActivity extends AppCompatActivity  {

    private static final int READ_REQUEST_CODE = 42;
    private static final int PERMISSION_REQUEST_STORAGE = 1000;
    private static final int CREATE_REQUEST_CODE = 40;
    private static final int OPEN_REQUEST_CODE = 41;
    private static final int SAVE_REQUEST_CODE = 42;

    private  TextView textView;
    private EditText messageText;
    private Button settingButton, sendButton, startButton;
    RadioGroup radioGroup;
    RadioButton radioButton;
    boolean textViewFlag;
    FileOutputStream fOut ;

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

        verifyStoragePermissions(this);






        textView = (TextView)findViewById(R.id.textViewMqtt);
        textView.setMovementMethod(new ScrollingMovementMethod());
        textViewFlag = true;
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

       // StaticClients.setUdpClient(new UdpClient(this,true));

        startButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                //                            StaticClients.setUdpClient(new UdpClient(MainActivity.this));
//                            StaticClients.getUdpClient().setParams("HELLO");
//                            StaticClients.getUdpClient().start();
                                service = new Service(component,MainActivity.this);
                                //service.serviceServerPorts();
//                        StaticClients.getMqttCallback().setParams();
                                service.serviceMain();


                            }
                        }).start();

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
                        saveFile();
//                        startActivity(intent);


                    }
                }
        );
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                                System.out.println("########## U  touch MY TRALALA   ##########"+ textViewFlag);

                if (textViewFlag)
                    textViewFlag = false;
                else
                    textViewFlag = true;

            }
        });
    //    textView.setOnTouchListener(new View.OnTouchListener() {
//            @SuppressLint("ClickableViewAccessibility")
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                System.out.println("########## U  touch MY TRALALA   ##########");
//
//                if (textViewFlag)
//                    textViewFlag = false;
//                else
//                    textViewFlag = true;
//                return false;
//            }
     //   });

    }
    private void performFileSearch(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");


        startActivityForResult(intent, READ_REQUEST_CODE);
        //startActivityForResult(intent, READ_REQUEST_CODE);

    }
    public void saveFile()
    {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/plain");

        startActivityForResult(intent, SAVE_REQUEST_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == READ_REQUEST_CODE)
        {
            if (data!=null){
                System.out.println("DATAAA: "+data.getDataString());
                Uri uri = data.getData();

                    String filePath = new FileUtils(getApplicationContext()).getPath(uri);
                    System.out.println("MYY NEW PATHHH: ++++++++++++++++++++++++ "+ filePath);




                String path = uri.getPath();
                System.out.println("URI+: "+ uri.getEncodedAuthority()+"   PATH:"+ path);
                path = path.substring(path.indexOf(":")+1);

              // path= path.replaceAll("/storage/emulated/0","");


              //  String file = "/sdcard/system/temp/RCR/client0.rcr";
                MainParser mainParser = null;
                System.out.println("\n\n\n\nPATH!!!: "+path);
                try {
                    mainParser = new MainParser(filePath);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                component = mainParser.getComponent();
                System.out.println("\n\n\n");
                System.out.println(component);

                 setText("################################" +
                         "\n#################################\n#################################\n \n  >  Component name : " + component.getcName()+"\n\n", Colors.localColor);
            }
        }

        if (resultCode == Activity.RESULT_OK)
        {
            if (requestCode == CREATE_REQUEST_CODE)
            {
                if (data != null) {
                    textView.setText("");
                }
            } else if (requestCode == SAVE_REQUEST_CODE) {

                if (data != null) {
                            try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput("logs.txt", Context.MODE_APPEND));
            outputStreamWriter.write("data");
            outputStreamWriter.close();
            System.out.println("######## DONE");
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }

                    // currentUri = resultData.getData();
                  //  writeFileContent(currentUri);
                }
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

                    if (scrollAmount > 0 && textViewFlag) {
//                        System.out.println("GETSCROLL Y "+scrollAmount );

                        textView.setGravity(Gravity.BOTTOM);//

                      //  textView.scrollBy(0, scrollAmount);
                    } else {
                        System.out.println("CLICK LICK  CLICK LICK!################################################");
                        textView.setGravity(Gravity.NO_GRAVITY);
                       // textView.scrollTo(0, 0);
                    }

                    //textView.scrollTo(textView.getScrollX(),textView.getScrollY());
                }
            }
        });
    }

    public  void addLog(final String value, MainActivity handler){

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                synchronized (handler) {
                    FileOutputStream fOut = null;

                    // handler.setText("UDP/Sent successfully packet:\n@     Flow     >>>     {"+ this.flow.getfType()+"}\n", Colors.udpColor);

                    System.out.println("############: DIRECTORY:" + handler.getApplicationContext().getFilesDir());
                    try {
                        // openFileInput()
                        // fOut = openFileOutput("savedData.txt",  MODE_APPEND);
                        fOut = new FileOutputStream(new File(handler.getApplicationContext().getFilesDir(), "logs.json"), true);

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    try {

                        fOut.write((value + "\n").getBytes());

                        //System.out.println("###########: !MAYBE SAVED ");
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if (fOut != null) {
                            try {
                                fOut.close();
                                //System.out.println("CLOSEEEEE");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                }
            }
        });
    }

    public  void addLogTime(final String value, MainActivity handler, String fileName){
        new Thread(new Runnable() {
      //  runOnUiThread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void run() {
              //  synchronized (handler) {
                    FileOutputStream fOut = null;

                    // handler.setText("UDP/Sent successfully packet:\n@     Flow     >>>     {"+ this.flow.getfType()+"}\n", Colors.udpColor);
                    File file = Environment.getExternalStorageDirectory();
                    System.out.println("############: DIRECTORY:" + handler.getApplicationContext().getFilesDir());
                    System.out.println("############: DIRECTORY:" +file.getAbsolutePath()+Environment.DIRECTORY_DOWNLOADS+"/time.txt" );

                    try {
                        // openFileInput()

                       // fOut = openFileOutput("savedData.txt",  MODE_APPEND);
                        fOut = new FileOutputStream(new File(handler.getApplicationContext().getFilesDir(), fileName), true); // bought samsung
                       // fOut = new FileOutputStream(new File( file.getAbsolutePath(),Environment.DIRECTORY_DOWNLOADS+"/time.txt"), true); //teplokram

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    try {

                        fOut.write((value + "\n").getBytes());

                        System.out.println("###########: !MAYBE SAVED ");
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if (fOut != null) {
                            try {
                                fOut.close();
                                System.out.println("CLOSEEEEE");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

            //   }
            }
        }).start();
    }

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    /**
     * Checks if the app has permission to write to device storage
     *
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

}