package com.farmingcmulator.controller;

import com.farmingcmulator.GameState;
import com.farmingcmulator.SceneManager;
import com.farmingcmulator.model.Crop;
import com.farmingcmulator.model.Rarity;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class StoreController implements Initializable {

    @FXML private Label coinsLabel;
    @FXML private Label actionsLabel;
    
    @FXML private VBox purchaseResultPopup;
    @FXML private Label purchaseResultLabel;
    @FXML private Label purchaseNewLabel;
    
    @FXML private VBox infoPopup;
    @FXML private Label infoPopupLabel;
    
    @FXML private VBox confirmPopup;
    @FXML private Label confirmPopupLabel;
    
    private GameState gameState;
    private int pendingBoxType;
    private int pendingCost;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        gameState = GameState.getInstance();
        updateDisplay();
        hideAllPopups();
    }

    private void updateDisplay() {
        coinsLabel.setText(gameState.getCoins() + " coins");
        actionsLabel.setText("Actions: " + gameState.getActionsRemaining());
        
        if (gameState.getCoins() < -400) {
            coinsLabel.setStyle("-fx-text-fill: #e74c3c; -fx-font-size: 40px; -fx-font-weight: bold;");
        } else if (gameState.getCoins() < 0) {
            coinsLabel.setStyle("-fx-text-fill: #f39c12; -fx-font-size: 40px; -fx-font-weight: bold;");
        } else {
            coinsLabel.setStyle("-fx-text-fill: #27ae60; -fx-font-size: 40px; -fx-font-weight: bold;");
        }
    }

    private void hideAllPopups() {
        setPopupVisible(purchaseResultPopup, false);
        setPopupVisible(infoPopup, false);
        setPopupVisible(confirmPopup, false);
    }
    
    private void setPopupVisible(VBox popup, boolean visible) {
        if (popup != null) {
            popup.setVisible(visible);
            popup.setManaged(visible);
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
    
    private void showConfirmPopup(String message) {
        if (confirmPopupLabel != null && confirmPopup != null) {
            confirmPopupLabel.setText(message);
            setPopupVisible(confirmPopup, true);
        }
    }
    
    @FXML
    private void onConfirmYes() {
        hideAllPopups();
        executePurchase(pendingBoxType, pendingCost);
    }
    
    @FXML
    private void onConfirmNo() {
        hideAllPopups();
        pendingBoxType = 0;
        pendingCost = 0;
    }

    @FXML
    private void onBuyCommonBox() {
        buyBox(1, "Common", 50);
    }

    @FXML
    private void onBuyRareBox() {
        buyBox(2, "Rare", 100);
    }

    @FXML
    private void onBuyEpicBox() {
        buyBox(3, "Epic", 200);
    }

    private void buyBox(int boxType, String boxName, int cost) {
        if (gameState.noActionsRemaining()) {
            showInfoPopup("No actions remaining today!");
            return;
        }
        
        if (gameState.getCoins() - cost <= GameState.BANKRUPT_LIMIT) {
            pendingBoxType = boxType;
            pendingCost = cost;
            showConfirmPopup("Warning! Buying this will risk bankruptcy!\nAre you sure?");
            return;
        }
        
        executePurchase(boxType, cost);
    }
    
    private void executePurchase(int boxType, int cost) {
        Crop crop = gameState.buySeedBox(boxType);
        
        if (crop != null) {
            showPurchaseResult(crop);
            updateDisplay();
        }
    }

    private void showPurchaseResult(Crop crop) {
        purchaseResultLabel.setText("You got: " + crop.getName());
        purchaseResultLabel.setStyle("-fx-text-fill: " + Rarity.getColor(crop.getRarity()) + "; -fx-font-size: 26px; -fx-font-weight: bold;");
        
        purchaseNewLabel.setText("(" + crop.getRarity() + ")");
        purchaseNewLabel.setStyle("-fx-text-fill: " + Rarity.getColor(crop.getRarity()) + ";");
        
        setPopupVisible(purchaseResultPopup, true);
    }

    @FXML
    private void onPurchaseResultClose() {
        hideAllPopups();
    }

    @FXML
    private void onBackClicked() {
        SceneManager.getInstance().switchScene("GameMenu");
    }
}
