import java.util.List;

public class CreditsRule implements EligibilityRule {

    public boolean check(StudentProfile s, List<String> reasons) {
        if (s.earnedCredits < 20) {
            reasons.add("credits below 20");
            return false;
        }
        return true;
    }
}