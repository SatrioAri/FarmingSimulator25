package com.farmingcmulator;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneManager {
    
    private static SceneManager instance;
    private Stage primaryStage;
    private Scene currentScene;
    
    public static final int WINDOW_WIDTH = 1920;
    public static final int WINDOW_HEIGHT = 1080;
    
    private SceneManager() {}
    
    public static SceneManager getInstance() {
        if (instance == null) {
            instance = new SceneManager();
        }
        return instance;
    }
    
    public void initialize(Stage stage) {
        this.primaryStage = stage;
    }
    
    public void switchScene(String fxmlName) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/" + fxmlName + ".fxml"));
            Parent root = loader.load();
            
            if (currentScene == null) {
                currentScene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
                currentScene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
                primaryStage.setScene(currentScene);
            } else {
                currentScene.setRoot(root);
            }
            
        } catch (IOException e) {
            System.err.println("Error loading FXML: " + fxmlName);
            e.printStackTrace();
        }
    }
    
    public Stage getPrimaryStage() {
        return primaryStage;
    }
    
    public void exitApplication() {
        primaryStage.close();
    }
}
