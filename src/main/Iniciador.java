package main;

import java.io.IOException;

public class Iniciador {
    public static Log LOG;
    public static void main(String[] args) {
        try {
            LOG = new Log("Log.txt");
            Servidor srv = new Servidor();
            srv.run();
        } catch (IOException e) {
            LOG.LOGGER.severe("\n\t :( EXCEPCIÓN: "+e);
        }
    }
}
