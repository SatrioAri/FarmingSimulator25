package com.farmingcmulator.controller;

import com.farmingcmulator.GameState;
import com.farmingcmulator.SceneManager;
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
            infoPopupLabel.setText(message);
            setPopupVisible(infoPopup, true);
        }
    }
    
    @FXML
    private void onInfoPopupClose() {
        setPopupVisible(infoPopup, false);
    }

    @FXML
    private void onFieldClicked() {
        SceneManager.getInstance().switchScene("Field");
    }

    @FXML
    private void onStoreClicked() {
        SceneManager.getInstance().switchScene("Store");
    }

    @FXML
    private void onSkipDayClicked() {
        setPopupVisible(skipDayConfirmPane, true);
    }
    
    @FXML
    private void onSkipDayConfirm() {
        hideAllPopups();
        gameState.skipDay();
        updateDisplay();
        showInfoPopup("Good morning! Day " + (GameState.SEASON_DAYS - gameState.getDaysRemaining()) + " has begun.");
    }
    
    @FXML
    private void onSkipDayCancel() {
        setPopupVisible(skipDayConfirmPane, false);
    }

    @FXML
    private void onRetireClicked() {
        setPopupVisible(retireConfirmPane, true);
    }
    
    @FXML
    private void onRetireConfirm() {
        hideAllPopups();
        showSeasonOver();
    }
    
    @FXML
    private void onRetireCancel() {
        setPopupVisible(retireConfirmPane, false);
    }

    private void showGameOver() {
        gameState.endGame();
        setPopupVisible(gameOverPane, true);
    }

    private void showSeasonOver() {
        gameState.endGame();
        
        if (seasonOverPane != null && finalScoreLabel != null) {
            finalScoreLabel.setText(gameState.getCoins() + " coins");
            if (gameState.getCoins() <= 0) {
                finalScoreLabel.setStyle("-fx-text-fill: #e74c3c; -fx-font-size: 30px; -fx-font-weight: bold;");
            } else {
                finalScoreLabel.setStyle("-fx-text-fill: #27ae60; -fx-font-size: 30px; -fx-font-weight: bold;");
            }
            setPopupVisible(seasonOverPane, true);
        }
    }

    @FXML
    private void onReturnToMenu() {
        SceneManager.getInstance().switchScene("MainMenu");
    }
}
