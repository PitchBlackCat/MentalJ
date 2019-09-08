package nl.awesome.neural.neuron;

import nl.awesome.neural.NeuronType;

public class InputNeuron extends Neuron {
    double _value = 0.0;

    public InputNeuron() {
        super(NeuronType.I);
    }

    @Override
    public double getValue() {
        return _value;
    }

    @Override
    public void setValue(double value) {
        _value = value;
    }
}
