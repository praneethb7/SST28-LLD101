import java.util.List;

public class CgrRule implements EligibilityRule {

    public boolean check(StudentProfile s, List<String> reasons) {
        if (s.cgr < 8.0) {
            reasons.add("CGR below 8.0");
            return false;
        }
        return true;
    }
}