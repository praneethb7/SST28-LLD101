public class Printer {

    public void printInput(String raw){
        System.out.println("INPUT: "+raw);
    }

    public void printErrors(ValidationResult error){
        System.out.println("ERROR: cannot register");
        for (String e : error.getErrors()) System.out.println("- " + e);
    }

    public void printSuccess(StudentRecord rec, int total){
        System.out.println("OK: created student " + rec.getId());
        System.out.println("Saved. Total students: " + total);
        System.out.println("CONFIRMATION:");
        System.out.println(rec);
    }
}
