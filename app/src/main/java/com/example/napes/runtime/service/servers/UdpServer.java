package com.example.napes.runtime.service.servers;


import android.graphics.Color;

import com.example.napes.MainActivity;
import com.example.napes.config.Config;
import com.example.napes.runtime.service.payload.Colors;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;

public class UdpServer extends Thread {
     int serverport;
    protected DatagramSocket socket = null;
    private MainActivity handler;
    //public static final String SERVERIP = "127.0.0.1";
    long realTime;


    public UdpServer(int serverport, MainActivity mainActivity)  {
        super("UDP");

        this.serverport = serverport;
        this.handler = mainActivity;
        try {
          //  InetAddress serverAddr = InetAddress.getByName(SERVERIP);
            socket = new DatagramSocket(serverport);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        System.out.println("JavaUdpServer run on: " + serverport);
    }
    ArrayList<Long> times;

    @Override
    public void run() {
        // alokacja pamięci dla przychodzącego pakietu
        byte[] buf = new byte[1024];
        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        while(Config.simulating){
            try {
                // w tym miejscu dziłanie wątku zatrzymuje sie
                // do póki serwer nie otrzyma pakiet
                socket.receive(packet);

                // zapisywanie czasu otrzymania wiadomości do listy
                times.add(System.currentTimeMillis());

                // uzyskiwanie danych klienta
                InetAddress address = packet.getAddress();
                int port = packet.getPort();

                // wyświetlanie informacji
                System.out.println("Request from: " + address + ":" + port);
                System.out.println("Received data: "+ new String(packet.getData()));
            } catch (IOException ex) {
                System.out.println(ex.toString());
            }

        }

    }



}
