package nl.awesome.neural.factory;

import nl.awesome.neural.*;
import nl.awesome.neural.lottery.Lottery;
import nl.awesome.neural.mutation.Mutations;
import nl.awesome.neural.neuron.Neuron;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

public class HiddenLayerNetworkFactory extends NetworkFactory {
    public List<Integer> HiddenLayerNeurons = asList(5, 5);

    @Override
    public Network CreateNetwork(int inputs, int outputs) {
        Network network = Network.Create();
        List<Neuron> previousLayer = new ArrayList<Neuron>();
        List<Neuron> currentLayer = new ArrayList<Neuron>();

        int layer = 0;
        previousLayer = CreateLayer(NeuronType.I, layer++, inputs, false);
        network.Neurons.addAll(previousLayer);

        for (int i = 0; i < HiddenLayerNeurons.size(); i++) {
            currentLayer = CreateLayer(NeuronType.H, layer++, HiddenLayerNeurons.get(i), false);
            network.Genome.addAll(ConnectLayers(previousLayer, currentLayer, false));

            network.Neurons.addAll(currentLayer);
            previousLayer = currentLayer;
            currentLayer = new ArrayList<Neuron>();
        }

        currentLayer = CreateLayer(NeuronType.O, layer, outputs, false);
        network.Neurons.addAll(currentLayer);
        network.Genome.addAll(ConnectLayers(previousLayer, currentLayer, false));
        return network;
    }

    @Override
    public Network MutateNetwork(Network source, int times) {
        Network network = Serializer.Clone(source);
        network.parentId = network.Id;
        network.Id = ++Network.NetworksGenerated;

        Lottery<Gene> geneMutations = new Lottery<Gene>()
                .add(.1f, Mutations::TweakGene)
                .add(.1f, Mutations::RandomizeGene)
                .add(.02f, Mutations::ToggleGene);

        while(times-- > 0) {
            Mutations.MutateGene(network, geneMutations);
        }

        return network;
    }


}

