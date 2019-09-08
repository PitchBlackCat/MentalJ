package nl.awesome.neural.compiler;

import nl.awesome.neural.Gene;
import nl.awesome.neural.Network;
import nl.awesome.neural.NeuronType;
import nl.awesome.neural.neuron.Neuron;
import org.ejml.simple.SimpleMatrix;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toSet;

public class Compiler {
    public static CompiledNetwork compile(Network network) {
        Map<Integer, Set<Neuron>> layers = network.Neurons.stream().collect(groupingBy(Neuron::getLayer, toSet()));

        ArrayList<Tuple> tuples = new ArrayList<>();
        tuples.add(new Tuple(null, Matrix.from(filterBias(layers.get(0)).size()), null));

        for (int i = 1; i < layers.size(); i++) {
            Set<Neuron> prevNeurons = filterBias(layers.get(i - 1));
            Set<Neuron> neurons = filterBias(layers.get(i));

            Double[][] weights = neurons.stream()
                    .sorted(Comparator.comparingInt(Neuron::getOrder))
                    .map(n -> prevNeurons.stream()
                            .sorted(Comparator.comparingInt(Neuron::getOrder))
                            .mapToDouble(pn -> getGene(pn, n).Weight)
                            .boxed()
                            .toArray(Double[]::new))
                    .toArray(Double[][]::new);

            SimpleMatrix w = Matrix.from(weights);
            SimpleMatrix l = Matrix.from(filterBias(neurons).size());
            SimpleMatrix b = Matrix.from(layers.size());

            tuples.add(new Tuple(w, l, b));
        }

        return new CompiledNetwork(tuples);
    }

    static private Gene getGene(Neuron in, Neuron out) {
        return out.In.stream().filter(g -> g.In == in).findFirst().orElse(null);
    }

    private static Set<Neuron> filterBias(Set<Neuron> set) {
        return set.stream().filter(n -> n.getType() != NeuronType.B).collect(toSet());
    }

    private static boolean hasBias(Set<Neuron> set) {
        return set.stream().anyMatch(n -> n.getType() == NeuronType.B);
    }
}

