package API;

import Entry.DashboardController;
import JDBC.MySQLservice;

import java.io.*;
import java.net.Socket;

public class APIservice {


    public static void handleClient(Socket clientSocket){

        try{
            DataInputStream reader = new DataInputStream(clientSocket.getInputStream());

            String request = reader.readUTF();

            RequestTypes requestType = determineRequest(request);

           DataOutputStream writer = new DataOutputStream(clientSocket.getOutputStream());

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
            if (req.substring(0, 10).equals("API:LOGIN:")) return RequestTypes.LOGIN;
        }
        if(req.length() >= 13){
            if (req.substring(0,13).equals("API:REGISTER:")) return RequestTypes.REGISTER;
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
        String res = (r) ? "Succesfully Registered" : "Registration Failed";

        SQL.closeConn();

        return res;
    }

    private static void processMessage(){

    }




}
