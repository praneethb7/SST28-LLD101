import java.util.List;

public interface EligibilityRule {
    boolean check(StudentProfile s, List<String> reasons);
}