package com.booking;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookingSystem {

    private String theaterName;
    private List<Screen> screens;
    private List<Movie> movies;
    private List<Show> shows;
    private Map<String, Booking> bookings;

    public BookingSystem(String theaterName) {
        this.theaterName = theaterName;
        this.screens = new ArrayList<>();
        this.movies = new ArrayList<>();
        this.shows = new ArrayList<>();
        this.bookings = new HashMap<>();
    }

    public void addScreen(Screen screen) {
        screens.add(screen);
    }

    public void addMovie(Movie movie) {
        movies.add(movie);
    }

    public List<Movie> getAllMovies() {
        return movies;
    }

    public void addShow(Show show) {
        shows.add(show);
    }

    public void removeShow(String showId) {
        shows.removeIf(s -> s.getShowId().equals(showId));
    }

    public List<Show> getShowsForMovie(String movieTitle) {
        List<Show> result = new ArrayList<>();
        for (Show s : shows) {
            if (s.getMovie().getTitle().equalsIgnoreCase(movieTitle)) {
                result.add(s);
            }
        }
        return result;
    }

    public List<Show> getAllShows() {
        return shows;
    }

    public Booking bookTickets(User user, Show show, List<String> seatIds) {
        List<Seat> seatsToBook = new ArrayList<>();

        for (String seatId : seatIds) {
            if (!show.isSeatAvailable(seatId)) {
                System.out.println("seat " + seatId + " not available for " + show.getShowId());
                for (Seat locked : seatsToBook) {
                    show.releaseSeat(locked.getSeatId());
                }
                return null;
            }
            show.lockSeat(seatId);
            Seat seat = show.getScreen().getSeatById(seatId);
            if (seat == null) {
                System.out.println("seat " + seatId + " doesnt exist");
                for (Seat locked : seatsToBook) {
                    show.releaseSeat(locked.getSeatId());
                }
                return null;
            }
            seatsToBook.add(seat);
        }

        for (Seat s : seatsToBook) {
            show.confirmSeat(s.getSeatId());
        }

        Booking booking = new Booking(user, show, seatsToBook);
        booking.confirm();
        bookings.put(booking.getBookingId(), booking);

        System.out.println("booked: " + booking);
        return booking;
    }

    public boolean cancelBooking(String bookingId) {
        Booking booking = bookings.get(bookingId);
        if (booking == null) {
            System.out.println("booking " + bookingId + " not found");
            return false;
        }

        for (Seat s : booking.getSeats()) {
            booking.getShow().releaseSeat(s.getSeatId());
        }

        bookings.remove(bookingId);
        System.out.println("cancelled: " + bookingId);
        return true;
    }

    public void showAvailability(Show show) {
        System.out.println(show);
        Screen scr = show.getScreen();
        for (SeatType type : SeatType.values()) {
            int free = 0;
            int total = 0;
            for (Seat s : scr.getSeats()) {
                if (s.getType() == type) {
                    total++;
                    if (show.isSeatAvailable(s.getSeatId())) free++;
                }
            }
            if (total > 0) {
                System.out.println("  " + type + " -> " + free + "/" + total + " available (Rs." + type.getPrice() + " each)");
            }
        }
    }

    public List<Booking> getBookingsForUser(String userId) {
        List<Booking> result = new ArrayList<>();
        for (Booking b : bookings.values()) {
            if (b.getUser().getUserId().equals(userId)) {
                result.add(b);
            }
        }
        return result;
    }

    public void printAllBookings() {
        System.out.println("--- All Bookings at " + theaterName + " ---");
        if (bookings.isEmpty()) {
            System.out.println("  no bookings yet");
            return;
        }
        for (Booking b : bookings.values()) {
            System.out.println("  " + b);
        }
    }
}
