package com.farmingcmulator.controller;

import com.farmingcmulator.GameState;
import com.farmingcmulator.SceneManager;
import com.farmingcmulator.util.SoundManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class EnterNameController implements Initializable {

    @FXML private TextField nameField;
    @FXML private Label errorLabel;

    private SoundManager sound = SoundManager.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        errorLabel.setVisible(false);
    }

    @FXML
    private void onStartClicked() {
        String name = nameField.getText().trim();
        
        if (name.isEmpty()) {
            sound.playError();
            errorLabel.setText("Name cannot be empty!");
            errorLabel.setVisible(true);
            return;
        }
        
        if (name.length() > 20) {
            sound.playError();
            errorLabel.setText("Name cannot exceed 20 characters!");
            errorLabel.setVisible(true);
            return;
        }
        
        sound.playClick();
        sound.playSuccess();
        GameState.getInstance().startNewGame(name);
        SceneManager.getInstance().switchScene("GameMenu");
    }

    @FXML
    private void onBackClicked() {
        sound.playClick();
        SceneManager.getInstance().switchScene("MainMenu");
    }
}
