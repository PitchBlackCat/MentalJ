package nl.awesome.neural.lottery;

import java.util.function.Consumer;

public class LotteryTicket<T> {
    public Consumer<T> Action;
    public float Chance;
}
