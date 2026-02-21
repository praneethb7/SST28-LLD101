public interface StudentRepository {
    void save(StudentRecord record);
    int count();
}
