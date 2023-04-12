package com.example.coilvision_2.dashboard;

import com.example.coilvision_2.daoManager.DatabaseConnector;
import javafx.scene.chart.XYChart;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.sql.SQLException;
import java.util.List;

public class DashboardControleur {


    public static void addDataToSeries_Friction(XYChart.Series<Number, Number> dataSeries) {
        List<Double> data = null;
        try {
            DatabaseConnector db = new DatabaseConnector();
            data = db.getDataFromH2_Friction();
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

    public static void addDataToSeries_RollSpeed(XYChart.Series<Number, Number> dataSeries) {
        List<Double> data = null;
        try {
            DatabaseConnector db = new DatabaseConnector();
            data = db.getDataFromH2_RollSpeed();
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

    public static void addDataToSeries_Sigma(XYChart.Series<Number, Number> dataSeries) {
        List<Double> data = null;
        try {
            DatabaseConnector db = new DatabaseConnector();
            data = db.getDataFromH2_Sigma();
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

}
