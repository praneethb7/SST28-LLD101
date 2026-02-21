public class Main {
    public static void main(String[] args) {
        System.out.println("=== Student Onboarding ===");
        FakeDb db = new FakeDb();

        StudentRepository repo = new FakeDBRepository(db);
        RawInputParser parser = new RawInputParser();
        StudentValidator validator = new StudentValidator();
        Printer printer = new Printer();

        OnboardingService svc = new OnboardingService(parser,validator,repo,printer);

        String raw = "name=Riya;email=riya@sst.edu;phone=9876543210;program=CSE";
        svc.registerFromRawInput(raw);

        System.out.println();
        System.out.println("-- DB DUMP --");
        System.out.print(TextTable.render3(db));
    }
}
