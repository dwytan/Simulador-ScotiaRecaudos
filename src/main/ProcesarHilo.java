package main;

import java.io.*;
import java.net.Socket;
import java.util.Properties;

public class ProcesarHilo implements Runnable{
    public Socket SOCKET_CONEXION = null;
    public String TEXTO_SERVIDOR = null;
    ObjectOutputStream OUT;
    ObjectInputStream IN;

    // RECAUDOS
    String R1_RUC;
    String R1_CONSULTA_APP_DATA;
    String R1_PAGO_APP_DATA;
    String R2_RUC;
    String R2_CONSULTA_APP_DATA;
    String R2_PAGO_APP_DATA;
    String R3_RUC;
    String R3_CONSULTA_APP_DATA;
    String R3_PAGO_APP_DATA;
    String R4_RUC;
    String R4_CONSULTA_APP_DATA;
    String R4_PAGO_APP_DATA;

    int TRAMA_ERROR_RESPONSE_CODE;
    int TRAMA_ERROR_STATUS;
    int DELAY;


    public ProcesarHilo(Socket SOCKET_CONEXION, String TEXTO_SERVIDOR) {
        this.SOCKET_CONEXION = SOCKET_CONEXION;
        this.TEXTO_SERVIDOR = TEXTO_SERVIDOR;
    }

    @Override
    public void run() {
        String INPUT = "";
        String OUTPUT = "";

        String MIT = "";
        String PRCODE = "";
        String AMMOUNT = "";
        String DATE_TIME = "";
        String TRACE = "";
        String TIME = "";
        String DATE = "";
        String BIT32 = "";
        String BIT37 = "";
        int BIT38;
        String TERMINAL = "";
        String BIT42 = "";
        String DIRECCION = "";
        String RUC = "";
        String EMPRESA = "";

        try {

            DataInputStream IN_CLIENTE = new DataInputStream(SOCKET_CONEXION.getInputStream());
            DataOutputStream OUT_CLIENTE = new DataOutputStream(SOCKET_CONEXION.getOutputStream());

            while (true) {
                INPUT = IN_CLIENTE.readUTF();

                MIT = INPUT.substring(0,4);
                PRCODE = INPUT.substring(54, 60);
                AMMOUNT = INPUT.substring(60, 72);
                DATE_TIME = INPUT.substring(72, 82);
                TRACE = INPUT.substring(82, 88);
                TIME = INPUT.substring(88, 94);
                DATE = INPUT.substring(94, 98);
                BIT32 = INPUT.substring(117, 123);
                BIT37 = INPUT.substring(162, 174);
                BIT38 = (int)(Math.random()*(100000-999999+1)+999999);
                if (MIT.equals("0200")){
                    TERMINAL = INPUT.substring(174, 182);
                    BIT42 = INPUT.substring(182, 197);
                    DIRECCION = INPUT.substring(197, 237);
                    RUC = INPUT.substring(240, 251);
                    Iniciador.LOG.LOGGER.info("\n\t<--\n\tRECIBIENDO: " +INPUT+
                            "\n\tMIT: " +MIT+
                            "\n\tPRCODE: " +PRCODE+
                            "\n\tMONTO: " +(Double)Double.parseDouble(AMMOUNT)/100+
                            "\n\tDATE & TIME: " +DATE_TIME+
                            "\n\tTRACE: " +TRACE+
                            "\n\tTIME: " +TIME+
                            "\n\tDATE: " +DATE+
                            "\n\tBIT 37: " +BIT37+
                            "\n\tTERMINAL: " +TERMINAL+
                            "\n\tBIT 42: " +BIT42+
                            "\n\tDIRECCIÓN: " +DIRECCION+
                            "\n\tRUC: " +RUC+"\n");
                }
                if (MIT.equals("0420")){
                    TERMINAL = INPUT.substring(176, 184);
                    BIT42 = INPUT.substring(184, 199);
                    DIRECCION = INPUT.substring(199, 239);
                    Iniciador.LOG.LOGGER.info("\n\t<--\n\tRECIBIENDO: " +INPUT+
                            "\n\tMIT: " +MIT+
                            "\n\tPRCODE: " +PRCODE+
                            "\n\tMONTO: " +(Double)Double.parseDouble(AMMOUNT)/100+
                            "\n\tDATE & TIME: " +DATE_TIME+
                            "\n\tTRACE: " +TRACE+
                            "\n\tTIME: " +TIME+
                            "\n\tDATE: " +DATE+
                            "\n\tBIT 37: " +BIT37+
                            "\n\tTERMINAL: " +TERMINAL+
                            "\n\tBIT 42: " +BIT42+
                            "\n\tDIRECCIÓN: " +DIRECCION+"\n");
                }

                OUTPUT = logicaResponse(MIT, PRCODE, AMMOUNT, DATE_TIME, TRACE, TIME, DATE, BIT32, BIT37, BIT38, TERMINAL, BIT42, DIRECCION, RUC);
                Iniciador.LOG.LOGGER.info("\n\t-->\n\tENVIANDO: "+OUTPUT);
                OUT_CLIENTE.writeUTF(OUTPUT);
            }

        }catch (EOFException eof) {
            try {
                SOCKET_CONEXION.close();
                Iniciador.LOG.LOGGER.info("\n\t :) CONEXIÓN CLIENTE CERRADA");
            } catch (IOException e) {
                Iniciador.LOG.LOGGER.severe("\n\t :( EXCEPCIÓN: "+e);
                e.printStackTrace();
            }
        }
        catch (IOException e) {
            Iniciador.LOG.LOGGER.severe("\n\t :( EXCEPCIÓN: "+e);
        } catch (Exception ex) {
            Iniciador.LOG.LOGGER.severe("\n\t :( EXCEPCIÓN: "+ex);
        }
    }

    public String logicaResponse(String MIT, String PRCODE, String AMMOUNT, String DATE_TIME, String TRACE, String TIME, String DATE, String BIT32, String BIT37, int BIT38, String TERMINAL, String BIT42, String DIRECCION, String RUC) {
        String TRAMA_OUTPUT = " :( MAL REQUERIMIENTO";
        String CONSULTA_APP_DATA = "";
        String PAGO_APP_DATA = "";

        // Cargando DATA
        try {
            // Leyendo Properties
            FileInputStream FILE = new FileInputStream("Configuracion.properties");
            Properties PROP = new Properties();
            PROP.load(FILE);

            // Asignando valores de Properties a variables locales
            R1_RUC = PROP.getProperty("RECAUDO_1_RUC");
            R1_CONSULTA_APP_DATA = PROP.getProperty("RECAUDO_1_CONSULTA_APPDATA");
            R1_PAGO_APP_DATA = PROP.getProperty("RECAUDO_1_PAGO_APPDATA");

            R2_RUC = PROP.getProperty("RECAUDO_2_RUC");
            R2_CONSULTA_APP_DATA = PROP.getProperty("RECAUDO_2_CONSULTA_APPDATA");
            R2_PAGO_APP_DATA = PROP.getProperty("RECAUDO_2_PAGO_APPDATA");

            R3_RUC = PROP.getProperty("RECAUDO_3_RUC");
            R3_CONSULTA_APP_DATA = PROP.getProperty("RECAUDO_3_CONSULTA_APPDATA");
            R3_PAGO_APP_DATA = PROP.getProperty("RECAUDO_3_PAGO_APPDATA");

            R4_RUC = PROP.getProperty("RECAUDO_4_RUC");
            R4_CONSULTA_APP_DATA = PROP.getProperty("RECAUDO_4_CONSULTA_APPDATA");
            R4_PAGO_APP_DATA = PROP.getProperty("RECAUDO_4_PAGO_APPDATA");

            TRAMA_ERROR_RESPONSE_CODE = (int) Integer.parseInt(PROP.getProperty("TRAMA_ERROR_RESPONSE_CODE"));
            TRAMA_ERROR_STATUS = (int) Integer.parseInt(PROP.getProperty("TRAMA_ERROR_STATUS"));
            DELAY = (int) Integer.parseInt(PROP.getProperty("DELAY"));

        } catch (IOException e) {
            Iniciador.LOG.LOGGER.severe("\n\t :( EXCEPCIÓN: "+e);
        } catch (Exception ex) {
            Iniciador.LOG.LOGGER.severe("\n\t :( EXCEPCIÓN: "+ex);
        }

        if (RUC.equals(R1_RUC)) {
            CONSULTA_APP_DATA = R1_CONSULTA_APP_DATA;
            PAGO_APP_DATA = R1_PAGO_APP_DATA;
        } else if (RUC.equals(R2_RUC)) {
            CONSULTA_APP_DATA = R2_CONSULTA_APP_DATA;
            PAGO_APP_DATA = R2_PAGO_APP_DATA;
        } else if (RUC.equals(R3_RUC)) {
            CONSULTA_APP_DATA = R3_CONSULTA_APP_DATA;
            PAGO_APP_DATA = R3_PAGO_APP_DATA;
        } else if (RUC.equals(R4_RUC)) {
            CONSULTA_APP_DATA = R4_CONSULTA_APP_DATA;
            PAGO_APP_DATA = R4_PAGO_APP_DATA;
        }

        if (MIT.equals("0200") && !CONSULTA_APP_DATA.equals("") && !PAGO_APP_DATA.equals("")) {
            // CONSULTA
            if (PRCODE.equals("934600")||PRCODE.equals("934500")) {
                return "0210FE3A84810EE0E4000000000006000080168888888800000000"+PRCODE+AMMOUNT+"000000000000000000000000"+DATE_TIME+TRACE+TIME+DATE+DATE+DATE+"9014000"+BIT37+BIT38+"00"+TERMINAL+BIT42+DIRECCION+"6040000000028                            28                            "+CONSULTA_APP_DATA.length()+CONSULTA_APP_DATA;
            }
            // PAGO
            if (TRAMA_ERROR_STATUS == 1) {
                try {
                    Thread.sleep(DELAY);
                } catch (InterruptedException e) {
                    Iniciador.LOG.LOGGER.severe("\n\t :( EXCEPCIÓN: "+e);
                }
                return "0210FE3A84810EE0E4000000000006000080168888888800000000"+PRCODE+AMMOUNT+"000000000000000000000000"+DATE_TIME+TRACE+TIME+DATE+DATE+DATE+"9014006"+BIT32+BIT37+"      "+TRAMA_ERROR_RESPONSE_CODE+TERMINAL+BIT42+DIRECCION+"6040000000028                            28                            000";
            } else if (TRAMA_ERROR_STATUS == 0) {
                if (PRCODE.equals("170046")||PRCODE.equals("170045")) {
                    try {
                        Thread.sleep(DELAY);
                    } catch (InterruptedException e) {
                        Iniciador.LOG.LOGGER.severe("\n\t :( EXCEPCIÓN: "+e);
                    }
                    return "0210FE3A84810EE0E4000000000006000080168888888800000000"+PRCODE+AMMOUNT+"000000000000000000000000"+DATE_TIME+TRACE+TIME+DATE+DATE+DATE+"9014000"+BIT37+BIT38+"00"+TERMINAL+BIT42+DIRECCION+"6040000000028                            28                            "+PAGO_APP_DATA.length()+PAGO_APP_DATA;
                }
            }
        }

        if (MIT.equals("0420")) {
            return "0430763A84810AE08000168888888800000000"+PRCODE+AMMOUNT+"000000000000"+DATE_TIME+TRACE+TIME+DATE+DATE+DATE+"0004000"+BIT37+"00"+TERMINAL+BIT42+DIRECCION+"604" ;
        }

        return "0"+((int)Integer.parseInt(MIT)+10)+"FE3A84810EE0E4000000000006000080168888888800000000"+PRCODE+"000000000000000000000000000000000000"+DATE_TIME+TRACE+TIME+DATE+DATE+DATE+"9014006"+BIT32+BIT37+"      12"+TERMINAL+BIT42+DIRECCION+"6040000000028                            28                            000";
    }
}
