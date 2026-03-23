package com.example.parkinglot;

import java.time.LocalDateTime;

public class App {

    public static void main(String[] args) {
        ParkingLot lot = new ParkingLot("City Mall Parking");

        ParkingFloor floor1 = new ParkingFloor(1);
        ParkingFloor floor2 = new ParkingFloor(2);
        ParkingFloor floor3 = new ParkingFloor(3);

        int slotId = 1;
        for (int i = 0; i < 5; i++) floor1.addSlot(new ParkingSlot(slotId++, SlotType.SMALL, 1));
        for (int i = 0; i < 3; i++) floor1.addSlot(new ParkingSlot(slotId++, SlotType.MEDIUM, 1));
        for (int i = 0; i < 2; i++) floor1.addSlot(new ParkingSlot(slotId++, SlotType.LARGE, 1));

        for (int i = 0; i < 3; i++) floor2.addSlot(new ParkingSlot(slotId++, SlotType.SMALL, 2));
        for (int i = 0; i < 5; i++) floor2.addSlot(new ParkingSlot(slotId++, SlotType.MEDIUM, 2));
        for (int i = 0; i < 2; i++) floor2.addSlot(new ParkingSlot(slotId++, SlotType.LARGE, 2));

        for (int i = 0; i < 2; i++) floor3.addSlot(new ParkingSlot(slotId++, SlotType.SMALL, 3));
        for (int i = 0; i < 3; i++) floor3.addSlot(new ParkingSlot(slotId++, SlotType.MEDIUM, 3));
        for (int i = 0; i < 4; i++) floor3.addSlot(new ParkingSlot(slotId++, SlotType.LARGE, 3));

        lot.addFloor(floor1);
        lot.addFloor(floor2);
        lot.addFloor(floor3);

        lot.addEntryGate(new EntryGate(1, 1));
        lot.addEntryGate(new EntryGate(2, 2));
        lot.addEntryGate(new EntryGate(3, 3));

        System.out.println("=== Initial Status ===");
        lot.status();

        LocalDateTime now = LocalDateTime.of(2026, 3, 23, 10, 0);

        System.out.println("\n--- Parking Vehicles ---\n");

        Vehicle bike = new Vehicle("KA-01-1234", VehicleType.TWO_WHEELER);
        ParkingTicket bikeTicket = lot.park(bike, now, SlotType.SMALL, 1);

        Vehicle car = new Vehicle("MH-02-5678", VehicleType.CAR);
        ParkingTicket carTicket = lot.park(car, now, SlotType.MEDIUM, 2);

        Vehicle bus = new Vehicle("TN-03-9012", VehicleType.BUS);
        ParkingTicket busTicket = lot.park(bus, now, SlotType.LARGE, 3);

        Vehicle bike2 = new Vehicle("KA-04-3456", VehicleType.TWO_WHEELER);
        ParkingTicket bike2Ticket = lot.park(bike2, now, SlotType.SMALL, 1);

        System.out.println("\n=== Status After Parking ===");
        lot.status();

        LocalDateTime exitTime = LocalDateTime.of(2026, 3, 23, 13, 30);

        System.out.println("\n--- Exiting Vehicles ---\n");

        if (bikeTicket != null) lot.exit(bikeTicket, exitTime);
        if (carTicket != null) lot.exit(carTicket, exitTime);
        if (busTicket != null) lot.exit(busTicket, exitTime);

        System.out.println("\n=== Status After Exits ===");
        lot.status();

        System.out.println("\n--- Bike in Medium Slot (Upgrade) ---\n");

        Vehicle bike3 = new Vehicle("DL-05-7890", VehicleType.TWO_WHEELER);
        ParkingTicket bike3Ticket = lot.park(bike3, now, SlotType.MEDIUM, 1);
        if (bike3Ticket != null) {
            lot.exit(bike3Ticket, exitTime);
        }
    }
}
