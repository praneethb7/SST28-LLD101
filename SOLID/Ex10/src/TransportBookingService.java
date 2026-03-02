public class TransportBookingService {

    private final DistanceService distanceService;
    private final DriverAllocationService driverAllocator;
    private final PaymentService paymentService;

    // ✅ default wiring (keeps Demo working)
    public TransportBookingService() {
        this(
                new DistanceCalculator(),
                new DriverAllocator(),
                new PaymentGateway()
        );
    }

    // ✅ DIP constructor
    public TransportBookingService(
            DistanceService distanceService,
            DriverAllocationService driverAllocator,
            PaymentService paymentService) {

        this.distanceService = distanceService;
        this.driverAllocator = driverAllocator;
        this.paymentService = paymentService;
    }

    public void book(TripRequest req) {

        System.out.println("=== Transport Booking ===");

        double distance = distanceService.distanceKm(req);
        System.out.println("DistanceKm=" + distance);

        String driver = driverAllocator.allocateDriver(req);
        System.out.println("Driver=" + driver);


        double fare = 50.0 + distance * 6.6666666667;
        fare = Math.round(fare * 100.0) / 100.0;

        String txn = paymentService.charge(fare);
        System.out.println("Payment=PAID txn=" + txn);

        System.out.printf(
                "RECEIPT: R-501 | fare=%.2f%n",
                fare
        );
    }
}