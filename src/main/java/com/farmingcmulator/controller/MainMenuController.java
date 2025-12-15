package com.farmingcmulator.controller;

import com.farmingcmulator.SceneManager;
import com.farmingcmulator.util.SoundManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.fxml.Initializable;
import java.net.URL;
import java.util.ResourceBundle;

public class MainMenuController implements Initializable {

    @FXML private Button btnPlay;
    @FXML private Button btnDatabase;
    @FXML private Button btnExit;

    private SoundManager sound = SoundManager.getInstance();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Note: Button hover sounds will be added via CSS or manual event handlers
        // if buttons have fx:id assigned
    }

    @FXML
    private void onPlayClicked() {
        sound.playClick();
        SceneManager.getInstance().switchScene("EnterName");
    }

    @FXML
    private void onDatabaseClicked() {
        sound.playClick();
        SceneManager.getInstance().switchScene("Database");
    }

    @FXML
    private void onExitClicked() {
        sound.playClick();
        SoundManager.getInstance().dispose();
        SceneManager.getInstance().exitApplication();
    }
}
