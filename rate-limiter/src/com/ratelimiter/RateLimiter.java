package com.ratelimiter;

public interface RateLimiter {
    boolean allowRequest(String key);
}
