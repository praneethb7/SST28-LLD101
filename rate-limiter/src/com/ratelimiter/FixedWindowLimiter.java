package com.ratelimiter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class FixedWindowLimiter implements RateLimiter {

    private RateLimitConfig config;
    private Map<String, WindowCounter> counters;

    public FixedWindowLimiter(RateLimitConfig config) {
        this.config = config;
        this.counters = new ConcurrentHashMap<>();
    }

    @Override
    public synchronized boolean allowRequest(String key) {
        long now = System.currentTimeMillis();
        long windowStart = (now / config.getWindowSizeMs()) * config.getWindowSizeMs();

        WindowCounter counter = counters.get(key);
        if (counter == null || counter.windowStart != windowStart) {
            counter = new WindowCounter(windowStart);
            counters.put(key, counter);
        }

        if (counter.count.get() >= config.getMaxRequests()) {
            return false;
        }
        counter.count.incrementAndGet();
        return true;
    }

    private static class WindowCounter {
        long windowStart;
        AtomicInteger count;

        WindowCounter(long windowStart) {
            this.windowStart = windowStart;
            this.count = new AtomicInteger(0);
        }
    }
}
