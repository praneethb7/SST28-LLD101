package com.elevator;

public class Building {

    private String name;
    private int totalFloors;
    private ElevatorController controller;

    public Building(String name, int totalFloors) {
        this.name = name;
        this.totalFloors = totalFloors;
        this.controller = new ElevatorController();
    }

    public void addElevator(Elevator e) {
        controller.addElevator(e);
    }

    public Elevator callElevator(int fromFloor, Direction dir) {
        if (fromFloor < 0 || fromFloor >= totalFloors) {
            System.out.println("invalid floor: " + fromFloor);
            return null;
        }
        return controller.requestElevator(fromFloor, dir);
    }

    public void pressFloorButton(Elevator e, int floor) {
        if (floor < 0 || floor >= totalFloors) {
            System.out.println("invalid floor: " + floor);
            return;
        }
        controller.selectFloor(e, floor);
    }

    public void simulate() {
        controller.runUntilDone();
    }

    public void status() {
        System.out.println("=== " + name + " (" + totalFloors + " floors) ===");
        controller.printStatus();
    }

    public ElevatorController getController() {
        return controller;
    }

    public int getTotalFloors() { return totalFloors; }
}
