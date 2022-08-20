package com.example.password_manager.Controllers;

import com.example.password_manager.Models.TextChecker;
import com.example.password_manager.Models.UserService;
import com.example.password_manager.PasswordApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class RegisterViewController {

    @FXML
    private TextField PasswordField;
    @FXML
    private TextField ConfirmPasswordField;
    @FXML
    private TextField UsernameField;

    public void onRegisterButtonClick(ActionEvent event) throws IOException, SQLException {
        if(UsernameField.getText().isBlank() || PasswordField.getText().isBlank() || ConfirmPasswordField.getText().isBlank()) {
            System.out.println("One or more fields are empty");
        }
        else if(!PasswordField.getText().equals(ConfirmPasswordField.getText())) {
            System.out.println("Passwords do not match");
        }
        else if(PasswordField.getText().length() < 8) {
            System.out.println("Password must be 8 or more characters in length.");
        }
        else if(!TextChecker.isLegalString(PasswordField.getText()) || !TextChecker.isLegalString(UsernameField.getText())) {
            System.out.println("Username and password must be alphanumeric.");
        }
        else {
            int result = UserService.RegisterAccount(UsernameField.getText().toLowerCase(), PasswordField.getText());
            if(result == 1) {
                Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                FXMLLoader fxmlLoader = new FXMLLoader(PasswordApplication.class.getResource("fxml/loginview.fxml"));
                Scene scene = new Scene(fxmlLoader.load());
                stage.setScene(scene);
            }
        }
    }

    public void onCancelButtonClick(ActionEvent event) throws IOException {
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(PasswordApplication.class.getResource("fxml/loginview.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
    }
}
