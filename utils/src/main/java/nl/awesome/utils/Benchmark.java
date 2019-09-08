package nl.awesome.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Duration;
import java.time.Instant;

public class Benchmark {
    public static final Logger logger = LogManager.getLogger(Benchmark.class);

    public static void run(String label, int times, Runnable f, Runnable post) {
        logger.info("[Benchmark] {} started!", label);
        Instant start = Instant.now();
        for (int i = 0; i < times; i++) {
            f.run();
        }
        Instant end = Instant.now();

        post.run();
        logger.info("[Benchmark] {} {}x, took {}", label, times, Duration.between(start, end).toMillis());
    }

    public static void run(String label, int times, Runnable f) {
        run(label, times, f, () -> {
        });
    }
}
