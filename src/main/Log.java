package main;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Log {
    public Logger LOGGER;
    FileHandler FILE_HANDLER;

    public Log(String NOMBRE_LOG) throws IOException {
        File FILE = new File(NOMBRE_LOG);
        if (!FILE.exists()){
            FILE.createNewFile();
        }

        FILE_HANDLER = new FileHandler(NOMBRE_LOG, true);
        LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
        LOGGER.setLevel(Level.ALL);
        LOGGER.addHandler(FILE_HANDLER);
        SimpleFormatter FORMATEADOR = new SimpleFormatter();
        FILE_HANDLER.setFormatter(FORMATEADOR);

    }
}
