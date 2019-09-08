package nl.awesome.mental.racer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RacerSettings {
    public static int initialPlayers = 10;
    public static int topPlayers = 5;

    public static void print() {
        Logger logger = LogManager.getLogger(RacerSettings.class);
        logger.info("[Settings] initialPlayers: {}", initialPlayers);
        logger.info("[Settings] topPlayers: {}", topPlayers);
    }
}
