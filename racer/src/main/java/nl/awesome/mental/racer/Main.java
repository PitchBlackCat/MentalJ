package nl.awesome.mental.racer;

import nl.awesome.neural.NeuralSettings;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import picocli.CommandLine;

import java.io.PrintWriter;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

@CommandLine.Command(name = "NeuralRacer", header = "%n@|green Neural Racer|@")
public class Main implements Callable<Integer> {
    public static final Logger logger = LogManager.getLogger(Main.class);

    @CommandLine.Option(names = {"-p", "--port"}, description = "socket port", defaultValue = "8888", showDefaultValue = CommandLine.Help.Visibility.ALWAYS)
    int port;

    @CommandLine.Option(names = {"-h", "--host"}, description = "socket host", defaultValue = "fe80::4ddc:287:3750:f172", showDefaultValue = CommandLine.Help.Visibility.ALWAYS)
    String host;

    @CommandLine.Option(names = {"-ip", "--initial-players"}, description = "number of initial players", defaultValue = "50", showDefaultValue = CommandLine.Help.Visibility.ALWAYS)
    int initalPlayers;

    @CommandLine.Option(names = {"-tp", "--top-players"}, description = "when a next generation starts, <topPlayers> will be selected to mutate.", defaultValue = "10", showDefaultValue = CommandLine.Help.Visibility.ALWAYS)
    int topPlayers;

    @CommandLine.Option(names = {"-iw", "--initial-weight"}, description = "the weights of a new network will fall between -<ig> and <ig>.", defaultValue = "10.0", showDefaultValue = CommandLine.Help.Visibility.ALWAYS)
    double initialGeneWeights;

    @CommandLine.Option(names = {"-mw", "--mutation-weight"}, description = "when mutation occurs, the weight of a gene will be shifted a random amount between -<mg> and <mg>.", defaultValue = "2.5", showDefaultValue = CommandLine.Help.Visibility.ALWAYS)
    double mutationGeneWeights;

    @CommandLine.Option(names = {"--min-mutations"}, description = "the minimum number of mutations", defaultValue = "1", showDefaultValue = CommandLine.Help.Visibility.ALWAYS)
    int minMutations;

    @CommandLine.Option(names = {"--max-mutations"}, description = "the maximum number of mutations", defaultValue = "1", showDefaultValue = CommandLine.Help.Visibility.ALWAYS)
    int maxMutations;

    @CommandLine.Parameters(description = "hidden layers")
    List<Integer> hiddenLayers;

    public static void main(String... args) {
        CommandLine.Help.Ansi ansi = CommandLine.Help.Ansi.ON;
        System.exit(
                new CommandLine(new Main())
                        .setOut(new PrintWriter(System.out))
                        .setErr(new PrintWriter(System.err))
                        .setColorScheme(CommandLine.Help.defaultColorScheme(ansi))
                        .execute(args)
        );
    }

    public Integer call() {
        NeuralSettings.hiddenLayers = hiddenLayers != null ? hiddenLayers : Collections.emptyList();
        NeuralSettings.initialGeneWeights = initialGeneWeights;
        NeuralSettings.mutationGeneWeights = mutationGeneWeights;
        NeuralSettings.minMutations = minMutations;
        NeuralSettings.maxMutations = maxMutations;
        RacerSettings.initialPlayers = initalPlayers;
        RacerSettings.topPlayers = topPlayers;

        RacerClient.startServer(host, port);
        return 0;
    }
}
