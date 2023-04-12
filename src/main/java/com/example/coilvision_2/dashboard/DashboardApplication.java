package com.example.coilvision_2.dashboard;

import com.example.coilvision_2.daoManager.DatabaseConnector;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.sql.SQLException;
import java.util.List;

public class DashboardApplication extends Application {

    private int privilege;

    public int getPrivilege() {
        return privilege;
    }

    public void setPrivilege(int privilege) {
        this.privilege = privilege;
    }

    public DashboardApplication() {
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        // Création du texte de bienvenue
        Text welcomeText = new Text("Bienvenue sur CoilVision !");
        welcomeText.setFont(Font.font("VAG Rounded", FontWeight.LIGHT, 30));

        // Création de la transition
        FadeTransition transition = new FadeTransition(Duration.seconds(2), welcomeText);
        transition.setFromValue(0);
        transition.setToValue(1);
        transition.setOnFinished(event -> {
            // Création de la scène principale
            BorderPane root = new BorderPane();
            root.setPadding(new Insets(10, 10, 10, 10));
            Scene scene = new Scene(root, 1100, 900);
            primaryStage.setScene(scene);

            // Ajout du titre de la scène
            Text titleText = new Text("CoilVision");
            titleText.setFont(Font.font("VAG Rounded", FontWeight.LIGHT, 36));
            VBox titleBox = new VBox(titleText);
            titleBox.setAlignment(Pos.CENTER);
            root.setTop(titleBox);

            // Ajout de la description
            Text descriptionText = new Text("Ce graphique montre l'évolution de la friction en fonction de x.");
            descriptionText.setFont(Font.font("VAG Rounded", 20));
            VBox descriptionBox = new VBox(descriptionText);
            descriptionBox.setAlignment(Pos.CENTER);
            root.setCenter(descriptionBox);

            // Changer la couleur de fond du titre
            titleBox.setStyle("-fx-background-color: #a9a7a7; -fx-background-radius: 5; -fx-padding: 10;");
            titleText.setFill(Color.valueOf("9A1414FF"));

            // Changer la couleur de fond de la description
            descriptionBox.setStyle("-fx-background-color: #9a1414; -fx-background-radius: 5; -fx-padding: 10;");

            // Changer la couleur de fond de la scène
            scene.setFill(Color.WHITE);

            // Création du bouton de sélection déroulante
            ChoiceBox<String> selectDataBox = new ChoiceBox<>();
            selectDataBox.getItems().addAll("Coefficient de Friction", "Roll Speed", "Sigma");
            selectDataBox.setValue("Coefficient de Friction");

            // Création du graphique
            final NumberAxis xAxis = new NumberAxis();
            final NumberAxis yAxis = new NumberAxis();
            xAxis.setLabel("Temps (s)");
            yAxis.setLabel(selectDataBox.getValue());
            final LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
            lineChart.setPrefSize(1100, 600);

            // Modification du style du lineChart pour utiliser les couleurs d'ArcelorMittal
            lineChart.setStyle("-fx-background-color: #ffffff; -fx-border-color: #9a1414; -fx-border-width: 2px;");
            lineChart.setHorizontalGridLinesVisible(true);
            lineChart.setVerticalGridLinesVisible(false);
            lineChart.setLegendVisible(false);

            // Création de la série de données
            XYChart.Series<Number, Number> dataSeries = new XYChart.Series<>();

            DashboardControleur.addDataToSeries_Friction(dataSeries);

            selectDataBox.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
                switch (newValue) {
                    case "Coefficient de Friction":
                        dataSeries.getData().clear();
                        DashboardControleur.addDataToSeries_Friction(dataSeries);
                        yAxis.setLabel("Coefficient de Friction");
                        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1), dataSeries.getNode());
                        fadeTransition.setFromValue(0.0);
                        fadeTransition.setToValue(1.0);
                        fadeTransition.play();
                        break;
                    case "Roll Speed":
                        dataSeries.getData().clear();
                        DashboardControleur.addDataToSeries_RollSpeed(dataSeries);
                        yAxis.setLabel("Roll Speed");
                        FadeTransition fadeTransition1 = new FadeTransition(Duration.seconds(1), dataSeries.getNode());
                        fadeTransition1.setFromValue(0.0);
                        fadeTransition1.setToValue(1.0);
                        fadeTransition1.play();
                        break;
                    case "Sigma":
                        dataSeries.getData().clear();
                        DashboardControleur.addDataToSeries_Sigma(dataSeries);
                        yAxis.setLabel("Sigma");
                        FadeTransition fadeTransition2 = new FadeTransition(Duration.seconds(1), dataSeries.getNode());
                        fadeTransition2.setFromValue(0.0);
                        fadeTransition2.setToValue(1.0);
                        fadeTransition2.play();
                        break;
                    default:
                        break;
                }
            });

            // Ajout de la série de données au graphique
            lineChart.getData().add(dataSeries);
            dataSeries.getNode().setStyle("-fx-stroke: darkred;");

            // Bouton de déconnexion
            Button disconnectButton = new Button("Déconnexion");

            disconnectButton.setOnAction(event2 -> {
                primaryStage.close();
            });


            // Création de la grille
            GridPane gridPane = new GridPane();
            gridPane.setPadding(new Insets(10, 10, 10, 10));
            gridPane.setVgap(10);
            gridPane.setHgap(10);

            // Ajout des éléments à la grille
            gridPane.add(lineChart, 0, 0, 2, 1);
            gridPane.add(new Label("Choix de la donnée à afficher :"), 0, 2, 1, 1);
            gridPane.add(selectDataBox, 1, 2, 1, 1);
            gridPane.add(disconnectButton, 1, 2, 1, 1);

            // Centrage des éléments en largeur
            GridPane.setHalignment(titleBox, HPos.CENTER);
            GridPane.setHalignment(descriptionBox, HPos.CENTER);
            GridPane.setHalignment(lineChart, HPos.CENTER);
            GridPane.setHalignment(disconnectButton, HPos.RIGHT);
            GridPane.setHgrow(lineChart, Priority.ALWAYS);

            // Ajout de la grille au centre du BorderPane
            root.setCenter(gridPane);

            // Création de la barre horizontale
            Separator separator = new Separator(Orientation.HORIZONTAL);

            // Création du label avec le nom d'utilisateur et le privilège
            Label userLabel = new Label("Utilisateur : " + "username" + " (" + privilege + ")");
            userLabel.setFont(Font.font("Arial", 14));

            // Création de l'HBox contenant le label et le séparateur
            HBox userBox = new HBox(userLabel, separator);
            userBox.setAlignment(Pos.CENTER);
            userBox.setPadding(new Insets(5));

            // Ajout de l'HBox en tant que noeud pour le setBottom() de la scène
            root.setBottom(userBox);

            // Centrage de la scène sur l'écran
            Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
            double centerX = primaryScreenBounds.getWidth() / 2 - scene.getWidth() / 2;
            double centerY = primaryScreenBounds.getHeight() / 2 - scene.getHeight() / 2;
            primaryStage.setX(centerX);
            primaryStage.setY(centerY);

            // Affichage de la scène
            primaryStage.centerOnScreen();
            primaryStage.show();
        });

        // Création du layout de la scène
        StackPane layout = new StackPane(welcomeText);
        layout.setAlignment(Pos.CENTER);

        // Création de la scène de bienvenue
        Scene welcomeScene = new Scene(layout, 600, 400);
        primaryStage.setScene(welcomeScene);
        primaryStage.centerOnScreen();
        primaryStage.show();

        // Lancement de la transition
        transition.play();
    }

    public static void main(String[] args) {
        launch(args);
    }
}