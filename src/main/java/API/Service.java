package API;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Service {

    private static final int PORT = 9999;

    private static Service instance = null;


    private Set<Socket> clients = new HashSet<>();

    private Thread serverThread;
    private ServerSocket serverSocket;

    private Service(){}

    public Set<Socket> getClients() {
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
            // Close the server socket to force accept method to throw exception
            try {
                if (serverSocket != null && !serverSocket.isClosed()) {
                    serverSocket.close();
                }
            } catch (IOException e) {
                System.err.println("Error closing server socket: " + e.getMessage());
            }
        }
    }

    public void startServer(){

        serverThread = new Thread(() -> {

            try{
                serverSocket = new ServerSocket(PORT);
                System.out.println("server running");
                while(instance != null){
                    Socket clientSocket = serverSocket.accept();
                    clients.add(clientSocket);
                    APIservice.handleClient(clientSocket);
                }
            }catch (IOException e){
                System.err.println("Client Disconnected");
            }

        });

        serverThread.start();
    }


}
