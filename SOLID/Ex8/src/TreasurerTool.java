public class TreasurerTool
        implements ClubAdminTools, FinanceOps {

    private int balance = 0;

    @Override
    public void addLedgerEntry(int amount, String source) {
        balance += amount;
        System.out.println("Ledger: +" + amount + " (" + source + ")");
    }

    @Override
    public int ledgerBalance() {
        return balance;
    }
}