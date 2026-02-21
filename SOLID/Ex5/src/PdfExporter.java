import java.nio.charset.StandardCharsets;

public class PdfExporter extends Exporter {

    @Override
    protected void validate(ExportRequest req) {
        if (req.body != null && req.body.length() > 20) {
            throw new IllegalArgumentException(
                    "PDF cannot handle content > 20 chars");
        }
    }

    @Override
    protected ExportResult doExport(ExportRequest req) {
        String fakePdf = "PDF(" + req.title + "):" + req.body;
        return new ExportResult(
                "application/pdf",
                fakePdf.getBytes(StandardCharsets.UTF_8)
        );
    }
}