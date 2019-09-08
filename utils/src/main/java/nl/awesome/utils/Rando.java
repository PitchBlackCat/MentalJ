package nl.awesome.utils;

import java.util.List;
import java.util.Random;

public class Rando {
    public static final Random random = new Random();

    public static int between(int min, int max) {
        return random.nextInt(max - min) + min;
    }

    public static <T> T fromList(List<T> items) {
        return items.get((int) Math.floor((items.size() - 0.001f) * random.nextFloat()));
    }

    public static int sign() {
        return Math.random() < .5 ? -1 : 1;
    }

    public static double max(double max) {
        return random.nextDouble() * max;
    }

    public static float max(float max) {
        return random.nextFloat() * max;
    }
}
