package nl.awesome.cli;

import nl.awesome.neural.factory.HiddenLayerNetworkFactory;
import nl.awesome.neuralracer.NeuralRacerClient;
import picocli.CommandLine;

import java.util.List;
import java.util.concurrent.Callable;

@CommandLine.Command(name = "NeuralRacer", header = "%n@|green Neural Racer|@")
public class NeuralRacer implements Callable<Integer> {
    @CommandLine.Option(names = {"-p", "--port"}, required = false, description = "socket port", defaultValue = "8888")
    String port;

    @CommandLine.Option(names = {"-h", "--host"}, required = false, description = "socket host", defaultValue = "fe80::4ddc:287:3750:f172")
    String host;

    @CommandLine.Option(names = {"-i", "--initial-players"}, required = false, description = "number of initial players", defaultValue = "100")
    String initalPlayers;

    @CommandLine.Option(names = {"-t", "--top-players"}, required = false, description = "the number of top players will spawn children for the next generation", defaultValue = "10")
    String topPlayers;

    @CommandLine.Parameters(/* type = File.class, */ description = "hidden layers")
    List<Integer> hiddenLayers; // picocli infers type from the generic type

    public static void main(String... args) {
        new CommandLine(new NeuralRacer()).execute(args);
    }

    public Integer call() {
        NeuralRacerClient.startServer(host, Integer.parseInt(port), Integer.parseInt(initalPlayers), Integer.parseInt(topPlayers), hiddenLayers);
        return 0;
    }
}
