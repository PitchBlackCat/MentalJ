package nl.awesome.neuralracer;

import nl.awesome.Utils;
import nl.awesome.neural.Network;
import nl.awesome.neural.Serializer;
import nl.awesome.neural.compiler.CompiledNetwork;
import nl.awesome.neural.compiler.Compiler;
import nl.awesome.neural.factory.HiddenLayerNetworkFactory;
import nl.awesome.socket.Client;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

public class NeuralRacerClient {
    public static final Logger logger = LogManager.getLogger(NeuralRacerClient.class);

    public static final String WAITING_FOR_GAME = "WAITING_FOR_GAME";
    public static final String WAITING_FOR_PLAYERS = "WAITING_FOR_PLAYERS";
    public static final String LISTENING = "LISTENING";
    public static final String IDLE = "IDLE";
    private static final String bye = "BYE";
    private static final String add_player = "ADD_PLAYER";
    private static final String next_gen = "NEXT_GEN";
    private static final String game_ready = "GAME_READY";
    private static final String update = "UPDATE";
    private static final HiddenLayerNetworkFactory factory = new HiddenLayerNetworkFactory();
    private static final ExecutorService executor = Executors.newFixedThreadPool(32);
    public static List<CompiledNetwork> compiled = new ArrayList<>();
    public static List<Network> networks = new ArrayList<>();
    public static List<Network> pending = new ArrayList<>();
    public static Client client;
    public static String mode = IDLE;
    private static int numPlayers = 80;
    private static int topPlayers = 10;
    private static int generation = 0;
    private static boolean debug = false;

    public static void startServer(String host, int port, int initialPlayers, int topPlayers, List<Integer> hiddenLayers) {
        client = new Client(host, port);
        client.init();

        factory.HiddenLayerNeurons = hiddenLayers;
        logger.info("Hidden layers: {}", hiddenLayers.stream().map(Object::toString).collect(joining(" ")));

        numPlayers = initialPlayers;
        NeuralRacerClient.topPlayers = topPlayers;

        startGame(2);
        while (true) {
            String s = client.read();
            if (s == null) continue;

            String[] parts = s.split(" ");
            String command = parts[0];

            logger.debug("[C] {}", s);
            if (command.equals(bye)) {
                break;
            }

            switch (mode) {
                case WAITING_FOR_GAME:
                    if (command.equals(game_ready)) {
                        setPlayers(numPlayers);
                    }
                    break;
                case WAITING_FOR_PLAYERS:
                    if (command.equals(add_player)) {
                        addPlayer(parts);
                    }
                    break;
                case LISTENING:
                    switch (command) {
                        case update:
                            int id = Integer.parseInt(parts[1]);
                            CompiledNetwork network = compiled.get(id);
                            executor.execute(new UpdateNetworkJob(parts, id, network));
                            break;
                        case next_gen:
                            nextGen(parts);
                            break;
                        default:
                            break;
                    }
                    break;
            }
        }

        client.close();
    }

    private static void addPlayer(String[] args) {
        int in = Integer.parseInt(args[1]);
        int out = Integer.parseInt(args[2]);
        Network network = pending
                .stream()
                .filter(n -> n.numInputs() == in && n.numOutputs() == out)
                .findAny()
                .orElse(factory.CreateNetwork(in, out));

        pending.remove(network);
        networks.add(network);
        compiled.add(Compiler.compile(network));

        logger.info("[PLAYER] parent: {}, id: {}, species: {}, in: {}, out: {}",
                network.parentId, network.Id, network.Species, in, out
        );

        if (networks.size() == numPlayers) {
            logger.info("[PLAYER] {} joined!", networks.size());
            setMode(LISTENING);
        }
    }

    public static void setMode(String m) {
        mode = m;
        logger.info("[MODE] {}", m);
    }

    public static void nextGen(String[] parts) {
        pending = new ArrayList<>();

        int top = topPlayers;
        int offspring = top;

        for (int i = 1; i < parts.length; i++) {
            String[] netparts = parts[i].split(":");
            int id = Integer.parseInt(netparts[0]);
            float score = Float.parseFloat(netparts[1]);
            networks.get(id).setFitness(score);
        }

        List<Network> collect = networks.stream()
                .sorted(Comparator.comparingInt(a -> (int) -a.Fitness))
                .limit(top)
                .collect(toList());

        Network child;
        for (int i = 0; i < collect.size(); i++) {
            Network parent = collect.get(i);
            logger.info(
                    "#{} id: {}, score: {}, best: {}, children: {}, genome: {}",
                    i, parent.Id, parent.Fitness, parent.BestFitness, offspring, Serializer.Serialize(parent)
            );

            for (int n = 0; n < Math.ceil(offspring * 0.5) + 1; n++) {
                child = factory.MutateNetwork(parent, (n * 2) + 1);
                pending.add(child);
            }

            offspring--;

            parent.setFitness(0);
            pending.add(parent);
        }

        Utils.consecutive(collect).forEach(p -> {
            Network c = factory.Breed(p.getKey(), p.getValue());

            logger.info(
                    "[BREEDING] {} with {}, result: {}",
                    p.getKey().Id, p.getValue().Id, c.Id
            );

            pending.add(c);
        });

        networks = new ArrayList<>();
        compiled = new ArrayList<>();
        numPlayers = pending.size() + 10;

        logger.info("[NEXT GEN] Generation #{}", ++generation);
        setPlayers(numPlayers);
    }

    public static void setPlayers(int num) {
        setMode(WAITING_FOR_PLAYERS);
        client.send("PLAYERS " + num);
    }

    public static void startGame(int num) {
        setMode(WAITING_FOR_GAME);
        client.send("GAME " + num);
    }
}
