package com.example.napes.clients;

import android.graphics.Color;
import android.os.Message;

import com.example.napes.MainActivity;
import com.example.napes.config.Config;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class UdpClient extends Thread{

    String dstAddress,message="";
    int dstPort;
    private boolean running;


    DatagramSocket socket;
    InetAddress address;
    MainActivity handler;

    public UdpClient(MainActivity mainActivity) {
        super();
        handler = mainActivity;
    }

    public void setParams(String message) {

        dstAddress = Config.ipAddress;
        this.message = message;
        dstPort = Config.udpPort;

    }

    public void setRunning(boolean running){
        this.running = running;
    }



    @Override
    public void run() {

        running = true;

        try {
            socket = new DatagramSocket();
            address = InetAddress.getByName(dstAddress);

            System.out.println("SENDING UDP TO :" + address+":"+dstPort);
            // send request
            byte[] buf = new byte[256];
            buf = message.getBytes();

            DatagramPacket packet =
                    new DatagramPacket(buf, buf.length, address, dstPort);

            socket.send(packet);
            buf = new byte[256];
            // get response
            packet = new DatagramPacket(buf, buf.length);
            socket.receive(packet);
            String line = new String(packet.getData(), 0, packet.getLength());

            System.out.println(line);
            handler.setText("\nUDP/Arrived:"+ line+"\n", Color.GREEN);

        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(socket != null){
                socket.close();
               // System.out.println("KONIEEC");
            }
        }

    }
}