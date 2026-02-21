public abstract class Exporter {


    public final ExportResult export(ExportRequest req) {

        if (req == null)
            throw new IllegalArgumentException("request cannot be null");

        validate(req);
        return doExport(req);
    }

    protected void validate(ExportRequest req) {}

    protected abstract ExportResult doExport(ExportRequest req);
}