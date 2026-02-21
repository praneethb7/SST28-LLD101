import java.util.List;

public class AttendanceRule implements EligibilityRule {

    public boolean check(StudentProfile s, List<String> reasons) {
        if (s.attendancePct < 75) {
            reasons.add("attendance below 75");
            return false;
        }
        return true;
    }
}