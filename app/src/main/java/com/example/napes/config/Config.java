package com.example.napes.config;

import java.io.FileOutputStream;

public class Config {

    public static FileOutputStream fos;

    public  static String ipAddress = "192.168.1.4";
    public  static String ipAddressTcp = "192.168.1.4";

    public  static String ipAddressBroker = "192.168.1.217";

    public  static boolean simulating = false;

    //MQTT
    public  static int mqttPort = 1883;
    public  static String mqttTopic = "test/";

    //UDP
    public  static int udpPort = 4445;

    //TCP
    public  static int tcpPort = 5000;
}
