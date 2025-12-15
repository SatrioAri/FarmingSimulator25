package com.farmingcmulator.controller;

import com.farmingcmulator.GameState;
import com.farmingcmulator.SceneManager;
import com.farmingcmulator.model.Inventory;
import com.farmingcmulator.model.Plot;
import com.farmingcmulator.model.Rarity;
import com.farmingcmulator.util.Randomizer;
import com.farmingcmulator.util.SoundManager;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class FieldController implements Initializable {

    @FXML private Label actionsLabel;
    
    @FXML private VBox plot1Box, plot2Box, plot3Box, plot4Box, plot5Box;
    @FXML private Label plot1Label, plot2Label, plot3Label, plot4Label, plot5Label;
    @FXML private Label plot1Water, plot2Water, plot3Water, plot4Water, plot5Water;
    @FXML private Label plot1Days, plot2Days, plot3Days, plot4Days, plot5Days;
    @FXML private Circle plot1Circle, plot2Circle, plot3Circle, plot4Circle, plot5Circle;
    
    @FXML private Button plantButton;
    @FXML private Button waterButton;
    @FXML private Button harvestButton;
    
    @FXML private VBox inventoryPopup;
    @FXML private VBox harvestResultPopup;
    @FXML private VBox infoPopup;
    @FXML private ListView<String> inventoryListView;
    @FXML private Label infoPopupLabel;
    
    @FXML private Label harvestCropLabel;
    @FXML private Label harvestQualityLabel;
    @FXML private Label harvestGradeLabel;
    @FXML private Label harvestMessageLabel;
    @FXML private Label harvestBaseLabel;
    @FXML private Label harvestBonusLabel;
    @FXML private Label harvestTotalLabel;
    @FXML private ProgressBar harvestQualityBar;
    
    private GameState gameState;
    private SoundManager sound = SoundManager.getInstance();
    private VBox[] plotBoxes;
    private Label[] plotLabels;
    private Label[] plotWaters;
    private Label[] plotDaysLabels;
    private Circle[] plotCircles;
    
    private int selectedPlotIndex = -1;
    private String currentAction = "";

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        gameState = GameState.getInstance();
        
        plotBoxes = new VBox[] { plot1Box, plot2Box, plot3Box, plot4Box, plot5Box };
        plotLabels = new Label[] { plot1Label, plot2Label, plot3Label, plot4Label, plot5Label };
        plotWaters = new Label[] { plot1Water, plot2Water, plot3Water, plot4Water, plot5Water };
        plotDaysLabels = new Label[] { plot1Days, plot2Days, plot3Days, plot4Days, plot5Days };
        plotCircles = new Circle[] { plot1Circle, plot2Circle, plot3Circle, plot4Circle, plot5Circle };
        
        hideAllPopups();
        updateDisplay();
        resetAllActionButtons();
    }

    private void updateDisplay() {
        actionsLabel.setText("Actions: " + gameState.getActionsRemaining());
        
        List<Plot> plots = gameState.getPlots();
        for (int i = 0; i < 5; i++) {
            updatePlotDisplay(i, plots.get(i));
        }
    }

    private void updatePlotDisplay(int index, Plot plot) {
        if (plot.isEmpty()) {
            plotLabels[index].setText("Empty");
            plotLabels[index].setStyle("-fx-text-fill: #7f8c8d;");
            plotWaters[index].setText("");
            plotDaysLabels[index].setText("");
            plotCircles[index].setFill(Color.web("#bdc3c7"));
            plotCircles[index].setVisible(false);
            plotBoxes[index].setStyle("-fx-background-color: rgba(255,255,255,0.92); -fx-background-radius: 18;");
        } else {
            plotLabels[index].setText(plot.getCropName());
            plotLabels[index].setStyle("-fx-text-fill: " + plot.getRarityColor() + "; -fx-font-weight: bold;");
            plotWaters[index].setText("Water: " + plot.getWaterRemaining());
            plotDaysLabels[index].setText("Days: " + plot.getDaysRemaining());
            plotCircles[index].setFill(Color.web(plot.getRarityColor()));
            plotCircles[index].setVisible(true);
            
            if (plot.isReadyToHarvest()) {
                plotBoxes[index].setStyle("-fx-background-color: #d5f5e3; -fx-background-radius: 18; -fx-border-color: #27ae60; -fx-border-width: 4; -fx-border-radius: 18;");
            } else {
                plotBoxes[index].setStyle("-fx-background-color: #fef9e7; -fx-background-radius: 18;");
            }
        }
    }

    private void hideAllPopups() {
        setPopupVisible(inventoryPopup, false);
        setPopupVisible(harvestResultPopup, false);
        setPopupVisible(infoPopup, false);
    }
    
    private void setPopupVisible(VBox popup, boolean visible) {
        if (popup != null) {
            popup.setVisible(visible);
            popup.setManaged(visible);
        }
    }
    
    private void resetAllActionButtons() {
        if (plantButton != null) {
            plantButton.getStyleClass().removeAll("field-action-btn-plant-active");
            if (!plantButton.getStyleClass().contains("field-action-btn-plant")) {
                plantButton.getStyleClass().add("field-action-btn-plant");
            }
        }
        if (waterButton != null) {
            waterButton.getStyleClass().removeAll("field-action-btn-water-active");
            if (!waterButton.getStyleClass().contains("field-action-btn-water")) {
                waterButton.getStyleClass().add("field-action-btn-water");
            }
        }
        if (harvestButton != null) {
            harvestButton.getStyleClass().removeAll("field-action-btn-harvest-active");
            if (!harvestButton.getStyleClass().contains("field-action-btn-harvest")) {
                harvestButton.getStyleClass().add("field-action-btn-harvest");
            }
        }
    }
    
    private void setButtonActive(Button button, String normalClass, String activeClass) {
        button.getStyleClass().removeAll(normalClass);
        if (!button.getStyleClass().contains(activeClass)) {
            button.getStyleClass().add(activeClass);
        }
    }

    @FXML private void onPlot1Clicked() { handlePlotClick(0); }
    @FXML private void onPlot2Clicked() { handlePlotClick(1); }
    @FXML private void onPlot3Clicked() { handlePlotClick(2); }
    @FXML private void onPlot4Clicked() { handlePlotClick(3); }
    @FXML private void onPlot5Clicked() { handlePlotClick(4); }

    private void handlePlotClick(int plotIndex) {
        if (currentAction.isEmpty()) return;
        
        Plot plot = gameState.getPlots().get(plotIndex);
        
        switch (currentAction) {
            case "plant":
                if (!plot.isEmpty()) {
                    showInfoPopup("This plot already has a crop!");
                    return;
                }
                if (gameState.noActionsRemaining()) {
                    showInfoPopup("No actions remaining!");
                    return;
                }
                selectedPlotIndex = plotIndex;
                showInventoryPopup();
                break;
                
            case "water":
                if (plot.isEmpty()) {
                    showInfoPopup("This plot is empty!");
                    return;
                }
                if (plot.getWaterRemaining() <= 0) {
                    showInfoPopup("This crop doesn't need water!");
                    return;
                }
                if (gameState.noActionsRemaining()) {
                    showInfoPopup("No actions remaining!");
                    return;
                }
                if (gameState.waterCrop(plotIndex)) {
                    sound.playWater();
                    showInfoPopup("Watered! Water left: " + plot.getWaterRemaining());
                    updateDisplay();
                }
                break;
                
            case "harvest":
                if (plot.isEmpty()) {
                    showInfoPopup("This plot is empty!");
                    return;
                }
                if (!plot.isReadyToHarvest()) {
                    showInfoPopup("Not ready! Water: " + plot.getWaterRemaining() + ", Days: " + plot.getDaysRemaining());
                    return;
                }
                if (gameState.noActionsRemaining()) {
                    showInfoPopup("No actions remaining!");
                    return;
                }
                String cropName = plot.getCropName();
                String cropRarity = plot.getCropRarity();
                int[] result = gameState.harvestCrop(plotIndex);
                if (result != null) {
                    sound.playHarvest();
                    showHarvestResult(cropName, cropRarity, result[2], result[0], result[1]);
                }
                break;
        }
    }

    @FXML
    private void onPlantClicked() {
        sound.playClick();
        if (currentAction.equals("plant")) {
            currentAction = "";
            resetAllActionButtons();
            return;
        }
        
        if (gameState.getInventory().isEmpty()) {
            sound.playError();
            showInfoPopup("Inventory empty! Buy seeds at store.");
            return;
        }
        
        currentAction = "plant";
        resetAllActionButtons();
        setButtonActive(plantButton, "field-action-btn-plant", "field-action-btn-plant-active");
    }

    @FXML
    private void onWaterClicked() {
        sound.playClick();
        if (currentAction.equals("water")) {
            currentAction = "";
            resetAllActionButtons();
            return;
        }
        
        currentAction = "water";
        resetAllActionButtons();
        setButtonActive(waterButton, "field-action-btn-water", "field-action-btn-water-active");
    }

    @FXML
    private void onHarvestClicked() {
        sound.playClick();
        if (currentAction.equals("harvest")) {
            currentAction = "";
            resetAllActionButtons();
            return;
        }
        
        currentAction = "harvest";
        resetAllActionButtons();
        setButtonActive(harvestButton, "field-action-btn-harvest", "field-action-btn-harvest-active");
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

    private void showInventoryPopup() {
        sound.playPopup();
        List<String> items = new ArrayList<>();
        for (Inventory inv : gameState.getInventory()) {
            items.add(inv.getCrop().getName() + " (" + inv.getCrop().getRarity() + ") x" + inv.getQuantity());
        }
        
        inventoryListView.getItems().clear();
        inventoryListView.getItems().addAll(items);
        setPopupVisible(inventoryPopup, true);
    }

    @FXML
    private void onCropSelected() {
        int selectedIndex = inventoryListView.getSelectionModel().getSelectedIndex();
        if (selectedIndex < 0) return;
        
        List<Inventory> inventory = gameState.getInventory();
        if (selectedIndex >= inventory.size()) return;
        
        Inventory selectedItem = inventory.get(selectedIndex);
        
        if (gameState.plantCrop(selectedPlotIndex, selectedItem)) {
            sound.playPlant();
            hideAllPopups();
            updateDisplay();
            showInfoPopup("Planted " + selectedItem.getCrop().getName() + " on Plot " + (selectedPlotIndex + 1));
        }
        
        selectedPlotIndex = -1;
    }

    @FXML
    private void onInventoryCancel() {
        sound.playClick();
        hideAllPopups();
        selectedPlotIndex = -1;
    }

    private void showHarvestResult(String cropName, String rarity, int basePrice, int finalPrice, int quality) {
        harvestCropLabel.setText(cropName + " (" + rarity + ")");
        harvestCropLabel.setStyle("-fx-text-fill: " + Rarity.getColor(rarity) + ";");
        
        harvestQualityLabel.setText(quality + "%");
        harvestQualityLabel.setStyle("-fx-text-fill: " + Randomizer.getQualityColor(quality) + ";");
        
        harvestGradeLabel.setText(Randomizer.getQualityTier(quality));
        harvestGradeLabel.setStyle("-fx-text-fill: " + Randomizer.getQualityColor(quality) + ";");
        
        harvestMessageLabel.setText("\"" + Randomizer.getQualityMessage(quality) + "\"");
        
        harvestBaseLabel.setText(basePrice + " coins");
        harvestBonusLabel.setText("+" + (finalPrice - basePrice) + " coins (+" + quality + "%)");
        harvestTotalLabel.setText(finalPrice + " coins");
        
        harvestQualityBar.setProgress(quality / 100.0);
        
        sound.playSuccess();
        sound.playCoins();
        setPopupVisible(harvestResultPopup, true);
    }

    @FXML
    private void onHarvestResultClose() {
        sound.playClick();
        hideAllPopups();
        updateDisplay();
    }

    @FXML
    private void onBackClicked() {
        sound.playClick();
        SceneManager.getInstance().switchScene("GameMenu");
    }
}
