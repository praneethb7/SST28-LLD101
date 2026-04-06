package com.ratelimiter;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SlidingWindowLimiter implements RateLimiter {

    private RateLimitConfig config;
    private Map<String, Deque<Long>> requestLogs;

    public SlidingWindowLimiter(RateLimitConfig config) {
        this.config = config;
        this.requestLogs = new ConcurrentHashMap<>();
    }

    @Override
    public synchronized boolean allowRequest(String key) {
        long now = System.currentTimeMillis();
        long windowStart = now - config.getWindowSizeMs();

        Deque<Long> log = requestLogs.get(key);
        if (log == null) {
            log = new LinkedList<>();
            requestLogs.put(key, log);
        }

        while (!log.isEmpty() && log.peekFirst() <= windowStart) {
            log.pollFirst();
        }

        if (log.size() >= config.getMaxRequests()) {
            return false;
        }
        log.addLast(now);
        return true;
    }
}
