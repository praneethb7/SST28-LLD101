package com.ratelimiter;

public class RateLimitedService {

    private ExternalService externalService;
    private RateLimiter rateLimiter;

    public RateLimitedService(ExternalService externalService, RateLimiter rateLimiter) {
        this.externalService = externalService;
        this.rateLimiter = rateLimiter;
    }

    public String process(String key, String request, boolean needsExternalCall) {
        if (!needsExternalCall) {
            return "handled internally for: " + request;
        }

        if (!rateLimiter.allowRequest(key)) {
            return "RATE_LIMITED: external call denied for key=" + key;
        }

        return externalService.call(request);
    }

    public void setRateLimiter(RateLimiter rateLimiter) {
        this.rateLimiter = rateLimiter;
    }
}
