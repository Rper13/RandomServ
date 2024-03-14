package API;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Service {

    private static final int PORT = 9999;

    private static Service instance = null;

    private Map<String,Socket> clients = new HashMap<>();

    private Thread serverThread;
    private Thread checkSockets;
    private ServerSocket serverSocket;

    private Service(){}
    public void startServer(){

        final Object mapLocker = new Object();

        serverThread = new Thread(() -> {
            try{
                serverSocket = new ServerSocket(PORT);
                System.out.println("server running");
                while(instance != null){

                    System.out.println(clients.size());
                    Socket clientSocket = serverSocket.accept();
                    String clientId = clientSocket.getInetAddress().getHostAddress();

                    synchronized (mapLocker) {
                        if (clients.containsKey(clientId)) {
                            System.out.printf("Client %s already exists%n", clientId);
                            clientSocket.close();
                            continue;
                        }
                        clients.put(clientId, clientSocket);
                    }

                    Thread clientHandler = new Thread(() -> {
                        while(!clientSocket.isClosed()) {
                            APIservice.handleClient(clientSocket);
                        }
                    });

                    clientHandler.start();

                }
            }catch (IOException e){
                System.err.println("Client Disconnected");
            }

        });

        checkSockets = new Thread(() -> {
            while(!Thread.currentThread().isInterrupted()){

                synchronized (mapLocker) {
                    for (String client : clients.keySet()) {
                        if (clients.get(client).isClosed()) {
                            clients.remove(client);
                            System.out.println(client + " disconnected");
                        }

                    }
                }

                try{
                    Thread.sleep(1000);
                }catch (Exception e){
                    System.out.println(e.getMessage());
                    Thread.currentThread().interrupt();
                }
            }
        });

        serverThread.start();
        checkSockets.start();
    }

    public Map<String,Socket> getClients() {
        return clients;
    }

    public static synchronized Service getInstance(){
        if(instance == null){
            instance = new Service();
        }
        return instance;
    }

    public void stop() {
        if (instance != null) {
            instance = null;
            try {
                if (serverSocket != null && !serverSocket.isClosed()) {
                    serverSocket.close();
                    for(String client : clients.keySet()){
                        clients.get(client).close();
                    }
                    checkSockets.interrupt();
                    serverThread.interrupt();
                }
            } catch (IOException e) {
                System.err.println("Error closing server socket: " + e.getMessage());
            }
        }
    }

}
