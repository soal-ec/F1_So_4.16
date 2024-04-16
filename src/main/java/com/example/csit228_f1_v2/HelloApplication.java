package com.example.csit228_f1_v2;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
//        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login-view.fxml"));
//        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
//        stage.setTitle("Hello!");
//        stage.setScene(scene);

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        Text txtWelcome = new Text("Welcome to CIT");
        txtWelcome.setFont(Font.font("Chiller", FontWeight.EXTRA_BOLD, 69));
        txtWelcome.setFill(Color.RED);
//        grid.setAlignment();
        grid.setPadding(new Insets(20));
//        grid.
        txtWelcome.setTextAlignment(TextAlignment.CENTER);
        grid.add(txtWelcome, 0, 0, 3, 1);

        Label lbUsername = new Label("Username: ");
        lbUsername.setTextFill(Color.LIGHTSKYBLUE);
        lbUsername.setFont(Font.font(30));
        grid.add(lbUsername, 0, 1);

        TextField tfUsername = new TextField();
        grid.add(tfUsername, 1, 1);
        tfUsername.setFont(Font.font(30));
//        tfUsername.setMaxWidth(150);

        Label lbPassword = new Label("Password");
        lbPassword.setFont(Font.font(30));
        lbPassword.setTextFill(Color.CHARTREUSE);
        grid.add(lbPassword, 0, 2);

        PasswordField pfPassword = new PasswordField();
        pfPassword.setFont(Font.font(30));
        grid.add(pfPassword, 1, 2);

        TextField tmpPassword = new TextField(pfPassword.getText());
        tmpPassword.setFont(Font.font(30));
        grid.add(tmpPassword, 1, 2);
        tmpPassword.setVisible(false);

        ToggleButton btnShow = new ToggleButton("( )");
//        btnShow.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent actionEvent) {
//                if (btnShow.isSelected()) {
//                    tmpPassword.setText(pfPassword.getText());
//                    tmpPassword.setVisible(true);
//                } else {
//                    tmpPassword.setVisible(false);
//                    pfPassword.setText(tmpPassword.getText());
//                }
//            }
//        });
        btnShow.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                tmpPassword.setText(pfPassword.getText());
                tmpPassword.setVisible(true);
            }
        });

        EventHandler<MouseEvent> release = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                tmpPassword.setVisible(false);
                pfPassword.setText(tmpPassword.getText());
            }
        };

        btnShow.setOnMouseReleased(release);
        btnShow.setOnMouseExited(release);
        grid.add(btnShow, 2,2);

        Button btnLogin = new Button("Log In");
        btnLogin.setFont(Font.font(40));
        grid.add(btnLogin, 0, 3, 2, 1);

        Button btnRegister = new Button("Register");
        btnLogin.setFont(Font.font(40));
        grid.add(btnRegister, 0, 4, 2, 1);

        Button btnNuke = new Button("Nuke DB");
        btnLogin.setFont(Font.font(40));
        grid.add(btnNuke, 0, 5, 2, 1);

        btnLogin.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                System.out.println("Logging in...");
                String username = tfUsername.getText();
                String password = pfPassword.getText();
                try (Connection c = MySQLConnection.getConnection()) {
                    Statement statement = c.createStatement();
                    String query = "SELECT username FROM users";
                    statement.execute(query);
                    System.out.println("Read Data Successful");
                    ResultSet res = statement.executeQuery(query);
                    while (res.next()) {
                        if (res.getString("username").equals(username)) {
                            System.out.println("Username found.");
                            statement = c.createStatement();
                            query = "SELECT password FROM users WHERE username == ?";
                            //statement.setString(1, password);
                            statement.execute(query);
                            System.out.println("Read Data Successful");
                            res = statement.executeQuery(query);

                            if (res.getString("password").equals(password)) {
                                System.out.println("Welcome to the club");
                                try {
                                    Parent p = FXMLLoader.load(getClass().getResource("homepage.fxml"));
                                    Scene s = new Scene(p);
                                    stage.setScene(s);
                                    stage.show();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                System.out.println("Incorrect Password.");
                                return;
                            }
                        }
                    }
                    System.out.println("Username not found.");
                } catch (SQLException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }

            }
        });

        btnRegister.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                System.out.println("Registering user...");
                String username = tfUsername.getText();
                String password = pfPassword.getText();
                if (username.equals("") || password.equals("")) {
                    System.out.println("Cannot register empty fields.");
                    return;
                }
                try (Connection c = MySQLConnection.getConnection();
                     PreparedStatement statement = c.prepareStatement(
                             "INSERT INTO users (username, password) VALUES (?, ?)"
                     )) {
                    statement.setString(1, username);
                    statement.setString(2, password);
                    int rowsInserted = statement.executeUpdate();
                    System.out.println("Rows Inserted: " + rowsInserted);

                } catch (SQLException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }
        });

        btnNuke.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                System.out.println("Nuking user database...");
                try (Connection c = MySQLConnection.getConnection();
                     PreparedStatement statement = c.prepareStatement(
                             "DELETE FROM users"
                     )) {
                    int rowsDeleted = statement.executeUpdate();
                    System.out.println("Rows Deleted: " + rowsDeleted);

                } catch (SQLException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }
        });

        Scene scene = new Scene(grid, 700, 500, Color.BLACK);
        stage.setScene(scene);
        scene.setFill(Color.CORNFLOWERBLUE);
        stage.show();
        txtWelcome.minWidth(grid.getWidth());
    }

    public static void main(String[] args) {
        launch();
    }
}