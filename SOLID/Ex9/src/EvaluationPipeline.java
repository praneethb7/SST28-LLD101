public class EvaluationPipeline {

    private final PlagiarismService plagiarism;
    private final CodeGradingService grader;
    private final ReportWriter writer;


    public EvaluationPipeline() {
        this(
                new PlagiarismChecker(),
                new CodeGrader(),
                new ConsoleReportWriter()
        );
    }


    public EvaluationPipeline(
            PlagiarismService plagiarism,
            CodeGradingService grader,
            ConsoleReportWriter writer) {

        this.plagiarism = plagiarism;
        this.grader = grader;
        this.writer = writer;
    }

    public void evaluate(Submission s) {

        int plagScore = plagiarism.check(s);
        System.out.println("PlagiarismScore=" + plagScore);

        int codeScore = grader.grade(s);
        System.out.println("CodeScore=" + codeScore);

        int total = plagScore + codeScore;

        writer.write(s, total);

        String result = total >= 90 ? "PASS" : "FAIL";
        System.out.println(
                "FINAL: " + result + " (total=" + total + ")"
        );
    }
}