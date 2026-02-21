import java.util.*;

public class OnboardingService {

    private final RawInputParser parser;
    private final StudentValidator validator;
    private final StudentRepository repo;
    private final Printer print;

    public OnboardingService(RawInputParser parser, StudentValidator validator, StudentRepository repo, Printer print) {
        this.parser = parser;
        this.validator = validator;
        this.repo = repo;
        this.print = print;
    }

    // fixed
    public void registerFromRawInput(String raw) {

        print.printInput(raw);

        Map<String,String> kv = parser.parse(raw);

        String name = kv.getOrDefault("name", "");
        String email = kv.getOrDefault("email", "");
        String phone = kv.getOrDefault("phone", "");
        String program = kv.getOrDefault("program", "");

        // validation fixed, printing fixed
       ValidationResult result = validator.validate(name,email,phone,program);

       if(!result.isValid()){
           print.printErrors(result);
           return;
       }

        String id = IdUtil.nextStudentId(repo.count());
        StudentRecord rec = new StudentRecord(id, name, email, phone, program);

        repo.save(rec);

        print.printSuccess(rec, repo.count());
    }
}
