package nl.awesome.neural.factory;

import nl.awesome.neural.*;
import nl.awesome.neural.lottery.Lottery;
import nl.awesome.neural.mutation.Mutations;
import nl.awesome.neural.neuron.Neuron;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class HiddenLayerNetworkFactory extends NetworkFactory {
    public static final Logger logger = LogManager.getLogger(HiddenLayerNetworkFactory.class);


    @Override
    public Network CreateNetwork(int inputs, int outputs) {
        Network network = Network.Create();
        List<Neuron> previousLayer;
        List<Neuron> currentLayer;

        int layer = 0;
        previousLayer = CreateLayer(NeuronType.I, layer++, inputs, false);
        network.Neurons.addAll(previousLayer);

        for (int i = 0; i < NeuralSettings.hiddenLayers.size(); i++) {
            currentLayer = CreateLayer(NeuronType.H, layer++, NeuralSettings.hiddenLayers.get(i), false);
            network.Genome.addAll(ConnectLayers(previousLayer, currentLayer));

            network.Neurons.addAll(currentLayer);
            previousLayer = currentLayer;
        }

        currentLayer = CreateLayer(NeuronType.O, layer, outputs, false);
        network.Neurons.addAll(currentLayer);
        network.Genome.addAll(ConnectLayers(previousLayer, currentLayer));
        return network;
    }

    @Override
    public Network cloneWithMutations(Network source, int times) {
        Network network = Serializer.Clone(source);
        network.parentId = network.id;
        network.id = ++Network.NetworksGenerated;

        logger.info("[Mutate] {} cloned into {} with {} mutations", network.parentId, network.id, times);

        Lottery<Gene> geneMutations = new Lottery<Gene>()
                .add(.1f, Mutations::TweakGenes)
                .add(.1f, Mutations::RandomizeGenes);

        while (times-- > 0) {
            Mutations.MutateGene(network, geneMutations);
        }

        return network;
    }


}

