package com.example.napes.runtime.service.servers;

import android.graphics.Color;
import android.os.Environment;

import com.example.napes.MainActivity;
import com.example.napes.runtime.service.payload.Colors;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class TcpServerNIO extends Thread{
    private Selector selector;

    private InetSocketAddress listenAddress;
    //private final static int PORT = 5000;
    MainActivity handler;

    long realTime;


    public TcpServerNIO(MainActivity handler, int port) {
        this.handler = handler;
        listenAddress = new InetSocketAddress( port);
    }

    /**
     * Start the server
     *
     * @throws IOException
     */
    @Override
    public void run()  {
        realTime = System.currentTimeMillis();
        try{
        this.selector = Selector.open();
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        serverChannel.configureBlocking(false);

        // bind server socket channel to port
        serverChannel.socket().bind(listenAddress);
        serverChannel.register(this.selector, SelectionKey.OP_ACCEPT);

       // handler.setText("TCP server started on >> " + listenAddress, Color.RED);
        System.out.println("Server started on >> " + listenAddress);

        while (true) {
            // wait for events
            int readyCount = selector.select();
            if (readyCount == 0) {
                continue;
            }


            // process selected keys...
            Set<SelectionKey> readyKeys = selector.selectedKeys();
            Iterator iterator = readyKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey key = (SelectionKey) iterator.next();

                // Remove key from set so we don't process it twice
                iterator.remove();

                if (!key.isValid()) {
                    continue;
                }

                if (key.isAcceptable()) { // Accept client connections
                    this.accept(key);
                } else if (key.isReadable()) { // Read from client
                    this.read(key);
                } else if (key.isWritable()) {
                    // write data to client...
                }
            }
        }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    // accept client connection
    private void accept(SelectionKey key) throws IOException {
        ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
        SocketChannel channel = serverChannel.accept();
        channel.configureBlocking(false);
        Socket socket = channel.socket();
        SocketAddress remoteAddr = socket.getRemoteSocketAddress();
        System.out.println("Connected to: " + remoteAddr);
        handler.setText("TCP/Connected to: "+remoteAddr,Colors.tcpColor);

        /*
         * Register channel with selector for further IO (record it for read/write
         * operations, here we have used read operation)
         */
        channel.register(this.selector, SelectionKey.OP_READ);
    }

    // read from the socket channel
    private void read(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        int numRead = -1;
        numRead = channel.read(buffer);

        if (numRead == -1) {
            Socket socket = channel.socket();
            SocketAddress remoteAddr = socket.getRemoteSocketAddress();
            System.out.println("Connection closed by client: " + remoteAddr);
            channel.close();
            key.cancel();
            return;
        }

        byte[] data = new byte[numRead];
        System.arraycopy(buffer.array(), 0, data, 0, numRead);
        System.out.println("Got: " + new String(data));
        realTime = System.currentTimeMillis() - realTime;
        System.out.println("REAL TIME TCP :::::::::::::: "+ realTime);
        realTime = System.currentTimeMillis();
//        File file = Environment.getExternalStorageDirectory();
//        File fileToWrite= new File(file.getAbsolutePath()+"/documents", "wynik.txt");
//
//        FileOutputStream stream = new FileOutputStream(fileToWrite);
//        try {
//            stream.write("text-to-write".getBytes());
//        } finally {
//            stream.close();
//        }
        handler.setText("TCP/Arrived message:\n@     PAYLOAD     >>>     \n "+new String(data)+"\n", Colors.tcpColor);
    }
}
