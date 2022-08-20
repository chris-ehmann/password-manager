module com.example.password_manager {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires java.sql;

    opens com.example.password_manager to javafx.fxml;
    exports com.example.password_manager;
    exports com.example.password_manager.Controllers;
    opens com.example.password_manager.Controllers to javafx.fxml;
    exports com.example.password_manager.Models;
    opens com.example.password_manager.Models to javafx.fxml;
}