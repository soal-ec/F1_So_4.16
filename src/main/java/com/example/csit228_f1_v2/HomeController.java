package com.example.csit228_f1_v2;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

public class HomeController {

    public static GridPane gridCharSheet = new GridPane();
    public Button btnUpdate= new Button("Update");
    public Button btnExit = new Button("Exit");
    public static Label lb0 = new Label();
    public static Label lb1 = new Label();
    public static Label lb2 = new Label();
    public static Label lb3 = new Label();
    public TextField tf0 = new TextField();
    public TextField tf1 = new TextField();
    public TextField tf2 = new TextField();
    public TextField tf3 = new TextField();

//    public ToggleButton tbNight;
    public static ProgressIndicator piProgress = new ProgressIndicator();
    public static int progress;


//    public void onSliderChange() {
//        double val = slSlider.getValue();
//        System.out.println(val);
//        piProgress.setProgress(val/100);
//        if (val == 100) {
//            System.exit(0);
//        }
//    }
//    public void onNightModeClick() {
//        if (tbNight.isSelected()) {
//            tbNight.getParent().setStyle("-fx-background-color: BLACK");
//            tbNight.setText("DAY");
//        } else {
//            tbNight.getParent().setStyle("-fx-background-color: WHITE");
//            tbNight.setText("NIGHT");
//        }
//    }

    @FXML
    public static void initialize() {
        String[] ses = MySQLConnection.getSession();
        lb0.setText("Name: " + ses[0]);
        lb0.setTextFill(Paint.valueOf(Words.TXTCOLOR));
        lb0.setFont(Font.font(Words.SMALLTEXT));
        // lb.layout();
        //gridCharSheet.add(lb0, 0, 0);
        System.out.println(lb0.getText());
        lb1.setText("CON : " + ses[1]);
        lb1.setTextFill(Paint.valueOf(Words.TXTCOLOR));
        lb1.setFont(Font.font(Words.SMALLTEXT));
        // lb.layout();
        //gridCharSheet.add(lb1, 0, 1);
        System.out.println(lb1.getText());
        lb2.setText("STR : " + ses[2]);
        lb2.setTextFill(Paint.valueOf(Words.TXTCOLOR));
        lb2.setFont(Font.font(Words.SMALLTEXT));
        // lb.layout();
        //gridCharSheet.add(lb2, 0, 2);
        System.out.println(lb2.getText());
        lb3.setText("WIS : " + ses[3]);
        lb3.setTextFill(Paint.valueOf(Words.TXTCOLOR));
        lb3.setFont(Font.font(Words.SMALLTEXT));
        // lb.layout();
        //gridCharSheet.add(lb3, 0, 3);
        System.out.println(lb3.getText());

        for (int i = 0; i < 4; i++) {
            if (!Objects.equals(ses[i], "-1") && ses[i] != null) {
                progress++;
            }
        }
        gridCharSheet.layout();
        progress = 0;
        piProgress.setProgress((double) progress /4);
    }
    public void update() {
        System.out.println("Updating...");
        String[] tfses = new String[4];
        if (tf0.getText() != null) {
            tfses[0] = tf0.getText();
        }
        if (tf1.getText() != null) {
            tfses[1] = tf1.getText();
        }
        if (tf2.getText() != null) {
            tfses[2] = tf2.getText();
        }
        if (tf3.getText() != null) {
            tfses[3] = tf3.getText();
        }
        MySQLConnection.updateSheet(tfses);
        // get db text
        String[] dbses = MySQLConnection.getSession();
        // db text to left text
        lb0.setText("Name: " + dbses[0]);
        lb1.setText("CON : " + dbses[1]);
        lb2.setText("STR : " + dbses[2]);
        lb3.setText("WIS : " + dbses[3]);
        //System.out.println(lb0 + ", " + lb1 + ", " + lb2 + ", " + lb3);
        gridCharSheet.layout();
        for (int i = 0; i < 4; i++) {
            if (!Objects.equals(dbses[i], "-1") && dbses[i] != null) {
                progress++;
            }
        }
        piProgress.layout();
    }
    public void exitSession()  {
        Stage stage = (Stage) btnExit.getScene().getWindow();
        stage.close();
        Parent p;
        try {
            p = FXMLLoader.load(getClass().getResource("home-view.fxml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //HelloApplication.sceneStack
        Scene s = new Scene(p);
        stage.setScene(s);
        stage.show();
    }
}
