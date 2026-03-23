public class GelPenWriting implements WritingStrategy {
    @Override
    public void write(String text) {
        System.out.println("Gel Pen writing: " + text);
    }

    @Override
    public int getInkConsumption() {
        return 10;
    }
}
