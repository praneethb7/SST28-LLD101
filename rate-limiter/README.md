# Pluggable Rate Limiter

Low-Level Design assignment implementing a pluggable rate limiting system for external resource usage in Java.

## How to Run

```bash
cd src
javac com/ratelimiter/*.java
java com.ratelimiter.Main
```

## Class Diagram

```
                    ┌────────────────────────────────────────────────┐
                    │             RateLimitedService                  │
                    │────────────────────────────────────────────────│
                    │ - externalService: ExternalService              │
                    │ - rateLimiter: RateLimiter                     │
                    │────────────────────────────────────────────────│
                    │ + process(key, request, needsExternal): String │
                    │ + setRateLimiter(rl): void                     │
                    └────────┬───────────────────────┬───────────────┘
                             │                       │
                    uses     │                       │  uses
                             ▼                       ▼
              ┌──────────────────────┐  ┌──────────────────────────────┐
              │  <<interface>>        │  │  <<interface>>                │
              │  ExternalService      │  │  RateLimiter                 │
              │──────────────────────│  │──────────────────────────────│
              │ + call(req): String   │  │ + allowRequest(key): boolean │
              └──────────┬───────────┘  └──────────────┬───────────────┘
                         │                             │
                         │ implements                  │ implements
                         ▼                             │
              ┌──────────────────────┐       ┌─────────┴──────────┐
              │   PaymentGateway     │       │                    │
              │──────────────────────│       ▼                    ▼
              │ + call(req): String  │  ┌─────────────────┐ ┌──────────────────┐
              └──────────────────────┘  │ FixedWindowLim   │ │ SlidingWindowLim  │
                                        │─────────────────│ │──────────────────│
                                        │ - config         │ │ - config          │
                                        │ - counters: Map  │ │ - requestLogs:Map │
                                        │─────────────────│ │──────────────────│
                                        │ + allowRequest() │ │ + allowRequest()  │
                                        └────────┬────────┘ └────────┬─────────┘
                                                 │                   │
                                                 │  both use         │
                                                 ▼                   ▼
                                        ┌────────────────────────────────┐
                                        │        RateLimitConfig          │
                                        │────────────────────────────────│
                                        │ - maxRequests: int              │
                                        │ - windowSizeMs: long            │
                                        │────────────────────────────────│
                                        │ + perMinute(max): Config        │
                                        │ + perHour(max): Config          │
                                        └────────────────────────────────┘

Future algorithms (same interface):
  ┌──────────────────┐  ┌──────────────────┐  ┌──────────────────┐
  │  TokenBucket      │  │  LeakyBucket     │  │  SlidingLog      │
  │──────────────────│  │──────────────────│  │──────────────────│
  │ + allowRequest()  │  │ + allowRequest() │  │ + allowRequest() │
  └──────────────────┘  └──────────────────┘  └──────────────────┘
```

## Design & Approach

### Where Rate Limiting Happens

Rate limiting is NOT on the incoming API. It guards the external resource call only.

```
Client Request
  │
  ▼
RateLimitedService.process(key, request, needsExternalCall)
  │
  ├── needsExternalCall = false?  -->  handle internally, NO rate check
  │
  └── needsExternalCall = true?
        │
        ├── rateLimiter.allowRequest(key) = true   -->  externalService.call()
        │
        └── rateLimiter.allowRequest(key) = false  -->  "RATE_LIMITED" response
```

### Rate Limiting Key

The `key` parameter is a string — the caller decides what it represents:
- `"tenant-A"` for per-tenant limiting
- `"apikey-XYZ"` for per-API-key limiting
- `"stripe"` for per-provider limiting

Each key gets its own independent quota.

### Fixed Window Counter

Divides time into fixed intervals (e.g. minute boundaries).
```
windowStart = (currentTime / windowSize) * windowSize

if current window counter < max:
    allow, increment counter
else:
    deny
```

**Pros**: Simple, low memory (one counter per key).
**Cons**: Boundary burst problem — a user can make `max` requests at the end of one window and `max` more at the start of the next, getting 2x throughput in a short span.

### Sliding Window Counter

Keeps a log of timestamps for each request per key.
```
remove all timestamps older than (now - windowSize)

if log.size() < max:
    allow, add timestamp
else:
    deny
```

**Pros**: No boundary burst issue, accurate per-key rate.
**Cons**: Higher memory — stores one timestamp per request. Cleanup cost on each call.

### Algorithm Trade-offs

| Aspect | Fixed Window | Sliding Window |
|---|---|---|
| Memory | O(1) per key | O(maxRequests) per key |
| Accuracy | Burst at boundaries | Smooth, accurate |
| Complexity | Very simple | Slightly more complex |
| Cleanup | Implicit (new window) | Explicit (trim old entries) |
| Best for | High-throughput, approximate | Strict enforcement |

### How to Switch Algorithms

The `RateLimiter` interface has one method: `allowRequest(key)`. Swapping is one line:

```java
service.setRateLimiter(new FixedWindowLimiter(config));
// or
service.setRateLimiter(new SlidingWindowLimiter(config));
```

No business logic changes. Future algorithms (TokenBucket, LeakyBucket) just implement the same interface.

### Thread Safety

Both implementations use `synchronized` on `allowRequest` and `ConcurrentHashMap` for the per-key data structures. This ensures correctness under concurrent access from multiple threads.

## Classes

| Class / Interface | Responsibility |
|---|---|
| `RateLimiter` | Interface — single method `allowRequest(key)` |
| `RateLimitConfig` | Holds max requests and window size, with factory methods |
| `FixedWindowLimiter` | Fixed time-window counter per key |
| `SlidingWindowLimiter` | Sliding timestamp log per key |
| `ExternalService` | Interface — represents a paid external resource |
| `PaymentGateway` | Sample external service implementation |
| `RateLimitedService` | Wraps external call with rate limit check |
