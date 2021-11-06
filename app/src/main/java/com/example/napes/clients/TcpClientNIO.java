package com.example.napes.clients;

import android.graphics.Color;
import android.os.Build;

import com.example.napes.MainActivity;
import com.example.napes.runtime.domains.flows.Flow;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class TcpClientNIO extends Thread{
    String message ;
    String dstAddress ;
    int dstPort ;
    int localPort;
    MainActivity handler;
    SocketChannel client;
    boolean isBinded;
    Flow flow;

    public void setParams(String message,Flow flow) {
        this.message = message;
        this.flow= flow;
    }

    public TcpClientNIO(String dstAddress, int dstPort, int localPort, MainActivity handler ) {
        this.dstAddress = dstAddress;
        this.dstPort = dstPort;
        this.localPort = localPort;
        this.handler = handler;
        isBinded = false;
    }
    public void connect(){
        InetSocketAddress hostAddress = new InetSocketAddress(dstAddress, dstPort);
        System.out.println("CONNECTING TO + "+hostAddress);
        try {
            client = SocketChannel.open();

            //if (client.)

           // System.out.println("CONNECTING TO + " + hostAddress);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && !isBinded) {
               // client.bind(new InetSocketAddress(56002));
                isBinded = true;
            }

            client.connect(hostAddress);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    public void closeSocket(){
        try {
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void run(){
        ByteBuffer buffer = ByteBuffer.allocate(74);
        buffer.put(message.getBytes());
        buffer.flip();
        try {
            client.write(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //System.out.println(messages[i]);
        buffer.clear();
        handler.setText("TCP/Sent successfully packet:\n@     Flow     >>>      {"+flow.getfType()+"}\n", Color.rgb(12,75,131));
    }


}
