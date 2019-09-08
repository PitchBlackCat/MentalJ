package nl.awesome.neural.factory;

import nl.awesome.neural.*;
import nl.awesome.neural.neuron.*;
import nl.awesome.utils.Rando;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class NetworkFactory {
    public static final Logger logger = LogManager.getLogger(NetworkFactory.class);

    public abstract Network CreateNetwork(int inputs, int outputs);

    public abstract Network cloneWithMutations(Network source, int times);

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

    List<Gene> ConnectLayers(List<Neuron> layerI, List<Neuron> layerO) {
        return layerI.stream().flatMap(i ->
                layerO.stream().map(o -> ConnectNeurons(i, o))
        ).collect(Collectors.toList());
    }

    public Gene ConnectNeurons(Neuron i, Neuron o) {
        Gene gene = new Gene();
        gene.In = i;
        gene.Out = o;
        gene.Weight = Rando.max(NeuralSettings.initialGeneWeights * 2) - NeuralSettings.initialGeneWeights;
        gene.Enabled = true;
        gene.Out.In.add(gene);
        return gene;
    }

    public Network Breed(Network a, Network b) {
        if (a.Species != b.Species) throw new RuntimeException("networks not compatible!");

        Network c = Serializer.Clone(a);
        c.id = Network.NetworksGenerated++;

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

        logger.info(
                "[BREEDING] {} with {} into {}",
                a.id, b.id, c.id
        );

        return c;
    }

    private Gene getNewestGene(Gene a, Gene b) {
        return b.Innovation > a.Innovation ? b : a;
    }
}
