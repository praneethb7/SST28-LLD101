public class PenFactory {

    public static Pen createPen(PenType penType,
                                OpeningType openingType,
                                String color) {

        WritingStrategy writingStrategy = getWritingStrategy(penType);
        OpeningStrategy openingStrategy = getOpeningStrategy(openingType);
        Ink ink = new Ink(color);

        return new Pen(writingStrategy, openingStrategy, ink);
    }

    private static WritingStrategy getWritingStrategy(PenType type) {
        switch (type) {
            case BALL:
                return new BallPenWriting();
            case GEL:
                return new GelPenWriting();
            case FOUNTAIN:
                return new FountainPenWriting();
            default:
                throw new IllegalArgumentException("Invalid pen type");
        }
    }

    private static OpeningStrategy getOpeningStrategy(OpeningType type) {
        switch (type) {
            case CAP:
                return new CapOpening();
            case CLICK:
                return new ClickOpening();
            default:
                throw new IllegalArgumentException("Invalid opening type");
        }
    }
}
