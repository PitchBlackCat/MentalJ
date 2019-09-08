package nl.awesome;

import nl.awesome.neural.Network;
import nl.awesome.neural.neuron.Neuron;
import nl.awesome.neural.NeuronType;

import java.util.List;
import java.util.stream.Collectors;

public class UpdateNetworkJob implements Runnable {

    private int id;
    private Network network;
    private String[] parts;

    public UpdateNetworkJob(String[] parts, int id, Network network) {
        this.parts = parts;
        this.id = id;
        this.network = network;
    }

    @Override
    public void run() {
        List<Neuron> inputs = network.GetNeuronsOf(NeuronType.I).collect(Collectors.toList());

        for (int i = 2; i < parts.length; i++) {
            inputs.get(i - 2).setValue(Float.parseFloat(parts[i]));
        }

        String output = network
                .GetNeuronsOf(NeuronType.O)
                .map(Neuron::getValue)
                .map(v -> String.format("%f", (v*2)-1))
                .reduce("", (a, b) -> a + " " + b);

        Main.client.send("UPDATE " + id + output);
    }
}
