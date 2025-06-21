package service;

import entity.Booking;
import entity.Seat;
import entity.Show;
import entity.User;
import utils.lock_provider.ISeatLockProvider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class BookingService {

    private final Map<String, Booking> showBookings;
    private final ISeatLockProvider seatLockProvider;
    private final AtomicInteger bookingIdCounter = new AtomicInteger(1);

    public BookingService(ISeatLockProvider seatLockProvider) {
        this.seatLockProvider = seatLockProvider;
        this.showBookings = new ConcurrentHashMap<>();
    }

    public Booking getBookings(final String bookingId) throws Exception{
        if(!showBookings.containsKey(bookingId)) {
            throw new Exception("No Booking exists for Id : " + bookingId);
        }
        return showBookings.get(bookingId);
    }

    public List<Booking> getAllBookings(final Show show) {
        List<Booking> response = new ArrayList<>();
        for(Booking booking: showBookings.values()) {
            if(booking.getShow().equals(show)) response.add(booking);
        }
        return response;
    }

    public Booking createBooking(User user, Show show, List<Seat> seats) throws Exception {
        if(isAnySeatAlreadyBooked(show, seats)) throw new Exception("Seat already Booked");
        seatLockProvider.lockSeats(show, seats, user);
        String bookingId = String.valueOf(bookingIdCounter.getAndIncrement());
        Booking newBooking = new Booking(bookingId, show, user, seats);
        showBookings.put(bookingId, newBooking);
        return newBooking;
    }

    public List<Seat> getBookedSeats(Show show) {
        return getAllBookings(show).stream().filter(Booking::isConfirmed).map(Booking::getSeatsBooked).flatMap(Collection::stream).collect(Collectors.toList());
    }
}
