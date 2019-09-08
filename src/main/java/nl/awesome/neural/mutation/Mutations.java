package nl.awesome.neural.mutation;

import nl.awesome.neural.Gene;
import nl.awesome.neural.Network;
import nl.awesome.neural.NeuronType;
import nl.awesome.neural.factory.NetworkFactory;
import nl.awesome.neural.lottery.Lottery;
import nl.awesome.neural.neuron.InputNeuron;
import nl.awesome.neural.neuron.Neuron;
import nl.awesome.neural.neuron.OutputNeuron;

import java.util.List;
import java.util.stream.Collectors;

public class Mutations {
    public static void MutateGene(Network network, Lottery<Gene> geneLottery) {
        boolean drawn = false;
        while(!drawn) {
            drawn = geneLottery.draw(GetRandomItem(network.Genome));
        }
    }

    public static void AddNeuronToNetwork(Network network, NetworkFactory factory) {
        Gene gene = network.Genome.stream().filter(g -> g.Enabled).findFirst().orElseThrow(null);
        gene.Enabled = false;

        Neuron newNeuron = factory.CreateNeuron(NeuronType.H);
        network.Neurons.add(newNeuron);

        Gene gin = factory.ConnectNeurons(gene.In, newNeuron, false);
        network.Genome.add(gin);

        Gene gout = factory.ConnectNeurons(newNeuron, gene.Out, false);
        network.Genome.add(gout);
    }

    public static void AddGeneToNetwork(Network network, NetworkFactory factory) {
        List<Neuron> randomOuts = network.Neurons.stream()
                .filter(n -> !(n instanceof InputNeuron))
                .collect(Collectors.toList());

        Neuron randomOut = GetRandomItem(randomOuts);

        List<Neuron> randomIns = network.Neurons.stream()
                .filter(n -> !(n instanceof OutputNeuron))
                .filter(n -> !n.hasDownstreamNeuron(randomOut))
                .collect(Collectors.toList());

        if (randomIns.size() == 0) {
            AddNeuronToNetwork(network, factory);
            AddGeneToNetwork(network, factory);
            return;
        }

        Neuron randomIn = GetRandomItem(randomIns);
        Gene gene = factory.ConnectNeurons(randomIn, randomOut, false);
        network.Genome.add(gene);
    }

    public static void TweakGene(Gene gene) {
        for (Gene g : gene.Out.In) {
            gene.Weight += (Math.random() * 2) * RandomSign();
            gene.Innovation++;
        }
    }

    public static void RandomizeGene(Gene gene) {
        gene.Weight = Math.random() * 2 - 1;
        gene.Innovation++;
    }

    public static void ToggleGene(Gene gene) {
        gene.Enabled = !gene.Enabled;
        gene.Innovation++;
    }

    private static <T> T GetRandomItem(List<T> items) {
        return items.get((int) Math.floor((items.size() - 0.001f) * Math.random()));
    }

    private static int RandomSign() {
        return Math.random() < .5 ? -1 : 1;
    }


}
