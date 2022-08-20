package com.example.password_manager.Controllers;

import com.example.password_manager.Models.DatabaseConnection;
import com.example.password_manager.Models.User;
import com.example.password_manager.PasswordApplication;
import com.example.password_manager.Models.PasswordHashing;
import com.example.password_manager.Models.UserService;
import javafx.event.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.Objects;

public class LoginViewController {

    @FXML
    private TextField UsernameField;
    @FXML
    private PasswordField PasswordField;
    @FXML
    private TextField PasswordVisible;
    @FXML
    private Label InvalidMessage;

    private boolean PasswordShown = false;

    public void initialize()
    {
        PasswordVisible.textProperty().bindBidirectional(PasswordField.textProperty());
    }

    public void onPasswordVisibilityButtonClick(ActionEvent event) {
        if (!PasswordShown) {
            PasswordShown = true;
            ((Button) event.getSource()).setText("Hide Password");
            PasswordField.setVisible(false);
            PasswordVisible.setVisible(true);
        }
        else {
            PasswordShown = false;
            ((Button) event.getSource()).setText("Show Password");
            PasswordField.setVisible(true);
            PasswordVisible.setVisible(false);
        }
    }

    public void onLoginButtonClick(ActionEvent event) throws IOException, SQLException {
        if (UsernameField.getText().isBlank() || PasswordField.getText().isBlank()) {
            InvalidMessage.setVisible(true);
            InvalidMessage.setText("Invalid login, please try again.");
        }
        else {
            int result = UserService.HandleLogin(UsernameField.getText().toLowerCase(), PasswordField.getText());
            if(result == 1) {
                Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                FXMLLoader fxmlLoader = new FXMLLoader(PasswordApplication.class.getResource("fxml/mainview.fxml"));
                Scene scene = new Scene(fxmlLoader.load());
                MainViewController controller = fxmlLoader.getController();
                controller.setCurrentUser(new User(UsernameField.getText().toLowerCase()));
                controller.setListView();
                stage.setScene(scene);
                stage.setResizable(false);
                stage.setTitle("Password Manager - " + controller.getCurrentUser());
                reset();
            }
        }
    }

    public void onRegisterButtonClick(ActionEvent event) throws IOException {
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(PasswordApplication.class.getResource("fxml/registerview.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        reset();
    }

    private void reset() {
        InvalidMessage.setVisible(false);
        UsernameField.setText("");
        PasswordField.setText("");
        PasswordShown = false;
    }
}