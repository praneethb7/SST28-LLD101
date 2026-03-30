# Elevator System

Low-Level Design assignment implementing an elevator system in Java.

## How to Run

```bash
cd src
javac com/elevator/*.java
java com.elevator.Main
```

## Class Diagram

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                                 Building                                   │
│─────────────────────────────────────────────────────────────────────────────│
│ - name: String                                                             │
│ - totalFloors: int                                                         │
│ - controller: ElevatorController                                           │
│─────────────────────────────────────────────────────────────────────────────│
│ + addElevator(e: Elevator): void                                           │
│ + callElevator(fromFloor, dir): Elevator                                   │
│ + pressFloorButton(e, floor): void                                         │
│ + simulate(): void                                                         │
│ + status(): void                                                           │
└──────────────────────────────────┬──────────────────────────────────────────┘
                                   │ 1
                                   ▼
┌─────────────────────────────────────────────────────────────────────────────┐
│                            ElevatorController                              │
│─────────────────────────────────────────────────────────────────────────────│
│ - elevators: List<Elevator>                                                │
│─────────────────────────────────────────────────────────────────────────────│
│ + addElevator(e: Elevator): void                                           │
│ + requestElevator(fromFloor, dir): Elevator                                │
│ + selectFloor(elevator, floor): void                                       │
│ + stepAll(): void                                                          │
│ + runUntilDone(): void                                                     │
│ + printStatus(): void                                                      │
└──────────────────────────────────┬──────────────────────────────────────────┘
                                   │ 1..*
                                   ▼
┌─────────────────────────────────────────────────────────────────────────────┐
│                                Elevator                                    │
│─────────────────────────────────────────────────────────────────────────────│
│ - id: int                                                                  │
│ - currentFloor: int                                                        │
│ - direction: Direction                                                     │
│ - upStops: TreeSet<Integer>                                                │
│ - downStops: TreeSet<Integer>                                              │
│ - capacity: int                                                            │
│ - currentLoad: int                                                         │
│─────────────────────────────────────────────────────────────────────────────│
│ + addStop(floor): void                                                     │
│ + addPassengers(count): void                                               │
│ + removePassengers(count): void                                            │
│ + step(): void                                                             │
│ + isFull(): boolean                                                        │
│ + hasStops(): boolean                                                      │
└─────────────────────────────────────────────────────────────────────────────┘

┌────────────────────┐   ┌────────────────────────────┐
│     Direction       │   │          Request            │
│────────────────────│   │────────────────────────────│
│  UP                 │   │ - floor: int                │
│  DOWN               │   │ - direction: Direction      │
│  IDLE               │   │────────────────────────────│
└────────────────────┘   │ + getFloor(): int            │
                          │ + getDirection(): Direction  │
                          └────────────────────────────┘
```

## Design & Approach

### SCAN Algorithm (Elevator Algorithm)
- Each elevator maintains two `TreeSet`s — one for upward stops and one for downward stops.
- The elevator serves all stops in the current direction before reversing.
- This is similar to the disk scheduling SCAN algorithm.

### Dispatcher Strategy
- The controller picks the best elevator using a scoring system:
  - **IDLE elevator**: score = distance to requested floor
  - **Same direction & on the way**: score = distance (best case)
  - **Same direction but already passed**: score = distance + 10 (penalty)
  - **Opposite direction**: score = distance + 20 (bigger penalty)
- Full elevators are skipped entirely.

### Capacity Tracking
- Each elevator has a max capacity and tracks current passenger load.
- `isFull()` prevents the controller from assigning more passengers.

## Classes

| Class | Responsibility |
|---|---|
| `Building` | Top-level facade — floors, elevators, controller |
| `ElevatorController` | Dispatches requests to the best elevator |
| `Elevator` | Single elevator — movement, stops, capacity |
| `Request` | Represents a floor call with direction |
| `Direction` | Enum: UP, DOWN, IDLE |
