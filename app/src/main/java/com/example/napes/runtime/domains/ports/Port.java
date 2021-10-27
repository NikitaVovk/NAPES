package com.example.napes.runtime.domains.ports;


import com.example.napes.runtime.domains.payloads.EndPoint;

public class Port {
    String pName;
    String pType;
    String pTransport;
    EndPoint endPointHere;
    ClientInfo clientInfo;

    public String getpType() {
        return pType;
    }

    public void setpType(String pType) {
        this.pType = pType;
    }

    public String getpTransport() {
        return pTransport;
    }

    public void setpTransport(String pTransport) {
        this.pTransport = pTransport;
    }

    public EndPoint getEndPointHere() {
        return endPointHere;
    }

    public void setEndPointHere(EndPoint endPointHere) {
        this.endPointHere = endPointHere;
    }

    public ClientInfo getClientInfo() {
        return clientInfo;
    }

    public void setClientInfo(ClientInfo clientInfo) {
        this.clientInfo = clientInfo;
    }

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

    @Override
    public String toString() {
        return "\n\tPort{" +
                "pName='" + pName + '\'' +
                ", pType='" + pType + '\'' +
                ", pTransport='" + pTransport + '\'' +
                ", endPointHere=" + endPointHere +
                ", clientInfo=" + clientInfo +
                '}';
    }
}
