package nl.awesome.mental.racer;

import nl.awesome.neural.compiler.CompiledNetwork;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Fps {
    public static final Logger logger = LogManager.getLogger(Fps.class);

    private static Map<CompiledNetwork, Data> networks = new ConcurrentHashMap<>();

    static void put(CompiledNetwork network) {
        Instant now = Instant.now();
        Data d = networks.get(network);
        if (d == null) {
            d = new Data(now, now);
        } else {
            d.prev = d.curr;
            d.curr = now;
        }
        networks.put(network, d);
    }

    static void update() {
        Instant timeout = Instant.now().minusSeconds(3);
        networks.keySet().stream()
                .filter(n -> networks.get(n).curr.isBefore(timeout))
                .forEach(n -> networks.remove(n));

        double ms = networks.keySet().stream()
                .mapToLong(n -> {
                    Data d = networks.get(n);
                    return Duration.between(d.prev, d.curr).toMillis();
                })
                .average().orElse(0);

        System.out.print(String.format("[FPS] %.1f %.0fms (%s networks)            \r", ms > 0 ? 1000 / ms : 0, ms, networks.size()));
    }

    static class Data {
        Instant prev;
        Instant curr;

        Data(Instant prev, Instant curr) {
            this.prev = prev;
            this.curr = curr;
        }
    }
}
