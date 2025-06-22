package utils.strategy.payment_strategy;

public class DebitCardStrategy implements PaymentStrategy{
    @Override
    public boolean processPayment() {
        return true;
    }
}
