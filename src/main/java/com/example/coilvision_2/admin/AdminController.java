package com.example.coilvision_2.admin;

import com.example.coilvision_2.daoManager.DatabaseConnector;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.sql.*;

public class AdminController {

    private static final String TABLE_NAME = "CONNEXION";

    private static final String JDBC_DRIVER = "org.h2.Driver";
    private static final String DB_URL = "jdbc:h2:~/test";
    private static final String USER = "username";
    private static final String PASS = "password";

    static void initializeDatabase() {
        try {
            Class.forName(JDBC_DRIVER);
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            Statement stmt = conn.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                    " (LOGIN VARCHAR(255) PRIMARY KEY, " +
                    "PASSWORD VARCHAR(255), " +
                    "PRIVILEGE INT, CATEGORIE VARCHAR(255))";
            stmt.executeUpdate(sql);
            sql = "SELECT * FROM " + TABLE_NAME;
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                String login = rs.getString("LOGIN");
                String password = rs.getString("PASSWORD");
                int privilege = rs.getInt("PRIVILEGE");
                User user = new User(login, password, privilege);
                AdminApplication.addUserList(user);
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void addUser(String login, String password, int privilege) {
        try {
            Class.forName(JDBC_DRIVER);
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            String sql = "INSERT INTO " + TABLE_NAME +
                    " (LOGIN, PASSWORD, PRIVILEGE, CATEGORIE) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, login);
            pstmt.setString(2, password);
            pstmt.setInt(3, privilege);
            pstmt.setString(4, "NA");
            pstmt.executeUpdate();
            pstmt.close();

            // SELECT user list after inserting new user
            ObservableList<User> userList = FXCollections.observableArrayList();
            String selectSql = "SELECT * FROM " + TABLE_NAME;
            ResultSet rs = conn.createStatement().executeQuery(selectSql);
            while (rs.next()) {
                userList.add(new User(rs.getString("LOGIN"), rs.getString("PASSWORD"), rs.getInt("PRIVILEGE")));
            }
            AdminApplication.addTable(userList);

            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void removeUser(String login) {
        try {
            Class.forName(JDBC_DRIVER);
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            String sql = "DELETE FROM " + TABLE_NAME + " WHERE LOGIN=?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, login);
            pstmt.executeUpdate();
            pstmt.close();

            String sql2 = "DELETE FROM DASHBOARD WHERE LOGIN=?";
            PreparedStatement pstmt2 = conn.prepareStatement(sql2);
            pstmt2.setString(1, login);
            pstmt2.executeUpdate();
            pstmt2.close();

            // SELECT user list after removing user
            ObservableList<User> userList = FXCollections.observableArrayList();
            String selectSql = "SELECT * FROM " + TABLE_NAME;
            ResultSet rs = conn.createStatement().executeQuery(selectSql);
            while (rs.next()) {
                userList.add(new User(rs.getString("LOGIN"), rs.getString("PASSWORD"), rs.getInt("PRIVILEGE")));
            }
            AdminApplication.addTable(userList);

            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void updateUserLogin(String oldLogin, String newLogin) {
        try {
            Class.forName(JDBC_DRIVER);
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            String sql = "UPDATE " + TABLE_NAME + " SET LOGIN=? WHERE LOGIN=?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, newLogin);
            pstmt.setString(2, oldLogin);
            pstmt.executeUpdate();
            pstmt.close();

            // SELECT user list after updating user login
            ObservableList<User> userList = FXCollections.observableArrayList();
            String selectSql = "SELECT * FROM " + TABLE_NAME;
            ResultSet rs = conn.createStatement().executeQuery(selectSql);
            while (rs.next()) {
                userList.add(new User(rs.getString("LOGIN"), rs.getString("PASSWORD"), rs.getInt("PRIVILEGE")));
            }
            AdminApplication.addTable(userList);

            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void updateUserPassword(String login, String newPassword) {
        try {
            Class.forName(JDBC_DRIVER);
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            String sql = "UPDATE " + TABLE_NAME + " SET PASSWORD=? WHERE LOGIN=?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, newPassword);
            pstmt.setString(2, login);
            pstmt.executeUpdate();
            pstmt.close();

            // SELECT user list after updating user password
            ObservableList<User> userList = FXCollections.observableArrayList();
            String selectSql = "SELECT * FROM " + TABLE_NAME;
            ResultSet rs = conn.createStatement().executeQuery(selectSql);
            while (rs.next()) {
                userList.add(new User(rs.getString("LOGIN"), rs.getString("PASSWORD"), rs.getInt("PRIVILEGE")));
            }
            AdminApplication.addTable(userList);

            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void updateUserPrivilege(String login, int newPrivilege) {
        try {
            Class.forName(JDBC_DRIVER);
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            String sql = "UPDATE " + TABLE_NAME + " SET PRIVILEGE=? WHERE LOGIN=?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, newPrivilege);
            pstmt.setString(2, login);
            pstmt.executeUpdate();
            pstmt.close();

            // SELECT user list after updating user privilege
            ObservableList<User> userList = FXCollections.observableArrayList();
            String selectSql = "SELECT * FROM " + TABLE_NAME;
            ResultSet rs = conn.createStatement().executeQuery(selectSql);
            while (rs.next()) {
                userList.add(new User(rs.getString("LOGIN"), rs.getString("PASSWORD"), rs.getInt("PRIVILEGE")));
            }
            AdminApplication.addTable(userList);

            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deconnexion(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    // Create Logout Button
    public static Button createLogoutButton() {
        Button logoutBtn = new Button("Logout");
        logoutBtn.setOnAction(event -> {
            AdminController.deconnexion(event);
        });
        return logoutBtn;
    }



}
