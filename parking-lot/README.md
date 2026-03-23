# Multilevel Parking Lot

Low-Level Design assignment implementing a multilevel parking lot system in Java.

## How to Run

```bash
cd src
javac com/example/parkinglot/*.java
java com.example.parkinglot.App
```

## Class Diagram

```
┌─────────────────────────────────────────────────────────────────────────────────┐
│                                  ParkingLot                                    │
│─────────────────────────────────────────────────────────────────────────────────│
│ - name: String                                                                 │
│ - floors: List<ParkingFloor>                                                   │
│ - entryGates: Map<Integer, EntryGate>                                          │
│ - activeTickets: Map<String, ParkingTicket>                                    │
│─────────────────────────────────────────────────────────────────────────────────│
│ + park(vehicle, entryTime, slotType, gateId): ParkingTicket                    │
│ + status(): String                                                             │
│ + exit(ticket, exitTime): Bill                                                 │
│ - isCompatible(vehicleType, slotType): boolean                                 │
│ - getCompatibleSlotTypes(slotType): List<SlotType>                             │
│ - getSortedFloorsByDistance(gateFloor): List<ParkingFloor>                      │
└──────────┬───────────────────────────┬───────────────────────┬──────────────────┘
           │ 1..*                      │ 1..*                  │ *
           ▼                           ▼                       ▼
┌────────────────────┐   ┌───────────────────┐   ┌─────────────────────────┐
│    ParkingFloor     │   │    EntryGate       │   │    ParkingTicket         │
│────────────────────│   │───────────────────│   │─────────────────────────│
│ - floorNumber: int  │   │ - gateId: int      │   │ - ticketId: String      │
│ - slots: List<Slot> │   │ - floorNumber: int  │   │ - vehicle: Vehicle      │
│────────────────────│   │───────────────────│   │ - slot: ParkingSlot      │
│ + findAvailableSlot │   │ + getGateId()      │   │ - entryTime: DateTime   │
│ + countAvailable()  │   │ + getFloorNumber()  │   │─────────────────────────│
│ + countTotal()      │   └───────────────────┘   │ + getTicketId()          │
└────────┬───────────┘                             │ + getVehicle()           │
         │ 1..*                                    │ + getSlot()              │
         ▼                                         │ + getEntryTime()         │
┌────────────────────┐                             └──────────┬──────────────┘
│    ParkingSlot      │                                       │
│────────────────────│                                       │ used by
│ - slotNumber: int   │                                       ▼
│ - slotType: SlotType│                             ┌─────────────────────────┐
│ - floorNumber: int  │                             │         Bill             │
│ - occupied: boolean │                             │─────────────────────────│
│ - vehicle: Vehicle  │                             │ - ticket: ParkingTicket  │
│────────────────────│                             │ - exitTime: DateTime     │
│ + parkVehicle()     │                             │ - durationHours: long    │
│ + removeVehicle()   │                             │ - totalAmount: int       │
│ + isOccupied()      │                             │─────────────────────────│
└────────────────────┘                             │ + getTotalAmount()       │
                                                    └─────────────────────────┘
┌────────────────────┐
│      Vehicle        │
│────────────────────│
│ - licensePlate: Str │   ┌────────────────┐   ┌──────────────────┐
│ - type: VehicleType │   │  VehicleType    │   │    SlotType       │
│────────────────────│   │────────────────│   │──────────────────│
│ + getLicensePlate() │   │  TWO_WHEELER   │   │  SMALL  (Rs.10/h) │
│ + getType()         │   │  CAR           │   │  MEDIUM (Rs.20/h) │
└────────────────────┘   │  BUS           │   │  LARGE  (Rs.40/h) │
                          └────────────────┘   └──────────────────┘
```

## Design & Approach

### Strategy Pattern
- Slot assignment uses nearest-available-compatible-slot strategy based on the entry gate's floor.

### Compatibility Matrix

| Vehicle Type | SMALL | MEDIUM | LARGE |
|---|---|---|---|
| TWO_WHEELER | ✅ | ✅ | ✅ |
| CAR | ❌ | ✅ | ✅ |
| BUS | ❌ | ❌ | ✅ |

### Billing
- Charges are based on **slot type**, not vehicle type.
- Duration is ceiling-rounded to the nearest hour.
- Rates: SMALL = Rs.10/h, MEDIUM = Rs.20/h, LARGE = Rs.40/h.

### Nearest Slot Assignment
- Floors are sorted by distance from the entry gate's floor.
- The first available compatible slot on the nearest floor is assigned.
- If the requested slot type is full, falls back to the next larger compatible type.

## Classes

| Class | Responsibility |
|---|---|
| `ParkingLot` | Core system — park, status, exit APIs |
| `ParkingFloor` | Floor with collection of slots |
| `ParkingSlot` | Individual slot with occupy/release |
| `ParkingTicket` | Ticket with vehicle, slot, entry time |
| `Bill` | Calculates charges on exit |
| `Vehicle` | Vehicle with license plate and type |
| `EntryGate` | Gate with ID and floor number |
| `VehicleType` | Enum: TWO_WHEELER, CAR, BUS |
| `SlotType` | Enum: SMALL, MEDIUM, LARGE (with rates) |
