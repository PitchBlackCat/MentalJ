package nl.awesome.mental.racer;

import nl.awesome.neural.compiler.CompiledNetwork;
import nl.awesome.socket.Client;

import java.util.Arrays;

import static java.util.stream.Collectors.joining;

public class CalculateAndSend implements Runnable {

    private final Client client;
    private final int id;
    private final CompiledNetwork network;
    private final String[] parts;

    public CalculateAndSend(String[] parts, int id, CompiledNetwork network, Client client) {
        this.parts = parts;
        this.id = id;
        this.network = network;
        this.client = client;
    }

    @Override
    public void run() {
        double[] d = new double[parts.length - 2];
        for (int i = 2; i < parts.length; i++) {
            d[i - 2] = Double.parseDouble(parts[i]);
        }

        client.send(String.format(
                "UPDATE %s %s",
                id,
                Arrays.stream(network.feedforward(d)).mapToObj(String::valueOf).collect(joining(" "))
        ));
    }
}
