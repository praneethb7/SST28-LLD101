public class ClickOpening implements OpeningStrategy {
    @Override
    public void start() {
        System.out.println("Click: Tip out");
    }

    @Override
    public void close() {
        System.out.println("Click: Tip in");
    }
}
