package com.example.password_manager.Controllers;

import com.example.password_manager.Models.DatabaseConnection;
import com.example.password_manager.Models.Password;
import com.example.password_manager.Models.User;
import com.example.password_manager.PasswordApplication;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MainViewController {

    @FXML
    private User CurrentUser;
    @FXML
    private MenuBar MenuBar;
    @FXML
    private ListView<Password> ListView;
    @FXML
    private VBox PasswordViewBox;
    @FXML
    private Label SiteLinkLabel;
    @FXML
    private TextField UsernameField;
    @FXML
    private TextField PasswordField;
    @FXML
    private TextField SiteNameField;
    @FXML
    private TextField SiteLinkField;

    private final ObservableList<Password> passwordList = FXCollections.observableArrayList();

    public void setListView() throws SQLException {
        DatabaseConnection RegistrationConnection = new DatabaseConnection();
        Connection ConnectToUsers = RegistrationConnection.GetConnection();
        try {
            String Query = "SELECT * FROM " + getCurrentUser() + " ORDER BY sitelink COLLATE NOCASE";
            PreparedStatement statement = ConnectToUsers.prepareStatement(Query);
            ResultSet results = statement.executeQuery();
            while(results.next()) {
                passwordList.add(new Password(results.getString("username"),
                        results.getString("password"),
                        results.getString("sitelink"),
                        results.getString("site"),
                        results.getInt("password_id")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        RegistrationConnection.close();
        setCellText();
    }

    public void setCurrentUser(User User) {
        this.CurrentUser = User;
    }

    public String getCurrentUser() {
        return CurrentUser.GetUser();
    }

    public ObservableList<Password> getPasswordList() { return passwordList; }

    public void onLogoutButtonClick() throws IOException {
        System.out.println("Logging out " + getCurrentUser());
        CurrentUser.CleanSession();
        Stage stage = (Stage) MenuBar.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(PasswordApplication.class.getResource("fxml/loginview.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.setResizable(true);
        stage.setTitle("Password Manager");
    }

    public void onNewButtonClick() throws IOException {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(PasswordApplication.class.getResource("fxml/addview.fxml"));
        AddViewController controller = new AddViewController(getCurrentUser(), getPasswordList());
        fxmlLoader.setController(controller);
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.setTitle("Add Password");
        stage.getIcons().add(new Image(String.valueOf(PasswordApplication.class.getResource("img/lock-icon.png"))));
        stage.show();
    }

    public void onEditButtonClick() throws SQLException {
        Password selectedPassword = ListView.getSelectionModel().getSelectedItem();
        selectedPassword.setSiteLink(SiteLinkField.getText());
        selectedPassword.setPassword(PasswordField.getText());
        selectedPassword.setUsername(UsernameField.getText());
        selectedPassword.setSiteName(SiteNameField.getText());
        SiteLinkLabel.setText(SiteLinkField.getText());

        DatabaseConnection RegistrationConnection = new DatabaseConnection();
        Connection ConnectToUsers = RegistrationConnection.GetConnection();
        try {
            String Query = "UPDATE " + getCurrentUser() +
                    " SET site = ?, username = ?, sitelink = ?, password = ? " +
                    " WHERE password_id = ?";
            PreparedStatement statement = ConnectToUsers.prepareStatement(Query);
            statement.setInt(5, selectedPassword.getId());
            statement.setString(1, selectedPassword.getSiteName());
            statement.setString(2, selectedPassword.getUsername());
            statement.setString(3, selectedPassword.getSiteLink());
            statement.setString(4, selectedPassword.getPassword());
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        setCellText();
        RegistrationConnection.close();
    }

    public void onDeleteButtonClick() throws SQLException {
        Password selectedPassword = ListView.getSelectionModel().getSelectedItem();
        passwordList.remove(selectedPassword);
        DatabaseConnection RegistrationConnection = new DatabaseConnection();
        Connection ConnectToUsers = RegistrationConnection.GetConnection();
        try {
            String Query = "DELETE FROM " + getCurrentUser() + " WHERE password_id = ?";
            PreparedStatement statement = ConnectToUsers.prepareStatement(Query);
            statement.setInt(1, selectedPassword.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        RegistrationConnection.close();
        reset();
    }

    public void onCloseButtonClick() {
        reset();
    }

    public void onListObjectButtonClick(MouseEvent event) {
        Password selectedPassword = ListView.getSelectionModel().getSelectedItem();
        if(!selectedPassword.getPassword().isBlank()) {
            PasswordViewBox.setVisible(true);
            SiteLinkLabel.setText(selectedPassword.getSiteLink());
            PasswordField.setText(selectedPassword.getPassword());
            UsernameField.setText(selectedPassword.getUsername());
            SiteLinkField.setText(selectedPassword.getSiteLink());
            SiteNameField.setText(selectedPassword.getSiteName());
        }
    }

    private void reset() {
        PasswordViewBox.setVisible(false);
        SiteLinkLabel.setText("");
        PasswordField.setText("");
        UsernameField.setText("");
        SiteLinkField.setText("");
        SiteNameField.setText("");
    }

    private void setCellText() {
        ListView.setItems(passwordList);
        ListView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Password item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null || item.getSiteName() == null) {
                    setText(null);
                } else {
                    setText(item.getSiteLink() + ": " + item.getUsername());
                }
            }
        });
    }
}
