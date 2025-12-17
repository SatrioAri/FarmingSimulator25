package com.farmingcmulator;

import com.farmingcmulator.util.SoundManager;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        SceneManager.getInstance().initialize(primaryStage);

        primaryStage.setTitle("Farming Simulator");
        primaryStage.setResizable(false);
        primaryStage.setWidth(1920);
        primaryStage.setHeight(1080);
        primaryStage.setMaximized(true);

        // Set application icon
        try {
            Image icon = new Image(getClass().getResourceAsStream("/images/icon.png"));
            primaryStage.getIcons().add(icon);
        } catch (Exception e) {
            System.out.println("Note: Application icon not found. Using default icon.");
        }

        // Start background music
        SoundManager.getInstance().playBGM();

        // Handle window close - cleanup sounds
        primaryStage.setOnCloseRequest(event -> {
            SoundManager.getInstance().dispose();
        });

        SceneManager.getInstance().switchScene("MainMenu");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}