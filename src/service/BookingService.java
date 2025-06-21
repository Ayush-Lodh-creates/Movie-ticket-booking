package service;

import entity.Booking;
import entity.Show;
import utils.lock_provider.ISeatLockProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

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


}
