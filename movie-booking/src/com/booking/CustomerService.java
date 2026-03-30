package com.booking;

import java.util.List;

public class CustomerService {

    private BookingSystem system;

    public CustomerService(BookingSystem system) {
        this.system = system;
    }

    public List<Movie> browseMovies() {
        List<Movie> movies = system.getAllMovies();
        System.out.println("--- Now Showing ---");
        for (Movie m : movies) {
            System.out.println("  " + m);
        }
        return movies;
    }

    public List<Show> getShowsForMovie(String title) {
        List<Show> shows = system.getShowsForMovie(title);
        System.out.println("shows for \"" + title + "\":");
        for (Show s : shows) {
            System.out.println("  " + s);
        }
        return shows;
    }

    public void checkAvailability(Show show) {
        system.showAvailability(show);
    }

    public Booking bookSeats(User user, Show show, List<String> seatIds) {
        return system.bookTickets(user, show, seatIds);
    }

    public boolean cancelBooking(String bookingId) {
        return system.cancelBooking(bookingId);
    }

    public List<Booking> getMyBookings(String userId) {
        List<Booking> mine = system.getBookingsForUser(userId);
        System.out.println("bookings for " + userId + ":");
        if (mine.isEmpty()) {
            System.out.println("  none");
        }
        for (Booking b : mine) {
            System.out.println("  " + b);
        }
        return mine;
    }
}
