package com.farmingcmulator.controller;

import com.farmingcmulator.GameState;
import com.farmingcmulator.SceneManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class EnterNameController implements Initializable {

    @FXML private TextField nameField;
    @FXML private Label errorLabel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        errorLabel.setVisible(false);
    }

    @FXML
    private void onStartClicked() {
        String name = nameField.getText().trim();
        
        if (name.isEmpty()) {
            errorLabel.setText("Name cannot be empty!");
            errorLabel.setVisible(true);
            return;
        }
        
        if (name.length() > 20) {
            errorLabel.setText("Name cannot exceed 20 characters!");
            errorLabel.setVisible(true);
            return;
        }
        
        GameState.getInstance().startNewGame(name);
        SceneManager.getInstance().switchScene("GameMenu");
    }

    @FXML
    private void onBackClicked() {
        SceneManager.getInstance().switchScene("MainMenu");
    }
}
