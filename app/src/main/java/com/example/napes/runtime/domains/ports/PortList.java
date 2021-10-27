package com.example.napes.runtime.domains.ports;

import java.util.ArrayList;

public class PortList {
    ArrayList<Port>ports;

    public PortList() {
        ports= new ArrayList<>();
    }

    public ArrayList<Port> getPorts() {
        return ports;
    }

    public void setPorts(ArrayList<Port> ports) {
        this.ports = ports;
    }

    @Override
    public String toString() {
        return "PortList{" +
                "ports=" + ports +
                '}';
    }
}
