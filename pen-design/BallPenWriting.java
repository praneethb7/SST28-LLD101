public class BallPenWriting implements WritingStrategy {
    @Override
    public void write(String text) {
        System.out.println("Ball Pen writing: " + text);
    }

    @Override
    public int getInkConsumption() {
        return 5;
    }
}
