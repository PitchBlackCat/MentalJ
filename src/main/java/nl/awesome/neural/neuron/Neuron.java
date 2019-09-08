package nl.awesome.neural.neuron;

import nl.awesome.neural.Gene;
import nl.awesome.neural.NeuronType;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.List;

public abstract class Neuron {
    public final List<Gene> In = new ArrayList<Gene>();
    private NeuronType Type;
    private int Innovation;
    private int layer;
    private int order;

    public Neuron(NeuronType type) {
        this.setType(type);
    }

    public static double Sigmoid(double d) {
        return 1 / (1 + Math.exp(-d));
    }

    public int getLayer() {
        return layer;
    }

    public void setLayer(int layer) {
        this.layer = layer;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public double getValue() {
        return Neuron.Sigmoid(In.stream()
                .filter(gene -> gene.Enabled)
                .map(Gene::getValue)
                .reduce(0.0, (a, b) -> a + b));
    }

    public void setValue(double value) {
        throw new NotImplementedException();
    }

    public boolean hasDownstreamNeuron(Neuron neuron) {
        return neuron == this || In.stream().anyMatch(g -> g.In.hasDownstreamNeuron(neuron));
    }

    public NeuronType getType() {
        return Type;
    }

    public void setType(NeuronType type) {
        Type = type;
    }

    public int getInnovation() {
        return Innovation;
    }

    public void setInnovation(int innovation) {
        Innovation = innovation;
    }
}

