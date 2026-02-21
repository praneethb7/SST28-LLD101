public interface InvoiceRepository {
    void save(String id, String text);
    int countLines(String id);
}