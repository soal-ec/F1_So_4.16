package com.example.csit228_f1_v2;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLConnection {
    static Connection connection;
    public static final String[] URL = {"jdbc:mysql://localhost:3306/wowdb", "jdbc:mysql://localhost:3306/user42/"};
    public static final String[] hostname = {"localhost", "user42"};
    public static final String[] username = {"root", "User42"};
    public static final String[] password = {null, "wowzer"};
    static final int who = 0;

    public static Connection getConnection() {
        Connection c = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            c = DriverManager.getConnection(URL[who], username[who], password[who]);
            System.out.print("Connection Successful - ");
        } catch (SQLException e) {
            System.err.println(e.getSQLState());
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return c;
    }



}
