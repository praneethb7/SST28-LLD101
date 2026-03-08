package com.example.reports;

public class ReportProxy implements Report {

    private final String reportId;
    private final String title;
    private final String classification;
    private final AccessControl accessControl = new AccessControl();


    private RealReport realReport;

    public ReportProxy(String reportId, String title, String classification) {
        this.reportId = reportId;
        this.title = title;
        this.classification = classification;
    }

    @Override
    public void display(User user) {
        if (!accessControl.canAccess(user, classification)) {
            System.out.println("[proxy] ACCESS DENIED: " + user.getName()
                    + " (role=" + user.getRole() + ") cannot view "
                    + reportId + " (classification=" + classification + ")");
            return;
        }
        if (realReport == null) {
            System.out.println("[proxy] First access — loading report " + reportId);
            realReport = new RealReport(reportId, title, classification);
            realReport.load();
        } else {
            System.out.println("[proxy] Serving cached report " + reportId);
        }
        realReport.display(user);
    }
}