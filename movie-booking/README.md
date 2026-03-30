# Movie Ticket Booking System

Low-Level Design assignment implementing a movie ticket booking system in Java.

## How to Run

```bash
cd src
javac com/booking/*.java
java com.booking.Main
```

## Class Diagram

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                             BookingSystem                                  │
│─────────────────────────────────────────────────────────────────────────────│
│ - theaterName: String                                                      │
│ - screens: List<Screen>                                                    │
│ - movies: List<Movie>                                                      │
│ - shows: List<Show>                                                        │
│ - bookings: Map<String, Booking>                                           │
│─────────────────────────────────────────────────────────────────────────────│
│ + addScreen / addMovie / addShow / removeShow                              │
│ + bookTickets(user, show, seatIds): Booking                                │
│ + cancelBooking(bookingId): boolean                                        │
│ + getBookingsForUser(userId): List<Booking>                                │
│ + showAvailability(show): void                                             │
└───────────────────────────┬─────────────────────────────────────────────────┘
                            │
              ┌─────────────┴─────────────┐
              │                           │
              ▼                           ▼
┌───────────────────────┐   ┌───────────────────────────┐
│     AdminService       │   │     CustomerService        │
│───────────────────────│   │───────────────────────────│
│ - system: BookingSystem│   │ - system: BookingSystem    │
│───────────────────────│   │───────────────────────────│
│ + createScreen()       │   │ + browseMovies()           │
│ + addMovie()           │   │ + getShowsForMovie()       │
│ + scheduleShow()       │   │ + checkAvailability()      │
│ + removeShow()         │   │ + bookSeats()              │
│ + viewAllBookings()    │   │ + cancelBooking()          │
│ + viewShowOccupancy()  │   │ + getMyBookings()          │
└───────────────────────┘   └───────────────────────────┘

┌──────────────────────────────┐
│            Show               │        ┌──────────────────────────────┐
│──────────────────────────────│        │          Booking              │
│ - showId: String              │        │──────────────────────────────│
│ - movie: Movie                │        │ - bookingId: String          │
│ - screen: Screen              │        │ - user: User                 │
│ - startTime: LocalDateTime    │        │ - show: Show                 │
│ - seatStatusMap: Map          │        │ - seats: List<Seat>          │
│──────────────────────────────│        │ - totalAmount: int           │
│ + isSeatAvailable()           │        │ - confirmed: boolean         │
│ + lockSeat() / confirmSeat()  │        │──────────────────────────────│
│ + releaseSeat()               │        │ + confirm(): void            │
│ + availableCount()            │        │ + getTotalAmount(): int      │
└──────────┬───────────────────┘        └──────────────────────────────┘
           │ 1
           ▼
┌──────────────────┐    ┌──────────────┐
│      Movie        │    │    Screen     │
│──────────────────│    │──────────────│
│ - movieId         │    │ - screenNum   │
│ - title           │    │ - seats: List │
│ - genre           │    │──────────────│
│ - durationMins    │    │ + addSeat()   │
└──────────────────┘    │ + getSeatById │
                         │ + getAvail()  │
┌──────────────┐        └──────┬───────┘
│     User      │               │ 1..*
│──────────────│               ▼
│ - userId      │        ┌──────────────┐
│ - name        │        │     Seat      │
│ - email       │        │──────────────│
└──────────────┘        │ - seatId      │
                         │ - row, col    │
┌──────────────────┐    │ - type        │
│    SeatType       │    │ - status      │
│──────────────────│    └──────────────┘
│ REGULAR (Rs.150)  │
│ PREMIUM (Rs.250)  │    ┌──────────────────┐
│ VIP     (Rs.400)  │    │   SeatStatus      │
└──────────────────┘    │──────────────────│
                         │ AVAILABLE         │
                         │ BOOKED            │
                         │ LOCKED            │
                         └──────────────────┘
```

## Design & Approach

### Admin vs Customer Separation
- `AdminService` handles theater setup: creating screens, adding movies, scheduling/removing shows, viewing all bookings and occupancy.
- `CustomerService` handles the customer flow: browsing movies, viewing showtimes, checking seat availability, booking, cancelling, and viewing their own bookings.
- Both operate on the same `BookingSystem` instance underneath.

### Seat Locking
- When a user selects seats, they are first **locked** to prevent concurrent bookings.
- If any seat in the selection is unavailable, all previously locked seats in that request are released (rollback).
- After payment (simulated), seats move from LOCKED to BOOKED.

### Show-Level Seat Tracking
- Each `Show` maintains its own `seatStatusMap` independent of other shows.
- The same screen can host multiple shows, and seat availability is tracked per-show.
- Booking seats in one show does not affect another show on the same screen.

### Pricing
- Price is determined by `SeatType`, not by movie or show time.
- Total amount = sum of prices of all selected seats.

### Cancellation
- Cancelling a booking releases the seats back to AVAILABLE for that show.
- The booking is removed from the system.

## Classes

| Class | Responsibility |
|---|---|
| `BookingSystem` | Core system — manages screens, movies, shows, bookings |
| `AdminService` | Admin operations — setup screens, movies, shows |
| `CustomerService` | Customer operations — browse, book, cancel |
| `Show` | A movie screening with per-show seat tracking |
| `Screen` | Collection of seats in a theater hall |
| `Seat` | Individual seat with row, column, type |
| `Movie` | Movie details — title, genre, duration |
| `User` | Customer info — name, email |
| `Booking` | Confirmed booking with seats and total amount |
| `SeatType` | Enum: REGULAR, PREMIUM, VIP (with prices) |
| `SeatStatus` | Enum: AVAILABLE, BOOKED, LOCKED |
