package com.example.coilvision_2.login;

import com.example.coilvision_2.admin.AdminLogin;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class LoginApplication extends Application {

    private Scene scene;
    private BorderPane layout;

    public static String getUsernameField() {
        return usernameField.toString();
    }

    public static void setUsernameField(TextField usernameField) {
        LoginApplication.usernameField = usernameField;
    }

    public static String getPasswordField() {
        return passwordField.toString();
    }

    public static void setPasswordField(PasswordField passwordField) {
        LoginApplication.passwordField = passwordField;
    }

    static TextField usernameField;
    static PasswordField passwordField;

    private Button loginButton;
    private PieChart chart;

    @Override
    public void start(Stage stage) throws Exception {
        // Créer la fenêtre de préchargement
        Stage preloaderStage = new Stage();
        preloaderStage.initOwner(stage);
        preloaderStage.initStyle(StageStyle.UNDECORATED);

        // Créer les éléments de la fenêtre de préchargement
        Label preloaderLabel = new Label("Chargement en cours...");
        preloaderLabel.setFont(Font.font("VAG Rounded", 18));
        preloaderLabel.setTextFill(Color.WHITE);

        ProgressBar preloaderProgressBar = new ProgressBar();
        preloaderProgressBar.setPrefWidth(220);

        ImageView logoImageView = new ImageView("/white_logo.png");

        logoImageView.setFitWidth(200);
        logoImageView.setPreserveRatio(true);

        // Créer la mise en page de la fenêtre de préchargement
        VBox preloaderBox = new VBox(20, logoImageView, preloaderProgressBar, preloaderLabel);
        preloaderBox.setAlignment(Pos.CENTER);
        preloaderBox.setPadding(new Insets(20));
        preloaderBox.setStyle("-fx-background-color: #333333;");

        // Créer la scène de la fenêtre de préchargement
        Scene preloaderScene = new Scene(preloaderBox, 400, 300);

        // Afficher la fenêtre de préchargement
        preloaderStage.setScene(preloaderScene);
        preloaderStage.show();

        // Simuler un chargement en tâche de fond
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                // Ici, vous pouvez ajouter du code pour simuler le chargement de vos données.
                // Dans cet exemple, nous allons simplement attendre 5 secondes.
                Thread.sleep(3000);
                return null;
            }
        };

        // Mettre à jour la barre de progression pendant le chargement
        preloaderProgressBar.progressProperty().bind(task.progressProperty());

        // Fermer la fenêtre de préchargement et afficher la scène principale une fois que le chargement est terminé
        task.setOnSucceeded(event -> {
            preloaderStage.close();
            afficherScenePrincipale(stage);
        });
        // Démarrer la tâche de chargement
        new Thread(task).start();
    }

    private void afficherScenePrincipale(Stage stage) {

        // Création de l'image du logo d'ArcelorMittal
        Image logoImage = new Image("/logo.png");

        // Créer une ImageView pour afficher l'image
        ImageView imageView = new ImageView(logoImage);
        imageView.setFitWidth(300); // ajuster la largeur de l'image pour qu'elle s'adapte à votre interface utilisateur
        imageView.setPreserveRatio(true); // conserver le ratio de l'image pour éviter toute distorsion

        // Création des éléments graphiques pour le nom d'utilisateur
        Label usernameLabel = new Label("Nom d'utilisateur");
        usernameLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        TextField usernameField = new TextField();

        // Création des éléments graphiques pour le mot de passe
        Label passwordLabel = new Label("Mot de passe");
        passwordLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        PasswordField passwordField = new PasswordField();

        // Création de l'élément graphique pour le bouton de connexion
        Button loginButton = new Button("Se connecter");
        loginButton.setStyle("-fx-font-size: 18px; -fx-background-color: #B22222; -fx-text-fill: white;");
        LoginController HC = new LoginController(usernameField, passwordField);
        HC.setHA(this);
        loginButton.setOnAction(HC);

        // Création de l'élément graphique pour le bouton Administrateur
        Button adminButton = new Button("Administrateur");
        adminButton.setStyle("-fx-font-size: 18px; -fx-background-color: #B22222; -fx-text-fill: white;");
        adminButton.setOnAction(event -> {
            // Fermer la scène actuelle
            stage.close();

            // Ouvrir une nouvelle scène AdminLogin
            AdminLogin adminLogin = new AdminLogin();
            adminLogin.start(new Stage());
        });



        // Création de la grille pour positionner les éléments graphiques
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(20);
        gridPane.setPadding(new Insets(40));
        gridPane.setAlignment(Pos.CENTER);
        gridPane.add(imageView, 0, 0, 2, 1); // ajouter l'image à la quatrième ligne de la grille, sur deux colonnes
        gridPane.add(usernameLabel, 0, 1);
        gridPane.add(usernameField, 1, 1);
        gridPane.add(passwordLabel, 0, 2);
        gridPane.add(passwordField, 1, 2);
        gridPane.add(loginButton, 1, 3);
        gridPane.add(adminButton, 0, 3);

        // Création de la zone d'affichage principale
        BorderPane layout = new BorderPane();
        layout.setCenter(gridPane);
        layout.setStyle("-fx-background-image: url('/coil2.jpg'); -fx-background-size: cover;");

        // Création de la transition de type FadeTransition pour faire apparaître la grille
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(2), gridPane);
        fadeTransition.setFromValue(0.0);
        fadeTransition.setToValue(1.0);
        fadeTransition.play();

        // Création de la scène et affichage de la fenêtre
        Scene scene = new Scene(layout, 720, 480);
        stage.setScene(scene);
        stage.setTitle("CoilVision");
        stage.show();

        // Add mouse event handler to change cursor when hovering over button
        loginButton.setOnMouseEntered(e -> {
            scene.setCursor(Cursor.HAND);
        });

        loginButton.setOnMouseExited(e -> {
            scene.setCursor(Cursor.DEFAULT);
        });

    }

    private void afficherDonnees() {
        // Code pour récupérer et afficher les données
        chart = new PieChart();
        chart.getData().add(new PieChart.Data("Données 1", 25));
        chart.getData().add(new PieChart.Data("Données 2", 75));

        VBox dataBox = new VBox(10);
        dataBox.getChildren().add(chart);
        dataBox.setAlignment(Pos.CENTER);

        layout.setCenter(dataBox);
    }

    public static void main(String[] args) throws Exception {
        launch();
        //System.out.println(usernameField);
        //System.out.println(passwordField);

    }

}