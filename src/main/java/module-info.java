module com.example.coilvision_2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires com.opencsv;
    requires java.mail;


    exports com.example.coilvision_2.login;
    opens com.example.coilvision_2.login to javafx.fxml;
    exports com.example.coilvision_2.dashboard;
    opens com.example.coilvision_2.dashboard to javafx.fxml;

    opens com.example.coilvision_2.admin to javafx.fxml;
    exports com.example.coilvision_2.admin;
    exports com.example.coilvision_2.daoManager;
    opens com.example.coilvision_2.daoManager to javafx.fxml;
    exports com.example.coilvision_2.engineer;
}