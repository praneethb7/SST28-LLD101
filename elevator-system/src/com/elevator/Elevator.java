package com.elevator;

import java.util.TreeSet;

public class Elevator {

    private int id;
    private int currentFloor;
    private Direction direction;
    private TreeSet<Integer> upStops;
    private TreeSet<Integer> downStops;
    private int capacity;
    private int currentLoad;

    public Elevator(int id, int capacity) {
        this.id = id;
        this.currentFloor = 0;
        this.direction = Direction.IDLE;
        this.upStops = new TreeSet<>();
        this.downStops = new TreeSet<>();
        this.capacity = capacity;
        this.currentLoad = 0;
    }

    public int getId() { return id; }
    public int getCurrentFloor() { return currentFloor; }
    public Direction getDirection() { return direction; }
    public int getCapacity() { return capacity; }
    public int getCurrentLoad() { return currentLoad; }

    public boolean isFull() {
        return currentLoad >= capacity;
    }

    public void addStop(int floor) {
        if (floor > currentFloor) {
            upStops.add(floor);
        } else if (floor < currentFloor) {
            downStops.add(floor);
        }

        if (direction == Direction.IDLE) {
            if (floor > currentFloor) direction = Direction.UP;
            else if (floor < currentFloor) direction = Direction.DOWN;
        }
    }

    public void addPassengers(int count) {
        currentLoad = Math.min(currentLoad + count, capacity);
    }

    public void removePassengers(int count) {
        currentLoad = Math.max(currentLoad - count, 0);
    }

    public void step() {
        if (direction == Direction.UP) {
            if (!upStops.isEmpty()) {
                int nextStop = upStops.first();
                if (currentFloor < nextStop) {
                    currentFloor++;
                }
                if (currentFloor == nextStop) {
                    upStops.remove(nextStop);
                    System.out.println("  Elevator-" + id + " stopped at floor " + currentFloor);
                }
            }
            if (upStops.isEmpty()) {
                if (!downStops.isEmpty()) {
                    direction = Direction.DOWN;
                } else {
                    direction = Direction.IDLE;
                }
            }
        } else if (direction == Direction.DOWN) {
            if (!downStops.isEmpty()) {
                int nextStop = downStops.last();
                if (currentFloor > nextStop) {
                    currentFloor--;
                }
                if (currentFloor == nextStop) {
                    downStops.remove(nextStop);
                    System.out.println("  Elevator-" + id + " stopped at floor " + currentFloor);
                }
            }
            if (downStops.isEmpty()) {
                if (!upStops.isEmpty()) {
                    direction = Direction.UP;
                } else {
                    direction = Direction.IDLE;
                }
            }
        }
    }

    public boolean hasStops() {
        return !upStops.isEmpty() || !downStops.isEmpty();
    }

    public int totalStops() {
        return upStops.size() + downStops.size();
    }

    @Override
    public String toString() {
        return "Elevator-" + id + "(floor=" + currentFloor + ", dir=" + direction
                + ", load=" + currentLoad + "/" + capacity
                + ", stops=" + totalStops() + ")";
    }
}
