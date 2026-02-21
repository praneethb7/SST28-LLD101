import java.util.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Hostel Fee Calculator ===");
        BookingRequest req = new BookingRequest(LegacyRoomTypes.DOUBLE, List.of(AddOn.LAUNDRY, AddOn.MESS));
        FakeBookingRepo repo = new FakeBookingRepo();

        HostelFeeCalculator calc =
                new HostelFeeCalculator(
                        repo,
                        List.of(
                                new RoomPricing(),
                                new AddOnPricing()
                        )
                );

        calc.process(req);
    }
}
