package nl.awesome.neural.mutation;

import nl.awesome.neural.Gene;
import nl.awesome.neural.Network;
import nl.awesome.neural.NeuralSettings;
import nl.awesome.neural.NeuronType;
import nl.awesome.neural.factory.NetworkFactory;
import nl.awesome.neural.lottery.Lottery;
import nl.awesome.neural.neuron.InputNeuron;
import nl.awesome.neural.neuron.Neuron;
import nl.awesome.neural.neuron.OutputNeuron;
import nl.awesome.utils.Rando;

import java.util.List;
import java.util.stream.Collectors;

public class Mutations {

    public static void MutateGene(Network network, Lottery<Gene> geneLottery) {
        boolean drawn = false;
        while (!drawn) {
            drawn = geneLottery.draw(Rando.fromList(network.Genome));
        }
    }

    public static void AddNeuronToNetwork(Network network, NetworkFactory factory) {
        Gene gene = network.Genome.stream().filter(g -> g.Enabled).findFirst().orElseThrow(null);
        gene.Enabled = false;

        Neuron newNeuron = factory.CreateNeuron(NeuronType.H);
        network.Neurons.add(newNeuron);

        Gene gin = factory.ConnectNeurons(gene.In, newNeuron);
        network.Genome.add(gin);

        Gene gout = factory.ConnectNeurons(newNeuron, gene.Out);
        network.Genome.add(gout);
    }

    public static void AddGeneToNetwork(Network network, NetworkFactory factory) {
        List<Neuron> randomOuts = network.Neurons.stream()
                .filter(n -> !(n instanceof InputNeuron))
                .collect(Collectors.toList());

        Neuron randomOut = Rando.fromList(randomOuts);

        List<Neuron> randomIns = network.Neurons.stream()
                .filter(n -> !(n instanceof OutputNeuron))
                .filter(n -> !n.hasDownstreamNeuron(randomOut))
                .collect(Collectors.toList());

        if (randomIns.size() == 0) {
            AddNeuronToNetwork(network, factory);
            AddGeneToNetwork(network, factory);
            return;
        }

        Neuron randomIn = Rando.fromList(randomIns);
        Gene gene = factory.ConnectNeurons(randomIn, randomOut);
        network.Genome.add(gene);
    }

    public static void TweakGenes(Gene gene) {
        for (Gene g : gene.Out.In) {
            g.Weight += Rando.max(NeuralSettings.mutationGeneWeights) * Rando.sign();
            g.Innovation++;
        }
    }

    public static void RandomizeGenes(Gene gene) {
        for (Gene g : gene.Out.In) {
            g.Weight = Rando.max(NeuralSettings.initialGeneWeights * 2) - NeuralSettings.initialGeneWeights;
            g.Innovation++;
        }
    }

    public static void ToggleGene(Gene gene) {
        gene.Enabled = !gene.Enabled;
        gene.Innovation++;
    }


}
