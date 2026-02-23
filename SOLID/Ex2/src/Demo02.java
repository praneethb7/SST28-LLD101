import java.util.*;

public class Demo02 {
    public static void main(String[] args) {
        System.out.println("=== Cafeteria Billing ===");

        PricingCalculator pricing = new PricingCalculator();
        TaxPolicy taxPolicy = new DefaultTaxPolicy();
        DiscountPolicy discountPolicy = new DefaultDiscountPolicy();
        InvoiceBuilder builder = new InvoiceBuilder();

        FileStore store = new FileStore();
        InvoiceRepository repo = new FileStoreRepository(store);


        CafeteriaSystem sys = new CafeteriaSystem(pricing, taxPolicy, discountPolicy, builder, repo);

        sys.addToMenu(new MenuItem("M1", "Veg Thali", 80.00));
        sys.addToMenu(new MenuItem("C1", "Coffee", 30.00));
        sys.addToMenu(new MenuItem("S1", "Sandwich", 60.00));

        List<OrderLine> order = List.of(
                new OrderLine("M1", 2),
                new OrderLine("C1", 1)
        );

        sys.checkout("student", order);
    }
}
