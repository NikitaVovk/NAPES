package com.example.napes.clients;

public class StaticClients {
    private static MqttCallbackImpl mqttCallback;
    private static UdpClient udpClient;
    private static TcpClient tcpClient;


    public static MqttCallbackImpl getMqttCallback() {
        return mqttCallback;
    }

    public static void setMqttCallback(MqttCallbackImpl mqttCallback) {
        StaticClients.mqttCallback = mqttCallback;
    }

    public static UdpClient getUdpClient() {
        return udpClient;
    }

    public static void setUdpClient(UdpClient udpClient) {
        StaticClients.udpClient = udpClient;
    }

    public static TcpClient getTcpClient() {
        return tcpClient;
    }

    public static void setTcpClient(TcpClient tcpClient) {
        StaticClients.tcpClient = tcpClient;
    }
}
