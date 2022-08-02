package com.example.napes.runtime.service.servers;


import android.graphics.Color;

import com.example.napes.MainActivity;
import com.example.napes.runtime.service.payload.Colors;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
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

    @Override
    public void run() {

        while(true){
            try {
                byte[] buf = new byte[1024];

                // receive request
                realTime = System.currentTimeMillis();
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);

                realTime = System.currentTimeMillis() - realTime;
                System.out.println("REAL TIME UDP :::::::::::::: "+ realTime);
                realTime = System.currentTimeMillis();

                String dString = "OK "+new Date().toString();

                buf = dString.getBytes();

                // send the response to the client at "address" and "port"
                InetAddress address = packet.getAddress();
                int port = packet.getPort();
                System.out.println("Request from: " + address + ":" + port);
                System.out.println("Received data: "+ new String(packet.getData()));
                handler.setText("UDP/Arrived message:\n@     PAYLOAD     >>>   \n "+ new String(packet.getData())+"\n", Colors.udpColor);
                //   System.out.println(dString+" "+buf.length);
                packet = new DatagramPacket(buf, buf.length, address, port);
                socket.send(packet);

            } catch (IOException ex) {
                System.out.println(ex.toString());
            }

        }

    }



}
