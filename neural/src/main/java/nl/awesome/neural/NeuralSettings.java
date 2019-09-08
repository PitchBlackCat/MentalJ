package nl.awesome.neural;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

import static java.util.Arrays.asList;

public class NeuralSettings {
    public static List<Integer> hiddenLayers = asList(5, 5);
    public static double initialGeneWeights = 1.0;
    public static double mutationGeneWeights = 1.0;
    public static int minMutations = 1;
    public static int maxMutations = 1;

    public static void print() {
        Logger logger = LogManager.getLogger(NeuralSettings.class);
        logger.info("[Settings] initialGeneWeights: {}", initialGeneWeights);
        logger.info("[Settings] mutationGeneWeights: {}", mutationGeneWeights);
        logger.info("[Settings] hiddenLayers: {}", hiddenLayers);
        logger.info("[Settings] minMutations: {}", minMutations);
        logger.info("[Settings] maxMutations: {}", maxMutations);
    }
}
