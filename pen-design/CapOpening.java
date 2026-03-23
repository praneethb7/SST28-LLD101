public class CapOpening implements OpeningStrategy {
    @Override
    public void start() {
        System.out.println("Cap opened");
    }

    @Override
    public void close() {
        System.out.println("Cap closed");
    }
}
