import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CafeteriaSystem {

    private final Map<String, MenuItem> menu = new LinkedHashMap<>();
    private final PricingCalculator pricing;
    private final TaxPolicy taxPolicy;
    private final DiscountPolicy discountPolicy;
    private final InvoiceBuilder builder;
    private final InvoiceRepository repo;

    private int invoiceSeq = 1000;

    public CafeteriaSystem(
            PricingCalculator pricing,
            TaxPolicy taxPolicy,
            DiscountPolicy discountPolicy,
            InvoiceBuilder builder,
            InvoiceRepository repo) {

        this.pricing = pricing;
        this.taxPolicy = taxPolicy;
        this.discountPolicy = discountPolicy;
        this.builder = builder;
        this.repo = repo;
    }

    public void addToMenu(MenuItem i) {
        menu.put(i.id, i);
    }

    public void checkout(String customerType, List<OrderLine> lines) {

        String invId = "INV-" + (++invoiceSeq);

        double subtotal = pricing.subtotal(menu, lines);
        double taxPct = taxPolicy.taxPercent(customerType);
        double tax = subtotal * (taxPct / 100.0);
        double discount =
                discountPolicy.discount(customerType, subtotal, lines.size());

        double total = subtotal + tax - discount;

        String printable = builder.build(
                invId, menu, lines,
                subtotal, taxPct, tax, discount, total);

        System.out.print(printable);

        repo.save(invId, printable);

        System.out.println(
                "Saved invoice: " + invId +
                        " (lines=" + repo.countLines(invId) + ")");
    }
}