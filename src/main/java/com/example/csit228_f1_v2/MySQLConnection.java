package com.example.csit228_f1_v2;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;
import java.sql.*;

public class MySQLConnection {
    //Connection connection;
    public static final String[] URL = {"jdbc:mysql://localhost:3306/dbwow", "jdbc:mysql://localhost:3306/dbwow"};
    public static final String[] username = {"root", "User42"};
    public static final String[] password = {null, ""};
    static final int who = 1;

    public static Connection getConnection() {
        Connection c = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            c = DriverManager.getConnection(URL[who], username[who], password[who]);
            System.out.print("Connection Successful");
        } catch (SQLException e) {
            System.err.println(e.getSQLState());
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return c;
    }

    public static boolean findUser(String user, String pass) {
        Connection c = null;
        try {
            c = MySQLConnection.getConnection();
            PreparedStatement statement = c.prepareStatement(
                    "SELECT password FROM tblusers WHERE username=?"
            );
            statement.setString(1, user);
            System.out.println(" - Read Data Successful");
            ResultSet res = statement.executeQuery();
            if (res.next()) {
                if (res.getString(1).equals(pass)) {
                    return true;
                } else {
                    System.out.println("Incorrect Password.");
                    return false;
                }
            }
            System.out.println("Username not found.");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return false;
    }

    public static void addUser(String user, String pass) {
        Connection c = null;
        try {
            c = MySQLConnection.getConnection();
            PreparedStatement statement = c.prepareStatement(
                    "INSERT INTO tblusers (username, password, isAdmin) VALUES (?, ?, false)"
            );
            statement.setString(1, user);
            statement.setString(2, pass);
            int rowsInserted = statement.executeUpdate();
            System.out.println(" - Rows Inserted: " + rowsInserted);

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            if (c != null) {
                try {
                    c.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void nukeDB() {
        Connection c = null;
        try {
            c = MySQLConnection.getConnection();
            PreparedStatement statement = c.prepareStatement(
                    "DELETE FROM tblusers WHERE isAdmin = false"
            );
            int rowsDeleted = statement.executeUpdate();
            System.out.println(" - Rows Deleted: " + rowsDeleted);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            if (c != null) {
                try {
                    c.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        addAdmin();
    }

    public static void addAdmin() {
        Connection c = null;
        try {
            c = MySQLConnection.getConnection();
            PreparedStatement statement2 = c.prepareStatement(
            "INSERT INTO tblusers (username, password, isAdmin) " +
                "VALUES (\"admin\", \"admin\", TRUE) "
             );
            if (statement2.execute())
                System.out.println(" - Admin Inserted");
        } catch (SQLException e) {
            e.printStackTrace();
        throw new RuntimeException(e);
        } finally {
            if (c != null) {
                try {
                    c.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println();
    }
    // NOTE: if "java.lang.ClassNotFoundException: com.mysql.cj.jdbc.Driver"
    // then do files > project structure > libraries > add > maven > mysql.connector.j
}
