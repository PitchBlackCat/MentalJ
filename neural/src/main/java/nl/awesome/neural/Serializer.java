package nl.awesome.neural;

import nl.awesome.neural.neuron.*;

import java.util.ArrayList;
import java.util.List;

public class Serializer {
    public static String Serialize(Network network) {
        List<String> data = new ArrayList<String>();
        data.add("" + network.Id);
        data.add("" + network.Species);
        data.add("" + network.BestFitness);
        data.add(SerializeNeurons(network));
        data.add(SerializeGenome(network));
        return String.join(";", data);
    }

    private static String SerializeNeurons(Network network) {
        List<String> data = new ArrayList<String>();

        for (Neuron neuron : network.Neurons) {
            List<String> c = new ArrayList<String>();
            Add(c, neuron.getType());
            Add(c, neuron.getLayer());
            Add(c, neuron.getOrder());
            Add(c, neuron.getInnovation());
            data.add(String.join(":", c));
        }
        return String.join(",", data);
    }

    public static Network Clone(Network network) {
        return Deserialize(Serialize(network));
    }

    private static String SerializeGenome(Network network) {
        List<String> data = new ArrayList<String>();
        for (Gene gene : network.Genome) {
            List<String> c = new ArrayList<String>();
            Add(c, gene.Innovation);
            Add(c, network.Neurons.indexOf(gene.In));
            Add(c, network.Neurons.indexOf(gene.Out));
            Add(c, gene.Weight);
            Add(c, gene.Inversed);
            Add(c, gene.Enabled);
            data.add(String.join(":", c));
        }
        return String.join(",", data);
    }

    public static Network Deserialize(String str) {
        String[] data = str.split(";");
        Network network = new Network();

        network.Id = Integer.parseInt(data[0]);
        network.Species = Integer.parseInt(data[1]);
        network.BestFitness = Float.parseFloat(data[2]);

        Network.SpeciesGenerated = Math.max(Network.SpeciesGenerated, network.Species);
        Network.NetworksGenerated = Math.max(Network.NetworksGenerated, network.Id);

        for (String neuronData : data[3].split(",")) {
            String[] info = neuronData.split(":");
            Neuron neuron;
            NeuronType type = ParseEnum(info[0]);
            switch (type) {
                case I:
                    neuron = new InputNeuron();
                    break;
                case O:
                    neuron = new OutputNeuron();
                    break;
                case H:
                    neuron = new HiddenNeuron();
                    break;
                case B:
                    neuron = new BiasNeuron();
                    break;
                default:
                    throw new RuntimeException("Can't parse nl.awesome.neural.NeuronType." + type.toString());
            }
            neuron.setLayer(Integer.parseInt(info[1]));
            neuron.setOrder(Integer.parseInt(info[2]));
            neuron.setInnovation(Integer.parseInt(info[3]));
            network.Neurons.add(neuron);
        }

        for (String geneData : data[4].split(",")) {
            String[] info = geneData.split(":");
            Gene gene = new Gene();
            gene.Innovation = Integer.parseInt(info[0]);
            gene.In = network.Neurons.get(Integer.parseInt(info[1]));
            gene.Out = network.Neurons.get(Integer.parseInt(info[2]));
            gene.Weight = Double.parseDouble(info[3]);
            gene.Inversed = Boolean.parseBoolean(info[4]);
            gene.Enabled = Boolean.parseBoolean(info[5]);

            gene.Out.In.add(gene);

            network.Genome.add(gene);
        }

        return network;
    }

    private static <T> void Add(List<String> data, T value) {
        data.add(value.toString());
    }

    private static NeuronType ParseEnum(String value) {
        return NeuronType.valueOf(value);
    }
}
