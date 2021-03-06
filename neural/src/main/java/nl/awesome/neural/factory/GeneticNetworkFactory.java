package nl.awesome.neural.factory;

import nl.awesome.neural.Gene;
import nl.awesome.neural.Network;
import nl.awesome.neural.NeuronType;
import nl.awesome.neural.Serializer;
import nl.awesome.neural.lottery.Lottery;
import nl.awesome.neural.mutation.Mutations;
import nl.awesome.neural.neuron.Neuron;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO: This class is a work in progress
 */
public class GeneticNetworkFactory extends NetworkFactory {
    @Override
    public Network CreateNetwork(int inputs, int outputs) {
        Network network = Network.Create();
        List<Neuron> previousLayer = new ArrayList<Neuron>();
        List<Neuron> currentLayer = new ArrayList<Neuron>();

        previousLayer = CreateLayer(NeuronType.I, 0, inputs, true);
        network.Neurons.addAll(previousLayer);

        currentLayer = CreateLayer(NeuronType.O, 1, outputs, false);
        network.Neurons.addAll(currentLayer);
        network.Genome.addAll(ConnectLayers(previousLayer, currentLayer));
        return network;
    }

    @Override
    public Network cloneWithMutations(Network source, int times) {
        Network network = Serializer.Clone(source);
        network.parentId = network.id;
        network.id = ++Network.NetworksGenerated;

        if (Math.random() > .9) {
            if (
                Math.random() > .1
                && network.GetNeuronsOf(NeuronType.H).count() > 0
            ) {
                Mutations.AddGeneToNetwork(network, this);
            } else {
                Mutations.AddNeuronToNetwork(network, this);
            }
            network.Species = ++Network.SpeciesGenerated;
        }

        Lottery<Gene> geneMutations = new Lottery<Gene>()
                .add(.1f, Mutations::TweakGenes)
                .add(.1f, Mutations::RandomizeGenes)
                .add(.02f, Mutations::ToggleGene);

        while(times-- > 0) {
            Mutations.MutateGene(network, geneMutations);
        }

        return network;
    }


}

