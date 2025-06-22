package controller;

import entity.User;
import service.PaymentService;

public class PaymentController {

    private PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    public void processPayment(String bookingid, User user) throws Exception {
        paymentService.processPayment(bookingid, user);
    }
}
