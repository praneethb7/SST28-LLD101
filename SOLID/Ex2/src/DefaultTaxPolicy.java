public class DefaultTaxPolicy implements TaxPolicy {
    public double taxPercent(String customerType) {
        return TaxRules.taxPercent(customerType);
    }
}