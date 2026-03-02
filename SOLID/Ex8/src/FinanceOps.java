public interface FinanceOps {
    void addLedgerEntry(int amount, String source);
    int ledgerBalance();
}