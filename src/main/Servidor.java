package main;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Servidor {

    public int PUERTO;
    public ServerSocket SERVERSOCKET;
    public boolean DETENIDO = false;
    public Thread HILO_ACTIVO = null;
    public ExecutorService GRUPO_HILOS = Executors.newFixedThreadPool(30);

    public Servidor() throws IOException {
        // Leyendo Properties
        FileInputStream FILE = new FileInputStream("Configuracion.properties");
        Properties PROP = new Properties();
        PROP.load(FILE);
        // Asignando valores de Properties a variables locales
        PUERTO = Integer.parseInt(PROP.getProperty("PUERTO"));
    }

    private void abrirServerSocket() {
        try {
            this.SERVERSOCKET = new ServerSocket(this.PUERTO);
            Iniciador.LOG.LOGGER.info("\n\t :) INICIANDO SERVIDOR\n \tESPERANDO PETICIONES EN PUERTO: "+PUERTO);
        } catch (IOException e) {
            Iniciador.LOG.LOGGER.severe("\n\t :( NO SE PUDO ABRIR PUERTO: " +PUERTO+ "\n \tRAZÓN: "+e.toString());
        }
    }

    // Retornando estado del servidor
    private synchronized boolean isDETENIDO() {
        return this.DETENIDO;
    }

    public void run() {
        synchronized (this) {
            this.HILO_ACTIVO = Thread.currentThread();
        }
        abrirServerSocket();

        while (!isDETENIDO()) {
            Socket SOCKET_CLIENTE = null;

            try {
                // Accept permite que solo se continuen las lineas de codigo SOLO si es que se conecta un cliente
                SOCKET_CLIENTE = this.SERVERSOCKET.accept();
                Iniciador.LOG.LOGGER.info("\n\t :) NUEVO CLIENTE CONECTADO\n\tIP: "+SOCKET_CLIENTE.getInetAddress());

            } catch (IOException e) {
                if (isDETENIDO()) {
                    Iniciador.LOG.LOGGER.info("\n\t :( SERVIDOR DETENIDO");
                    break;
                }
                Iniciador.LOG.LOGGER.severe("\n\t :( ERROR ACEPTANDO CLIENTE\n\tRAZÓN: "+e);
            }

            this.GRUPO_HILOS.execute(new ProcesarHilo(SOCKET_CLIENTE, "Thread Pooled Server"));
        }
        this.GRUPO_HILOS.shutdown();
        Iniciador.LOG.LOGGER.info("\n\t :( SERVIDOR DETENIDO");
    }
}
