package com.example.password_manager.Controllers;

import com.example.password_manager.Models.DatabaseConnection;
import com.example.password_manager.Models.Password;
import com.example.password_manager.Models.User;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AddViewController {

    private final String User;
    private ObservableList<Password> passwordList;

    @FXML
    private TextField UsernameField;
    @FXML
    private TextField PasswordField;
    @FXML
    private TextField SiteNameField;
    @FXML
    private TextField SiteLinkField;
    @FXML
    private Label UsernameError;
    @FXML
    private Label PasswordError;
    @FXML
    private Label SiteNameError;
    @FXML
    private Label SiteLinkError;

    public AddViewController(String User, ObservableList<Password> passwordList) {
        this.User = User;
        this.passwordList = passwordList;
    }

    public void onAddButtonClick(ActionEvent event) {
        if(UsernameField.getText().isBlank() || PasswordField.getText().isBlank() || SiteNameField.getText().isBlank() || SiteLinkField.getText().isBlank()) {
            UsernameError.setVisible(UsernameField.getText().isBlank());
            PasswordError.setVisible(PasswordField.getText().isBlank());
            SiteNameError.setVisible(SiteNameField.getText().isBlank());
            SiteLinkError.setVisible(SiteLinkField.getText().isBlank());
        }
        else {
            DatabaseConnection RegistrationConnection = new DatabaseConnection();
            Connection ConnectToDatabase = RegistrationConnection.GetConnection();
            try {
                String AddToDatabase = "INSERT INTO " +
                        User +
                        " (site, username, password, sitelink) " +
                        "VALUES (?, ?, ?, ?)";
                PreparedStatement statement = ConnectToDatabase.prepareStatement(AddToDatabase);
                statement.setString(1, SiteNameField.getText());
                statement.setString(2, UsernameField.getText());
                statement.setString(3, PasswordField.getText());
                statement.setString(4, SiteLinkField.getText());
                statement.executeUpdate();
                PreparedStatement idStatement = ConnectToDatabase.prepareStatement("SELECT password_id FROM " + User + " WHERE password_id = (SELECT MAX(password_id) FROM " + User + ")");
                ResultSet result = idStatement.executeQuery();
                int id = result.getInt("password_id");
                passwordList.add(new Password(UsernameField.getText(),
                                PasswordField.getText(),
                                SiteLinkField.getText(),
                                SiteNameField.getText(),
                                id));
                ConnectToDatabase.close();
                close();
            } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }

        }
    }

    public void onCancelButtonClick(ActionEvent event) {
        close();
    }

    public void close() {
        Stage stage = (Stage) UsernameField.getScene().getWindow();
        stage.close();
    }
}
