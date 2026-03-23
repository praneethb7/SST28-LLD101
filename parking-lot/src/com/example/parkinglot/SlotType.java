package com.example.parkinglot;

public enum SlotType {
    SMALL(10),
    MEDIUM(20),
    LARGE(40);

    private final int hourlyRate;

    SlotType(int hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public int getHourlyRate() {
        return hourlyRate;
    }
}
