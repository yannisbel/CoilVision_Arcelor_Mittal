package com.example.coilvision_2.model;

import java.io.IOException;
import java.sql.*;
import java.util.List;

import com.example.coilvision_2.daoManager.DatabaseConnector;
import com.opencsv.exceptions.CsvValidationException;

public class SaveCSVToH2 {
    public static void main(String[] args) throws SQLException, IOException, CsvValidationException {

        try {DatabaseConnector db = new DatabaseConnector();
            db.drop_table("DATA_IN2");
            db.drop_table("DATA_IN3");
            db.drop_table("DATA_OUT2_OROWAN");
            db.drop_table("DATA_OUT3_OROWAN");
            db.createTableDataInF2("DATA_IN2");
            DatabaseConnector.csv_data_in_F2("src/main/java/com/example/Fichiers/Krakov/csv_in/1939351_F2.csv", "DATA_IN2");
            db.createTableDataInF2("DATA_IN3");
            DatabaseConnector.csv_data_in_F3("src/main/java/com/example/Fichiers/Krakov/csv_in/1939351_F3.csv", "DATA_IN3");
            DatabaseConnector.execute_computing();
            //DatabaseConnector.callExecutable();
            db.createTableDataOutOrowan("DATA_OUT2_OROWAN");
            DatabaseConnector.csv_data_out_F2("src/main/java/com/example/Fichiers/Model/output2.csv", "DATA_OUT2_OROWAN");
            db.createTableDataOutOrowan("DATA_OUT3_OROWAN");
            DatabaseConnector.csv_data_out_F2("src/main/java/com/example/Fichiers/Model/output3.csv", "DATA_OUT3_OROWAN");
            db.create_avg_table("DATA_OUT2_AVG");
            db.compute_avg_data(2);
            db.create_avg_table("DATA_OUT3_AVG");
            db.compute_avg_data(3);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
