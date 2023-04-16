package com.example.coilvision_2.dashboard;

import com.example.coilvision_2.daoManager.DatabaseConnector;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class DashboardControleur {

    private static Timeline timeline; // variable de classe statique pour stocker la timeline
    public int seuil;
    public boolean play_seuil;

    public static void addDataToSeries_Friction(XYChart.Series<Number, Number> dataSeries, int a) {
        List<Double> data = null;
        try {
            DatabaseConnector db = new DatabaseConnector();
            data = db.getDataFromH2_Friction(a);
            dataSeries.setName("Friction");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        for (int i = 0; i < data.size(); i++) {
            XYChart.Data<Number, Number> dataPoint = new XYChart.Data<>(i, data.get(i));
            Circle circle = new Circle(4, Color.web("#7E0023")); // Créer un cercle de rayon 4 et de couleur grenat
            circle.setStroke(Color.BLACK);
            circle.setStrokeWidth(1);
            dataPoint.setNode(circle); // Affecter le cercle personnalisé au point de données
            dataSeries.getData().add(dataPoint);
        }
    }

    public static void addDataToSeries_RollSpeed(XYChart.Series<Number, Number> dataSeries, int a) {
        List<Double> data = null;
        try {
            DatabaseConnector db = new DatabaseConnector();
            data = db.getDataFromH2_RollSpeed(a);
            dataSeries.setName("RollSpeed");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        for (int i = 0; i < data.size(); i++) {
            XYChart.Data<Number, Number> dataPoint = new XYChart.Data<>(i, data.get(i));
            Circle circle = new Circle(4, Color.web("#7E0023")); // Créer un cercle de rayon 4 et de couleur grenat
            circle.setStroke(Color.BLACK);
            circle.setStrokeWidth(1);
            dataPoint.setNode(circle); // Affecter le cercle personnalisé au point de données
            dataSeries.getData().add(dataPoint);
        }
    }

    public static void addDataToSeries_Sigma(XYChart.Series<Number, Number> dataSeries, int a) {
        List<Double> data = null;
        try {
            DatabaseConnector db = new DatabaseConnector();
            data = db.getDataFromH2_Sigma(a);
            dataSeries.setName("Sigma");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        for (int i = 0; i < data.size(); i++) {
            XYChart.Data<Number, Number> dataPoint = new XYChart.Data<>(i, data.get(i));
            Circle circle = new Circle(4, Color.web("#7E0023")); // Créer un cercle de rayon 4 et de couleur grenat
            circle.setStroke(Color.BLACK);
            circle.setStrokeWidth(1);
            dataPoint.setNode(circle); // Affecter le cercle personnalisé au point de données
            dataSeries.getData().add(dataPoint);
        }
    }

    public static void updateDataSeries(XYChart.Series<Number, Number> dataSeries, NumberAxis xAxis, int a, int b) {
        if (timeline != null) {
            timeline.stop(); // Arrêter la timeline existante si elle existe
        }
        List<XYChart.Data<Number, Number>> dataPoints = new ArrayList<>();
        XYChart.Series<Number, Number> dataSeries0 = new XYChart.Series<>();
        if(a == 0){
            addDataToSeries_Friction(dataSeries0, b);
        }
        if(a == 1){
            addDataToSeries_RollSpeed(dataSeries0, b);
        }
        if(a == 2){
            addDataToSeries_Sigma(dataSeries0, b);
        }
        double lastXValue = dataSeries.getData().isEmpty() ? 0 : (double) dataSeries.getData().get(dataSeries.getData().size() - 1).getXValue();
        double xOffset = lastXValue + 1; // décalage pour les nouvelles valeurs de x
        for (int i = 0; i < dataSeries0.getData().size(); i++) {
            XYChart.Data<Number, Number> dataPoint = dataSeries0.getData().get(i);
            dataPoints.add(new XYChart.Data<>(dataPoint.getXValue().doubleValue() + xOffset, dataPoint.getYValue()));
        }

        timeline = new Timeline();
        EventHandler<ActionEvent> addDataHandler = event -> {
            if (!dataPoints.isEmpty()) {
                XYChart.Data<Number, Number> dataPoint = dataPoints.get(0);
                dataSeries.getData().add(new XYChart.Data<>(dataPoint.getXValue(), dataPoint.getYValue()));
                dataPoints.remove(0);
            }
        };

        timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(1), addDataHandler));
        timeline.setCycleCount(dataPoints.size());
        timeline.setOnFinished(event -> {
            // Attendre 2 secondes
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // Relancer la méthode updateDataSeries avec la même série de données
            updateDataSeries(dataSeries, xAxis, a, b);
        });
        timeline.play();
    }

    public static Animation getTimeline() {
        return timeline;
    }

    public static void resetTimeline() {
        timeline.stop();
        timeline = new Timeline();
    }

    static void insertReport(String login, String object, String report, int graph_number) throws SQLException {
        DatabaseConnector db = new DatabaseConnector();
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO DASHBOARD (LOGIN, TEXT_OBJECT, REPORT, GRAPH_NUMBER) VALUES (?, ?, ?, ?)")) {
            stmt.setString(1, login);
            stmt.setString(2, object);
            stmt.setString(3, report);
            stmt.setString(4, String.valueOf(graph_number));
            stmt.executeUpdate();
            System.out.println("Rapport ajouté avec succès");
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'insertion du rapport : " + e.getMessage());
        }
    }

    public void setPlaySeuil(boolean b) {
        this.play_seuil = b;
    }
}
