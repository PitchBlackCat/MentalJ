package nl.awesome.neural.lottery;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Lottery<T> {
    private List<LotteryTicket<T>> Tickets = new ArrayList<LotteryTicket<T>>();

    public Lottery<T> add(float chance, Consumer<T> action) {
        LotteryTicket t = new LotteryTicket();
        t.Action = action;
        t.Chance = chance;
        Tickets.add(t);
        return this;
    }

    public boolean draw(T value) {
        float multiplier = Math.max(1, Tickets.stream().map(t -> t.Chance).reduce(0f, (a, b) -> a + b));
        double randomValue = Math.random() * multiplier;
        float runningTotal = 0;

        for (LotteryTicket<T> ticket : Tickets) {
            runningTotal += ticket.Chance;
            if (randomValue < runningTotal) {
                ticket.Action.accept(value);
                return true;
            }
        }
        return false;
    }
}

