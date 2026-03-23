package com.example.parkinglot;

import java.util.ArrayList;
import java.util.List;

public class ParkingFloor {

    private final int floorNumber;
    private final List<ParkingSlot> slots;

    public ParkingFloor(int floorNumber) {
        this.floorNumber = floorNumber;
        this.slots = new ArrayList<>();
    }

    public int getFloorNumber() {
        return floorNumber;
    }

    public void addSlot(ParkingSlot slot) {
        slots.add(slot);
    }

    public List<ParkingSlot> getSlots() {
        return slots;
    }

    public ParkingSlot findAvailableSlot(SlotType requiredType) {
        for (ParkingSlot slot : slots) {
            if (!slot.isOccupied() && slot.getSlotType() == requiredType) {
                return slot;
            }
        }
        return null;
    }

    public int countAvailable(SlotType type) {
        int count = 0;
        for (ParkingSlot slot : slots) {
            if (!slot.isOccupied() && slot.getSlotType() == type) {
                count++;
            }
        }
        return count;
    }

    public int countTotal(SlotType type) {
        int count = 0;
        for (ParkingSlot slot : slots) {
            if (slot.getSlotType() == type) {
                count++;
            }
        }
        return count;
    }
}
