package com.farmingcmulator.controller;

import com.farmingcmulator.GameState;
import com.farmingcmulator.SceneManager;
import com.farmingcmulator.util.SoundManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class GameMenuController implements Initializable {

    @FXML private Label playerNameLabel;
    @FXML private Label seasonLabel;
    @FXML private Label daysRemainingLabel;
    @FXML private Label coinsLabel;
    @FXML private Label actionsRemainingLabel;
    @FXML private Label levelLabel;
    @FXML private Label expLabel;
    @FXML private ProgressBar expBar;
    
    @FXML private VBox gameOverPane;
    @FXML private VBox seasonOverPane;
    @FXML private Label finalScoreLabel;
    
    @FXML private VBox skipDayConfirmPane;
    @FXML private VBox retireConfirmPane;
    @FXML private VBox infoPopup;
    @FXML private Label infoPopupLabel;
    
    @FXML private VBox coinsBox;
    @FXML private VBox expBox;
    @FXML private ImageView backgroundImage;

    private GameState gameState;
    private SoundManager sound = SoundManager.getInstance();

    // Cheat code counters
    private int coinsClickCount = 0;
    private int expClickCount = 0;
    private Timer coinsResetTimer;
    private Timer expResetTimer;

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

        // Update season display
        seasonLabel.setText(gameState.getCurrentSeason());
        updateSeasonLabelColor();
        updateSeasonBackground();

        // Update level and EXP display
        levelLabel.setText(String.valueOf(gameState.getPlayerLevel()));
        expLabel.setText(gameState.getCurrentExp() + "/" + gameState.getExpForNextLevel());
        expBar.setProgress(gameState.getExpProgress());

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

    private void updateSeasonLabelColor() {
        String season = gameState.getCurrentSeason();
        String color;
        switch (season) {
            case "Spring":
                color = "#FF69B4"; // Pink
                break;
            case "Summer":
                color = "#FFD700"; // Gold
                break;
            case "Fall":
                color = "#FF8C00"; // Dark Orange
                break;
            case "Winter":
                color = "#87CEEB"; // Sky Blue
                break;
            default:
                color = "#FFD966";
        }
        seasonLabel.setStyle("-fx-text-fill: " + color + "; -fx-font-size: 18px; -fx-font-weight: bold;");
    }

    private void updateSeasonBackground() {
        String season = gameState.getCurrentSeason().toLowerCase();
        try {
            Image bgImage = new Image(getClass().getResourceAsStream("/images/gamemenu_" + season + ".png"));
            if (bgImage != null && !bgImage.isError()) {
                backgroundImage.setImage(bgImage);
            }
        } catch (Exception e) {
            // Keep current background if image not found
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

    // ==================== CHEAT CODES (sneaky, no visual feedback) ====================

    @FXML
    private void onCoinsBoxClicked() {
        // Cancel previous timer if exists
        if (coinsResetTimer != null) {
            coinsResetTimer.cancel();
        }

        coinsClickCount++;

        if (coinsClickCount >= 5) {
            // Add 200 coins silently
            gameState.addCoins(200);
            updateDisplay();
            coinsClickCount = 0;
        } else {
            // Reset counter after 2 seconds of no clicks
            coinsResetTimer = new Timer();
            coinsResetTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    coinsClickCount = 0;
                }
            }, 2000);
        }
    }

    @FXML
    private void onExpBoxClicked() {
        // Cancel previous timer if exists
        if (expResetTimer != null) {
            expResetTimer.cancel();
        }

        expClickCount++;

        if (expClickCount >= 5) {
            // Add 1 level silently
            gameState.addLevel();
            updateDisplay();
            expClickCount = 0;
        } else {
            // Reset counter after 2 seconds of no clicks
            expResetTimer = new Timer();
            expResetTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    expClickCount = 0;
                }
            }, 2000);
        }
    }
}
