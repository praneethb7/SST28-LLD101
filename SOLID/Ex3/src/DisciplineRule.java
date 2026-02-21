import java.util.List;

public class DisciplineRule implements EligibilityRule {

    public boolean check(StudentProfile s, List<String> reasons) {
        if (s.disciplinaryFlag != LegacyFlags.NONE) {
            reasons.add("disciplinary flag present");
            return false;
        }
        return true;
    }
}