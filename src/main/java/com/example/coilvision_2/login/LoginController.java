package com.example.coilvision_2.login;

import com.example.coilvision_2.daoManager.DatabaseConnector;
import com.example.coilvision_2.dashboard.DashboardApplication;
import com.example.coilvision_2.dashboard.DashboardControleur;
import com.example.coilvision_2.engineer.EngineerApplication;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.sql.*;

public class LoginController implements EventHandler<ActionEvent> {

    private static Connection conn;
    private static PreparedStatement ps;
    private static ResultSet rs;

    private static javafx.scene.control.TextField l1;

    private static javafx.scene.control.PasswordField l2;

    private int getPrivilege() {
        return this.privilege;
    }

    private int privilege;

    private LoginApplication HA;
    private String TextField;

    public LoginController(javafx.scene.control.TextField usernameField, javafx.scene.control.PasswordField passwordField) {
        this.l1 = usernameField;
        this.l2 = passwordField;

        System.out.println("user : "+usernameField.toString());
        System.out.println("pswd : "+passwordField.toString());

    }

    public void setTextField(String textField) {
        this.TextField = textField;
    }

    public void setPasswordField(String textField) {
        this.TextField = textField;
    }

    public void setHA(LoginApplication HA){
        this.HA = HA;
    }

    public String getTextField() {
        return TextField;
    }

    public String getPasswordField() {
        return TextField;
    }

    public LoginApplication getHA(){
        return this.HA;
    }

    @FXML
    private Button loginButton;


    @Override
    public void handle(ActionEvent event) {

        assert(this.l1.getText() != "" || this.l2.getText() != "");

        //Cette ligne est très importante!
        //Elle permet d'afficher la fenêtre suivante!

        try {
            DatabaseConnector connector = new DatabaseConnector();
            // Vérification de l'existence d'un utilisateur dans la table
            String username = this.l1.getText();
            String password = this.l2.getText();
            int privilege = connector.checkUser(username, password);
            this.privilege = privilege;
            if (privilege == 1) {
                System.out.println("User authenticated, privilege level: " + privilege);
                DashboardApplication dashboardApplication = new DashboardApplication();
                dashboardApplication.setPrivilege(privilege);
                dashboardApplication.setLogin_dash(username);
                try {
                    // Call the start method to launch the Dashboard window
                    DatabaseConnector.filling_h2_database();
                    dashboardApplication.start(new Stage());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (privilege == 2) {
                System.out.println("User authenticated, privilege level: " + privilege);
                EngineerApplication engineerApplication = new EngineerApplication();
                engineerApplication.setPrivilege(String.valueOf(privilege));
                engineerApplication.setUser(username);
                try {
                    // Call the start method to launch the Dashboard window
                    DatabaseConnector.filling_h2_database();
                    engineerApplication.start(new Stage());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (privilege == -1) {
                System.out.println("User not found or invalid password");
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Attention");
                alert.setHeaderText(null);
                alert.setContentText("Nom d'utilisateur ou mot de passe incorrect.");
                alert.showAndWait();
            }
            connector.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}
