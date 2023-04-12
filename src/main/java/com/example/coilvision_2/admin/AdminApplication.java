package com.example.coilvision_2.admin;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.sql.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class AdminApplication extends Application {

    private static ObservableList<User> userList = FXCollections.observableArrayList();

    private static TableView<User> table = new TableView<>();

    private String username = "Username";


    @Override
    public void start(Stage stage) throws Exception {
        // Initialize database connection and load data into userList
        AdminController.initializeDatabase();

        // Create UI elements
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        // Table view
        TableColumn<User, String> loginCol = new TableColumn<>("Login");
        loginCol.setCellValueFactory(new PropertyValueFactory<>("login"));
        TableColumn<User, String> passwordCol = new TableColumn<>("Password");
        passwordCol.setCellValueFactory(new PropertyValueFactory<>("password"));
        TableColumn<User, Integer> privilegeCol = new TableColumn<>("Privilege");
        privilegeCol.setCellValueFactory(new PropertyValueFactory<>("privilege"));
        table.getColumns().addAll(loginCol, passwordCol, privilegeCol);
        table.setItems(userList);
        table.setPrefWidth(450);
        table.setPrefHeight(320);

        //Select a user
        Label userLabel = new Label("Sélectionner un utilisateur: ");
        userLabel.setStyle("-fx-text-fill: white;");
        ComboBox<User> userComboBox = new ComboBox<>(FXCollections.observableArrayList(userList));
        userComboBox.setCellFactory(lv -> new ListCell<User>() {
            @Override
            public void updateItem(User item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(item.getLogin());
                }
            }
        });

        // Delete user button
        ComboBox<User> deleteUserField = new ComboBox<>();
        deleteUserField.getItems().addAll(userList);
        deleteUserField.setPromptText("Select user to delete");
        Button deleteUserButton = new Button("Delete User");
        deleteUserButton.setOnAction(event -> {
            User selectedUser = deleteUserField.getValue();
            if (selectedUser != null) {
                AdminController.removeUser(selectedUser.getLogin());
                userList.remove(selectedUser);
                deleteUserField.getItems().remove(selectedUser);
                table.refresh();
                userComboBox.getItems().clear();
                table.getItems().forEach(userComboBox.getItems()::add);
            }
        });

        // Add user button
        TextField loginField = new TextField();
        TextField passwordField = new TextField();
        ComboBox<Integer> privilegeField = new ComboBox<>();
        privilegeField.getItems().addAll(1, 2);
        privilegeField.setValue(1);
        Button addUserButton = new Button("Add User");
        addUserButton.setOnAction(event -> {
            String login = loginField.getText().trim();
            String password = passwordField.getText().trim();
            int privilege = privilegeField.getValue();
            if (!login.isEmpty() && !password.isEmpty()) {
                AdminController.addUser(login, password, privilege);
                loginField.clear();
                passwordField.clear();
                privilegeField.setValue(1);
                deleteUserField.getItems().add(new User(login, password, privilege));
                userComboBox.getItems().add(new User(login, password, privilege));
                table.refresh();
            }
        });

        // Title Label
        Label titleTable = new Label("Liste des utilisateurs :");
        titleTable.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");
        titleTable.setFont(new Font(20));

        // Left top grid
        GridPane leftTopGrid = new GridPane();
        leftTopGrid.setPadding(new Insets(10));
        leftTopGrid.setHgap(10);
        leftTopGrid.setVgap(10);
        leftTopGrid.setAlignment(Pos.CENTER_LEFT);
        Label log = new Label("Login: ");
        log.setStyle("-fx-text-fill: white;");
        leftTopGrid.add(log, 0, 0);
        leftTopGrid.add(loginField, 1, 0);
        Label pass = new Label("Password: ");
        pass.setStyle("-fx-text-fill: white;");
        leftTopGrid.add(pass, 0, 1);
        leftTopGrid.add(passwordField, 1, 1);
        Label priv = new Label("Privilege: ");
        priv.setStyle("-fx-text-fill: white;");
        leftTopGrid.add(priv, 0, 2);
        leftTopGrid.add(privilegeField, 1, 2);
        leftTopGrid.add(addUserButton, 1, 3);

        // Left bottom grid
        GridPane leftBottomGrid = new GridPane();
        leftBottomGrid.setPadding(new Insets(10));
        leftBottomGrid.setHgap(10);
        leftBottomGrid.setVgap(10);
        leftBottomGrid.setAlignment(Pos.CENTER_LEFT);
        leftBottomGrid.add(deleteUserField, 0, 0);
        leftBottomGrid.add(deleteUserButton, 1, 0);

        // Left VBox
        VBox leftVBox = new VBox();
        leftVBox.setAlignment(Pos.CENTER);
        leftVBox.setPadding(new Insets(10));
        leftVBox.setSpacing(10);

        table.setStyle("-fx-background-color: white; -fx-text-background-color: black;");

        leftVBox.getChildren().addAll(titleTable, table, leftTopGrid, leftBottomGrid);

        // Right top HBox
        HBox rightTopHBox = new HBox();
        Button logOut = AdminController.createLogoutButton();
        logOut.setText("Déconnexion");
        rightTopHBox.setAlignment(Pos.TOP_RIGHT);
        //rightTopHBox.fillHeightProperty();
        //rightTopHBox.setPadding(new Insets(10, 10, 0, 0));
        rightTopHBox.getChildren().add(logOut);

        // Create the username field and label
        Label usernameLabel = new Label("Changer le username:");
        usernameLabel.setStyle("-fx-text-fill: white;");
        TextField usernameField = new TextField();

        // Create the password field and label
        Label passwordLabel = new Label("Changer le password:");
        passwordLabel.setStyle("-fx-text-fill: white;");
        PasswordField passwordField2 = new PasswordField();

        // Create the privilege field and label
        Label privilegeLabel = new Label("Changer le privilège:");
        privilegeLabel.setStyle("-fx-text-fill: white;");
        ChoiceBox<String> privilegeChoiceBox = new ChoiceBox<>();
        privilegeChoiceBox.getItems().addAll("Admin", "User");

        // Create the change button
        Button changeLoginButton = new Button("Changer login");
        changeLoginButton.setOnAction(event -> {
            User selectedUser = userComboBox.getValue();
            if (selectedUser != null) {
                String newLogin = usernameField.getText().trim();
                if (!newLogin.isEmpty()) {
                    AdminController.updateUserLogin(selectedUser.getLogin(), newLogin);
                    selectedUser.setLogin(newLogin);
                    table.refresh();
                    userComboBox.getItems().clear();
                    table.getItems().forEach(userComboBox.getItems()::add);
                    deleteUserField.getItems().clear();
                    table.getItems().forEach(deleteUserField.getItems()::add);
                    usernameField.clear();
                }
            }
        });


        // Create the change button
        Button changePasswordButton = new Button("Changer password");
        changePasswordButton.setOnAction(event -> {
            User selectedUser = userComboBox.getValue();
            if (selectedUser != null) {
                String newPassword = passwordField2.getText().trim();
                if (!newPassword.isEmpty()) {
                    AdminController.updateUserPassword(selectedUser.getLogin(), newPassword);
                    selectedUser.setPassword(newPassword);
                    table.refresh();
                    passwordField2.clear();
                }
            }
        });

        // Create the change button
        Button changePrivilegeButton = new Button("Changer privilege");
        changePrivilegeButton.setOnAction(event -> {
            User selectedUser = userComboBox.getValue();
            if (selectedUser != null) {
                String privilege = privilegeChoiceBox.getValue();
                if ("Admin".equals(privilege)) {
                    AdminController.updateUserPrivilege(selectedUser.getLogin(), 2);
                    selectedUser.setPrivilege(2);
                    table.refresh();
                }
                if ("User".equals(privilege)) {
                    AdminController.updateUserPrivilege(selectedUser.getLogin(), 1);
                    selectedUser.setPrivilege(1);
                    table.refresh();
                }
            }
        });

        // Create the login VBox
        VBox loginVBox = new VBox();
        loginVBox.setSpacing(10);
        loginVBox.getChildren().addAll(usernameLabel, usernameField, changeLoginButton);

        // Create the password VBox
        VBox passwordVBox = new VBox();
        passwordVBox.setSpacing(10);
        passwordVBox.getChildren().addAll(passwordLabel, passwordField2, changePasswordButton);

        // Create the privilege VBox
        VBox privilegeVBox = new VBox();
        privilegeVBox.setSpacing(10);
        privilegeVBox.getChildren().addAll(privilegeLabel, privilegeChoiceBox, changePrivilegeButton);

        HBox right0 = new HBox();
        Label titleLabel = new Label("Modifier les utilisateurs :");
        titleLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");
        titleLabel.setFont(new Font(20));
        right0.getChildren().addAll(titleLabel);

        HBox right1 = new HBox();
        right1.getChildren().addAll(userLabel, userComboBox);
        HBox.setMargin(right1, new Insets(10, 0, 0, 10));

        HBox right2 = new HBox();
        right2.getChildren().addAll(loginVBox);
        HBox.setMargin(right2, new Insets(10, 0, 0, 10));

        HBox right3 = new HBox();
        right3.getChildren().addAll(passwordVBox);
        HBox.setMargin(right3, new Insets(10, 0, 0, 10));

        HBox right4 = new HBox();
        right4.getChildren().addAll(privilegeVBox);
        HBox.setMargin(right4, new Insets(10, 0, 0, 10));

        // Right VBox
        VBox rightVBox = new VBox();
        rightVBox.setAlignment(Pos.TOP_RIGHT);
        rightVBox.setSpacing(10);
        Separator separator1 = new Separator();
        separator1.setOrientation(Orientation.HORIZONTAL);
        Separator separator2 = new Separator();
        separator2.setOrientation(Orientation.HORIZONTAL);
        Separator separator3 = new Separator();
        separator3.setOrientation(Orientation.HORIZONTAL);
        rightVBox.getChildren().addAll(rightTopHBox, new VBox(20, right0, right1, separator1, right2, separator2, right3, separator3, right4));

        // Separator
        Separator separator = new Separator();
        separator.setOrientation(Orientation.VERTICAL);

        // Set up border pane
        root.setLeft(leftVBox);
        root.setCenter(separator);
        root.setMargin(rightVBox, new Insets(0, 10, 0, 0));
        root.setRight(rightVBox);

        // Set up border pane
        root.setLeft(leftVBox);
        root.setRight(rightVBox);

        HBox bottomHBox = new HBox();
        bottomHBox.setAlignment(Pos.CENTER);
        bottomHBox.setPadding(new Insets(10, 0, 10, 0));
        bottomHBox.setSpacing(10);

        Image image = new Image(getClass().getResourceAsStream("/white_logo.png"));
        ImageView imageViewl = new ImageView(image);
        ImageView imageViewr = new ImageView(image);
        imageViewl.setFitWidth(85);
        imageViewl.setFitHeight(40);
        imageViewr.setFitWidth(85);
        imageViewr.setFitHeight(40);
        Label userLabelb = new Label("Utilisateur: "+this.username);
        userLabelb.setStyle("-fx-text-fill: white;");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        Label timeLabel = new Label("Heure de connexion: " + LocalTime.now().format(formatter).toString());
        timeLabel.setStyle("-fx-text-fill: white;");

        // Separator
        Separator separatorb = new Separator();
        separatorb.setOrientation(Orientation.HORIZONTAL);

        // Create bottom HBox
        HBox bottomHBoxb = new HBox();
        bottomHBoxb.setAlignment(Pos.CENTER);
        bottomHBoxb.setSpacing(10);
        bottomHBoxb.getChildren().addAll(imageViewl, userLabelb, timeLabel, imageViewr);

        // Create bottom VBox
        VBox bottomVBox = new VBox();
        bottomVBox.setAlignment(Pos.CENTER);
        bottomHBoxb.setSpacing(30);
        bottomVBox.getChildren().addAll(separatorb, bottomHBoxb);

        // Add bottom VBox to root
        root.setBottom(bottomVBox);

        // Set up scene
        Scene scene = new Scene(root, 800, 600);
        stage.setTitle("Admin Dashboard");
        stage.setScene(scene);
        stage.setResizable(false);

        // Création du dégradé
        Stop[] stops = new Stop[] { new Stop(0, Color.ORANGERED), new Stop(1, Color.DARKRED)};
        LinearGradient lg = new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE, stops);

        // Création du Background
        BackgroundFill bgFill = new BackgroundFill(lg, CornerRadii.EMPTY, Insets.EMPTY);
        Background bg = new Background(bgFill);

        // Appliquer le Background à BorderPane
        root.setBackground(bg);

        // Créer un objet CSS pour définir le style global
        String style = "-fx-text-fill: white;";

        // Appliquer le style à la racine de la scène
        root.setStyle(style);

        stage.show();
    }

    public static void addUserList(User user) {
        userList.add(user);
    }

    public static void addTable(ObservableList<User> userList){
        table.setItems(userList);
    }

    public void setUsername(String usrn){
        this.username = usrn;
    }

}
