public class FileStoreRepository implements InvoiceRepository {

    private final FileStore store;

    public FileStoreRepository(FileStore store) {
        this.store = store;
    }

    public void save(String id, String text) {
        store.save(id, text);
    }

    public int countLines(String id) {
        return store.countLines(id);
    }
}