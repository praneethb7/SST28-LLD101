package com.example.metrics;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * - Uses MetricsRegistry.getInstance() to grab the global singleton instance.
 */
public class MetricsLoader {

    public MetricsRegistry loadFromFile(String path) throws IOException {
        Properties props = new Properties();
        InputStream is =
                MetricsLoader.class
                        .getClassLoader()
                        .getResourceAsStream("metrics.properties");

        if (is == null) {
            throw new IOException("metrics.properties not found in classpath");
        }

        props.load(is);

        // FIXED: We ask for the global instance instead of creating a new one.
        MetricsRegistry registry = MetricsRegistry.getInstance();

        for (String key : props.stringPropertyNames()) {
            String raw = props.getProperty(key, "0").trim();
            long v;
            try {
                v = Long.parseLong(raw);
            } catch (NumberFormatException e) {
                v = 0L;
            }
            registry.setCount(key, v);
        }
        return registry;
    }
}