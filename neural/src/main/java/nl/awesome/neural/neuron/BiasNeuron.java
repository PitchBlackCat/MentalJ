package nl.awesome.neural.neuron;

import nl.awesome.neural.NeuronType;

public class BiasNeuron extends Neuron {
    public BiasNeuron() {
        super(NeuronType.B);
    }

    @Override
    public double getValue() {
        return 1.0;
    }
}
