package com.example.parkinglot;

import java.time.Duration;
import java.time.LocalDateTime;

public class Bill {

    private final ParkingTicket ticket;
    private final LocalDateTime exitTime;
    private final long durationHours;
    private final int totalAmount;

    public Bill(ParkingTicket ticket, LocalDateTime exitTime) {
        this.ticket = ticket;
        this.exitTime = exitTime;
        long minutes = Duration.between(ticket.getEntryTime(), exitTime).toMinutes();
        this.durationHours = (minutes + 59) / 60;
        this.totalAmount = (int) (durationHours * ticket.getSlot().getSlotType().getHourlyRate());
    }

    public ParkingTicket getTicket() {
        return ticket;
    }

    public LocalDateTime getExitTime() {
        return exitTime;
    }

    public long getDurationHours() {
        return durationHours;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    @Override
    public String toString() {
        return "Bill[" + ticket.getTicketId() + "] " + ticket.getVehicle()
                + " | Slot: " + ticket.getSlot().getSlotType()
                + " | Duration: " + durationHours + "h"
                + " | Amount: Rs." + totalAmount;
    }
}
