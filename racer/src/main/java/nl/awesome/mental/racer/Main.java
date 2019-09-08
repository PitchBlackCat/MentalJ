package nl.awesome.mental.racer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import picocli.CommandLine;

import java.io.PrintWriter;
import java.util.List;
import java.util.concurrent.Callable;

@CommandLine.Command(name = "NeuralRacer", header = "%n@|green Neural Racer|@")
public class Main implements Callable<Integer> {
    public static final Logger logger = LogManager.getLogger(Main.class);

    @CommandLine.Option(names = {"-p", "--port"}, description = "socket port", defaultValue = "8888", showDefaultValue = CommandLine.Help.Visibility.ALWAYS)
    int port;

    @CommandLine.Option(names = {"-h", "--host"}, description = "socket host", defaultValue = "fe80::4ddc:287:3750:f172", showDefaultValue = CommandLine.Help.Visibility.ALWAYS)
    String host;

    @CommandLine.Option(names = {"-i", "--initial-players"}, description = "number of initial players", defaultValue = "50", showDefaultValue = CommandLine.Help.Visibility.ALWAYS)
    int initalPlayers;

    @CommandLine.Option(names = {"-t", "--top-players"}, description = "when a next generation starts, <topPlayers> will be selected to mutate.", defaultValue = "10", showDefaultValue = CommandLine.Help.Visibility.ALWAYS)
    int topPlayers;

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
        RacerClient.startServer(host, port, initalPlayers, topPlayers, hiddenLayers);
        return 0;
    }
}
