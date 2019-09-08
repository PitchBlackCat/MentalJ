package nl.awesome.neural.factory;

import nl.awesome.neural.Gene;
import nl.awesome.neural.Network;
import nl.awesome.neural.NeuronType;
import nl.awesome.neural.Serializer;
import nl.awesome.neural.neuron.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class NetworkFactory {
    public abstract Network CreateNetwork(int inputs, int outputs);

    public abstract Network MutateNetwork(Network source, int times);

    List<Neuron> CreateLayer(NeuronType type, int layer, int count, boolean addBias) {
        ArrayList<Neuron> l = new ArrayList<>();
        int order = 0;
        for (int i = 0; i < count; i++) {
            Neuron n = CreateNeuron(type);
            n.setOrder(order++);
            l.add(n);
        }

        if (addBias) {
            Neuron n = CreateNeuron(NeuronType.B);
            n.setOrder(order);
            l.add(n);
        }

        l.forEach(n -> n.setLayer(layer));
        return l;
    }

    public Neuron CreateNeuron(NeuronType type) {
        Neuron t;

        switch (type) {
            case I:
                t = new InputNeuron();
                break;
            case O:
                t = new OutputNeuron();
                break;
            case B:
                t = new BiasNeuron();
                break;
            default:
                t = new HiddenNeuron();
        }

        return t;
    }

    List<Gene> ConnectLayers(List<Neuron> layerI, List<Neuron> layerO, boolean inversed) {
        return layerI.stream().flatMap(i ->
                layerO.stream().map(o -> ConnectNeurons(i, o, inversed))
        ).collect(Collectors.toList());
    }

    public Gene ConnectNeurons(Neuron i, Neuron o, boolean inversed) {
        Gene gene = new Gene();
        gene.In = i;
        gene.Out = o;
        gene.Weight = Math.random() * 20 - 10;
        gene.Enabled = true;
        gene.Inversed = inversed;
        gene.Out.In.add(gene);
        return gene;
    }

    public Network Breed(Network a, Network b) {
        if (a.Species != b.Species) throw new RuntimeException("networks not compatible!");

        Network c = Serializer.Clone(a);
        c.Id = Network.NetworksGenerated++;

        for (int i = 0; i < c.Genome.size(); i++) {
            Gene ga = a.Genome.get(i);
            Gene gb = b.Genome.get(i);
            Gene gc = c.Genome.get(i);
            Gene gn = getNewestGene(ga, gb);

            gc.Weight = gn.Weight;
            gc.Innovation = gn.Innovation;

            if ((!ga.Enabled || !gb.Enabled) && Math.random() > .5) {
                gc.Enabled = !gc.Enabled;
                gc.Innovation++;
            }
        }

        return c;
    }

    private Gene getNewestGene(Gene a, Gene b) {
        return b.Innovation > a.Innovation ? b : a;
    }
}
