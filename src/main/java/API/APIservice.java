package API;

import JDBC.MySQLservice;
import Objects.User;

import java.io.*;
import java.net.Socket;

public class APIservice {

    private static ObjectInputStream reader = null;
    private static ObjectOutputStream writer = null;

    public static synchronized void handleClient(Socket clientSocket){

        try{
            if(reader == null)
                reader = new ObjectInputStream(new DataInputStream(clientSocket.getInputStream()));

            if(writer == null)
                writer = new ObjectOutputStream(new DataOutputStream(clientSocket.getOutputStream()));

            String request = reader.readUTF();

            RequestTypes requestType = determineRequest(request);

            switch (requestType){

                case LOGIN -> {
                    request = request.substring(10);
                    writer.writeUTF(processLoginRequest(request));
                    writer.flush();
                }

                case REGISTER -> {
                    request = request.substring(13);
                    writer.writeUTF(processRegisterRequest(request));
                    writer.flush();
                }

                case USER_INFO -> {
                    request = request.substring(14);
                    User user = MySQLservice.getInstance().retrieveUser(request);
                    writer.writeObject(user);
                    writer.flush();
                }

                case MESSAGE -> {
                    processMessage();
                }

            }



        }catch (IOException e){
            System.err.println("Error with retrieving BufferedReader (IOException)");
        }


    }

    private static RequestTypes determineRequest(String req){

        if(req.length() >=10 ) {
            if (req.startsWith("API:LOGIN:")) return RequestTypes.LOGIN;
        }
        if(req.length() >= 13){
            if (req.startsWith("API:REGISTER:")) return RequestTypes.REGISTER;
        }
        if(req.length() >= 14){
            if(req.startsWith("API:USER_INFO:")) return RequestTypes.USER_INFO;
        }

        return RequestTypes.MESSAGE;
    }

    private static String processLoginRequest(String request){

        String[] parts = request.split(",");
        String username = parts[0];
        String password = parts[1];

        MySQLservice SQL = MySQLservice.getInstance();


        String res = SQL.AuthRequest(username,password);
        SQL.closeConn();

        return res;
    }

    private static String processRegisterRequest(String request){
        String[] parts = request.split(",");
        String name = parts[0];
        String last_name = parts[1];
        String username = parts[2];
        String password = parts[3];
        String phone_number = parts[4];

        MySQLservice SQL = MySQLservice.getInstance();

        boolean r = SQL.RegisterRequest(name,last_name,username,password,phone_number);
        String res = (r) ? "Successfully Registered" : "Registration Failed";

        SQL.closeConn();

        return res;
    }

    private static void processMessage(){

    }




}
