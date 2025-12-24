package com.farmingcmulator.controller;

import com.farmingcmulator.GameState;
import com.farmingcmulator.SceneManager;
import com.farmingcmulator.model.Crop;
import com.farmingcmulator.model.Rarity;
import com.farmingcmulator.util.SoundManager;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class StoreController implements Initializable {

    @FXML private Label coinsLabel;
    @FXML private Label actionsLabel;
    @FXML private Label levelLabel;

    @FXML private VBox purchaseResultPopup;
    @FXML private Label purchaseResultLabel;
    @FXML private Label purchaseNewLabel;

    @FXML private VBox infoPopup;
    @FXML private Label infoPopupLabel;

    @FXML private VBox confirmPopup;
    @FXML private Label confirmPopupLabel;

    // Water Upgrade elements
    @FXML private VBox waterUpgradeCard;
    @FXML private Label waterUpgradeLevelLabel;
    @FXML private Label waterUpgradeEffectLabel;
    @FXML private Label waterUpgradeCostLabel;
    @FXML private Label waterUpgradeReqLabel;
    @FXML private Button waterUpgradeBtn;

    // Harvest Upgrade elements
    @FXML private VBox harvestUpgradeCard;
    @FXML private Label harvestUpgradeLevelLabel;
    @FXML private Label harvestUpgradeEffectLabel;
    @FXML private Label harvestUpgradeCostLabel;
    @FXML private Label harvestUpgradeReqLabel;
    @FXML private Button harvestUpgradeBtn;

    // Fertilizer elements
    @FXML private Label fertilizerCountLabel;

    private GameState gameState;
    private SoundManager sound = SoundManager.getInstance();
    private int pendingBoxType;
    private int pendingCost;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        gameState = GameState.getInstance();
        updateDisplay();
        updateUpgradeDisplay();
        updateItemsDisplay();
        hideAllPopups();
    }

    private void updateDisplay() {
        coinsLabel.setText(gameState.getCoins() + " coins");
        actionsLabel.setText("Actions: " + gameState.getActionsRemaining());
        levelLabel.setText("Level " + gameState.getPlayerLevel());

        if (gameState.getCoins() < -400) {
            coinsLabel.setStyle("-fx-text-fill: #e74c3c; -fx-font-size: 40px; -fx-font-weight: bold;");
        } else if (gameState.getCoins() < 0) {
            coinsLabel.setStyle("-fx-text-fill: #f39c12; -fx-font-size: 40px; -fx-font-weight: bold;");
        } else {
            coinsLabel.setStyle("-fx-text-fill: #27ae60; -fx-font-size: 40px; -fx-font-weight: bold;");
        }
    }

    private void updateUpgradeDisplay() {
        // Water Upgrade
        int waterLevel = gameState.getWaterUpgradeLevel();
        int waterCapacity = gameState.getWaterCapacity();
        boolean waterMaxed = gameState.isWaterMaxLevel();

        waterUpgradeLevelLabel.setText("Level " + waterLevel + "/2");
        waterUpgradeEffectLabel.setText("Capacity: " + waterCapacity);

        if (waterMaxed) {
            waterUpgradeCostLabel.setText("MAX LEVEL");
            waterUpgradeReqLabel.setText("");
            waterUpgradeBtn.setText("MAXED");
            waterUpgradeBtn.setDisable(true);
            waterUpgradeBtn.getStyleClass().removeAll("upgrade-btn");
            waterUpgradeBtn.getStyleClass().add("upgrade-btn-disabled");
            waterUpgradeCard.getStyleClass().add("upgrade-maxed");
        } else {
            int cost = gameState.getWaterUpgradeCost();
            int reqLevel = gameState.getWaterUpgradeRequiredLevel();
            waterUpgradeCostLabel.setText(cost + " coins");
            waterUpgradeReqLabel.setText("Requires Lv." + reqLevel);

            if (gameState.canUpgradeWater()) {
                waterUpgradeBtn.setDisable(false);
                waterUpgradeBtn.getStyleClass().removeAll("upgrade-btn-disabled");
                if (!waterUpgradeBtn.getStyleClass().contains("upgrade-btn")) {
                    waterUpgradeBtn.getStyleClass().add("upgrade-btn");
                }
                waterUpgradeReqLabel.setStyle("-fx-text-fill: #27ae60;");
            } else {
                waterUpgradeBtn.setDisable(true);
                waterUpgradeBtn.getStyleClass().removeAll("upgrade-btn");
                waterUpgradeBtn.getStyleClass().add("upgrade-btn-disabled");
                waterUpgradeReqLabel.setStyle("-fx-text-fill: #e74c3c;");
            }
        }

        // Harvest Upgrade
        int harvestLevel = gameState.getHarvestUpgradeLevel();
        int harvestBonus = gameState.getHarvestBonus();
        boolean harvestMaxed = gameState.isHarvestMaxLevel();

        harvestUpgradeLevelLabel.setText("Level " + harvestLevel + "/5");
        harvestUpgradeEffectLabel.setText("Bonus: +" + harvestBonus + "%");

        if (harvestMaxed) {
            harvestUpgradeCostLabel.setText("MAX LEVEL");
            harvestUpgradeReqLabel.setText("");
            harvestUpgradeBtn.setText("MAXED");
            harvestUpgradeBtn.setDisable(true);
            harvestUpgradeBtn.getStyleClass().removeAll("upgrade-btn");
            harvestUpgradeBtn.getStyleClass().add("upgrade-btn-disabled");
            harvestUpgradeCard.getStyleClass().add("upgrade-maxed");
        } else {
            int cost = gameState.getHarvestUpgradeCost();
            int reqLevel = gameState.getHarvestUpgradeRequiredLevel();
            harvestUpgradeCostLabel.setText(cost + " coins");
            harvestUpgradeReqLabel.setText("Requires Lv." + reqLevel);

            if (gameState.canUpgradeHarvest()) {
                harvestUpgradeBtn.setDisable(false);
                harvestUpgradeBtn.getStyleClass().removeAll("upgrade-btn-disabled");
                if (!harvestUpgradeBtn.getStyleClass().contains("upgrade-btn")) {
                    harvestUpgradeBtn.getStyleClass().add("upgrade-btn");
                }
                harvestUpgradeReqLabel.setStyle("-fx-text-fill: #27ae60;");
            } else {
                harvestUpgradeBtn.setDisable(true);
                harvestUpgradeBtn.getStyleClass().removeAll("upgrade-btn");
                harvestUpgradeBtn.getStyleClass().add("upgrade-btn-disabled");
                harvestUpgradeReqLabel.setStyle("-fx-text-fill: #e74c3c;");
            }
        }
    }

    private void updateItemsDisplay() {
        if (fertilizerCountLabel != null) {
            fertilizerCountLabel.setText("Owned: " + gameState.getFertilizerCount());
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

    private void showConfirmPopup(String message) {
        if (confirmPopupLabel != null && confirmPopup != null) {
            sound.playPopup();
            confirmPopupLabel.setText(message);
            setPopupVisible(confirmPopup, true);
        }
    }

    @FXML
    private void onConfirmYes() {
        sound.playClick();
        hideAllPopups();
        executePurchase(pendingBoxType, pendingCost);
    }

    @FXML
    private void onConfirmNo() {
        sound.playClick();
        hideAllPopups();
        pendingBoxType = 0;
        pendingCost = 0;
    }

    @FXML
    private void onBuyCommonBox() {
        sound.playClick();
        buyBox(1, "Common", 50);
    }

    @FXML
    private void onBuyRareBox() {
        sound.playClick();
        buyBox(2, "Rare", 100);
    }

    @FXML
    private void onBuyEpicBox() {
        sound.playClick();
        buyBox(3, "Epic", 200);
    }

    private void buyBox(int boxType, String boxName, int cost) {
        if (gameState.noActionsRemaining()) {
            sound.playError();
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
            sound.playPurchase();
            showPurchaseResult(crop);
            updateDisplay();
        }
    }

    private void showPurchaseResult(Crop crop) {
        purchaseResultLabel.setText("You got: " + crop.getName());
        purchaseResultLabel.setStyle("-fx-text-fill: " + Rarity.getColor(crop.getRarity()) + "; -fx-font-size: 26px; -fx-font-weight: bold;");

        String currentSeason = gameState.getCurrentSeason();
        String cropSeason = crop.getSeason();
        String seasonMatch = cropSeason.equals(currentSeason) ? " ✓ In Season!" : " ✗ Off Season";
        purchaseNewLabel.setText("(" + crop.getRarity() + " | " + cropSeason + seasonMatch + ")");
        purchaseNewLabel.setStyle("-fx-text-fill: " + Rarity.getColor(crop.getRarity()) + ";");

        sound.playSuccess();
        setPopupVisible(purchaseResultPopup, true);
    }

    @FXML
    private void onPurchaseResultClose() {
        sound.playClick();
        hideAllPopups();
    }

    // Upgrade handlers
    @FXML
    private void onWaterUpgrade() {
        sound.playClick();

        if (!gameState.canUpgradeWater()) {
            if (gameState.isWaterMaxLevel()) {
                showInfoPopup("Water upgrade is already at max level!");
            } else if (gameState.getPlayerLevel() < gameState.getWaterUpgradeRequiredLevel()) {
                showInfoPopup("You need to be Level " + gameState.getWaterUpgradeRequiredLevel() + " to upgrade!");
            } else {
                showInfoPopup("Not enough coins! Need " + gameState.getWaterUpgradeCost() + " coins.");
            }
            return;
        }

        if (gameState.purchaseWaterUpgrade()) {
            sound.playPurchase();
            sound.playSuccess();
            showInfoPopup("Water upgraded! Capacity: " + gameState.getWaterCapacity());
            updateDisplay();
            updateUpgradeDisplay();
        }
    }

    @FXML
    private void onHarvestUpgrade() {
        sound.playClick();

        if (!gameState.canUpgradeHarvest()) {
            if (gameState.isHarvestMaxLevel()) {
                showInfoPopup("Harvest upgrade is already at max level!");
            } else if (gameState.getPlayerLevel() < gameState.getHarvestUpgradeRequiredLevel()) {
                showInfoPopup("You need to be Level " + gameState.getHarvestUpgradeRequiredLevel() + " to upgrade!");
            } else {
                showInfoPopup("Not enough coins! Need " + gameState.getHarvestUpgradeCost() + " coins.");
            }
            return;
        }

        if (gameState.purchaseHarvestUpgrade()) {
            sound.playPurchase();
            sound.playSuccess();
            showInfoPopup("Harvest upgraded! Bonus: +" + gameState.getHarvestBonus() + "%");
            updateDisplay();
            updateUpgradeDisplay();
        }
    }

    // Fertilizer handler
    @FXML
    private void onBuyFertilizer() {
        sound.playClick();

        if (gameState.getCoins() < GameState.FERTILIZER_COST) {
            sound.playError();
            showInfoPopup("Not enough coins! Need " + GameState.FERTILIZER_COST + " coins.");
            return;
        }

        if (gameState.buyFertilizer()) {
            sound.playPurchase();
            showInfoPopup("Fertilizer purchased! You now have " + gameState.getFertilizerCount() + " fertilizer(s).");
            updateDisplay();
            updateItemsDisplay();
        }
    }

    @FXML
    private void onBackClicked() {
        sound.playClick();
        SceneManager.getInstance().switchScene("GameMenu");
    }
}
