package com.ratelimiter;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        ExternalService gateway = new PaymentGateway();

        System.out.println("========== FIXED WINDOW (5 per minute) ==========\n");

        RateLimitConfig config = RateLimitConfig.perMinute(5);
        RateLimiter fixedWindow = new FixedWindowLimiter(config);
        RateLimitedService service = new RateLimitedService(gateway, fixedWindow);

        String tenantA = "tenant-A";
        String tenantB = "tenant-B";

        System.out.println("--- tenant-A makes 7 requests needing external call ---");
        for (int i = 1; i <= 7; i++) {
            String result = service.process(tenantA, "payment-" + i, true);
            System.out.println("  req " + i + ": " + result);
        }

        System.out.println("\n--- tenant-B is independent, gets its own quota ---");
        for (int i = 1; i <= 3; i++) {
            String result = service.process(tenantB, "payment-" + i, true);
            System.out.println("  req " + i + ": " + result);
        }

        System.out.println("\n--- request that doesnt need external call (no rate check) ---");
        String internal = service.process(tenantA, "lookup-cache", false);
        System.out.println("  " + internal);

        System.out.println("\n========== SWITCHING TO SLIDING WINDOW ==========\n");

        RateLimiter slidingWindow = new SlidingWindowLimiter(RateLimitConfig.perMinute(5));
        service.setRateLimiter(slidingWindow);

        System.out.println("--- tenant-A with sliding window, 7 requests ---");
        for (int i = 1; i <= 7; i++) {
            String result = service.process(tenantA, "txn-" + i, true);
            System.out.println("  req " + i + ": " + result);
        }

        System.out.println("\n========== SLIDING WINDOW RECOVERY ==========\n");

        RateLimiter shortWindow = new SlidingWindowLimiter(new RateLimitConfig(3, 2000));
        service.setRateLimiter(shortWindow);

        System.out.println("--- 3 per 2 seconds, send 5 quickly ---");
        for (int i = 1; i <= 5; i++) {
            String result = service.process(tenantA, "fast-" + i, true);
            System.out.println("  req " + i + ": " + result);
        }

        System.out.println("\n--- wait 2.5 seconds, window slides, quota resets ---");
        Thread.sleep(2500);

        for (int i = 6; i <= 8; i++) {
            String result = service.process(tenantA, "fast-" + i, true);
            System.out.println("  req " + i + ": " + result);
        }

        System.out.println("\n========== PER-API-KEY RATE LIMITING ==========\n");

        RateLimiter perKeyLimiter = new FixedWindowLimiter(RateLimitConfig.perMinute(2));
        service.setRateLimiter(perKeyLimiter);

        String[] apiKeys = {"apikey-X", "apikey-Y", "apikey-X", "apikey-X", "apikey-Y"};
        for (int i = 0; i < apiKeys.length; i++) {
            String result = service.process(apiKeys[i], "call-" + (i + 1), true);
            System.out.println("  " + apiKeys[i] + " req: " + result);
        }
    }
}
