package nl.awesome.utils;

import javafx.util.Pair;

import java.util.LinkedList;
import java.util.List;

public class ListUtils {
    public static <T> List<Pair<T, T>> mapToPairs(List<T> list) {
        List<Pair<T, T>> pairs = new LinkedList<>();
        list.stream().reduce((a, b) -> {
            pairs.add(new Pair<>(a, b));
            return b;
        });
        return pairs;
    }
}
