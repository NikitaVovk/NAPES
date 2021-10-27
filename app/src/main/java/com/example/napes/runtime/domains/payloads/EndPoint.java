package com.example.napes.runtime.domains.payloads;

public class EndPoint {
    String IP;
    int port;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    @Override
    public String toString() {
        return "EndPoint{" +
                "IP='" + IP + '\'' +
                ", port=" + port +
                '}';
    }
}
