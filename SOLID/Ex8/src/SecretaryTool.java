public class SecretaryTool
        implements ClubAdminTools, MinutesOps {

    private int minutes = 0;

    @Override
    public void addMinutes(String text) {
        minutes++;
        System.out.println("Minutes added: \"" + text + "\"");
    }

    @Override
    public int minutesCount() {
        return minutes;
    }
}