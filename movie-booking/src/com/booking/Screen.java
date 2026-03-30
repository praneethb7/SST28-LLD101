package com.booking;

import java.util.ArrayList;
import java.util.List;

public class Screen {

    private int screenNumber;
    private List<Seat> seats;

    public Screen(int screenNumber) {
        this.screenNumber = screenNumber;
        this.seats = new ArrayList<>();
    }

    public int getScreenNumber() { return screenNumber; }
    public List<Seat> getSeats() { return seats; }

    public void addSeat(Seat seat) {
        seats.add(seat);
    }

    public Seat getSeatById(String seatId) {
        for (Seat s : seats) {
            if (s.getSeatId().equals(seatId)) return s;
        }
        return null;
    }

    public List<Seat> getAvailableSeats() {
        List<Seat> available = new ArrayList<>();
        for (Seat s : seats) {
            if (s.isAvailable()) available.add(s);
        }
        return available;
    }

    public List<Seat> getAvailableByType(SeatType type) {
        List<Seat> result = new ArrayList<>();
        for (Seat s : seats) {
            if (s.isAvailable() && s.getType() == type) result.add(s);
        }
        return result;
    }

    public void resetAllSeats() {
        for (Seat s : seats) {
            s.setStatus(SeatStatus.AVAILABLE);
        }
    }
}
