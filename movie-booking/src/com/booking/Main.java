package com.booking;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        BookingSystem system = new BookingSystem("PVR Cinemas");
        AdminService admin = new AdminService(system);
        CustomerService customer = new CustomerService(system);

        System.out.println("========== ADMIN SIDE ==========\n");

        Screen s1 = admin.createScreen(1, 1, 1, 2, 5);
        Screen s2 = admin.createScreen(2, 1, 2, 2, 6);

        System.out.println();
        Movie m1 = admin.addMovie("MOV1", "Interstellar", "Sci-Fi", 169);
        Movie m2 = admin.addMovie("MOV2", "The Dark Knight", "Action", 152);
        Movie m3 = admin.addMovie("MOV3", "Inception", "Thriller", 148);

        System.out.println();
        Show sh1 = admin.scheduleShow("SH1", m1, s1, LocalDateTime.of(2026, 3, 30, 14, 0));
        Show sh2 = admin.scheduleShow("SH2", m1, s1, LocalDateTime.of(2026, 3, 30, 18, 0));
        Show sh3 = admin.scheduleShow("SH3", m2, s2, LocalDateTime.of(2026, 3, 30, 16, 0));
        Show sh4 = admin.scheduleShow("SH4", m3, s2, LocalDateTime.of(2026, 3, 30, 20, 0));

        System.out.println("\n========== CUSTOMER SIDE ==========\n");

        User rahul = new User("U1", "Rahul", "rahul@mail.com");
        User priya = new User("U2", "Priya", "priya@mail.com");
        User amit = new User("U3", "Amit", "amit@mail.com");

        customer.browseMovies();

        System.out.println();
        List<Show> interstellarShows = customer.getShowsForMovie("Interstellar");

        System.out.println();
        customer.checkAvailability(sh1);

        System.out.println("\n--- Rahul books 2 VIP seats for Interstellar 2pm ---");
        Booking b1 = customer.bookSeats(rahul, sh1, Arrays.asList("S1-A1", "S1-A2"));

        System.out.println("\n--- Priya books 3 Premium seats for Interstellar 2pm ---");
        Booking b2 = customer.bookSeats(priya, sh1, Arrays.asList("S1-B1", "S1-B2", "S1-B3"));

        System.out.println("\n--- Amit books seats for Dark Knight ---");
        Booking b3 = customer.bookSeats(amit, sh3, Arrays.asList("S2-A1", "S2-B1", "S2-C1"));

        System.out.println("\n--- Amit also books Inception ---");
        Booking b4 = customer.bookSeats(amit, sh4, Arrays.asList("S2-A3", "S2-A4"));

        System.out.println();
        customer.checkAvailability(sh1);

        System.out.println("\n--- Amit tries to book Rahul's seat (should fail) ---");
        Booking fail = customer.bookSeats(amit, sh1, Arrays.asList("S1-A1"));

        System.out.println("\n--- Rahul cancels his booking ---");
        if (b1 != null) customer.cancelBooking(b1.getBookingId());

        System.out.println();
        customer.checkAvailability(sh1);

        System.out.println("\n--- Amit grabs the freed VIP seats ---");
        Booking b5 = customer.bookSeats(amit, sh1, Arrays.asList("S1-A1", "S1-A2"));

        System.out.println("\n--- Amit's bookings ---");
        customer.getMyBookings("U3");

        System.out.println("\n--- Interstellar 6pm show is unaffected ---");
        customer.checkAvailability(sh2);

        System.out.println("\n========== ADMIN VIEW ==========\n");

        admin.viewAllBookings();

        System.out.println();
        System.out.println("occupancy per show:");
        admin.viewShowOccupancy(sh1);
        System.out.println();
        admin.viewShowOccupancy(sh3);

        System.out.println("\n--- Admin removes a show ---");
        admin.removeShow("SH4");
        System.out.println("remaining shows:");
        for (Show s : system.getAllShows()) {
            System.out.println("  " + s);
        }
    }
}
