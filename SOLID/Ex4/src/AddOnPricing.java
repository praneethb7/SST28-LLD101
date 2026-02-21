import java.util.Map;

public class AddOnPricing implements PricingComponent {

    private final Map<AddOn, Double> addOnPrices = Map.of(
            AddOn.MESS, 1000.0,
            AddOn.LAUNDRY, 500.0,
            AddOn.GYM, 300.0
    );

    public Money monthlyCharge(BookingRequest req) {

        double total = 0.0;

        for (AddOn a : req.addOns) {
            total += addOnPrices.getOrDefault(a, 0.0);
        }

        return new Money(total);
    }
}