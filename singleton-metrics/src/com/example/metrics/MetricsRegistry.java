package com.example.metrics;

import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Proper Singleton:
 * - Lazy initialized
 * - Thread-safe (Double Checked Locking)
 * - Reflection protected
 * - Serialization safe
 */
public class MetricsRegistry implements Serializable {

    private static final long serialVersionUID = 1L;

    // volatile required for DCL
    private static volatile MetricsRegistry INSTANCE;

    private final Map<String, Long> counters = new HashMap<>();

    // ---- PRIVATE CONSTRUCTOR ----
    private MetricsRegistry() {
        // blocks reflection attack AFTER instance created
        if (INSTANCE != null) {
            throw new IllegalStateException(
                    "Singleton already initialized"
            );
        }
    }

    // ---- LAZY + THREAD SAFE ----
    public static MetricsRegistry getInstance() {
        if (INSTANCE == null) {
            synchronized (MetricsRegistry.class) {
                if (INSTANCE == null) {
                    INSTANCE = new MetricsRegistry();
                }
            }
        }
        return INSTANCE;
    }

    // ---- SERIALIZATION PROTECTION ----
    private Object readResolve() throws ObjectStreamException {
        return getInstance();
    }

    // ---- BUSINESS METHODS ----
    public synchronized void setCount(String key, long value) {
        counters.put(key, value);
    }

    public synchronized void increment(String key) {
        counters.put(key, getCount(key) + 1);
    }

    public synchronized long getCount(String key) {
        return counters.getOrDefault(key, 0L);
    }

    public synchronized Map<String, Long> getAll() {
        return Collections.unmodifiableMap(new HashMap<>(counters));
    }
}