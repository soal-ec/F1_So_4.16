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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;

public class HelloApplication extends Application {
    //private Stage primaryStage = new Stage();
    //private Stack<Scene> sceneStack = new Stack<>();
    // Integer session_id = -1;
    @Override
    public void start(Stage stage) throws IOException {
//        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login-view.fxml"));
//        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
//        stage.setTitle("Hello!");
//        stage.setScene(scene);

        Text txtWelcome = new Text(Words.TITLE);
        txtWelcome.setFont(Font.font(Words.FONT, FontWeight.EXTRA_BOLD, Words.BIGTEXT));
        txtWelcome.setFill(Paint.valueOf(Words.TXTCOLOR));
        txtWelcome.setTextAlignment(TextAlignment.CENTER);

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setStyle("-fx-background-color: #ffffff;");
        grid.setPadding(new Insets(20));
        grid.add(txtWelcome, 0, 0, 3, 1);
        grid.setBorder(new Border(new BorderStroke(Paint.valueOf(Words.TXTCOLOR), BorderStrokeStyle.SOLID, null, BorderWidths.FULL)));

        txtWelcome.minWidth(grid.getWidth());

        Label lbUsername = new Label("Username: ");
        lbUsername.setTextFill(Paint.valueOf(Words.TXTCOLOR));
        lbUsername.setFont(Font.font(Words.SMALLTEXT));
        grid.add(lbUsername, 0, 1);

        TextField tfUsername = new TextField();
        grid.add(tfUsername, 1, 1);
        tfUsername.setFont(Font.font(16));
//        tfUsername.setMaxWidth(150);

        Label lbPassword = new Label("Password: ");
        lbPassword.setTextFill(Paint.valueOf(Words.TXTCOLOR));
        lbPassword.setFont(Font.font(Words.SMALLTEXT));
        grid.add(lbPassword, 0, 2);

        PasswordField pfPassword = new PasswordField();
        pfPassword.setFont(Font.font(Words.SMALLTEXT));
        grid.add(pfPassword, 1, 2);

        TextField tmpPassword = new TextField(pfPassword.getText());
        tmpPassword.setFont(Font.font(Words.SMALLTEXT));
        grid.add(tmpPassword, 1, 2);
        tmpPassword.setVisible(false);

        ToggleButton btnShow = new ToggleButton("<o>");
        btnShow.setFont(Font.font(Words.FONT, FontWeight.THIN, Words.SMALLTEXT));
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
        btnLogin.setFont(Font.font(Words.FONT, FontWeight.MEDIUM, Words.SMALLTEXT));
        grid.add(btnLogin, 0, 3, 3, 1);

        Button btnRegister = new Button("Register");
        btnRegister.setFont(Font.font(Words.FONT, FontWeight.MEDIUM, Words.SMALLTEXT));
        grid.add(btnRegister, 0, 4, 2, 1);

        Button btnNuke = new Button("Nuke DB");
        btnNuke.setFont(Font.font(Words.FONT, FontWeight.MEDIUM, Words.SMALLTEXT));
        btnNuke.setPrefWidth(btnNuke.getMaxWidth());
        grid.add(btnNuke, 0, 5, 3, 1);

        btnLogin.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                System.out.println("Logging in...");
                String username = tfUsername.getText();
                String password = pfPassword.getText();
                if (MySQLConnection.logInSheet(username, password) != -1) {
                    nextScene("homepage.fxml", btnLogin.getScene().getWindow());
                }
            }
        });

        btnRegister.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                System.out.println("Registering user...");
                String username = tfUsername.getText();
                String password = pfPassword.getText();
                if (username.isEmpty() || password.isEmpty()) {
                    System.out.println("Cannot register empty fields.");
                    return;
                }
                MySQLConnection.addSheet(username, password);
            }
        });

        btnNuke.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                System.out.println("Nuking user database...");
                MySQLConnection.nukeDB();
            }
        });

        Scene scene = new Scene(grid, 700, 500, Paint.valueOf(Words.BGCOLOR));
        stage.setScene(scene);
        scene.setFill(Paint.valueOf(Words.BGCOLOR));
        stage.show();
    }

    public void nextScene(String fxml, Window window) {
        Parent p;
        try {
            p = FXMLLoader.load(getClass().getResource(fxml));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // System.out.println("ses " + session_id);
        // sceneStack.push(primaryStage.getScene());
        //primaryStage.setScene(new Scene(p));
        Stage stage = (Stage) window;
        Scene s = new Scene(p);
        stage.setScene(s);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}