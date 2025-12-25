package com.farmingcmulator.controller;

import com.farmingcmulator.GameState;
import com.farmingcmulator.SceneManager;
import com.farmingcmulator.model.Inventory;
import com.farmingcmulator.model.MarketPrice;
import com.farmingcmulator.model.Plot;
import com.farmingcmulator.model.Rarity;
import com.farmingcmulator.util.Randomizer;
import com.farmingcmulator.util.SoundManager;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import com.farmingcmulator.model.StorageItem;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class FieldController implements Initializable {

    @FXML private Label seasonLabel;
    @FXML private Label actionsLabel;
    @FXML private ImageView backgroundImage;

    // Plot boxes (StackPane for layered layout with badge)
    @FXML private StackPane plot1Box, plot2Box, plot3Box, plot4Box, plot5Box;
    @FXML private StackPane plot6Box, plot7Box, plot8Box, plot9Box, plot10Box;

    // Plot labels
    @FXML private Label plot1Label, plot2Label, plot3Label, plot4Label, plot5Label;
    @FXML private Label plot6Label, plot7Label, plot8Label, plot9Label, plot10Label;

    // Plot water labels
    @FXML private Label plot1Water, plot2Water, plot3Water, plot4Water, plot5Water;
    @FXML private Label plot6Water, plot7Water, plot8Water, plot9Water, plot10Water;

    // Plot days labels
    @FXML private Label plot1Days, plot2Days, plot3Days, plot4Days, plot5Days;
    @FXML private Label plot6Days, plot7Days, plot8Days, plot9Days, plot10Days;

    // Plot number labels
    @FXML private Label plot1Number, plot2Number, plot3Number, plot4Number, plot5Number;
    @FXML private Label plot6Number, plot7Number, plot8Number, plot9Number, plot10Number;

    // Plot images
    @FXML private ImageView plot1Image, plot2Image, plot3Image, plot4Image, plot5Image;
    @FXML private ImageView plot6Image, plot7Image, plot8Image, plot9Image, plot10Image;

    @FXML private Button plantButton;
    @FXML private Button waterButton;
    @FXML private Button fertilizeButton;
    @FXML private Button harvestButton;

    @FXML private VBox inventoryPopup;
    @FXML private VBox harvestResultPopup;
    @FXML private VBox infoPopup;
    @FXML private VBox pestPopup;
    @FXML private ListView<String> inventoryListView;
    @FXML private Label infoPopupLabel;
    @FXML private Label pestPopupLabel;

    @FXML private Label harvestCropLabel;
    @FXML private Label harvestQualityLabel;
    @FXML private Label harvestGradeLabel;
    @FXML private Label harvestMessageLabel;
    @FXML private Label harvestBaseLabel;
    @FXML private Label harvestBonusLabel;
    @FXML private Label harvestTotalLabel;
    @FXML private ProgressBar harvestQualityBar;

    // Market trends
    @FXML private Label priceUpdateLabel;
    @FXML private Label goodPrice1, goodPrice2, goodPrice3;
    @FXML private Label badPrice1, badPrice2, badPrice3;

    // Storage panel
    @FXML private VBox storagePanel;
    @FXML private TableView<StorageItem> storageTable;
    @FXML private TableColumn<StorageItem, String> storageCropColumn;
    @FXML private TableColumn<StorageItem, String> storageQtyColumn;
    @FXML private TableColumn<StorageItem, String> storageQualityColumn;
    @FXML private TableColumn<StorageItem, String> storagePriceColumn;
    @FXML private Label storageCapacityLabel;
    @FXML private Label storageTotalLabel;
    @FXML private Button sellButton;
    @FXML private Button sellAllButton;
    @FXML private VBox decayWarningBox;
    @FXML private Label decayWarningLabel;

    private GameState gameState;
    private SoundManager sound = SoundManager.getInstance();

    private StackPane[] plotBoxes;
    private Label[] plotLabels;
    private Label[] plotWaters;
    private Label[] plotDaysLabels;
    private Label[] plotNumbers;
    private ImageView[] plotImages;

    private int selectedPlotIndex = -1;
    private String currentAction = "";

    private Image placeholderImage;
    private Image lockedImage;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        gameState = GameState.getInstance();

        plotBoxes = new StackPane[] { plot1Box, plot2Box, plot3Box, plot4Box, plot5Box,
                                       plot6Box, plot7Box, plot8Box, plot9Box, plot10Box };
        plotLabels = new Label[] { plot1Label, plot2Label, plot3Label, plot4Label, plot5Label,
                                    plot6Label, plot7Label, plot8Label, plot9Label, plot10Label };
        plotWaters = new Label[] { plot1Water, plot2Water, plot3Water, plot4Water, plot5Water,
                                    plot6Water, plot7Water, plot8Water, plot9Water, plot10Water };
        plotDaysLabels = new Label[] { plot1Days, plot2Days, plot3Days, plot4Days, plot5Days,
                                        plot6Days, plot7Days, plot8Days, plot9Days, plot10Days };
        plotNumbers = new Label[] { plot1Number, plot2Number, plot3Number, plot4Number, plot5Number,
                                     plot6Number, plot7Number, plot8Number, plot9Number, plot10Number };
        plotImages = new ImageView[] { plot1Image, plot2Image, plot3Image, plot4Image, plot5Image,
                                        plot6Image, plot7Image, plot8Image, plot9Image, plot10Image };

        // Load placeholder images
        try {
            placeholderImage = new Image(getClass().getResourceAsStream("/images/crops/placeholder.png"));
        } catch (Exception e) {
            placeholderImage = null;
        }
        try {
            lockedImage = new Image(getClass().getResourceAsStream("/images/crops/locked.png"));
        } catch (Exception e) {
            lockedImage = null;
        }

        hideAllPopups();
        initializeStorageTable();
        updateDisplay();
        updateStorageDisplay();
        resetAllActionButtons();

        // Check for pest invasion notification
        checkPestInvasionNotification();
    }

    /**
     * Check if there was a pest invasion and show notification.
     */
    private void checkPestInvasionNotification() {
        if (gameState.hadPestInvasion()) {
            showPestInvasionPopup();
        }
    }

    /**
     * Show pest invasion popup with damage info.
     */
    private void showPestInvasionPopup() {
        if (pestPopup != null && pestPopupLabel != null) {
            String info = gameState.getPestInvasionInfo();
            pestPopupLabel.setText(info);
            sound.playError();
            setPopupVisible(pestPopup, true);
        }
    }

    /**
     * Initialize the storage table columns.
     */
    private void initializeStorageTable() {
        if (storageTable == null) return;

        // Crop name column
        storageCropColumn.setCellValueFactory(cellData -> {
            StorageItem item = cellData.getValue();
            return new SimpleStringProperty(item.getCropName());
        });

        // Quantity column
        storageQtyColumn.setCellValueFactory(cellData -> {
            StorageItem item = cellData.getValue();
            return new SimpleStringProperty(String.valueOf(item.getQuantity()));
        });

        // Quality column
        storageQualityColumn.setCellValueFactory(cellData -> {
            StorageItem item = cellData.getValue();
            return new SimpleStringProperty(item.getDisplayQuality());
        });

        // Price column (current value based on market)
        storagePriceColumn.setCellValueFactory(cellData -> {
            StorageItem item = cellData.getValue();
            double multiplier = gameState.getMarketMultiplier(item.getCropName());
            int value = item.getCurrentValue(multiplier);
            return new SimpleStringProperty(value + " coins");
        });
    }

    /**
     * Update the storage display with current items.
     */
    private void updateStorageDisplay() {
        if (storageTable == null) return;

        // Update table data
        List<StorageItem> storage = gameState.getStorage();
        storageTable.setItems(FXCollections.observableArrayList(storage));

        // Update capacity label
        if (storageCapacityLabel != null) {
            int current = gameState.getStorageSlotCount();
            int capacity = gameState.getStorageCapacity();
            storageCapacityLabel.setText(current + "/" + capacity);

            // Change color if near full
            if (current >= capacity) {
                storageCapacityLabel.setStyle("-fx-text-fill: #FF6B6B;");
            } else if (current >= capacity - 2) {
                storageCapacityLabel.setStyle("-fx-text-fill: #FFD93D;");
            } else {
                storageCapacityLabel.setStyle("-fx-text-fill: #6BCB77;");
            }
        }

        // Update total value label
        if (storageTotalLabel != null) {
            int totalValue = gameState.getStorageTotalValue();
            storageTotalLabel.setText("Total Value: " + totalValue + " coins");
        }

        // Update decay warning
        updateDecayWarning();

        // Refresh table to update prices
        storageTable.refresh();
    }

    /**
     * Update the decay warning display based on items about to decay.
     */
    private void updateDecayWarning() {
        if (decayWarningBox == null || decayWarningLabel == null) return;

        List<StorageItem> decayingItems = gameState.getItemsAboutToDecay();

        if (decayingItems.isEmpty()) {
            decayWarningBox.setVisible(false);
            decayWarningBox.setManaged(false);
        } else {
            StringBuilder warning = new StringBuilder();
            for (int i = 0; i < decayingItems.size(); i++) {
                StorageItem item = decayingItems.get(i);
                int daysLeft = item.getDaysUntilDecay();
                warning.append(item.getCropName())
                       .append(" (-10% in ")
                       .append(daysLeft)
                       .append(daysLeft == 1 ? " day)" : " days)");
                if (i < decayingItems.size() - 1) {
                    warning.append(", ");
                }
            }
            decayWarningLabel.setText(warning.toString());
            decayWarningBox.setVisible(true);
            decayWarningBox.setManaged(true);
        }
    }

    private void updateDisplay() {
        actionsLabel.setText("Actions: " + gameState.getActionsRemaining());
        seasonLabel.setText(gameState.getCurrentSeason());
        updateSeasonLabelColor();
        updateSeasonBackground();
        updateMarketTrends();

        List<Plot> plots = gameState.getPlots();
        for (int i = 0; i < GameState.NUM_PLOTS; i++) {
            updatePlotDisplay(i, plots.get(i));
        }
    }

    private void updateSeasonLabelColor() {
        String season = gameState.getCurrentSeason();
        String color;
        switch (season) {
            case "Spring": color = "#FF69B4"; break;
            case "Summer": color = "#FFD700"; break;
            case "Fall": color = "#FF8C00"; break;
            case "Winter": color = "#87CEEB"; break;
            default: color = "#FFD966";
        }
        seasonLabel.setStyle("-fx-text-fill: " + color + "; -fx-font-size: 20px; -fx-font-weight: bold;");
    }

    private void updateSeasonBackground() {
        String season = gameState.getCurrentSeason().toLowerCase();
        try {
            Image bgImage = new Image(getClass().getResourceAsStream("/images/field_" + season + ".png"));
            if (bgImage != null && !bgImage.isError()) {
                backgroundImage.setImage(bgImage);
            }
        } catch (Exception e) {
            // Keep current background if image not found
        }
    }

    /**
     * Update the market trends display with current prices.
     */
    private void updateMarketTrends() {
        // Update countdown to next price change
        int daysUntil = gameState.getDaysUntilPriceUpdate();
        if (priceUpdateLabel != null) {
            priceUpdateLabel.setText("Updates in: " + daysUntil + " day" + (daysUntil != 1 ? "s" : ""));
        }

        // Get top 3 and bottom 3 prices
        List<MarketPrice> topPrices = gameState.getTopPriceCrops(3);
        List<MarketPrice> bottomPrices = gameState.getBottomPriceCrops(3);

        // Good prices labels array for iteration
        Label[] goodLabels = { goodPrice1, goodPrice2, goodPrice3 };
        Label[] badLabels = { badPrice1, badPrice2, badPrice3 };

        // Populate good prices
        for (int i = 0; i < goodLabels.length; i++) {
            if (goodLabels[i] != null && i < topPrices.size()) {
                MarketPrice mp = topPrices.get(i);
                String text = mp.getCropName() + " " + mp.getMultiplierDisplay();
                goodLabels[i].setText(text);
                goodLabels[i].setStyle("-fx-text-fill: " + Rarity.getColor(mp.getCropRarity()) + ";");
            }
        }

        // Populate bad prices
        for (int i = 0; i < badLabels.length; i++) {
            if (badLabels[i] != null && i < bottomPrices.size()) {
                MarketPrice mp = bottomPrices.get(i);
                String text = mp.getCropName() + " " + mp.getMultiplierDisplay();
                badLabels[i].setText(text);
                badLabels[i].setStyle("-fx-text-fill: " + Rarity.getColor(mp.getCropRarity()) + ";");
            }
        }
    }

    private void updatePlotDisplay(int index, Plot plot) {
        boolean isUnlocked = gameState.isPlotUnlocked(index);

        // Clear all dynamic style classes first
        plotBoxes[index].getStyleClass().removeAll(
            "plot-card-locked", "plot-card-ready",
            "plot-rarity-common", "plot-rarity-uncommon", "plot-rarity-rare",
            "plot-rarity-epic", "plot-rarity-legendary"
        );
        plotBoxes[index].setStyle(""); // Clear inline styles

        if (!isUnlocked) {
            // Locked plot
            int reqLevel = gameState.getPlotRequiredLevel(index);
            plotLabels[index].setText("Locked");
            plotWaters[index].setText("Req: Lv." + reqLevel);
            plotDaysLabels[index].setText("");
            plotNumbers[index].setText(String.valueOf(index + 1));
            plotBoxes[index].getStyleClass().add("plot-card-locked");

            if (lockedImage != null) {
                plotImages[index].setImage(lockedImage);
            } else {
                plotImages[index].setImage(null);
            }
            return;
        }

        plotNumbers[index].setText(String.valueOf(index + 1));

        if (plot.isEmpty()) {
            plotLabels[index].setText("Empty");
            plotLabels[index].setStyle("");
            plotWaters[index].setText("");
            plotDaysLabels[index].setText("");

            if (placeholderImage != null) {
                plotImages[index].setImage(placeholderImage);
            } else {
                plotImages[index].setImage(null);
            }
        } else {
            plotLabels[index].setText(plot.getCropName());
            plotLabels[index].setStyle(""); // Let CSS handle the color based on rarity
            plotWaters[index].setText("Water: " + plot.getWaterRemaining());
            plotDaysLabels[index].setText("Days: " + plot.getDaysRemaining());

            // Load crop image
            Image cropImage = loadCropImage(plot.getCropName());
            plotImages[index].setImage(cropImage);

            // Apply rarity style - this will set background, border, and text colors
            String rarityClass = Rarity.getCssClass(plot.getCropRarity());
            plotBoxes[index].getStyleClass().add(rarityClass);

            // Apply ready-to-harvest style if applicable (adds green highlight)
            if (plot.isReadyToHarvest()) {
                plotBoxes[index].getStyleClass().add("plot-card-ready");
            }
        }
    }

    private Image loadCropImage(String cropName) {
        try {
            String filename = cropName.toLowerCase().replace(" ", "_") + ".png";
            Image img = new Image(getClass().getResourceAsStream("/images/crops/" + filename));
            if (img != null && !img.isError()) {
                return img;
            }
        } catch (Exception e) {
            // Fall through to placeholder
        }
        return placeholderImage;
    }

    private void hideAllPopups() {
        setPopupVisible(inventoryPopup, false);
        setPopupVisible(harvestResultPopup, false);
        setPopupVisible(infoPopup, false);
        setPopupVisible(pestPopup, false);
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
        if (fertilizeButton != null) {
            fertilizeButton.getStyleClass().removeAll("field-action-btn-fertilize-active");
            if (!fertilizeButton.getStyleClass().contains("field-action-btn-fertilize")) {
                fertilizeButton.getStyleClass().add("field-action-btn-fertilize");
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

    // Plot click handlers
    @FXML private void onPlot1Clicked() { handlePlotClick(0); }
    @FXML private void onPlot2Clicked() { handlePlotClick(1); }
    @FXML private void onPlot3Clicked() { handlePlotClick(2); }
    @FXML private void onPlot4Clicked() { handlePlotClick(3); }
    @FXML private void onPlot5Clicked() { handlePlotClick(4); }
    @FXML private void onPlot6Clicked() { handlePlotClick(5); }
    @FXML private void onPlot7Clicked() { handlePlotClick(6); }
    @FXML private void onPlot8Clicked() { handlePlotClick(7); }
    @FXML private void onPlot9Clicked() { handlePlotClick(8); }
    @FXML private void onPlot10Clicked() { handlePlotClick(9); }

    private void handlePlotClick(int plotIndex) {
        if (currentAction.isEmpty()) return;

        // Check if plot is unlocked
        if (!gameState.isPlotUnlocked(plotIndex)) {
            int reqLevel = gameState.getPlotRequiredLevel(plotIndex);
            showInfoPopup("This plot is locked! Reach Level " + reqLevel + " to unlock.");
            return;
        }

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
                    int waterCapacity = gameState.getWaterCapacity();
                    String msg = "Watered";
                    if (waterCapacity > 1) {
                        msg += " (x" + waterCapacity + ")";
                    }
                    msg += "! Water left: " + plot.getWaterRemaining();
                    showInfoPopup(msg);
                    updateDisplay();
                }
                break;

            case "fertilize":
                if (plot.isEmpty()) {
                    showInfoPopup("This plot is empty!");
                    return;
                }
                if (plot.isFertilized()) {
                    showInfoPopup("This crop is already fertilized!");
                    return;
                }
                if (gameState.getFertilizerCount() <= 0) {
                    showInfoPopup("No fertilizer! Buy some at the store.");
                    return;
                }
                if (gameState.noActionsRemaining()) {
                    showInfoPopup("No actions remaining!");
                    return;
                }
                if (gameState.useFertilizer(plotIndex)) {
                    sound.playPlant(); // Use plant sound for fertilizing
                    int qualityBonus = plot.getFertilizerQualityBonus();
                    showInfoPopup("Fertilized! Quality +" + qualityBonus + "%, Days reduced!");
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
                if (gameState.isStorageFull()) {
                    showInfoPopup("Storage is full! Sell some crops first.");
                    return;
                }
                String cropName = plot.getCropName();
                String cropRarity = plot.getCropRarity();
                int[] result = gameState.harvestCrop(plotIndex);
                if (result != null) {
                    sound.playHarvest();
                    // result: [quality, basePrice, expGained]
                    showHarvestResult(cropName, cropRarity, result[0], result[1], result[2]);
                    updateStorageDisplay();
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
    private void onFertilizeClicked() {
        sound.playClick();
        if (currentAction.equals("fertilize")) {
            currentAction = "";
            resetAllActionButtons();
            return;
        }

        if (gameState.getFertilizerCount() <= 0) {
            sound.playError();
            showInfoPopup("No fertilizer! Buy some at the store.");
            return;
        }

        currentAction = "fertilize";
        resetAllActionButtons();
        setButtonActive(fertilizeButton, "field-action-btn-fertilize", "field-action-btn-fertilize-active");
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

    @FXML
    private void onPestPopupClose() {
        sound.playClick();
        setPopupVisible(pestPopup, false);
        gameState.clearPestInvasions();  // Clear after displaying
    }

    private void showInventoryPopup() {
        sound.playPopup();
        String currentSeason = gameState.getCurrentSeason();
        List<String> items = new ArrayList<>();
        for (Inventory inv : gameState.getInventory()) {
            String cropSeason = inv.getCrop().getSeason();
            String seasonIndicator = cropSeason.equals(currentSeason) ? " ✓" : " ✗";
            items.add(inv.getCrop().getName() + " (" + inv.getCrop().getRarity() + ", " + cropSeason + seasonIndicator + ") x" + inv.getQuantity());
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

    private void showHarvestResult(String cropName, String rarity, int quality, int basePrice, int expGained) {
        harvestCropLabel.setText(cropName + " (" + rarity + ")");
        harvestCropLabel.setStyle("-fx-text-fill: " + Rarity.getColor(rarity) + ";");

        harvestQualityLabel.setText(quality + "%");
        harvestQualityLabel.setStyle("-fx-text-fill: " + Randomizer.getQualityColor(quality) + ";");

        harvestGradeLabel.setText(Randomizer.getQualityTier(quality));
        harvestGradeLabel.setStyle("-fx-text-fill: " + Randomizer.getQualityColor(quality) + ";");

        harvestMessageLabel.setText("\"" + Randomizer.getQualityMessage(quality) + "\"");

        // Show stored info instead of immediate sale
        double marketMultiplier = gameState.getMarketMultiplier(cropName);
        int adjustedBase = (int) Math.round(basePrice * marketMultiplier);
        int qualityBonus = (int) Math.round(adjustedBase * quality / 100.0);
        int potentialValue = adjustedBase + qualityBonus;

        harvestBaseLabel.setText(adjustedBase + " coins (market)");
        harvestBonusLabel.setText("+" + qualityBonus + " coins (+" + quality + "%)");
        harvestTotalLabel.setText(potentialValue + " coins");

        harvestQualityBar.setProgress(quality / 100.0);

        sound.playSuccess();
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

    // ==================== STORAGE SELL ACTIONS ====================

    @FXML
    private void onSellClicked() {
        sound.playClick();

        if (storageTable == null) return;

        StorageItem selected = storageTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showInfoPopup("Select a crop to sell!");
            return;
        }

        int index = storageTable.getSelectionModel().getSelectedIndex();
        int[] result = gameState.sellFromStorage(index);

        if (result != null) {
            sound.playCoins();
            showInfoPopup("Sold " + selected.getCropName() + " x" + result[1] + " for " + result[0] + " coins!");
            updateStorageDisplay();
            updateDisplay();
        }
    }

    @FXML
    private void onSellAllClicked() {
        sound.playClick();

        if (gameState.getStorage().isEmpty()) {
            showInfoPopup("Storage is empty!");
            return;
        }

        int totalEarned = gameState.sellAllFromStorage();
        sound.playCoins();
        showInfoPopup("Sold all crops for " + totalEarned + " coins!");
        updateStorageDisplay();
        updateDisplay();
    }
}
