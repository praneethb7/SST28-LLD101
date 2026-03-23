package com.example.parkinglot;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParkingLot {

    private final String name;
    private final List<ParkingFloor> floors;
    private final Map<Integer, EntryGate> entryGates;
    private final Map<String, ParkingTicket> activeTickets;

    public ParkingLot(String name) {
        this.name = name;
        this.floors = new ArrayList<>();
        this.entryGates = new HashMap<>();
        this.activeTickets = new HashMap<>();
    }

    public void addFloor(ParkingFloor floor) {
        floors.add(floor);
    }

    public void addEntryGate(EntryGate gate) {
        entryGates.put(gate.getGateId(), gate);
    }

    public ParkingTicket park(Vehicle vehicle, LocalDateTime entryTime,
                              SlotType requestedSlotType, int entryGateId) {
        EntryGate gate = entryGates.get(entryGateId);
        if (gate == null) {
            System.out.println("Invalid gate ID: " + entryGateId);
            return null;
        }

        if (!isCompatible(vehicle.getType(), requestedSlotType)) {
            System.out.println("Vehicle " + vehicle + " cannot fit in " + requestedSlotType + " slot");
            return null;
        }

        List<SlotType> candidates = getCompatibleSlotTypes(requestedSlotType);
        ParkingSlot bestSlot = null;
        int bestDistance = Integer.MAX_VALUE;

        for (SlotType candidate : candidates) {
            List<ParkingFloor> sorted = getSortedFloorsByDistance(gate.getFloorNumber());
            for (ParkingFloor floor : sorted) {
                ParkingSlot slot = floor.findAvailableSlot(candidate);
                if (slot != null) {
                    int distance = Math.abs(floor.getFloorNumber() - gate.getFloorNumber());
                    if (distance < bestDistance) {
                        bestDistance = distance;
                        bestSlot = slot;
                    }
                    break;
                }
            }
        }

        if (bestSlot == null) {
            System.out.println("No available slot for " + vehicle);
            return null;
        }

        bestSlot.parkVehicle(vehicle);
        ParkingTicket ticket = new ParkingTicket(vehicle, bestSlot, entryTime);
        activeTickets.put(ticket.getTicketId(), ticket);

        System.out.println("Parked: " + ticket);
        return ticket;
    }

    public String status() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== ").append(name).append(" Status ===\n");
        for (ParkingFloor floor : floors) {
            sb.append("Floor ").append(floor.getFloorNumber()).append(":\n");
            for (SlotType type : SlotType.values()) {
                int available = floor.countAvailable(type);
                int total = floor.countTotal(type);
                if (total > 0) {
                    sb.append("  ").append(type).append(": ")
                      .append(available).append("/").append(total).append(" available\n");
                }
            }
        }
        String result = sb.toString();
        System.out.print(result);
        return result;
    }

    public Bill exit(ParkingTicket ticket, LocalDateTime exitTime) {
        if (ticket == null) {
            System.out.println("Invalid ticket");
            return null;
        }

        ticket.getSlot().removeVehicle();
        activeTickets.remove(ticket.getTicketId());

        Bill bill = new Bill(ticket, exitTime);
        System.out.println("Exit: " + bill);
        return bill;
    }

    private boolean isCompatible(VehicleType vehicleType, SlotType slotType) {
        switch (vehicleType) {
            case TWO_WHEELER:
                return true;
            case CAR:
                return slotType == SlotType.MEDIUM || slotType == SlotType.LARGE;
            case BUS:
                return slotType == SlotType.LARGE;
            default:
                return false;
        }
    }

    private List<SlotType> getCompatibleSlotTypes(SlotType requested) {
        List<SlotType> types = new ArrayList<>();
        types.add(requested);
        if (requested == SlotType.SMALL) {
            types.add(SlotType.MEDIUM);
            types.add(SlotType.LARGE);
        } else if (requested == SlotType.MEDIUM) {
            types.add(SlotType.LARGE);
        }
        return types;
    }

    private List<ParkingFloor> getSortedFloorsByDistance(int gateFloor) {
        List<ParkingFloor> sorted = new ArrayList<>(floors);
        sorted.sort((a, b) -> {
            int distA = Math.abs(a.getFloorNumber() - gateFloor);
            int distB = Math.abs(b.getFloorNumber() - gateFloor);
            if (distA != distB) return distA - distB;
            return a.getFloorNumber() - b.getFloorNumber();
        });
        return sorted;
    }
}
