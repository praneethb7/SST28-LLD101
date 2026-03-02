public class DriverAllocator implements DriverAllocationService {

    @Override
    public String allocateDriver(TripRequest req) {
        return "DRV-17";
    }
}