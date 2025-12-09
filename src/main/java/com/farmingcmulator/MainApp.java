package com.farmingcmulator;

import javafx.application.Application;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        SceneManager.getInstance().initialize(primaryStage);
        
        primaryStage.setTitle("Farming C-Mulator");
        primaryStage.setResizable(false);
        primaryStage.setWidth(1920);
        primaryStage.setHeight(1080);
        primaryStage.setMaximized(true);
        
        SceneManager.getInstance().switchScene("MainMenu");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
