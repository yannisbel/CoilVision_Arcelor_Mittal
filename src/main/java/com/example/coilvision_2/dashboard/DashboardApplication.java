package com.example.coilvision_2.dashboard;

import com.example.coilvision_2.daoManager.DatabaseConnector;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.concurrent.Task;
import javafx.geometry.*;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Properties;

public class DashboardApplication extends Application {

    private int privilege;

    public int getPrivilege() {
        return privilege;
    }

    public void setPrivilege(int privilege) {
        this.privilege = privilege;
    }

    private int graphe_number = 0;

    private String login_dash;

    private int n_number;

    private double seuil = 100.0;

    private boolean play_seuil = true;

    public DashboardApplication() {
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        // Création du texte de bienvenue
        Text welcomeText = new Text("Bienvenue sur CoilVision !");
        welcomeText.setFont(Font.font("VAG Rounded", FontWeight.LIGHT, 30));

        // Création de la transition
        FadeTransition transition = new FadeTransition(Duration.seconds(4), welcomeText);
        transition.setFromValue(0);
        transition.setToValue(1);
        transition.setOnFinished(event -> {
            // Création de la scène principale
            BorderPane root = new BorderPane();
            root.setPadding(new Insets(10, 10, 10, 10));
            Scene scene = new Scene(root, 1700, 900);
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

            // Création de la boîte de sélection pour la ligne de production
            ChoiceBox<String> selectLineBox = new ChoiceBox<>();
            selectLineBox.getItems().addAll("Ligne F2", "Ligne F3");
            selectLineBox.setValue("Ligne F2");

            if (selectLineBox.getValue().equals("Ligne F2")) {
                n_number = 2;
            } else if (selectLineBox.getValue().equals("Ligne F3")) {
                n_number = 3;
            }

            // Création du graphique
            final NumberAxis xAxis = new NumberAxis();
            final NumberAxis yAxis = new NumberAxis();
            xAxis.setLabel("Temps (s)");
            yAxis.setLabel(selectDataBox.getValue());

            // Création de la série de données
            XYChart.Series<Number, Number> dataSeries = new XYChart.Series<>();

            DashboardControleur.updateDataSeries(dataSeries, xAxis, 0, n_number);

            selectDataBox.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
                switch (newValue) {
                    case "Coefficient de Friction":
                        graphe_number = 0;
                        dataSeries.getData().clear();
                        DashboardControleur.resetTimeline();
                        DashboardControleur.updateDataSeries(dataSeries, xAxis, 0, n_number);
                        yAxis.setLabel("Coefficient de Friction");
                        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1), dataSeries.getNode());
                        fadeTransition.setFromValue(0.0);
                        fadeTransition.setToValue(1.0);
                        fadeTransition.play();
                        break;
                    case "Roll Speed":
                        graphe_number = 1;
                        dataSeries.getData().clear();
                        DashboardControleur.updateDataSeries(dataSeries, xAxis, 1, n_number);
                        yAxis.setLabel("Roll Speed");
                        FadeTransition fadeTransition1 = new FadeTransition(Duration.seconds(1), dataSeries.getNode());
                        fadeTransition1.setFromValue(0.0);
                        fadeTransition1.setToValue(1.0);
                        fadeTransition1.play();
                        break;
                    case "Sigma":
                        graphe_number = 2;
                        dataSeries.getData().clear();
                        DashboardControleur.updateDataSeries(dataSeries, xAxis, 2, n_number);
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

            final LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
            lineChart.setPrefSize(1100, 600);

            // Modification du style du lineChart pour utiliser les couleurs d'ArcelorMittal
            lineChart.setStyle("-fx-background-color: #ffffff; -fx-border-color: #9a1414; -fx-border-width: 2px;");
            lineChart.setHorizontalGridLinesVisible(true);
            lineChart.setVerticalGridLinesVisible(false);
            lineChart.setLegendVisible(false);

            // Ajout de la série de données mise à jour au graphique
            lineChart.getData().add(dataSeries);

            // Bouton de déconnexion
            Button disconnectButton = new Button("Déconnexion");
            disconnectButton.setStyle("-fx-font-size: 18px; -fx-background-color: #9a1414; -fx-text-fill: white;");

            disconnectButton.setOnAction(event2 -> {
                primaryStage.close();
            });

            Button pauseButton = new Button("Pause");
            pauseButton.setStyle("-fx-font-size: 18px; -fx-background-color: #9a1414; -fx-text-fill: white;");


            pauseButton.setOnAction(event2 -> {
                Animation timeline = DashboardControleur.getTimeline();
                if (timeline.getStatus() == Animation.Status.RUNNING) {
                    timeline.pause();
                    pauseButton.setText("Reprendre");

                    Tooltip tooltip = new Tooltip();
                    for (XYChart.Data<Number, Number> data : dataSeries.getData()) {
                        // Récupérer l'indice du point dans la série de données
                        int index = dataSeries.getData().indexOf(data);

                        // Supprimer les anciens écouteurs
                        data.getNode().setOnMouseEntered(null);
                        data.getNode().setOnMouseExited(null);

                        // Ajouter un nouvel écouteur sur l'événement "mouseEntered" pour afficher les informations du point
                        data.getNode().setOnMouseEntered(event3 -> {
                            // Mettre à jour le texte du tooltip
                            tooltip.setText("Point " + index + ": x=" + data.getXValue() + ", y=" + data.getYValue());

                            // Positionner le tooltip à côté du point
                            tooltip.show(data.getNode().getScene().getWindow(),
                                    event3.getScreenX() + 10,
                                    event3.getScreenY() + 10);

                            System.out.println("Point " + index + ": x=" + data.getXValue() + ", y=" + data.getYValue());
                        });

                        // Ajouter un nouvel écouteur sur l'événement "mouseExited" pour supprimer les informations du point
                        data.getNode().setOnMouseExited(event3 -> {
                            tooltip.hide();
                            System.out.println("Point " + index + " exited");
                        });
                    }

                } else {
                    timeline.play();
                    pauseButton.setText("Pause");
                }
            });

            // Vérification du seuil à chaque mise à jour de la série de données
            dataSeries.getData().addListener((ListChangeListener<XYChart.Data<Number, Number>>) change -> {
                while (change.next()) {
                    for (XYChart.Data<Number, Number> data : change.getAddedSubList()) {
                        System.out.println(data.getYValue().doubleValue());
                        if (data.getYValue().doubleValue() > seuil && isPlay_seuil()) {
                            Animation timeline = DashboardControleur.getTimeline();
                            timeline.pause();
                            pauseButton.setText("Reprendre");

                            // Créer une nouvelle scène avec un message d'alerte
                            Label alertLabel = new Label(String.format("Seuil dépassé : %.2f", data.getYValue().doubleValue()));
                            alertLabel.setStyle("-fx-text-fill: #770f0f; -fx-font-size: 18px;");
                            Label seuilLabel = new Label(String.format("Seuil : %.2f", seuil));
                            seuilLabel.setStyle("-fx-text-fill: black; -fx-font-size: 16px;");
                            Button okButton = new Button("OK");
                            Button pauseButton2 = new Button("Pause Seuil");
                            VBox alertBox = new VBox(alertLabel, seuilLabel, okButton, pauseButton2);
                            alertBox.setSpacing(10);
                            alertBox.setPadding(new Insets(20));
                            Scene alertScene = new Scene(alertBox, 400, 200);
                            Stage alertStage = new Stage();
                            alertStage.setTitle("Attention !");
                            alertStage.setScene(alertScene);
                            okButton.setOnAction(e -> alertStage.close());
                            pauseButton2.setOnAction(e -> {
                                setPlay_seuil(false);
                                alertStage.close();
                                System.out.println(play_seuil);
                            });
                            alertStage.show();
                        }
                    }
                }
            });

            // Création de la grille
            GridPane gridPane = new GridPane();
            gridPane.setPadding(new Insets(10, 10, 10, 10));
            gridPane.setVgap(10);
            gridPane.setHgap(20);

            // Création d'une colonne pour la boîte de dialogue qui prendra le reste de la largeur
            ColumnConstraints chatColumn = new ColumnConstraints();
            chatColumn.setHgrow(Priority.ALWAYS);
            chatColumn.setFillWidth(true);
            gridPane.getColumnConstraints().addAll(new ColumnConstraints(), new ColumnConstraints(), chatColumn);

            // Création des éléments pour la modification du seuil
            Label seuilLabel = new Label("Seuil : ");
            TextField seuilField = new TextField();
            seuilField.setPromptText("Entrez la valeur du seuil");
            Button seuilButton = new Button("Appliquer");
            seuilButton.setStyle("-fx-font-size: 18px; -fx-background-color: #9a1414; -fx-text-fill: white;");
            seuilButton.setOnAction(e -> {
                try {
                    double seuil_saisie = Double.parseDouble(seuilField.getText());
                    setSeuil(seuil_saisie);
                } catch (NumberFormatException ex) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Erreur !");
                    alert.setHeaderText("Valeur de seuil invalide");
                    alert.setContentText("Le seuil doit être un nombre décimal.");
                    alert.showAndWait();
                }
            });

            // Création des éléments pour la modification de play_seuil
            Label playSeuilLabel = new Label("Activer le seuil : ");
            ToggleGroup playSeuilToggle = new ToggleGroup();
            RadioButton playSeuilTrue = new RadioButton("Oui");
            RadioButton playSeuilFalse = new RadioButton("Non");
            playSeuilTrue.setToggleGroup(playSeuilToggle);
            playSeuilFalse.setToggleGroup(playSeuilToggle);
            playSeuilTrue.setSelected(true);
            playSeuilTrue.selectedProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal) {
                    setPlay_seuil(true);
                }
            });

            playSeuilFalse.selectedProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal) {
                    setPlay_seuil(false);
                }
            });

            // Ajout des éléments du seuil dans un HBox
            HBox seuilBox = new HBox(seuilLabel, seuilField, seuilButton);
            seuilBox.setSpacing(10);

            // Ajout des éléments de play_seuil dans un HBox
            HBox playSeuilBox = new HBox(playSeuilLabel, playSeuilTrue, playSeuilFalse);
            playSeuilBox.setSpacing(10);

            Label titre_seuil = new Label("Paramétrage du seuil :");

            VBox seuil = new VBox(titre_seuil, seuilBox, playSeuilBox);
            seuil.setSpacing(10);
            seuil.setStyle("-fx-background-color: #a9a7a7; -fx-background-radius: 5; -fx-padding: 10;");
            seuil.setSpacing(10);

            // Ajout des éléments du seuil et de play_seuil à la grille
            gridPane.add(seuil, 0, 3, 1, 1);

            // Ajout des éléments à la grille
            gridPane.add(lineChart, 0, 0, 2, 1);
            gridPane.add(new Label("Choix de ligne de production à afficher :"), 0, 1, 1, 1);
            gridPane.add(selectLineBox, 1, 1, 1, 1);
            gridPane.add(new Label("Choix de la donnée à afficher :"), 0, 2, 1, 1);
            gridPane.add(selectDataBox, 1, 2, 1, 1);
            gridPane.add(pauseButton, 1, 2, 1, 1);
            gridPane.add(disconnectButton, 1, 3, 1, 1);

            selectDataBox.setStyle("-fx-font-size: 12px; -fx-background-color: #a9a7a7; -fx-text-fill: white;");
            selectLineBox.setStyle("-fx-font-size: 12px; -fx-background-color: #a9a7a7; -fx-text-fill: white;");

            // Création des éléments pour la saisie de rapport
            Label titleLabel = new Label("Rapport d'activité :");
            titleLabel.setFont(Font.font("VAG Rounded", FontWeight.LIGHT, 30));
            TextField objectTextField = new TextField();
            objectTextField.setPromptText("Objet du rapport");
            TextArea reportTextArea = new TextArea();
            Button submitButton = new Button("Soumettre");
            submitButton.setOnAction(event6 -> {
                String login = login_dash; // Récupérez le login de l'utilisateur connecté
                String object = objectTextField.getText();
                String report = reportTextArea.getText();
                try {
                    DashboardControleur.insertReport(login, object, report, graphe_number);
                    objectTextField.setText("");
                    reportTextArea.setText("");
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText(null);
                    alert.setContentText("Le rapport a été envoyé avec succès !");
                    alert.showAndWait();

                    // Envoie d'un e-mail à l'ingénieur
                    Properties props = new Properties();
                    props.put("mail.smtp.host", "smtp-relay.sendinblue.com");
                    props.put("mail.smtp.port", "587");
                    props.put("mail.smtp.auth", "true");
                    props.put("mail.smtp.starttls.enable", "true");

                    Session session = Session.getInstance(props, new Authenticator() {
                        @Override
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication("belkhiter@yahoo.com", "pTkqQFYMacRD61UW");
                        }
                    });

                    // création du message
                    MimeMessage message = new MimeMessage(session);
                    message.setFrom(new InternetAddress("belkhiter@yahoo.com"));
                    message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("belkhiter@yahoo.com"));
                    message.setSubject("Nouveau rapport disponible");

                    // lecture du contenu HTML à partir du fichier
                    String htmlBody = new String(Files.readAllBytes(Paths.get("src/main/resources/report.html")));

                    // ajout du contenu HTML au message
                    message.setContent(htmlBody, "text/html");

                    Transport.send(message);

                } catch (SQLException e) {
                    throw new RuntimeException(e);
                } catch (AddressException e) {
                    throw new RuntimeException(e);
                } catch (MessagingException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            submitButton.setStyle("-fx-font-size: 18px; -fx-background-color: #9a1414; -fx-text-fill: white;");
            VBox reportBox = new VBox(titleLabel, objectTextField, reportTextArea, submitButton);
            reportBox.setSpacing(10);

            // Ajout de la saisie de rapport à la grille
            gridPane.add(reportBox, 2, 0, 1, 4);

            // Configuration des contraintes pour que la saisie de rapport prenne toute la hauteur de la grille
            GridPane.setRowSpan(reportBox, GridPane.REMAINING);

            // Création de la barre horizontale
            Separator separator = new Separator(Orientation.HORIZONTAL);

            // Création des éléments pour la description du profil
            Label loginLabel = new Label("Login : " + login_dash);
            loginLabel.setFont(Font.font("VAG Rounded", FontWeight.NORMAL, 20));
            loginLabel.setTextFill(Color.web("#9a1414"));
            Label privilegeLabel = new Label("Privilège : " + privilege);
            privilegeLabel.setFont(Font.font("VAG Rounded", FontWeight.NORMAL, 20));
            privilegeLabel.setTextFill(Color.web("#9a1414"));
            Label benchLabel = new Label("Ligne de production : " + selectLineBox.getValue());
            selectLineBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    benchLabel.setText("Ligne de production : " + newValue);
                    if (newValue.equals("Ligne F2")) {
                        n_number = 2;
                    } else if (newValue.equals("Ligne F3")) {
                        n_number = 3;
                    }

                }
            });
            benchLabel.setTextFill(Color.web("#9a1414"));
            VBox profileBox = new VBox(loginLabel, privilegeLabel, benchLabel);
            profileBox.setStyle("-fx-background-color: #a9a7a7; -fx-background-radius: 5; -fx-padding: 10;");
            profileBox.setSpacing(10);

            // Ajout de la barre horizontale et de la description du profil à la grille
            gridPane.add(separator, 2, 1, 2, 1);
            gridPane.add(profileBox, 2, 3, 2, 1);

            // Centrage des éléments en largeur
            GridPane.setHalignment(titleBox, HPos.CENTER);
            GridPane.setHalignment(descriptionBox, HPos.CENTER);
            GridPane.setHalignment(lineChart, HPos.CENTER);
            GridPane.setHalignment(pauseButton, HPos.RIGHT);
            GridPane.setHalignment(disconnectButton, HPos.RIGHT);
            GridPane.setHgrow(lineChart, Priority.ALWAYS);

            // Ajout de la grille au centre du BorderPane
            root.setCenter(gridPane);

            // Création de la barre horizontale
            Separator separator2 = new Separator(Orientation.HORIZONTAL);

            LocalDateTime currentTime = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            String formattedTime = currentTime.format(formatter);
            Label time = new Label("Utilisateur : " + this.login_dash + " (" + privilege + ") - Connexion : " + formattedTime);
            time.setFont(Font.font("VAG Rounded", 14));

            // Création de l'HBox contenant le label et le séparateur
            HBox userBox = new HBox(time, separator2);
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

            //scene.getRoot().setStyle("-fx-background-color: linear-gradient(to bottom right, #62250f, #800000);");

            // Affichage de la scène
            primaryStage.centerOnScreen();
            primaryStage.show();
        });

        // Création du logo
        Image logoImage = new Image("/logo.png");
        ImageView logoImageView = new ImageView(logoImage);
        logoImageView.setFitWidth(200);
        logoImageView.setPreserveRatio(true);

        // Création du layout de la scène avec le logo et le label
        VBox layout = new VBox(20, logoImageView, welcomeText); // ajouter un espace de 10 pixels entre le logo et le label
        layout.setAlignment(Pos.CENTER);

        // Création de la scène de bienvenue
        Scene welcomeScene = new Scene(layout, 400, 300);
        primaryStage.setScene(welcomeScene);
        primaryStage.centerOnScreen();
        primaryStage.show();

        // Lancement de la transition
        transition.play();
    }

    public String getLogin_dash() {
        return login_dash;
    }

    public void setLogin_dash(String login_dash) {
        this.login_dash = login_dash;
    }

    public double getSeuil() {
        return seuil;
    }

    public void setSeuil(double seuil) {
        this.seuil = seuil;
    }

    public boolean isPlay_seuil() {
        return play_seuil;
    }

    public void setPlay_seuil(boolean play_seuil) {
        this.play_seuil = play_seuil;
    }

    public static void main(String[] args) {
        launch(args);
    }
}