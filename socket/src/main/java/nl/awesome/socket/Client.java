package nl.awesome.socket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class Client {
    public static final Logger logger = LogManager.getLogger(Client.class);
    private int port = 8888;
    private String host = "fe80::4ddc:287:3750:f172";

    private Socket socket = null;
    private DataOutputStream out = null;
    private BufferedReader in = null;
    private Queue<String> qin = new LinkedBlockingQueue<>();
    private Queue<String> qout = new LinkedBlockingQueue<>();

    public Client(String host, int port) {
        this.port = port;
        this.host = host;
    }

    public void init() {
        try {
            socket = new Socket(host, port);
            out = new DataOutputStream(socket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            new Thread(() -> {
                String fromServer;
                while (true) {
                    try {
                        while ((fromServer = in.readLine()) != null) {
                            qin.add(fromServer);
                            logger.trace("[I] {}", fromServer);
                        }
                    } catch (IOException e) {
                        close();
                    }
                }
            }).start();

            new Thread(() -> {
                while (!socket.isClosed()) {
                    String fromClient;
                    if ((fromClient = qout.poll()) == null) continue;
                    try {
                        out.write((fromClient + "\n").getBytes("US-ASCII"));
                        out.flush();
                        logger.trace("[O] {}", fromClient);
                    } catch (IOException e) {
                        close();
                    }
                }
            }).start();

        } catch (UnknownHostException e) {
            logger.fatal("Don't know about host: {}", host);
            System.exit(1);
        } catch (IOException e) {
            logger.fatal("Couldn't get I/O for the connection unpack: {}", host);
            System.exit(1);
        }
    }

    public void send(String s) {
        qout.add(s);
    }

    public String read() {
        return qin.poll();
    }

    public synchronized void close() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
