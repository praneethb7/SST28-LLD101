package com.booking;

import java.time.LocalDateTime;

public class AdminService {

    private BookingSystem system;

    public AdminService(BookingSystem system) {
        this.system = system;
    }

    public Screen createScreen(int screenNumber, int vipRows, int premiumRows, int regularRows, int seatsPerRow) {
        Screen screen = new Screen(screenNumber);
        int row = 1;
        for (int r = 0; r < vipRows; r++) {
            for (int c = 1; c <= seatsPerRow; c++) {
                String id = "S" + screenNumber + "-" + (char)('A' + row - 1) + c;
                screen.addSeat(new Seat(id, row, c, SeatType.VIP));
            }
            row++;
        }
        for (int r = 0; r < premiumRows; r++) {
            for (int c = 1; c <= seatsPerRow; c++) {
                String id = "S" + screenNumber + "-" + (char)('A' + row - 1) + c;
                screen.addSeat(new Seat(id, row, c, SeatType.PREMIUM));
            }
            row++;
        }
        for (int r = 0; r < regularRows; r++) {
            for (int c = 1; c <= seatsPerRow; c++) {
                String id = "S" + screenNumber + "-" + (char)('A' + row - 1) + c;
                screen.addSeat(new Seat(id, row, c, SeatType.REGULAR));
            }
            row++;
        }
        system.addScreen(screen);
        System.out.println("admin: created Screen-" + screenNumber + " with " + screen.getSeats().size() + " seats");
        return screen;
    }

    public Movie addMovie(String movieId, String title, String genre, int duration) {
        Movie movie = new Movie(movieId, title, genre, duration);
        system.addMovie(movie);
        System.out.println("admin: added movie " + movie);
        return movie;
    }

    public Show scheduleShow(String showId, Movie movie, Screen screen, LocalDateTime time) {
        Show show = new Show(showId, movie, screen, time);
        system.addShow(show);
        System.out.println("admin: scheduled " + show);
        return show;
    }

    public void removeShow(String showId) {
        system.removeShow(showId);
        System.out.println("admin: removed show " + showId);
    }

    public void viewAllBookings() {
        system.printAllBookings();
    }

    public void viewShowOccupancy(Show show) {
        system.showAvailability(show);
    }
}
