package com.farmingcmulator.controller;

import com.farmingcmulator.GameState;
import com.farmingcmulator.SceneManager;
import com.farmingcmulator.util.SoundManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class GameMenuController implements Initializable {

    @FXML private Label playerNameLabel;
    @FXML private Label daysRemainingLabel;
    @FXML private Label coinsLabel;
    @FXML private Label actionsRemainingLabel;
    
    @FXML private VBox gameOverPane;
    @FXML private VBox seasonOverPane;
    @FXML private Label finalScoreLabel;
    
    @FXML private VBox skipDayConfirmPane;
    @FXML private VBox retireConfirmPane;
    @FXML private VBox infoPopup;
    @FXML private Label infoPopupLabel;
    
    private GameState gameState;
    private SoundManager sound = SoundManager.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        gameState = GameState.getInstance();
        updateDisplay();
        hideAllPopups();
    }
    
    private void hideAllPopups() {
        setPopupVisible(gameOverPane, false);
        setPopupVisible(seasonOverPane, false);
        setPopupVisible(skipDayConfirmPane, false);
        setPopupVisible(retireConfirmPane, false);
        setPopupVisible(infoPopup, false);
    }
    
    private void setPopupVisible(VBox popup, boolean visible) {
        if (popup != null) {
            popup.setVisible(visible);
            popup.setManaged(visible);
        }
    }

    public void updateDisplay() {
        playerNameLabel.setText(gameState.getPlayerName());
        daysRemainingLabel.setText(String.valueOf(gameState.getDaysRemaining()));
        coinsLabel.setText(String.valueOf(gameState.getCoins()));
        actionsRemainingLabel.setText(String.valueOf(gameState.getActionsRemaining()));
        
        if (gameState.getCoins() < -400) {
            coinsLabel.setStyle("-fx-text-fill: #e74c3c; -fx-font-size: 24px; -fx-font-weight: bold;");
        } else if (gameState.getCoins() < 0) {
            coinsLabel.setStyle("-fx-text-fill: #f39c12; -fx-font-size: 24px; -fx-font-weight: bold;");
        } else {
            coinsLabel.setStyle("-fx-text-fill: #27ae60; -fx-font-size: 24px; -fx-font-weight: bold;");
        }
        
        checkGameState();
    }

    private void checkGameState() {
        if (gameState.isBankrupt()) {
            showGameOver();
        } else if (gameState.isSeasonOver()) {
            showSeasonOver();
        }
    }
    
    private void showInfoPopup(String message) {
        if (infoPopupLabel != null && infoPopup != null) {
            sound.playPopup();
            infoPopupLabel.setText(message);
            setPopupVisible(infoPopup, true);
        }
    }
    
    @FXML
    private void onInfoPopupClose() {
        sound.playClick();
        setPopupVisible(infoPopup, false);
    }

    @FXML
    private void onFieldClicked() {
        sound.playClick();
        SceneManager.getInstance().switchScene("Field");
    }

    @FXML
    private void onStoreClicked() {
        sound.playClick();
        SceneManager.getInstance().switchScene("Store");
    }

    @FXML
    private void onSkipDayClicked() {
        sound.playClick();
        sound.playPopup();
        setPopupVisible(skipDayConfirmPane, true);
    }
    
    @FXML
    private void onSkipDayConfirm() {
        sound.playClick();
        hideAllPopups();
        gameState.skipDay();
        updateDisplay();
        showInfoPopup("Good morning! Day " + (GameState.SEASON_DAYS - gameState.getDaysRemaining()) + " has begun.");
    }
    
    @FXML
    private void onSkipDayCancel() {
        sound.playClick();
        setPopupVisible(skipDayConfirmPane, false);
    }

    @FXML
    private void onRetireClicked() {
        sound.playClick();
        sound.playPopup();
        setPopupVisible(retireConfirmPane, true);
    }
    
    @FXML
    private void onRetireConfirm() {
        sound.playClick();
        hideAllPopups();
        showSeasonOver();
    }
    
    @FXML
    private void onRetireCancel() {
        sound.playClick();
        setPopupVisible(retireConfirmPane, false);
    }

    private void showGameOver() {
        sound.playError();
        gameState.endGame();
        setPopupVisible(gameOverPane, true);
    }

    private void showSeasonOver() {
        gameState.endGame();
        
        if (seasonOverPane != null && finalScoreLabel != null) {
            finalScoreLabel.setText(gameState.getCoins() + " coins");
            if (gameState.getCoins() <= 0) {
                sound.playError();
                finalScoreLabel.setStyle("-fx-text-fill: #e74c3c; -fx-font-size: 30px; -fx-font-weight: bold;");
            } else {
                sound.playSuccess();
                sound.playCoins();
                finalScoreLabel.setStyle("-fx-text-fill: #27ae60; -fx-font-size: 30px; -fx-font-weight: bold;");
            }
            setPopupVisible(seasonOverPane, true);
        }
    }

    @FXML
    private void onReturnToMenu() {
        sound.playClick();
        SceneManager.getInstance().switchScene("MainMenu");
    }
}
