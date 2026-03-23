public class Pen {
    private WritingStrategy writingStrategy;
    private OpeningStrategy openingStrategy;
    private Ink ink;

    public Pen(WritingStrategy writingStrategy,
               OpeningStrategy openingStrategy,
               Ink ink) {
        this.writingStrategy = writingStrategy;
        this.openingStrategy = openingStrategy;
        this.ink = ink;
    }

    public void start() {
        openingStrategy.start();
    }

    public void write(String text) {
        if (ink == null || ink.isEmpty()) {
            System.out.println("Cannot write. Ink empty.");
            return;
        }

        writingStrategy.write(text + " [Color: " + ink.getColor() + "]");

        int consumption = writingStrategy.getInkConsumption();
        ink.consume(consumption);

        System.out.println("Ink left: " + ink.getLevel() + "%");
    }

    public void close() {
        openingStrategy.close();
    }

    public void refill() {
        ink.refill();
    }
}
