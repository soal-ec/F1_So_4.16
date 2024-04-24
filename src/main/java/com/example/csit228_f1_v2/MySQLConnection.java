package com.example.csit228_f1_v2;

import java.sql.*;

public class MySQLConnection {
    static Integer session_id;

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

    public static int logInSheet(String user, String pass) {
        try (Connection c = MySQLConnection.getConnection();
             PreparedStatement statement = c.prepareStatement(
                     "SELECT ID,password FROM tblsheets WHERE username=?"
             )) {
            statement.setString(1, user);
            //System.out.println("Read Data Successful");
            ResultSet res = statement.executeQuery();
            if (res.next()) {
                if (res.getString("password").equals(String.valueOf(pass.hashCode()))) {
                    session_id = res.getInt("ID");
                    return res.getInt("ID");
                } else {
                    System.out.println("Incorrect Password.");
                    return -1;
                }
            }
            System.out.println("Username not found.");
        } catch (SQLException e) {
            missingTable(e);
        }
        return -1;
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

        } catch (SQLIntegrityConstraintViolationException e) {
            System.err.println("User already exists");
        } catch (SQLException e) {
            missingTable(e);
        }
    }
    public static void updateSheet(String[] ses) {
        try (Connection c = MySQLConnection.getConnection();
             PreparedStatement statement = c.prepareStatement(
                     "UPDATE tblsheets SET name=?, con=?, str=?, wis=? WHERE id=?"
             )) {
            statement.setString(1, ses[0]);
            statement.setString(2, ses[1]);
            statement.setString(3, ses[2]);
            statement.setString(4, ses[3]);
            statement.setString(5, String.valueOf(session_id));
            boolean yeah = statement.execute();
            // System.out.println("Updated: " + yeah);

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

    public static String[] getSession() {
        String[] ses = new String[4];
        try (Connection c = MySQLConnection.getConnection();
             PreparedStatement statement = c.prepareStatement(
                     "SELECT * FROM tblsheets WHERE id=?"
             )) {
            statement.setString(1, String.valueOf(session_id));
            ResultSet res = statement.executeQuery();
            while (res.next()) {
                ses[0] = res.getString("name");
                ses[1] = res.getString("con");
                ses[2] = res.getString("str");
                ses[3] = res.getString("wis");
            }
//            for (int i = 0; i < 4; i++){
//                System.out.println(ses[i]);
//            }

            //System.out.println("Read Data Successful");
            return ses;
        } catch (SQLException e) {
            missingTable(e);
        }
        return ses;
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
