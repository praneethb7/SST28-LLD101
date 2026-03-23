public class Ink {
    private String color;
    private int level;

    public Ink(String color) {
        this.color = color;
        this.level = 100;
    }

    public void consume(int amount) {
        if (level <= 0) {
            System.out.println("Ink finished");
            return;
        }
        level -= amount;
        if (level < 0) level = 0;
    }

    public void refill() {
        level = 100;
        System.out.println("Ink refilled to 100%");
    }

    public boolean isEmpty() {
        return level <= 0;
    }

    public String getColor() {
        return color;
    }

    public int getLevel() {
        return level;
    }
}
