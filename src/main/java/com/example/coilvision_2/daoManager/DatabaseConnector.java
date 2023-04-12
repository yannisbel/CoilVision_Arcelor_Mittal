package com.example.coilvision_2.daoManager;

import java.io.*;
import java.sql.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;


public class DatabaseConnector {
    private static Connection connection;

    public Connection getConnection(){
        return this.connection;
    }

    public DatabaseConnector() throws SQLException {
        // Connexion à la base de données
        String url = "jdbc:h2:~/test";
        String user = "username";
        String password = "password";
        connection = DriverManager.getConnection(url, user, password);
    }

    public int checkUser(String username, String password) throws SQLException {
        // Requête pour vérifier si les données utilisateur sont présentes dans la table
        String query = "SELECT PRIVILEGE FROM CONNEXION WHERE LOGIN = ? AND PASSWORD = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, username);
        statement.setString(2, password);
        ResultSet result = statement.executeQuery();
        if (result.next()) {
            return result.getInt("PRIVILEGE");
        } else {
            return -1;
        }
    }

    public void close() throws SQLException {
        // Fermeture de la connexion
        connection.close();
    }

    /*

    CREATE TABLE DATA_IN2_OROWAN (
    Cas int,
    He Double,
    Hs Double,
    Te Double,
    Ts Double,
    Diam_WR Double,
    WRyoung Double,
    offset Double,
    mu_ini Double,
    Force Double,
    G Double
);

INSERT INTO DATA_IN2_OROWAN (Cas, He, Hs, Te, Ts, Diam_WR, WRyoung, offset, mu_ini, Force, G)
SELECT 1, EnThick, ExThick, EnTens, ExTens, Diameter, YoungModulus, AverageSigma, Mu, RollForce, FSlip FROM DATA_IN;

     */

    public static void createTableDataInF2(String table) throws SQLException {
        String createTableQuery = "CREATE TABLE IF NOT EXISTS "+ table +" ("
                + "Lp INT, "
                + "MatID INT, "
                + "xTime Double, "
                + "xLoc DOUBLE, "
                + "EnThick DOUBLE, "
                + "ExThick DOUBLE, "
                + "EnTens DOUBLE, "
                + "ExTens DOUBLE, "
                + "RollForce DOUBLE, "
                + "FSlip DOUBLE, "
                + "Diameter DOUBLE, "
                + "RolledLengthWorkRolls DOUBLE, "
                + "youngModulus DOUBLE, "
                + "BackupRollDiameter DOUBLE, "
                + "RolledLengthBackupRolls DOUBLE, "
                + "mu DOUBLE, "
                + "torque DOUBLE, "
                + "averageSigma DOUBLE, "
                + "inputError DOUBLE, "
                + "LubWFlUp DOUBLE, "
                + "LubWFlLo DOUBLE, "
                + "LubOilFlUp DOUBLE, "
                + "LubOilFlLo DOUBLE, "
                + "WorkRollSpeed DOUBLE)";

        Statement statement = connection.createStatement();
        statement.execute(createTableQuery);
        connection.commit();
    }



    public void drop_table(String table) throws SQLException {
        String s = "DROP TABLE IF EXISTS "+table+";";
        Statement statement = connection.createStatement();
        statement.execute(s);
        connection.commit();
    }

    public static List<String[]> readCsv(String filePath) {
        List<String[]> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(";");
                lines.add(values);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lines;
    }

    public static void csv_data_in_F2(String url, String table) throws SQLException {
        List<String[]> csvData = readCsv(url);
        int n = csvData.size();
        System.out.println("taille du CSV : " + n);
        int i;

        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:h2:~/test", "username", "password");

            for (i = 0; i < n; i++) {
                String[] values = csvData.get(i);
                int length = csvData.get(i).length;

                String sql = "INSERT INTO " + table + " (Lp, MatID, xTime, xLoc, EnThick, ExThick, EnTens, ExTens, RollForce, FSlip, Diameter, RolledLengthWorkRolls, youngModulus, BackupRollDiameter, RolledLengthBackupRolls, mu, torque, averageSigma, inputError, LubWFlUp, LubWFlLo, LubOilFlUp, LubOilFlLo, WorkRollSpeed) " +
                        "VALUES (";

                sql += Integer.parseInt(values[0].trim()); // Lp en entier
                sql += ", ";
                sql += Integer.parseInt(values[1].trim()); // MatID en entier

                for (int j = 2; j < length - 1; j++) {
                    sql += ", ";
                    sql += Double.parseDouble(values[j].trim());
                }

                sql += ");";

                Statement statement = connection.createStatement();
                statement.execute(sql);
                connection.commit();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    public static void create_selected_column_table(String table) throws SQLException {
        String s = "CREATE TABLE "+ table + " (\n" +
                "    Cas INT,\n" +
                "    He DOUBLE,\n" +
                "    Hs DOUBLE,\n" +
                "    Te DOUBLE,\n" +
                "    Ts DOUBLE,\n" +
                "    Diam_WR DOUBLE,\n" +
                "    WRyoung DOUBLE,\n" +
                "    offset_value DOUBLE,\n" +
                "    mu_ini DOUBLE,\n" +
                "    Force DOUBLE,\n" +
                "    G DOUBLE\n" +
                ");";
        Statement statement = connection.createStatement();
        statement.execute(s);
        connection.commit();
    }

    public static void filling_data_in_orowan(String table) throws SQLException {
        String s = "INSERT INTO DATA_IN2_OROWAN (Cas, He, Hs, Te, Ts, Diam_WR, WRyoung, offset_value, mu_ini, Force, G)\n" +
                "SELECT 1, EnThick, ExThick, EnTens, ExTens, Diameter, YoungModulus, AverageSigma, Mu, RollForce, FSlip FROM "+ table +";\n";
        Statement statement = connection.createStatement();
        statement.execute(s);
        connection.commit();
    }

    public static void filling_h2_database(){
        try {DatabaseConnector db = new DatabaseConnector();
            db.drop_table("DATA_IN2");
            db.drop_table("DATA_IN2_OROWAN");
            db.createTableDataInF2("DATA_IN2");
            DatabaseConnector.csv_data_in_F2("src/main/java/com/example/Fichiers/Krakov/csv_in/1939351_F2.csv", "DATA_IN2");
            DatabaseConnector.create_selected_column_table("DATA_IN2_OROWAN");
            DatabaseConnector.filling_data_in_orowan("DATA_IN2");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void export_dataIn_from_h2() throws SQLException {
        String s = "CALL CSVWRITE('src\\main\\java\\com\\example\\Fichiers\\Model\\inv_cst_data2.csv',  'SELECT * FROM DATA_IN2_OROWAN',  'charset=UTF-8 fieldSeparator=; fieldDelimiter=');";
        Statement statement = connection.createStatement();
        statement.execute(s);
        connection.commit();
    }

    public static void CsvToTxtConverter() {
        String csvFilePath = "src/main/java/com/example/Fichiers/Model/inv_cst_data2.csv";
        String txtFilePath = "src/main/java/com/example/Fichiers/Model/inv_cst_data2.txt";
        File csvFile = new File(csvFilePath);
        File txtFile = new File(txtFilePath);

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile));
             BufferedWriter bw = new BufferedWriter(new FileWriter(txtFile))) {

            String line;
            while ((line = br.readLine()) != null) {
                // Remplacez les virgules par des tabulations pour chaque ligne
                line = line.replace(";", "\t");
                bw.write(line + "\n"); // Écriture de chaque ligne dans le fichier txt
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void createTableDataOutOrowan(String table) throws SQLException {

        String createTableQuery = "CREATE TABLE IF NOT EXISTS "+ table +" ("
                + " TestCase INT NOT NULL, " +
                " Errors TEXT NOT NULL, " +
                " OffsetYield DOUBLE NOT NULL, " +
                " Friction DOUBLE NOT NULL, " +
                " Rolling_Torque DOUBLE NOT NULL, " +
                " Sigma_Moy DOUBLE NOT NULL, " +
                " Sigma_Ini DOUBLE NOT NULL, " +
                " Sigma_Out DOUBLE NOT NULL, " +
                " Sigma_Max DOUBLE NOT NULL, " +
                " Force_Error DOUBLE NOT NULL, " +
                " Slip_Error DOUBLE NOT NULL, " +
                " Has_Converged TEXT NOT NULL)";

        Statement statement = connection.createStatement();
        statement.execute(createTableQuery);
        connection.commit();
    }

    public static void csv_data_out_F2(String url, String table) throws SQLException {
        List<String[]> csvData = readCsv(url);
        int n = csvData.size();
        System.out.println("taille du CSV : " + n);
        int i;

        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:h2:~/test", "username", "password");

            for (i = 1; i < n; i++) {
                String[] values = csvData.get(i);
                int length = csvData.get(i).length;

                String sql = "INSERT INTO " + table + " (TestCase, Errors, OffsetYield, Friction, Rolling_Torque, Sigma_Moy, Sigma_Ini, Sigma_Out, Sigma_Max, Force_Error, Slip_Error, Has_Converged) " +
                        "VALUES (";

                // Enclose integer values in single quotes
                sql += Integer.parseInt(values[0].trim()); // Case
                sql += ", ";
                sql += "'" + values[1].trim() + "'"; // Errors

                for (int j = 2; j < length - 1; j++) {
                    sql += ", ";
                    sql += Double.parseDouble(values[j].trim());
                }

                sql += ", ";
                sql += "'" + values[length - 1].trim() + "'"; // Has_Converged
                sql += ");";

                Statement statement = connection.createStatement();
                statement.execute(sql);
                connection.commit();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    /*

    public static void callExecutable() throws IOException, InterruptedException {

        String entryFileName = "src/main/java/com/example/Fichiers/Model/inv_cst_data2.txt";
        String exitFileName = "src/main/java/com/example/Fichiers/Model/output_data2.txt";

        String[] cmd = {
                "src/main/java/com/example/Fichiers/Model/Orowan_x64.exe",
                "i",
                "c",
                entryFileName,
                exitFileName
        };

        Process process = new ProcessBuilder(cmd).start();
        process.waitFor();

        try {
            File entryFile = new File(entryFileName);
            File exitFile = new File(exitFileName);
            exitFile.createNewFile();
            if (exitFile.exists()) {
                exitFile.delete();
            }
            entryFile.renameTo(exitFile);
        } catch (SecurityException e) {
            System.err.println("Cannot write to the output file: " + e.getMessage());
        }
    }

     */

    public static List<Double> getDataFromH2_Friction() throws SQLException {

        // Exécution de la requête pour récupérer les données de la table DATA_OUT_OROWAN
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT AVG_Friction FROM DATA_OUT_AVG");

        // Récupération des valeurs de la colonne Friction
        List<Double> data = new ArrayList<>();
        while (rs.next()) {
            data.add(rs.getDouble("AVG_Friction"));
        }

        // Fermeture des ressources
        rs.close();
        stmt.close();
        connection.close();

        return data;
    }

    public static List<Double> getDataFromH2_RollSpeed() throws SQLException {

        // Exécution de la requête pour récupérer les données de la table DATA_OUT_OROWAN
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT avg_roll_speed FROM DATA_OUT_AVG");

        // Récupération des valeurs de la colonne Friction
        List<Double> data = new ArrayList<>();
        while (rs.next()) {
            data.add(rs.getDouble("avg_roll_speed"));
        }

        // Fermeture des ressources
        rs.close();
        stmt.close();
        connection.close();

        return data;
    }

    public static List<Double> getDataFromH2_Sigma() throws SQLException {

        // Exécution de la requête pour récupérer les données de la table DATA_OUT_OROWAN
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT avg_sigma FROM DATA_OUT_AVG");

        // Récupération des valeurs de la colonne Friction
        List<Double> data = new ArrayList<>();
        while (rs.next()) {
            data.add(rs.getDouble("avg_sigma"));
        }

        // Fermeture des ressources
        rs.close();
        stmt.close();
        connection.close();

        return data;
    }

    public void create_avg_table() throws SQLException {
        // Requête SQL pour créer la table DATA_OUT_AVG
        String createTableSql = "CREATE TABLE DATA_OUT_AVG (" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "avg_friction FLOAT," +
                "avg_roll_speed FLOAT," +
                "avg_sigma FLOAT," +
                "time FLOAT" +
                ")";

        Statement statement = connection.createStatement();
        statement.execute(createTableSql);
        connection.commit();
    }

    public void compute_avg_data() {

        // Requête SQL pour sélectionner les données de la table DATA_OUT_OROWAN
        String selectDataSql = "SELECT Friction, ROLLING_TORQUE, SIGMA_MOY FROM DATA_OUT_OROWAN";

        // Requête SQL pour insérer les données dans la table DATA_OUT_AVG
        String insertDataSql = "INSERT INTO DATA_OUT_AVG (avg_friction, avg_roll_speed, avg_sigma, time) VALUES (?, ?, ?, ?)";

        try (PreparedStatement selectDataStatement = connection.prepareStatement(selectDataSql);
             PreparedStatement insertDataStatement = connection.prepareStatement(insertDataSql)) {

            // Sélectionner les données de la table DATA_OUT_OROWAN
            ResultSet resultSet = selectDataStatement.executeQuery();

            // Initialiser les variables pour le calcul de la moyenne
            float sum_fri = 0;
            float sum_roll = 0;
            float sum_sigm = 0;
            int count = 0;
            int time = 0;

            // Parcourir les données
            while (resultSet.next()) {
                // Ajouter la valeur à la somme
                sum_fri += resultSet.getFloat("Friction");
                sum_roll += resultSet.getFloat("ROLLING_TORQUE");
                sum_sigm += resultSet.getFloat("SIGMA_MOY");
                count++;

                // Si on a atteint 5 valeurs, calculer la moyenne et l'insérer dans la table DATA_OUT_AVG
                if (count == 5) {
                    float avgFriction = sum_fri / 5;
                    float avgRoll = sum_roll / 5;
                    float avgSigm = sum_sigm / 5;
                    insertDataStatement.setFloat(1, avgFriction);
                    insertDataStatement.setFloat(2, avgRoll);
                    insertDataStatement.setFloat(3, avgSigm);
                    insertDataStatement.setFloat(4, time);
                    insertDataStatement.executeUpdate();
                    count = 0;
                    sum_fri = 0;
                    sum_roll = 0;
                    sum_sigm = 0;
                    time++;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void execute_computing() throws SQLException {
        DatabaseConnector.export_dataIn_from_h2();
        DatabaseConnector.CsvToTxtConverter();
    }

    public static void main(String[] args) throws SQLException {
        DatabaseConnector db = new DatabaseConnector();
        db.create_avg_table();
        db.compute_avg_data();
    }

}
