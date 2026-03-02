public class PaymentGateway implements PaymentService {

    @Override
    public String charge(double amount) {
        return "TXN-9001";
    }
}