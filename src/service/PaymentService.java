package service;

import entity.Booking;
import entity.User;
import utils.strategy.payment_strategy.PaymentStrategy;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PaymentService {

    Map<Booking, Integer> bookingFailures;
    private PaymentStrategy paymentStrategy;
    private BookingService bookingService;

    public PaymentService(PaymentStrategy paymentStrategy, BookingService bookingService) {
        this.bookingFailures = new ConcurrentHashMap<>();
        this.paymentStrategy = paymentStrategy;
        this.bookingService = bookingService;
    }

    public void processPaymentFailed(String booking, User user) throws Exception {
        Booking booking1 = bookingService.getBookings(booking);
        if(!booking1.getUser().equals(user)) {
            throw new Exception("Payment Failed");
        }
        if(!bookingFailures.containsKey(booking1)) {
            bookingFailures.put(booking1, 0);
        }
        Integer currentFailuresCount = bookingFailures.get(booking1);
        Integer newFailuresCount = currentFailuresCount + 1;
        bookingFailures.put(booking1, newFailuresCount);
        System.out.println("Could not process the payment for Booking with ID : " + booking);
    }

    public void processPayment(final String bookingId, final User user) throws Exception {
        if(paymentStrategy.processPayment()){
            bookingService.confirmBooking(bookingService.getBookings(bookingId), user);
        }else {
            processPaymentFailed(bookingId, user);
        }
    }
}
