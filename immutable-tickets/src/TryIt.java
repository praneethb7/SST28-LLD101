import com.example.tickets.IncidentTicket;
import com.example.tickets.TicketService;

import java.util.List;

/**
 * FIXED STATE:
 * - Updates return a NEW ticket instance.
 * - Original ticket remains completely unchanged.
 * - External modifications to tags are blocked by Java.
 */
public class TryIt {

    public static void main(String[] args) {
        TicketService service = new TicketService();

        System.out.println("--- 1. Creating Initial Ticket ---");
        IncidentTicket ticketV1 = service.createTicket("TCK-1001", "reporter@example.com", "Payment failing on checkout");
        printTicket("Ticket V1", ticketV1);

        System.out.println("\n--- 2. Updating Ticket (Testing Immutability) ---");
        // FIXED: We capture the new instances returned by the service methods
        IncidentTicket ticketV2 = service.assign(ticketV1, "agent@example.com");
        IncidentTicket ticketV3 = service.escalateToCritical(ticketV2);

        System.out.println("Did the original ticket change? (It shouldn't!)");
        printTicket("Ticket V1 (Original)", ticketV1);

        System.out.println("\nHere is the final, fully updated ticket:");
        printTicket("Ticket V3 (Updated)", ticketV3);

        System.out.println("\n--- 3. Testing Defensive Copying (The Hacker Attack) ---");
        try {
            System.out.println("Attempting to secretly add a 'HACKED' tag to Ticket V3...");
            List<String> tags = ticketV3.getTags();
            tags.add("HACKED_FROM_OUTSIDE");
            System.out.println("FAIL: The list was modified! (You should never see this)");
        } catch (UnsupportedOperationException e) {
            System.out.println("SUCCESS: Blocked by UnsupportedOperationException! The tags are locked down.");
        }
    }

    // A simple helper method so we don't have to rely on a toString() method
    private static void printTicket(String label, IncidentTicket t) {
        System.out.printf("%s -> ID: %s | Priority: %s | Tags: %s | Assignee: %s%n",
                label, t.getId(), t.getPriority(), t.getTags(), t.getAssigneeEmail());
    }
}