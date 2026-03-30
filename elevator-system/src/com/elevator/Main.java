package com.elevator;

public class Main {

    public static void main(String[] args) {
        Building building = new Building("Tech Park Tower", 10);

        Elevator e1 = new Elevator(1, 8);
        Elevator e2 = new Elevator(2, 8);
        Elevator e3 = new Elevator(3, 6);

        building.addElevator(e1);
        building.addElevator(e2);
        building.addElevator(e3);

        System.out.println("=== INITIAL STATE ===");
        building.status();

        System.out.println("\n--- Person at floor 3 wants to go UP ---");
        Elevator assigned1 = building.callElevator(3, Direction.UP);
        building.simulate();
        if (assigned1 != null) {
            assigned1.addPassengers(1);
            building.pressFloorButton(assigned1, 7);
            building.simulate();
        }
        building.status();

        System.out.println("\n--- Person at floor 5 wants to go DOWN ---");
        Elevator assigned2 = building.callElevator(5, Direction.DOWN);
        building.simulate();
        if (assigned2 != null) {
            assigned2.addPassengers(2);
            building.pressFloorButton(assigned2, 1);
            building.simulate();
        }
        building.status();

        System.out.println("\n--- Person at floor 0 wants to go UP ---");
        Elevator assigned3 = building.callElevator(0, Direction.UP);
        building.simulate();
        if (assigned3 != null) {
            assigned3.addPassengers(3);
            building.pressFloorButton(assigned3, 9);
            building.simulate();
        }
        building.status();

        System.out.println("\n--- Multiple requests at once ---");
        Elevator a = building.callElevator(2, Direction.UP);
        Elevator b = building.callElevator(8, Direction.DOWN);

        building.simulate();

        if (a != null) {
            a.addPassengers(1);
            building.pressFloorButton(a, 6);
        }
        if (b != null) {
            b.addPassengers(1);
            building.pressFloorButton(b, 0);
        }

        building.simulate();
        building.status();

        System.out.println("\n--- Person at floor 4 going to 4 (same floor) ---");
        Elevator same = building.callElevator(4, Direction.UP);
        if (same != null) {
            building.simulate();
            same.addPassengers(1);
            building.pressFloorButton(same, 4);
            same.removePassengers(1);
        }
        building.status();

        System.out.println("\n--- Testing elevator capacity ---");
        Elevator cap = building.callElevator(0, Direction.UP);
        if (cap != null) {
            building.simulate();
            cap.addPassengers(cap.getCapacity());
            System.out.println("Elevator full? " + cap.isFull());
            building.pressFloorButton(cap, 5);
            building.simulate();
            cap.removePassengers(cap.getCapacity());
        }
        building.status();
    }
}
