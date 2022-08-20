package com.example.password_manager.Models;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextChecker {
    public static boolean isLegalString(String string) {
        return string.matches("[A-Za-z0-9]+");
    }
}
