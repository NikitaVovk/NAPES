package com.example.napes.clients;

import android.graphics.Color;
import android.os.Message;

import com.example.napes.MainActivity;
import com.example.napes.config.Config;
import com.example.napes.runtime.domains.flows.Flow;
import com.example.napes.runtime.service.payload.Colors;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

public class UdpClient extends Thread{

    String dstAddress,message="";
    int dstPort;
    private boolean running;
    Flow flow;
    boolean logLoader;

     static boolean payLoader = false;
    DatagramSocket socket;
    InetAddress address;
    MainActivity handler;

    public UdpClient(MainActivity mainActivity,boolean flag) {
        super();
        handler = mainActivity;
        this.logLoader = flag;
    }

    public void setParams(String message,Flow flow) {

        dstAddress = Config.ipAddress;
        this.message = message;
        dstPort = Config.udpPort;
        this.flow = flow;


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
            socket.setSoTimeout(5000);



            System.out.println("SENDING UDP TO :" + address+":"+dstPort);
            // send request

            int max =65507;
            System.out.println("rozmiar pakietu: "+flow.getfParametr());
            byte[] buf = new byte[max];//flow.getfParametr()];
            //buf = message.getBytes();




            DatagramPacket packet =
                    new DatagramPacket(buf, buf.length, address, dstPort);//dstPort);

           // System.out.println("packetlenght "+packet.getLength());
           // packet.setLength(600);
            socket.send(packet);
            long sentTimel = System.currentTimeMillis();
            String sentTime = (Long.toString(sentTimel));


            //JSON LOGS
            handler.addLog("{\"pid\":\"Node1\",\"tid\":\"packetSend\",\"ts\":"+sentTime+",\"ph\":\"E\",\"cat\":\"sequence_manager\",\"name\":\""+
                  (payLoader?"k":"k+1")
                    +"\",\"args\":{}},",handler);
            handler.addLog("{\"pid\":\"Node1\",\"tid\":\"packetSend\",\"ts\":"+sentTime+",\"ph\":\"B\",\"cat\":\"sequence_manager\",\"name\":\""+
                    (payLoader?"k+1":"k")
                    +"\",\"args\":{}},",handler);


            handler.addLogTime(sentTime,handler,"sentTimes");
//            handler.addLogTime("{\"pid\":\"Node1\",\"tid\":\"fsm1\",\"ts\":"+sentTime+
//                    ",\"ph\":\"e\",\"cat\":\"service_flows\",\"name\":\""+(payLoader?"k(n+1)":"k(n)")
//                    +"\",\"id\": 2,\"args\":{}},",handler);
//            handler.addLogTime("{\"pid\":\"Node1\",\"tid\":\"fsm1\",\"ts\":"+sentTime+
//                    ",\"ph\":\"b\",\"cat\":\"service_flows\",\"name\":\""+(payLoader?"k(n)":"k(n+1)")
//                    +"\",\"id\": 2,\"args\":{}},",handler);

            payLoader=!payLoader;
            buf = new byte[flow.getfParametr()];
            // commented 17.12.22

            // get response
            packet = new DatagramPacket(buf, buf.length);


            try {
                socket.receive(packet);

                long rtt = System.currentTimeMillis()- sentTimel;
                String rttTime = (Long.toString(rtt))+"\t"+Integer.toString(max);  //flow.getfParametr();

                handler.addLogTime(rttTime,handler,"rttTimes");
            }catch (SocketTimeoutException e){
//                System.out.println(e.getCause().getMessage());
            }

          //  String line = new String(packet.getData(), 0, packet.getLength());




//            synchronized (handler) {
//                FileOutputStream fOut = null;
//               // System.out.println(line);
//                // handler.setText("UDP/Sent successfully packet:\n@     Flow     >>>     {"+ this.flow.getfType()+"}\n", Colors.udpColor);
//
//                System.out.println("############: DIRECTORY:" + handler.getApplicationContext().getFilesDir());
//                try {
//                    // openFileInput()
//                    // fOut = openFileOutput("savedData.txt",  MODE_APPEND);
//                    fOut = new FileOutputStream(new File(handler.getApplicationContext().getFilesDir(), "logs.json"), true);
//
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                }
//
//                try {
//
//                    fOut.write((sentTime + "\n").getBytes());
//
//                    //System.out.println("###########: !MAYBE SAVED ");
//                } catch (IOException e) {
//                    e.printStackTrace();
//                } finally {
//                    if (fOut != null) {
//                        try {
//                            fOut.close();
//                            //System.out.println("CLOSEEEEE");
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//
//            }
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