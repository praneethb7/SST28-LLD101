public class FountainPenWriting implements WritingStrategy {
    @Override
    public void write(String text) {
        System.out.println("Fountain Pen writing: " + text);
    }
    @Override
    public int getInkConsumption() {
        return 20;
    }
}
