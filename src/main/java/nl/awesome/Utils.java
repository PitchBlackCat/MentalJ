package nl.awesome;

import javafx.util.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Duration;
import java.time.Instant;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

public class Utils {
    public static final Logger logger = LogManager.getLogger(Main.class);


    public static <T> List<Pair<T, T>> consecutive(List<T> list) {
        List<Pair<T, T>> pairs = new LinkedList<>();
        list.stream().reduce((a, b) -> {
            pairs.add(new Pair<>(a, b));
            return b;
        });
        return pairs;
    }

    public static void benchmark(String label, int times, Runnable f, Runnable post) {
        logger.info("[Benchmark] {} started!", label);
        Instant start = Instant.now();
        for (int i = 0; i < times; i++) {
            f.run();
        }
        Instant end = Instant.now();

        post.run();
        logger.info("[Benchmark] {} {}x, took {}", label, times, Duration.between(start, end).toMillis());
    }
}
