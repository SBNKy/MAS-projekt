package org.example.finalproject;

import javafx.application.Application;
import org.example.finalproject.util.DataManager;

public class Launcher {
    public static void main(String[] args) {
        DataManager.loadFromFile();
        Application.launch(HelloApplication.class, args);
    }
}
