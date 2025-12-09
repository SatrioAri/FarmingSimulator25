package com.farmingcmulator.controller;

import com.farmingcmulator.SceneManager;
import javafx.fxml.FXML;

public class MainMenuController {

    @FXML
    private void onPlayClicked() {
        SceneManager.getInstance().switchScene("EnterName");
    }

    @FXML
    private void onDatabaseClicked() {
        SceneManager.getInstance().switchScene("Database");
    }

    @FXML
    private void onExitClicked() {
        SceneManager.getInstance().exitApplication();
    }
}
