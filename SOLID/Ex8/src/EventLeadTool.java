public class EventLeadTool
        implements ClubAdminTools, EventOps {

    private int events = 0;

    @Override
    public void createEvent(String name, int budget) {
        events++;
        System.out.println(
                "Event created: " + name + " (budget=" + budget + ")"
        );
    }

    @Override
    public int eventCount() {
        return events;
    }
}