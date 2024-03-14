package JDBC;

import java.sql.*;

public class MySQLservice {

    private static final String CONN_STRING = "jdbc:mysql://localhost:3306";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";

    public static MySQLservice instance = null;

    private Connection connection;

    private MySQLservice(){
        try {
            connection = DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);
        }catch (SQLException e){
            System.err.println("Error during establishing connection to SQL server");
        }

    }

    public static synchronized MySQLservice getInstance(){
        if(instance == null){
            instance = new MySQLservice();
        }
        return instance;
    }

    public void closeConn() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                instance = null;
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public String AuthRequest(String username, String password){

        try {

            PreparedStatement statement = connection.prepareStatement("" +
                    "SELECT username, password " +
                    "FROM chat.users " +
                    "WHERE username = ?");

            statement.setString(1, username);

            ResultSet resultSet = statement.executeQuery();

            if(resultSet.next()){
                if(resultSet.getString("password").equals(password)){
                    System.out.println("Auth Successful SQL says yes");
                    return "Success";
                }
                else{
                    System.out.println("Auth Unsuccessful SQL wrong pass");
                    return "Wrong Password";
                }
            }
            else{
                System.out.println("Auth Unsuccessful SQL wrong user");
                return "Wrong Username";
            }

        }catch(SQLException e){
            System.err.println("Error while retrieving login info");
            return "Authentication error";
        }
    }

    public boolean RegisterRequest(String name, String last_name, String username, String password, String phone_number){

        int inserted = 0;

        try {
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO chat.users (name,last_name,username,password,phone_number) " +
                            "VALUES (?, ? , ? , ? , ?)");
            statement.setString(1, name);
            statement.setString(2, last_name);
            statement.setString(3, username);
            statement.setString(4, password);
            statement.setString(5, phone_number);

            inserted = statement.executeUpdate();
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return inserted > 0;
    }




}
