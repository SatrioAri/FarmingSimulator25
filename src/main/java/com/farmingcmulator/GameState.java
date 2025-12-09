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
    public static final int NUM_PLOTS = 5;
    public static final int BANKRUPT_LIMIT = -500;
    
    private String playerName;
    private int coins;
    private int daysRemaining;
    private int actionsRemaining;
    
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
        
        plot.water();
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
        
        int[] result = randomizer.harvestWithQuality(basePrice);
        int finalPrice = result[0];
        int quality = result[1];
        
        coins += finalPrice;
        plot.harvest();
        actionsRemaining--;
        
        return new int[] { finalPrice, quality, basePrice };
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
        for (Plot plot : plots) {
            if (!plot.isEmpty() && plot.getDaysRemaining() > 0) {
                plot.decreaseDay();
            }
        }
        
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
}
