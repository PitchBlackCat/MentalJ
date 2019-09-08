package nl.awesome.neural;

import nl.awesome.neural.neuron.Neuron;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

public class Network {
    public static final Logger logger = LogManager.getLogger(Network.class);

    public static int NetworksGenerated = 0;
    public static int SpeciesGenerated = 0;

    public static Network Create() {
        Network n = new Network();
        n.id = ++NetworksGenerated;
        return n;
    }

    public float Fitness = 0;
    public float BestFitness = 0;
    public int id = 0;
    public int parentId = 0;
    public int Species = 0;

    public List<Neuron> Neurons = new ArrayList<Neuron>();
    public List<Gene> Genome = new ArrayList<Gene>();

    public Stream<Neuron> GetNeuronsOf(NeuronType type) {
        return Neurons.stream().filter(n -> n.getType() == type);
    }
    public Stream<Neuron> GetLayer(int layer) {
        return Neurons.stream().filter(n -> n.getLayer() == layer).sorted(Comparator.comparingInt(n -> n.getOrder()));
    }

    public long numInputs() {
        return GetNeuronsOf(NeuronType.I).count();
    }

    public long numOutputs() {
        return GetNeuronsOf(NeuronType.O).count();
    }

    public void setFitness(float fitness) {
        this.Fitness = fitness;
        BestFitness = Math.max(fitness, BestFitness);
    }

    public void print() {
        logger.info("| Network {}", id);
        Neurons.forEach(n -> logger.info("| {}{}: {}", n.getType(), Neurons.indexOf(n), String.format("%5.5s", n.getValue())));
        for (int i = 0; i < Genome.size(); i++) {
            Gene g = Genome.get(i);
            logger.info(
                    "| {}{}-{}{}: {}",
                    g.In.getType(),
                    Neurons.indexOf(g.In),
                    g.Out.getType(),
                    Neurons.indexOf(g.Out),
                    String.format("%5.5s", g.Weight)
            );
        }
    }
}
