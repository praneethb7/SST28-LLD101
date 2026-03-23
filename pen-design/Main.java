public class Main {
    public static void main(String[] args) {

        Pen pen = PenFactory.createPen(
            PenType.FOUNTAIN,
            OpeningType.CAP,
            "Black"
        );

        pen.start();

        pen.write("Hello");
        pen.write("World");

        pen.close();

        pen.refill();
        pen.write("After refill");
    }
}
