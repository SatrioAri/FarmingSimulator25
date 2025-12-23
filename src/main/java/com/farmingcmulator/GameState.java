package com.farmingcmulator;

import com.farmingcmulator.model.Crop;
import com.farmingcmulator.model.Highscore;
import com.farmingcmulator.model.Inventory;
import com.farmingcmulator.model.Plot;
import com.farmingcmulator.model.Rarity;
import com.farmingcmulator.util.FileManager;
import com.farmingcmulator.util.Randomizer;

import java.util.ArrayList;
import java.util.List;

public class GameState {
    
    private static GameState instance;
    
    public static final int STARTING_COINS = 100;
    public static final int SEASON_DAYS = 90;
    public static final int DAILY_ACTIONS = 10;
    public static final int NUM_PLOTS = 10;
    public static final int BANKRUPT_LIMIT = -500;
    public static final int BASE_EXP_REQUIREMENT = 100;

    private String playerName;
    private int coins;
    private int daysRemaining;
    private int actionsRemaining;

    // Level system
    private int playerLevel;
    private int currentExp;

    // Upgrade system
    private int waterUpgradeLevel;
    private int harvestUpgradeLevel;
    
    private List<Plot> plots;
    private List<Inventory> inventory;
    private List<Crop> cropDatabase;
    private List<Highscore> highscores;
    
    private FileManager fileManager;
    private Randomizer randomizer;
    
    private GameState() {
        fileManager = new FileManager();
        randomizer = new Randomizer();
        plots = new ArrayList<>();
        inventory = new ArrayList<>();
        cropDatabase = new ArrayList<>();
        highscores = new ArrayList<>();
    }
    
    public static GameState getInstance() {
        if (instance == null) {
            instance = new GameState();
        }
        return instance;
    }
    
    public void startNewGame(String playerName) {
        this.playerName = playerName;
        this.coins = STARTING_COINS;
        this.daysRemaining = SEASON_DAYS;
        this.actionsRemaining = DAILY_ACTIONS;

        // Initialize level system
        this.playerLevel = 1;
        this.currentExp = 0;

        // Initialize upgrade system
        this.waterUpgradeLevel = 0;
        this.harvestUpgradeLevel = 0;

        plots.clear();
        for (int i = 1; i <= NUM_PLOTS; i++) {
            plots.add(new Plot(i));
        }

        inventory.clear();
        cropDatabase = fileManager.readCropData();
        highscores = fileManager.readHighscores();
    }
    
    public boolean plantCrop(int plotIndex, Inventory selectedItem) {
        if (actionsRemaining <= 0) return false;
        if (plotIndex < 0 || plotIndex >= NUM_PLOTS) return false;
        
        Plot plot = plots.get(plotIndex);
        if (!plot.isEmpty()) return false;
        if (selectedItem == null || selectedItem.getQuantity() <= 0) return false;
        
        Crop crop = selectedItem.getCrop();
        Rarity rarity = Rarity.getRarityByName(crop.getRarity());
        
        plot.plant(crop.getName(), crop.getRarity(), rarity.getWater(), rarity.getTime());
        
        selectedItem.setQuantity(selectedItem.getQuantity() - 1);
        if (selectedItem.getQuantity() <= 0) {
            inventory.remove(selectedItem);
        }
        
        actionsRemaining--;
        return true;
    }
    
    public boolean waterCrop(int plotIndex) {
        if (actionsRemaining <= 0) return false;
        if (plotIndex < 0 || plotIndex >= NUM_PLOTS) return false;

        Plot plot = plots.get(plotIndex);
        if (plot.isEmpty()) return false;
        if (plot.getWaterRemaining() <= 0) return false;

        plot.water(getWaterCapacity());
        actionsRemaining--;
        return true;
    }
    
    public int[] harvestCrop(int plotIndex) {
        if (actionsRemaining <= 0) return null;
        if (plotIndex < 0 || plotIndex >= NUM_PLOTS) return null;

        Plot plot = plots.get(plotIndex);
        if (!plot.isReadyToHarvest()) return null;

        Rarity rarity = Rarity.getRarityByName(plot.getCropRarity());
        int basePrice = rarity.getSellValue();

        // Get quality with penalty and harvest bonus
        int penalty = plot.getQualityPenalty();
        int harvestBonus = getHarvestBonus();
        int[] result = randomizer.harvestWithQuality(basePrice, penalty, harvestBonus);
        int finalPrice = result[0];
        int quality = result[1];

        coins += finalPrice;

        // Calculate and add EXP
        int expGained = (int) Math.round((quality / 100.0) * basePrice);
        addExp(expGained);

        plot.harvest();
        actionsRemaining--;

        return new int[] { finalPrice, quality, basePrice, expGained };
    }
    
    public Crop buySeedBox(int boxType) {
        int cost;
        int cropIndex;
        
        switch (boxType) {
            case 1:
                cost = 50;
                cropIndex = randomizer.commonBox();
                break;
            case 2:
                cost = 100;
                cropIndex = randomizer.rareBox();
                break;
            case 3:
                cost = 200;
                cropIndex = randomizer.epicBox();
                break;
            default:
                return null;
        }
        
        coins -= cost;
        
        if (cropIndex < 1 || cropIndex > cropDatabase.size()) return null;
        Crop selectedCrop = cropDatabase.get(cropIndex - 1);
        
        if (selectedCrop.getStatus().equals("Not")) {
            selectedCrop.setStatus("Obtained");
            fileManager.writeCropData(cropDatabase);
        }
        
        boolean found = false;
        for (Inventory item : inventory) {
            if (item.getCrop().getName().equals(selectedCrop.getName())) {
                item.setQuantity(item.getQuantity() + 1);
                found = true;
                break;
            }
        }
        
        if (!found) {
            Crop newCrop = new Crop(0, selectedCrop.getName(), selectedCrop.getRarity(), "Obtained");
            inventory.add(new Inventory(newCrop, 1));
        }
        
        actionsRemaining--;
        return selectedCrop;
    }
    
    public void skipDay() {
        // Apply quality penalty to crops that still need water
        for (Plot plot : plots) {
            if (!plot.isEmpty() && plot.needsWater() && plot.getWaterRemaining() > 0) {
                plot.addQualityPenalty(10);
            }
        }

        // Progress crop growth
        for (Plot plot : plots) {
            if (!plot.isEmpty() && plot.getDaysRemaining() > 0) {
                plot.decreaseDay();
            }
        }

        // Add water requirement (crops get thirsty)
        for (Plot plot : plots) {
            if (!plot.isEmpty()) {
                int waterIncrease = 1 + randomizer.nextInt(3);
                for (int i = 0; i < waterIncrease; i++) {
                    plot.increaseWater();
                }
            }
        }

        daysRemaining--;
        actionsRemaining = DAILY_ACTIONS;
    }
    
    public void endGame() {
        if (coins > 0) {
            highscores.add(new Highscore(playerName, coins));
            fileManager.writeHighscores(highscores);
        }
    }
    
    public boolean isBankrupt() {
        return coins <= BANKRUPT_LIMIT;
    }
    
    public boolean isSeasonOver() {
        return daysRemaining <= 0;
    }
    
    public boolean noActionsRemaining() {
        return actionsRemaining <= 0;
    }
    
    public String getPlayerName() { return playerName; }
    public int getCoins() { return coins; }
    public int getDaysRemaining() { return daysRemaining; }
    public int getActionsRemaining() { return actionsRemaining; }
    public List<Plot> getPlots() { return plots; }
    public List<Inventory> getInventory() { return inventory; }
    public List<Crop> getCropDatabase() { return cropDatabase; }
    public List<Highscore> getHighscores() { return highscores; }
    
    public void reloadHighscores() {
        highscores = fileManager.readHighscores();
    }

    public void reloadCropDatabase() {
        cropDatabase = fileManager.readCropData();
    }

    // ==================== LEVEL SYSTEM ====================

    public int getPlayerLevel() {
        return playerLevel;
    }

    public int getCurrentExp() {
        return currentExp;
    }

    public int getExpForNextLevel() {
        // Gradual progression with 1.2x multiplier per level
        // Level 1->2: 100, Level 2->3: 120, Level 3->4: 144, etc.
        return (int) Math.round(BASE_EXP_REQUIREMENT * Math.pow(1.2, playerLevel - 1));
    }

    public void addExp(int amount) {
        currentExp += amount;
        while (currentExp >= getExpForNextLevel()) {
            currentExp -= getExpForNextLevel();
            playerLevel++;
        }
    }

    public double getExpProgress() {
        return (double) currentExp / getExpForNextLevel();
    }

    // ==================== PLOT UNLOCK SYSTEM ====================

    public boolean isPlotUnlocked(int plotIndex) {
        if (plotIndex == 0) return true; // Plot 1 always unlocked
        // Plot 2-10 unlock at level 5, 10, 15, 20, 25, 30, 35, 40, 45
        int requiredLevel = plotIndex * 5;
        return playerLevel >= requiredLevel;
    }

    public int getPlotRequiredLevel(int plotIndex) {
        if (plotIndex == 0) return 1;
        return plotIndex * 5;
    }

    // ==================== UPGRADE SYSTEM ====================

    public int getWaterUpgradeLevel() {
        return waterUpgradeLevel;
    }

    public int getHarvestUpgradeLevel() {
        return harvestUpgradeLevel;
    }

    public int getWaterCapacity() {
        // Base: 1, Tier 1: 2, Tier 2: 3
        if (waterUpgradeLevel >= 2) return 3;
        if (waterUpgradeLevel >= 1) return 2;
        return 1;
    }

    public int getHarvestBonus() {
        // 0%, 2%, 4%, 6%, 8%, 10%
        return harvestUpgradeLevel * 2;
    }

    public int getWaterUpgradeCost() {
        // Tier 1: 150, Tier 2: 400
        if (waterUpgradeLevel == 0) return 150;
        if (waterUpgradeLevel == 1) return 400;
        return -1; // Max level
    }

    public int getWaterUpgradeRequiredLevel() {
        // Tier 1: Level 5, Tier 2: Level 12
        if (waterUpgradeLevel == 0) return 5;
        if (waterUpgradeLevel == 1) return 12;
        return -1; // Max level
    }

    public int getHarvestUpgradeCost() {
        // 50, 100, 200, 350, 500
        int[] costs = {50, 100, 200, 350, 500};
        if (harvestUpgradeLevel < costs.length) return costs[harvestUpgradeLevel];
        return -1; // Max level
    }

    public int getHarvestUpgradeRequiredLevel() {
        // 3, 6, 9, 12, 15
        int[] levels = {3, 6, 9, 12, 15};
        if (harvestUpgradeLevel < levels.length) return levels[harvestUpgradeLevel];
        return -1; // Max level
    }

    public boolean canUpgradeWater() {
        int cost = getWaterUpgradeCost();
        int reqLevel = getWaterUpgradeRequiredLevel();
        if (cost == -1) return false; // Max level
        return coins >= cost && playerLevel >= reqLevel;
    }

    public boolean canUpgradeHarvest() {
        int cost = getHarvestUpgradeCost();
        int reqLevel = getHarvestUpgradeRequiredLevel();
        if (cost == -1) return false; // Max level
        return coins >= cost && playerLevel >= reqLevel;
    }

    public boolean purchaseWaterUpgrade() {
        if (!canUpgradeWater()) return false;
        coins -= getWaterUpgradeCost();
        waterUpgradeLevel++;
        return true;
    }

    public boolean purchaseHarvestUpgrade() {
        if (!canUpgradeHarvest()) return false;
        coins -= getHarvestUpgradeCost();
        harvestUpgradeLevel++;
        return true;
    }

    public boolean isWaterMaxLevel() {
        return waterUpgradeLevel >= 2;
    }

    public boolean isHarvestMaxLevel() {
        return harvestUpgradeLevel >= 5;
    }

    // ==================== CHEAT CODES (for presentation) ====================

    public void addCoins(int amount) {
        coins += amount;
    }

    public void addLevel() {
        playerLevel++;
        currentExp = 0;
    }
}
