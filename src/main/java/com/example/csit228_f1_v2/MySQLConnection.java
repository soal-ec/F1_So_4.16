package com.example.csit228_f1_v2;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;
import java.sql.*;

public class MySQLConnection {
    //Connection connection;

    public static Connection getConnection() {
        Connection c = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            c = DriverManager.getConnection(Words.URL[Words.who], Words.username[Words.who], Words.password[Words.who]);
            ///System.out.print("Connection Successful");
        } catch (SQLException e) {
            System.err.println(e.getSQLState());
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return c;
    }

    public static boolean findSheet(String user, String pass) {
        try (Connection c = MySQLConnection.getConnection();
             PreparedStatement statement = c.prepareStatement(
             "SELECT password FROM tblsheets WHERE username=?"
             )) {
            statement.setString(1, user);
            System.out.println("Read Data Successful");
            ResultSet res = statement.executeQuery();
            if (res.next()) {
                if (res.getString(1).equals(String.valueOf(pass.hashCode()))) {
                    return true;
                } else {
                    System.out.println("Incorrect Password.");
                    return false;
                }
            }
            System.out.println("Username not found.");
        } catch (SQLException e) {
                missingTable(e);
        }
        return false;
    }

        public static void addSheet(String user, String pass) {
        try (Connection c = MySQLConnection.getConnection();
             PreparedStatement statement = c.prepareStatement(
             "INSERT INTO tblsheets (username, password) " +
                 "VALUES (?, ?)"
             )) {
            statement.setString(1, user);
            statement.setString(2, String.valueOf(pass.hashCode()));
            int rowsInserted = statement.executeUpdate();
            System.out.println("Rows Inserted: " + rowsInserted);

        } catch (SQLException e) {
            missingTable(e);
        }
    }

    public static void nukeDB() {
        try (Connection c = MySQLConnection.getConnection();
             PreparedStatement statement = c.prepareStatement(
             "DELETE FROM tblsheets"
             )) {
            int rowsDeleted = statement.executeUpdate();
            System.out.println("Rows Deleted: " + rowsDeleted);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static void createTable() {
        try (Connection c = MySQLConnection.getConnection();
             PreparedStatement statement = c.prepareStatement(
             "CREATE TABLE tblsheets (" +
                 "id INT AUTO_INCREMENT PRIMARY KEY,\n" +
                 "username VARCHAR(100) NOT NULL UNIQUE,\n" +
                 "password VARCHAR(100) NOT NULL, \n" +
                 "name VARCHAR(100), \n" +
                     "con INT, \n" +
                     "str INT, \n" +
                     "wis INT \n" +
                 ")"
             )) {
            boolean b = statement.execute();
            if (b) {
                System.out.println("New Table Created");
            }
        } catch (SQLException e) {
            missingTable(e);
        }
    }

    static void missingTable(SQLException e)  {
        if (e.getMessage().contains("Table 'dbwow.tblsheets' doesn't exist")) {
            createTable();
        } else {
            e.printStackTrace();
        }
    }

    // NOTE: if "java.lang.ClassNotFoundException: com.mysql.cj.jdbc.Driver"
    // then do files > project structure > libraries > add > maven > mysql.connector.j
}
