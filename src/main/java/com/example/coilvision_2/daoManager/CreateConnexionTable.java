package com.example.coilvision_2.daoManager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CreateConnexionTable {
    private static final String CREATE_TABLE_QUERY =
            "CREATE TABLE CONNEXION (" +
                    "    CATEGORIE VARCHAR(255)," +
                    "    PRIVILEGE INT," +
                    "    LOGIN VARCHAR(255)," +
                    "    PASSWORD VARCHAR(255)" +
                    ")";

    private static final String INSERT_QUERY =
            "INSERT INTO CONNEXION (CATEGORIE, PRIVILEGE, LOGIN, PASSWORD) VALUES (?, ?, ?, ?)";

    public static void main(String[] args) throws SQLException {
        try {
            // Charger le driver JDBC
            Class.forName("org.h2.Driver");

            // Se connecter à la base de données
            Connection connection = DriverManager.getConnection("jdbc:h2:~/test", "username", "password");

            // Créer la table CONNEXION
            PreparedStatement createTableStatement = connection.prepareStatement(CREATE_TABLE_QUERY);
            createTableStatement.execute();
            System.out.println("La table CONNEXION a été créée.");

            // Insérer des données dans la table CONNEXION
            PreparedStatement insertStatement = connection.prepareStatement(INSERT_QUERY);
            insertStatement.setString(1, "ingénieur");
            insertStatement.setInt(2, 2);
            insertStatement.setString(3, "engineer");
            insertStatement.setString(4, "password");
            insertStatement.execute();

            insertStatement.setString(1, "technicien");
            insertStatement.setInt(2, 1);
            insertStatement.setString(3, "technician");
            insertStatement.setString(4, "password");
            insertStatement.execute();

            System.out.println("Des données ont été insérées dans la table CONNEXION.");

            // Fermer la connexion
            connection.close();
        } catch (ClassNotFoundException e) {
            System.err.println("Impossible de charger le driver JDBC.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Erreur SQL : " + e.getMessage());
            e.printStackTrace();
        }
        DatabaseConnector db = new DatabaseConnector();
        db.createDashboardTable();
    }
}
