public class ConsoleReportWriter implements ReportWriter {

    @Override
    public void write(Submission s, int total) {
        System.out.println(
                "Report written: report-" + s.roll + ".txt"
        );
    }
}