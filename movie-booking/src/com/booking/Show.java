package com.booking;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class Show {

    private String showId;
    private Movie movie;
    private Screen screen;
    private LocalDateTime startTime;
    private Map<String, SeatStatus> seatStatusMap;

    public Show(String showId, Movie movie, Screen screen, LocalDateTime startTime) {
        this.showId = showId;
        this.movie = movie;
        this.screen = screen;
        this.startTime = startTime;
        this.seatStatusMap = new HashMap<>();
        for (Seat s : screen.getSeats()) {
            seatStatusMap.put(s.getSeatId(), SeatStatus.AVAILABLE);
        }
    }

    public String getShowId() { return showId; }
    public Movie getMovie() { return movie; }
    public Screen getScreen() { return screen; }
    public LocalDateTime getStartTime() { return startTime; }

    public boolean isSeatAvailable(String seatId) {
        SeatStatus st = seatStatusMap.get(seatId);
        return st != null && st == SeatStatus.AVAILABLE;
    }

    public boolean lockSeat(String seatId) {
        if (!isSeatAvailable(seatId)) return false;
        seatStatusMap.put(seatId, SeatStatus.LOCKED);
        return true;
    }

    public void confirmSeat(String seatId) {
        seatStatusMap.put(seatId, SeatStatus.BOOKED);
    }

    public void releaseSeat(String seatId) {
        seatStatusMap.put(seatId, SeatStatus.AVAILABLE);
    }

    public SeatStatus getSeatStatus(String seatId) {
        return seatStatusMap.getOrDefault(seatId, null);
    }

    public int availableCount() {
        int count = 0;
        for (SeatStatus st : seatStatusMap.values()) {
            if (st == SeatStatus.AVAILABLE) count++;
        }
        return count;
    }

    @Override
    public String toString() {
        return "Show[" + showId + "] " + movie.getTitle() + " | Screen-" + screen.getScreenNumber()
                + " | " + startTime + " | " + availableCount() + " seats free";
    }
}
