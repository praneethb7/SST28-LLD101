public class DefaultDiscountPolicy implements DiscountPolicy {
    public double discount(String customerType,
                           double subtotal,
                           int distinctLines) {
        return DiscountRules.discountAmount(
                customerType, subtotal, distinctLines);
    }
}