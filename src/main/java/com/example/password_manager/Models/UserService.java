package com.example.password_manager.Models;

import com.example.password_manager.PasswordApplication;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.*;
import java.text.MessageFormat;

public class UserService {

    public static int HandleLogin(String username, String password) {
        DatabaseConnection RegistrationConnection = new DatabaseConnection();
        Connection ConnectToUsers = RegistrationConnection.GetConnection();
        String CheckIfUserExists = "SELECT * FROM users WHERE username = ?";

        try {
            PreparedStatement statement = ConnectToUsers.prepareStatement(CheckIfUserExists);
            statement.setString(1, username);
            ResultSet QueryResult = statement.executeQuery();
            int size = 0;

            while (QueryResult.next()) {
                size++;
            }

            if (size == 0) {
                System.out.println("This user does not exist.");
                return 0;
            }
            else {
                ResultSet SaltQuery = statement.executeQuery();
                byte[] salt = SaltQuery.getBytes("salt");
                String OriginalHashedPassword = SaltQuery.getString("password");
                String CheckHashedPassword = PasswordHashing.generatePasswordHash(password, salt);

                if(OriginalHashedPassword.equals(CheckHashedPassword)) {
                    System.out.println("Password matches");
                    ConnectToUsers.close();
                    return 1;
                }
                else {
                    System.out.println("Username or password is incorrect.");
                    RegistrationConnection.close();
                    return 0;
                }
            }
        }catch(Exception e) {
            e.printStackTrace();
            e.getCause();
        }
        return 0;
    }

    public static int RegisterAccount(String username, String password) throws SQLException {
        DatabaseConnection RegistrationConnection = new DatabaseConnection();
        Connection ConnectToUsers = RegistrationConnection.GetConnection();
        String CheckIfUserAlreadyExists = "SELECT * FROM users WHERE username = '" + username + "'";

        try {
            Statement statement = ConnectToUsers.createStatement();
            ResultSet QueryResult = statement.executeQuery(CheckIfUserAlreadyExists);
            int size = 0;

            while (QueryResult.next()) {
                size++;
            }

            if (size != 0) {
                System.out.println("This user already exists!");
                return 0;
            } else {
                byte[] salt = PasswordHashing.getSalt();
                String HashedPassword = PasswordHashing.generatePasswordHash(password, salt);
                String AddUser = "INSERT INTO users (username, password, salt)";
                AddUser += " VALUES (?, ?, ?)";
                PreparedStatement newStatement = ConnectToUsers.prepareStatement(AddUser);
                newStatement.setString(1, username);
                newStatement.setString(2, HashedPassword);
                newStatement.setBytes(3, salt);
                newStatement.executeUpdate();

                String CreateUserTable = "CREATE TABLE {0} (site STRING, " +
                        "username STRING, " +
                        "password STRING, " +
                        "sitelink STRING, " +
                        "password_id INTEGER PRIMARY KEY AUTOINCREMENT)";
                Statement CreateTableStatement = ConnectToUsers.createStatement();
                CreateTableStatement.execute(MessageFormat.format(CreateUserTable, username));
                ConnectToUsers.close();
                return 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
        ConnectToUsers.close();
        return 0;
    }
}
