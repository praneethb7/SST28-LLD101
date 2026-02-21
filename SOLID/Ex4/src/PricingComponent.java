public interface PricingComponent {
    Money monthlyCharge(BookingRequest req);
}