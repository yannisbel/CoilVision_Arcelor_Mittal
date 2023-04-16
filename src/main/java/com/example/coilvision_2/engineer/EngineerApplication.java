package com.example.coilvision_2.engineer;

import com.example.coilvision_2.daoManager.DatabaseConnector;
import javafx.animation.Interpolator;
import javafx.animation.ScaleTransition;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import javafx.util.Duration;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static com.example.coilvision_2.daoManager.DatabaseConnector.createDashboardTable;

public class EngineerApplication extends Application {

    private String privilege;

    public String getPrivilege() {
        return privilege;
    }

    public void setPrivilege(String privilege) {
        this.privilege = privilege;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    private String user;

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Récupérer les données de la table DASHBOARD
        List<DashboardData> dashboardDataList = getAllDashboardData();

        // Créer une TableView pour afficher les données
        TableView<DashboardData> dashboardTable = new TableView<>();
        dashboardTable.setItems(FXCollections.observableArrayList(dashboardDataList));

        // Ajouter les colonnes pour les données
        TableColumn<DashboardData, String> loginCol = new TableColumn<>("Login");
        loginCol.setCellValueFactory(new PropertyValueFactory<>("login"));

        TableColumn<DashboardData, Boolean> isReadCol = new TableColumn<>("Is_Read");
        isReadCol.setCellValueFactory(new PropertyValueFactory<>("isRead"));

        TableColumn<DashboardData, String> objectCol = new TableColumn<>("Text_Object");
        objectCol.setCellValueFactory(new PropertyValueFactory<>("textObject"));

        TableColumn<DashboardData, String> reportCol = new TableColumn<>("Report");
        reportCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<DashboardData, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<DashboardData, String> param) {
                return new SimpleStringProperty("Voir le rapport");
            }
        });

        TableColumn<DashboardData, Integer> graphNumberCol = new TableColumn<>("Graph_Number");
        graphNumberCol.setCellValueFactory(new PropertyValueFactory<>("graphNumber"));

        TableColumn<DashboardData, Integer> reportNumberCol = new TableColumn<>("Report_Number");
        reportNumberCol.setCellValueFactory(new PropertyValueFactory<>("reportNumber"));

        dashboardTable.getColumns().addAll(loginCol, objectCol, reportCol, graphNumberCol, reportNumberCol);

        reportCol.setCellFactory(new Callback<TableColumn<DashboardData, String>, TableCell<DashboardData, String>>() {
            @Override
            public TableCell<DashboardData, String> call(TableColumn<DashboardData, String> param) {
                return new TableCell<DashboardData, String>() {
                    final Button btn = new Button();

                    {
                        btn.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {
                                DashboardData data = getTableView().getItems().get(getIndex());
                                String report = data.getReport();
                                // Créer une nouvelle fenêtre pour afficher le rapport
                                Stage reportStage = new Stage();
                                Label reportLabel = new Label(report);
                                reportLabel.setWrapText(true);
                                VBox reportRoot = new VBox();
                                reportRoot.setPadding(new Insets(10));
                                reportRoot.setSpacing(10);
                                reportRoot.getChildren().add(reportLabel);
                                Scene reportScene = new Scene(reportRoot, 600, 400);
                                reportStage.setScene(reportScene);
                                reportStage.setTitle("Rapport");
                                reportStage.show();
                            }
                        });
                    }

                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (!empty && item != null) {
                            // Créer un bouton pour ouvrir le rapport
                            Button viewReportBtn = new Button("Voir le rapport");
                            viewReportBtn.setStyle("-fx-font-size: 12px; -fx-background-color: #9a1414; -fx-text-fill: white;");
                            viewReportBtn.setOnAction(event -> {
                                // Récupérer les données de la ligne sélectionnée
                                DashboardData data = getTableView().getItems().get(getIndex());

                                // Créer une nouvelle fenêtre pour afficher le rapport
                                Stage reportStage = new Stage();
                                reportStage.initStyle(StageStyle.TRANSPARENT);

                                // Conteneur principal pour la fenêtre de rapport
                                StackPane reportRoot = new StackPane();
                                reportRoot.setPadding(new Insets(20));
                                reportRoot.setStyle("-fx-background-color: rgb(154,20,20); -fx-background-radius: 10px;");

                                // Titre de la fenêtre de rapport
                                Label titleLabel = new Label("Rapport");
                                titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: white;");

                                // Conteneur pour l'objet et le rapport
                                VBox contentBox = new VBox();
                                contentBox.setSpacing(10);
                                contentBox.setAlignment(Pos.CENTER);

                                // Objet
                                Label objectLabel = new Label("Objet :");
                                objectLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: white;");
                                Label objectValueLabel = new Label(data.getTextObject());
                                objectValueLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: white;");
                                contentBox.getChildren().addAll(objectLabel, objectValueLabel);

                                // Rapport
                                Label reportLabel = new Label("Rapport :");
                                reportLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fil: white;");
                                TextArea reportTextArea = new TextArea();
                                reportTextArea.setEditable(false);
                                reportTextArea.setPrefRowCount(20);
                                reportTextArea.setText(data.getReport());
                                reportTextArea.setStyle("-fx-font-size: 16px; -fx-text-fill: black; -fx-background-color: transparent; -fx-border-color: white;");
                                contentBox.getChildren().addAll(reportLabel, reportTextArea);

                                // Ajouter les éléments à la racine de la fenêtre de rapport
                                reportRoot.getChildren().addAll(titleLabel, contentBox);

                                // Animation d'ouverture de la fenêtre de rapport
                                ScaleTransition st = new ScaleTransition(Duration.millis(200), reportRoot);
                                st.setFromX(0);
                                st.setFromY(0);
                                st.setToX(1);
                                st.setToY(1);
                                st.setInterpolator(Interpolator.EASE_BOTH);

                                // Animation de fermeture de la fenêtre de rapport
                                ScaleTransition stClose = new ScaleTransition(Duration.millis(200), reportRoot);
                                stClose.setFromX(1);
                                stClose.setFromY(1);
                                stClose.setToX(0);
                                stClose.setToY(0);
                                stClose.setInterpolator(Interpolator.EASE_BOTH);

                                // Fermer la fenêtre de rapport en cliquant à l'extérieur
                                reportRoot.setOnMouseClicked(e -> {
                                    if (e.getButton() == MouseButton.PRIMARY) {
                                        stClose.play();
                                        stClose.setOnFinished(event1 -> reportStage.close());
                                    }
                                });

                                // Afficher la fenêtre de rapport
                                Scene reportScene = new Scene(reportRoot, 800, 600);
                                reportStage.setScene(reportScene);
                                reportStage.show();
                            });

                            setGraphic(viewReportBtn);
                        } else {
                            setGraphic(null);
                        }
                    }
                };
            }
        });


// Créer un conteneur pour la TableView et le bouton
        VBox root = new VBox();
        root.setPadding(new Insets(10));
        root.setSpacing(10);
        root.setAlignment(Pos.CENTER);

// Titre
        Label titleLabel = new Label("Liste des rapports");
        titleLabel.setFont(Font.font("VAG Rounded", FontWeight.LIGHT, 30));
        root.getChildren().add(titleLabel);

// TableView
        root.getChildren().add(dashboardTable);

// Bouton de suppression
        Button deleteButton = new Button("Supprimer le rapport sélectionné");
        deleteButton.setStyle("-fx-font-size: 16px; -fx-background-color: #9a1414; -fx-text-fill: white;");
        deleteButton.setOnAction(event -> {
            ObservableList<DashboardData> selectedReports = dashboardTable.getSelectionModel().getSelectedItems();
            dashboardTable.getItems().removeAll(selectedReports);
            System.out.println(selectedReports.get(0).getTextObject());
            try {
                DatabaseConnector.deleteDashboardRow(selectedReports.get(0).getTextObject());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        root.getChildren().add(deleteButton);

        // Bouton de déconnexion
        Button deconnexion = new Button("Se déconnecter");
        deconnexion.setStyle("-fx-font-size: 16px; -fx-background-color: #9a1414; -fx-text-fill: white;");
        deconnexion.setOnAction(event -> {
            primaryStage.close();
        });
        root.getChildren().add(deconnexion);

// Afficher la scène
        Scene scene = new Scene(root, 560, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Dashboard");
        primaryStage.show();
    }

    public static void main(String[] args) throws SQLException {
        launch(args);
    }

    public static List<DashboardData> getAllDashboardData() throws SQLException {
        List<DashboardData> dashboardDataList = new ArrayList<>();
        String query = "SELECT * FROM dashboard";
        DatabaseConnector db = new DatabaseConnector();
        try( Connection connection = db.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                String login = resultSet.getString("LOGIN");
                int numF = resultSet.getInt("NUM_F");
                boolean isRead = resultSet.getBoolean("IS_READ");
                String object = resultSet.getString("TEXT_OBJECT");
                String report = resultSet.getString("REPORT");
                int graphNumber = resultSet.getInt("GRAPH_NUMBER");
                int reportNumber = resultSet.getInt("REPORT_NUMBER");

                DashboardData dashboardData = new DashboardData(login, numF, isRead, object, report, graphNumber, reportNumber);
                dashboardDataList.add(dashboardData);
            }
        }
        return dashboardDataList;
    }

}

