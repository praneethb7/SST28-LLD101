package com.booking;

import java.util.List;

public class Booking {

    private static int counter = 1000;

    private String bookingId;
    private User user;
    private Show show;
    private List<Seat> seats;
    private int totalAmount;
    private boolean confirmed;

    public Booking(User user, Show show, List<Seat> seats) {
        this.bookingId = "BK" + (counter++);
        this.user = user;
        this.show = show;
        this.seats = seats;
        this.totalAmount = 0;
        for (Seat s : seats) {
            totalAmount += s.getType().getPrice();
        }
        this.confirmed = false;
    }

    public String getBookingId() { return bookingId; }
    public User getUser() { return user; }
    public Show getShow() { return show; }
    public List<Seat> getSeats() { return seats; }
    public int getTotalAmount() { return totalAmount; }
    public boolean isConfirmed() { return confirmed; }

    public void confirm() {
        this.confirmed = true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Booking[").append(bookingId).append("] ");
        sb.append(user.getName()).append(" | ");
        sb.append(show.getMovie().getTitle()).append(" | ");
        sb.append("Seats: ");
        for (int i = 0; i < seats.size(); i++) {
            if (i > 0) sb.append(", ");
            sb.append(seats.get(i).getSeatId());
        }
        sb.append(" | Rs.").append(totalAmount);
        if (confirmed) sb.append(" [CONFIRMED]");
        return sb.toString();
    }
}
