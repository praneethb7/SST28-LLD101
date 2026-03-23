package com.example.parkinglot;

public class EntryGate {

    private final int gateId;
    private final int floorNumber;

    public EntryGate(int gateId, int floorNumber) {
        this.gateId = gateId;
        this.floorNumber = floorNumber;
    }

    public int getGateId() {
        return gateId;
    }

    public int getFloorNumber() {
        return floorNumber;
    }

    @Override
    public String toString() {
        return "Gate-" + gateId + " (Floor-" + floorNumber + ")";
    }
}
