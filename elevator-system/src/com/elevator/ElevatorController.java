package com.elevator;

import java.util.ArrayList;
import java.util.List;

public class ElevatorController {

    private List<Elevator> elevators;

    public ElevatorController() {
        this.elevators = new ArrayList<>();
    }

    public void addElevator(Elevator e) {
        elevators.add(e);
    }

    public Elevator requestElevator(int fromFloor, Direction dir) {
        Elevator best = null;
        int bestScore = Integer.MAX_VALUE;

        for (Elevator e : elevators) {
            if (e.isFull()) continue;

            int dist = Math.abs(e.getCurrentFloor() - fromFloor);
            int score = dist;

            if (e.getDirection() == Direction.IDLE) {
                score = dist;
            } else if (e.getDirection() == dir) {
                if (dir == Direction.UP && e.getCurrentFloor() <= fromFloor) {
                    score = dist;
                } else if (dir == Direction.DOWN && e.getCurrentFloor() >= fromFloor) {
                    score = dist;
                } else {
                    score = dist + 10;
                }
            } else {
                score = dist + 20;
            }

            if (score < bestScore) {
                bestScore = score;
                best = e;
            }
        }

        if (best != null) {
            best.addStop(fromFloor);
            System.out.println("assigned " + best + " for floor " + fromFloor + " going " + dir);
        } else {
            System.out.println("no elevator available for floor " + fromFloor);
        }

        return best;
    }

    public void selectFloor(Elevator elevator, int destinationFloor) {
        elevator.addStop(destinationFloor);
        System.out.println("Elevator-" + elevator.getId() + " will go to floor " + destinationFloor);
    }

    public void stepAll() {
        for (Elevator e : elevators) {
            if (e.hasStops()) {
                e.step();
            }
        }
    }

    public void runUntilDone() {
        int maxIterations = 200;
        int i = 0;
        while (i < maxIterations) {
            boolean anyActive = false;
            for (Elevator e : elevators) {
                if (e.hasStops()) {
                    anyActive = true;
                    break;
                }
            }
            if (!anyActive) break;
            stepAll();
            i++;
        }
    }

    public void printStatus() {
        System.out.println("--- Elevator Status ---");
        for (Elevator e : elevators) {
            System.out.println("  " + e);
        }
    }

    public List<Elevator> getElevators() {
        return elevators;
    }
}
