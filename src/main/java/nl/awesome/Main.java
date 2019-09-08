package nl.awesome;

import nl.awesome.neural.Network;
import nl.awesome.neural.NeuronType;
import nl.awesome.neural.Serializer;
import nl.awesome.neural.compiler.CompiledNetwork;
import nl.awesome.neural.compiler.Compiler;
import nl.awesome.neural.factory.HiddenLayerNetworkFactory;
import nl.awesome.neural.neuron.Neuron;
import nl.awesome.socket.Client;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

public class Main {
    public static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
    }
}
