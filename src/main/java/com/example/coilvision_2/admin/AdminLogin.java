package com.example.coilvision_2.admin;

import com.example.coilvision_2.admin.AdminApplication;
import com.example.coilvision_2.dashboard.DashboardApplication;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.Random;

public class AdminLogin extends Application {
    private final String ADMIN_EMAIL = "belkhiter@yahoo.com";
    private final String ADMIN_PASSWORD = "pTkqQFYMacRD61UW";

    private final String LOGIN_PSWD = "admin";

    private TextField usernameTextField;
    private PasswordField passwordField;

    @Override
    public void start(Stage primaryStage) {
        // Créer une grille pour la mise en page
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        // Ajouter un titre à la page
        Label scenetitle = new Label("Admin Login");
        scenetitle.setFont(Font.font("VAG Rounded", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 0, 0, 2, 1);

        // Ajouter des champs de saisie pour le nom d'utilisateur et le mot de passe
        Label usernameLabel = new Label("Username:");
        usernameLabel.setFont(Font.font("VAG Rounded", FontWeight.NORMAL, 14));
        grid.add(usernameLabel, 0, 1);
        usernameTextField = new TextField();
        grid.add(usernameTextField, 1, 1);

        Label passwordLabel = new Label("Password:");
        passwordLabel.setFont(Font.font("VAG Rounded", FontWeight.NORMAL, 14));
        grid.add(passwordLabel, 0, 2);
        passwordField = new PasswordField();
        grid.add(passwordField, 1, 2);

        // Ajouter des boutons pour se connecter et annuler
        Button loginButton = new Button("Login");
        loginButton.setStyle("-fx-font-size: 18px; -fx-background-color: #B22222; -fx-text-fill: white;");
        HBox hbLoginButton = new HBox(10);
        hbLoginButton.setAlignment(Pos.BOTTOM_RIGHT);
        hbLoginButton.getChildren().add(loginButton);
        grid.add(hbLoginButton, 1, 4);

        Button cancelButton = new Button("Cancel");
        cancelButton.setStyle("-fx-font-size: 18px; -fx-background-color: #B22222; -fx-text-fill: white;");
        HBox hbCancelButton = new HBox(10);
        hbCancelButton.setAlignment(Pos.BOTTOM_RIGHT);
        hbCancelButton.getChildren().add(cancelButton);
        grid.add(hbCancelButton, 0, 4);

        // Gérer l'action de connexion
        loginButton.setOnAction(event -> {
            String username = usernameTextField.getText();
            String password = passwordField.getText();

            if (username.equals("admin") && password.equals(LOGIN_PSWD)) {
                // Envoyer un e-mail de double authentification avec un code
                String code = generateCode();
                sendEmailCode(code);

                // Créer une nouvelle page pour la saisie du code de double authentification
                createCodeVerificationPage(primaryStage, code);
            } else {
                // Afficher un message d'erreur si les informations d'identification sont incorrectes
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Invalid credentials");
                alert.setContentText("The username or password you entered is incorrect.");
                alert.showAndWait();
            }
        });

        // Gérer l'action d'annulation
        cancelButton.setOnAction(event -> {
            System.exit(0);
        });

        // Créer la scène et afficher la fenêtre
        Scene scene = new Scene(grid, 400, 275);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Génère un code de 6 chiffres pour la double authentification
     */
    private String generateCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }

    /**
     * Envoie un e-mail de double authentification contenant le code spécifié
     */
    private void sendEmailCode(String code) {
        // Configurez les propriétés SMTP
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp-relay.sendinblue.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        // Créer une session avec l'authentification SMTP
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(ADMIN_EMAIL, ADMIN_PASSWORD);
            }
        });

        try {
            // Créer le message e-mail
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(ADMIN_EMAIL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("belkhiter@yahoo.com"));
            message.setSubject("Double authentication code");
            message.setText("Your double authentication code is: " + code);

            // Envoyer le message e-mail
            Transport.send(message);
            System.out.println("Email sent.");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    /**
     * Crée une page de saisie de code de double authentification
     */
    private void createCodeVerificationPage(Stage primaryStage, String expectedCode) {
        // Créer une grille pour la mise en page
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        // Ajouter un titre à la page
        Label scenetitle = new Label("Double Authentication");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 0, 0, 2, 1);

        // Ajouter un champ de saisie pour le code
        Label codeLabel = new Label("Code:");
        grid.add(codeLabel, 0, 1);
        TextField codeTextField = new TextField();
        grid.add(codeTextField, 1, 1);

        // Ajouter un bouton pour la vérification de code
        Button verifyButton = new Button("Verify");
        HBox hbVerifyButton = new HBox(10);
        hbVerifyButton.setAlignment(Pos.BOTTOM_RIGHT);
        hbVerifyButton.getChildren().add(verifyButton);
        grid.add(hbVerifyButton, 1, 4);

        // Gérer l'action de vérification de code
        verifyButton.setOnAction(event -> {
            String code = codeTextField.getText();

            if (code.equals(expectedCode)) {
                // Fermer les pages de connexion et de double authentification
                primaryStage.close();

                // Ouvrir la page du tableau de bord de l'application
                AdminApplication admin = new AdminApplication();
                try {
                    admin.setUsername(String.valueOf(this.usernameTextField.getText()));
                    admin.start(new Stage());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            } else {
                // Afficher un message d'erreur si le code est incorrect
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Invalid Code");
                alert.setContentText("The code you entered is incorrect. Please try again.");
                alert.showAndWait();
            }
        });

        // Créer la scène et afficher la fenêtre
        Scene scene = new Scene(grid, 400, 275);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

}
