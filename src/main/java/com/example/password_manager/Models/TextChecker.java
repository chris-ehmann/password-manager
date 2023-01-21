package com.example.password_manager.Models;

public class TextChecker {
    public static boolean isIllegalString(String string) {
        return !string.matches("[A-Za-z0-9]+");
    }
}
