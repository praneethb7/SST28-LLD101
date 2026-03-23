package com.example.parkinglot;

public class ParkingSlot {

    private final int slotNumber;
    private final SlotType slotType;
    private final int floorNumber;
    private boolean occupied;
    private Vehicle parkedVehicle;

    public ParkingSlot(int slotNumber, SlotType slotType, int floorNumber) {
        this.slotNumber = slotNumber;
        this.slotType = slotType;
        this.floorNumber = floorNumber;
        this.occupied = false;
    }

    public int getSlotNumber() {
        return slotNumber;
    }

    public SlotType getSlotType() {
        return slotType;
    }

    public int getFloorNumber() {
        return floorNumber;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public Vehicle getParkedVehicle() {
        return parkedVehicle;
    }

    public void parkVehicle(Vehicle vehicle) {
        this.parkedVehicle = vehicle;
        this.occupied = true;
    }

    public void removeVehicle() {
        this.parkedVehicle = null;
        this.occupied = false;
    }

    @Override
    public String toString() {
        return "Slot-" + slotNumber + " (" + slotType + ", Floor-" + floorNumber + ")";
    }
}
