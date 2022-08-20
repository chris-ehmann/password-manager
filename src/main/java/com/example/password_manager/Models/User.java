package com.example.password_manager.Models;

public class User
{
    private String Username;

    public User(String Username) {
        this.Username = Username;
    }

    public String GetUser() {
        return Username;
    }

    public void CleanSession() {
        this.Username = "";
    }
}
