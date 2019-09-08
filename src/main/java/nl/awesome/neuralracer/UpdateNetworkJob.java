package nl.awesome.neuralracer;

import nl.awesome.Main;
import nl.awesome.cli.NeuralRacer;
import nl.awesome.neural.compiler.CompiledNetwork;

import java.util.Arrays;

import static java.util.stream.Collectors.joining;

public class UpdateNetworkJob implements Runnable {

    private int id;
    private CompiledNetwork network;
    private String[] parts;

    public UpdateNetworkJob(String[] parts, int id, CompiledNetwork network) {
        this.parts = parts;
        this.id = id;
        this.network = network;
    }

    @Override
    public void run() {
        double[] d = new double[parts.length - 2];
        for (int i = 2; i < parts.length; i++) {
            d[i - 2] = Double.parseDouble(parts[i]);
        }

        NeuralRacerClient.client.send(String.format(
                "UPDATE %s %s",
                id,
                Arrays.stream(network.feedforward(d)).mapToObj(String::valueOf).collect(joining(" "))
        ));
    }
}
