import java.util.Map;

public class RoomPricing implements PricingComponent {

    private final Map<Integer, Double> prices = Map.of(
            LegacyRoomTypes.SINGLE, 14000.0,
            LegacyRoomTypes.DOUBLE, 15000.0,
            LegacyRoomTypes.TRIPLE, 12000.0
    );

    public Money monthlyCharge(BookingRequest req) {
        double base = prices.getOrDefault(req.roomType, 16000.0);
        return new Money(base);
    }
}