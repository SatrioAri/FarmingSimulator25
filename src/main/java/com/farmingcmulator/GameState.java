package com.farmingcmulator;

import com.farmingcmulator.model.Crop;
import com.farmingcmulator.model.Highscore;
import com.farmingcmulator.model.Inventory;
import com.farmingcmulator.model.Plot;
import com.farmingcmulator.model.Rarity;
import com.farmingcmulator.util.FileManager;
import com.farmingcmulator.util.Randomizer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.farmingcmulator.model.MarketPrice;
import com.farmingcmulator.model.StorageItem;

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

    // Items
    private int fertilizerCount;
    
    private List<Plot> plots;
    private List<Inventory> inventory;
    private List<Crop> cropDatabase;
    private List<Highscore> highscores;

    private FileManager fileManager;
    private Randomizer randomizer;

    // Market price system
    private Map<String, MarketPrice> marketPrices;
    private int lastPriceUpdateDay;
    public static final int PRICE_UPDATE_INTERVAL = 3;

    // Storage system
    private List<StorageItem> storage;
    private int storageUpgradeLevel;
    public static final int BASE_STORAGE_CAPACITY = 10;
    public static final int STORAGE_UPGRADE_SLOTS = 5;
    public static final int MAX_STORAGE_UPGRADES = 2;

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

        // Initialize items
        this.fertilizerCount = 0;

        plots.clear();
        for (int i = 1; i <= NUM_PLOTS; i++) {
            plots.add(new Plot(i));
        }

        inventory.clear();
        cropDatabase = fileManager.readCropData();
        highscores = fileManager.readHighscores();

        // Initialize market prices
        marketPrices = new HashMap<>();
        lastPriceUpdateDay = SEASON_DAYS;
        initializeMarketPrices();

        // Initialize storage
        storage = new ArrayList<>();
        storageUpgradeLevel = 0;
    }
    
    public boolean plantCrop(int plotIndex, Inventory selectedItem) {
        if (actionsRemaining <= 0) return false;
        if (plotIndex < 0 || plotIndex >= NUM_PLOTS) return false;

        Plot plot = plots.get(plotIndex);
        if (!plot.isEmpty()) return false;
        if (selectedItem == null || selectedItem.getQuantity() <= 0) return false;

        Crop crop = selectedItem.getCrop();
        Rarity rarity = Rarity.getRarityByName(crop.getRarity());

        int baseDays = rarity.getTime();
        int baseWater = rarity.getWater();
        int seasonQualityBonus = 0;

        // Apply season effects
        if (isCropInSeason(crop.getSeason())) {
            // In-season: reduce days by 1, quality bonus +15%
            baseDays = Math.max(1, baseDays - 1);
            seasonQualityBonus = 15;
        } else {
            // Off-season: increase days by 1, quality penalty -10%
            baseDays = baseDays + 1;
            seasonQualityBonus = -10;
        }

        plot.plant(crop.getName(), crop.getRarity(), crop.getSeason(), baseWater, baseDays, seasonQualityBonus);

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
        if (isStorageFull()) return null;  // Check storage capacity

        Plot plot = plots.get(plotIndex);
        if (!plot.isReadyToHarvest()) return null;

        String cropName = plot.getCropName();
        String cropRarity = plot.getCropRarity();
        Rarity rarity = Rarity.getRarityByName(cropRarity);
        int basePrice = rarity.getSellValue();

        // Get quality with penalty, harvest bonus, season bonus, and fertilizer bonus
        int penalty = plot.getQualityPenalty();
        int harvestBonus = getHarvestBonus();
        int seasonBonus = plot.getSeasonQualityBonus();
        int fertilizerBonus = plot.getFertilizerQualityBonus();
        int[] result = randomizer.harvestWithQuality(basePrice, penalty, harvestBonus + seasonBonus + fertilizerBonus);
        int quality = result[1];

        // Store the harvested crop instead of selling immediately
        storeHarvest(cropName, cropRarity, quality, basePrice);

        // Calculate and add EXP
        int expGained = (int) Math.round((quality / 100.0) * basePrice);
        addExp(expGained);

        plot.harvest();
        actionsRemaining--;

        // Return: quality, basePrice, expGained (no immediate sale)
        return new int[] { quality, basePrice, expGained };
    }
    
    public Crop buySeedBox(int boxType) {
        int cost;
        int cropIndex;

        switch (boxType) {
            case 1:
                cost = 50;
                cropIndex = getSeasonBiasedCrop(() -> randomizer.commonBox());
                break;
            case 2:
                cost = 100;
                cropIndex = getSeasonBiasedCrop(() -> randomizer.rareBox());
                break;
            case 3:
                cost = 200;
                cropIndex = getSeasonBiasedCrop(() -> randomizer.epicBox());
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
            Crop newCrop = new Crop(0, selectedCrop.getName(), selectedCrop.getRarity(), "Obtained", selectedCrop.getSeason());
            inventory.add(new Inventory(newCrop, 1));
        }

        actionsRemaining--;
        return selectedCrop;
    }

    // Helper method to bias crop selection towards current season
    private int getSeasonBiasedCrop(java.util.function.Supplier<Integer> boxMethod) {
        String currentSeason = getCurrentSeason();
        int maxAttempts = 3;
        int cropIndex = boxMethod.get();

        // 40% chance to try re-rolling for a season-matching crop
        for (int i = 0; i < maxAttempts - 1; i++) {
            if (cropIndex >= 1 && cropIndex <= cropDatabase.size()) {
                Crop crop = cropDatabase.get(cropIndex - 1);
                if (crop.getSeason().equals(currentSeason)) {
                    break; // Already matches season, keep it
                }
                // 40% chance to re-roll
                if (randomizer.nextInt(100) < 40) {
                    cropIndex = boxMethod.get();
                } else {
                    break;
                }
            }
        }
        return cropIndex;
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

        // Check if market prices need updating
        checkAndUpdateMarketPrices();

        // Process storage decay
        processStorageDecay();

        // Process random pest invasion
        processPestInvasion();
    }

    /**
     * Process storage items - increment days and apply decay if needed.
     */
    private void processStorageDecay() {
        for (StorageItem item : storage) {
            item.incrementDay();
            if (item.shouldDecay()) {
                item.applyDecay();
            }
        }
    }

    /**
     * Get items that are about to decay (within 2 days).
     */
    public java.util.List<StorageItem> getItemsAboutToDecay() {
        java.util.List<StorageItem> decaying = new java.util.ArrayList<>();
        for (StorageItem item : storage) {
            if (item.isAboutToDecay()) {
                decaying.add(item);
            }
        }
        return decaying;
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

    // ==================== SEASON SYSTEM ====================

    public String getCurrentSeason() {
        // 90 days divided into 4 seasons (approximately 22-23 days each)
        // Spring: Days 90-68, Summer: Days 67-45, Fall: Days 44-23, Winter: Days 22-1
        if (daysRemaining >= 68) {
            return "Spring";
        } else if (daysRemaining >= 45) {
            return "Summer";
        } else if (daysRemaining >= 23) {
            return "Fall";
        } else {
            return "Winter";
        }
    }

    public boolean isCropInSeason(String cropSeason) {
        return getCurrentSeason().equals(cropSeason);
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

    // ==================== FERTILIZER SYSTEM ====================

    public static final int FERTILIZER_COST = 50;

    public int getFertilizerCount() {
        return fertilizerCount;
    }

    public boolean buyFertilizer() {
        if (coins < FERTILIZER_COST) return false;
        coins -= FERTILIZER_COST;
        fertilizerCount++;
        return true;
    }

    public boolean useFertilizer(int plotIndex) {
        if (actionsRemaining <= 0) return false;
        if (fertilizerCount <= 0) return false;
        if (plotIndex < 0 || plotIndex >= NUM_PLOTS) return false;

        Plot plot = plots.get(plotIndex);
        if (plot.isEmpty()) return false;
        if (plot.isFertilized()) return false;

        // Random quality boost between 10-25%
        int qualityBoost = 10 + randomizer.nextInt(16);
        // Random days reduction between 1-2
        int daysReduction = 1 + randomizer.nextInt(2);

        plot.applyFertilizer(qualityBoost, daysReduction);
        fertilizerCount--;
        actionsRemaining--;
        return true;
    }

    public int[] getFertilizerEffectPreview() {
        // Returns [minQuality, maxQuality, minDays, maxDays]
        return new int[] { 10, 25, 1, 2 };
    }

    // ==================== PEST INVASION SYSTEM ====================

    public static final int PEST_CHANCE_NORMAL = 15;      // 15% chance for normal crops
    public static final int PEST_CHANCE_FERTILIZED = 5;   // 5% chance for fertilized crops
    public static final int PEST_MIN_DAMAGE = 1;          // Minimum quality damage
    public static final int PEST_MAX_DAMAGE = 10;         // Maximum quality damage

    private java.util.List<int[]> lastPestInvasions = new java.util.ArrayList<>();  // [plotNumber, damage]

    /**
     * Process pest invasions - randomly infest one planted crop.
     */
    private void processPestInvasion() {
        lastPestInvasions.clear();

        // Clear previous pest status from all plots
        for (Plot plot : plots) {
            plot.clearPestStatus();
        }

        // Get all planted crops that are not yet ready to harvest
        java.util.List<Integer> eligiblePlots = new java.util.ArrayList<>();
        for (int i = 0; i < plots.size(); i++) {
            Plot plot = plots.get(i);
            if (!plot.isEmpty() && !plot.isReadyToHarvest()) {
                eligiblePlots.add(i);
            }
        }

        if (eligiblePlots.isEmpty()) return;

        // Randomly select one plot to potentially infest
        int selectedIndex = eligiblePlots.get(randomizer.nextInt(eligiblePlots.size()));
        Plot selectedPlot = plots.get(selectedIndex);

        // Determine chance based on fertilization
        int pestChance = selectedPlot.isFertilized() ? PEST_CHANCE_FERTILIZED : PEST_CHANCE_NORMAL;

        // Roll for pest invasion
        if (randomizer.nextInt(100) < pestChance) {
            // Apply random damage between 1-10%
            int damage = PEST_MIN_DAMAGE + randomizer.nextInt(PEST_MAX_DAMAGE - PEST_MIN_DAMAGE + 1);
            selectedPlot.applyPestDamage(damage);
            lastPestInvasions.add(new int[] { selectedPlot.getNumber(), damage });
        }
    }

    /**
     * Get list of pest invasions from last skip day.
     * Returns list of [plotNumber, damage] arrays.
     */
    public java.util.List<int[]> getLastPestInvasions() {
        return lastPestInvasions;
    }

    /**
     * Check if there were any pest invasions.
     */
    public boolean hadPestInvasion() {
        return !lastPestInvasions.isEmpty();
    }

    /**
     * Get the infested plot info for display.
     */
    public String getPestInvasionInfo() {
        if (lastPestInvasions.isEmpty()) return "";

        StringBuilder sb = new StringBuilder();
        for (int[] invasion : lastPestInvasions) {
            int plotNum = invasion[0];
            int damage = invasion[1];
            Plot plot = plots.get(plotNum - 1);  // Plot numbers are 1-based
            sb.append("Plot ").append(plotNum).append(" (").append(plot.getCropName())
              .append("): -").append(damage).append("% quality");
        }
        return sb.toString();
    }

    /**
     * Clear pest invasion notifications after displaying.
     */
    public void clearPestInvasions() {
        lastPestInvasions.clear();
    }

    // ==================== CHEAT CODES (for presentation) ====================

    public void addCoins(int amount) {
        coins += amount;
    }

    public void addLevel() {
        playerLevel++;
        currentExp = 0;
    }

    // ==================== MARKET PRICE SYSTEM ====================

    /**
     * Initialize market prices for all crops in the database.
     * Called at game start and whenever prices need regeneration.
     */
    private void initializeMarketPrices() {
        String currentSeason = getCurrentSeason();

        for (Crop crop : cropDatabase) {
            double multiplier = randomizer.generatePriceMultiplier();
            MarketPrice marketPrice = new MarketPrice(
                crop.getName(),
                crop.getRarity(),
                multiplier
            );

            // Add seasonal bonus if crop is in season
            if (crop.getSeason().equals(currentSeason)) {
                marketPrice.setSeasonalBonus(randomizer.generateSeasonalBonus());
            }

            marketPrices.put(crop.getName(), marketPrice);
        }
    }

    /**
     * Regenerate all market prices. Called every 3 days.
     */
    public void regenerateMarketPrices() {
        initializeMarketPrices();
        lastPriceUpdateDay = daysRemaining;
    }

    /**
     * Check if market prices should be updated and update if needed.
     * Called during skipDay().
     */
    private void checkAndUpdateMarketPrices() {
        // Calculate days since last update
        int daysSinceUpdate = lastPriceUpdateDay - daysRemaining;

        // Regenerate if 3 or more days have passed
        if (daysSinceUpdate >= PRICE_UPDATE_INTERVAL) {
            regenerateMarketPrices();
        }
    }

    /**
     * Get market price multiplier for a specific crop.
     * @param cropName The name of the crop
     * @return The total multiplier (daily + seasonal)
     */
    public double getMarketMultiplier(String cropName) {
        MarketPrice price = marketPrices.get(cropName);
        if (price != null) {
            return price.getTotalMultiplier();
        }
        return 1.0; // Default to no change if not found
    }

    /**
     * Get the MarketPrice object for a specific crop.
     * @param cropName The name of the crop
     * @return MarketPrice object or null if not found
     */
    public MarketPrice getMarketPrice(String cropName) {
        return marketPrices.get(cropName);
    }

    /**
     * Get all market prices.
     * @return Map of crop names to MarketPrice objects
     */
    public Map<String, MarketPrice> getAllMarketPrices() {
        return marketPrices;
    }

    /**
     * Get the top N crops with the best price multipliers.
     * @param count Number of crops to return
     * @return List of MarketPrice objects sorted by multiplier descending
     */
    public List<MarketPrice> getTopPriceCrops(int count) {
        return marketPrices.values().stream()
            .sorted(Comparator.comparingDouble(MarketPrice::getTotalMultiplier).reversed())
            .limit(count)
            .collect(Collectors.toList());
    }

    /**
     * Get the bottom N crops with the worst price multipliers.
     * @param count Number of crops to return
     * @return List of MarketPrice objects sorted by multiplier ascending
     */
    public List<MarketPrice> getBottomPriceCrops(int count) {
        return marketPrices.values().stream()
            .sorted(Comparator.comparingDouble(MarketPrice::getTotalMultiplier))
            .limit(count)
            .collect(Collectors.toList());
    }

    /**
     * Get days until next price update.
     * @return Number of days until prices change
     */
    public int getDaysUntilPriceUpdate() {
        int daysSinceUpdate = lastPriceUpdateDay - daysRemaining;
        int daysUntil = PRICE_UPDATE_INTERVAL - daysSinceUpdate;
        return Math.max(1, daysUntil); // At minimum 1 day
    }

    // ==================== STORAGE SYSTEM ====================

    /**
     * Get the storage list.
     */
    public List<StorageItem> getStorage() {
        return storage;
    }

    /**
     * Get current storage capacity based on upgrade level.
     */
    public int getStorageCapacity() {
        return BASE_STORAGE_CAPACITY + (storageUpgradeLevel * STORAGE_UPGRADE_SLOTS);
    }

    /**
     * Get number of unique crop types in storage (not total quantity).
     */
    public int getStorageSlotCount() {
        return storage.size();
    }

    /**
     * Check if storage is full (at capacity for unique crop types).
     */
    public boolean isStorageFull() {
        return storage.size() >= getStorageCapacity();
    }

    /**
     * Store a harvested crop. Stacks with existing crops of the same name.
     */
    public void storeHarvest(String cropName, String cropRarity, int quality, int basePrice) {
        // Check if this crop already exists in storage
        for (StorageItem item : storage) {
            if (item.getCropName().equals(cropName)) {
                // Add to existing stack
                item.addToStack(quality);
                return;
            }
        }

        // New crop type - add new entry if there's space
        if (!isStorageFull()) {
            storage.add(new StorageItem(cropName, cropRarity, quality, basePrice));
        }
    }

    /**
     * Sell a specific item from storage.
     * @param index Index of the item to sell
     * @return Array with [totalCoins, quantity, averageQuality] or null if invalid
     */
    public int[] sellFromStorage(int index) {
        if (index < 0 || index >= storage.size()) return null;

        StorageItem item = storage.get(index);
        double marketMultiplier = getMarketMultiplier(item.getCropName());
        int totalValue = item.getCurrentValue(marketMultiplier);
        int quantity = item.getQuantity();
        int avgQuality = item.getAverageQuality();

        coins += totalValue;
        storage.remove(index);

        return new int[] { totalValue, quantity, avgQuality };
    }

    /**
     * Sell all items from storage.
     * @return Total coins earned
     */
    public int sellAllFromStorage() {
        int totalEarned = 0;

        for (StorageItem item : storage) {
            double marketMultiplier = getMarketMultiplier(item.getCropName());
            totalEarned += item.getCurrentValue(marketMultiplier);
        }

        coins += totalEarned;
        storage.clear();

        return totalEarned;
    }

    /**
     * Calculate total value of all stored items at current market prices.
     */
    public int getStorageTotalValue() {
        int total = 0;
        for (StorageItem item : storage) {
            double marketMultiplier = getMarketMultiplier(item.getCropName());
            total += item.getCurrentValue(marketMultiplier);
        }
        return total;
    }

    // ==================== STORAGE UPGRADE SYSTEM ====================

    public int getStorageUpgradeLevel() {
        return storageUpgradeLevel;
    }

    public boolean isStorageMaxLevel() {
        return storageUpgradeLevel >= MAX_STORAGE_UPGRADES;
    }

    public int getStorageUpgradeCost() {
        // Tier 1: 500 coins, Tier 2: 800 coins
        if (storageUpgradeLevel == 0) return 500;
        if (storageUpgradeLevel == 1) return 800;
        return -1; // Max level
    }

    public int getStorageUpgradeRequiredLevel() {
        // Tier 1: Level 10, Tier 2: Level 15
        if (storageUpgradeLevel == 0) return 10;
        if (storageUpgradeLevel == 1) return 15;
        return -1; // Max level
    }

    public boolean canUpgradeStorage() {
        int cost = getStorageUpgradeCost();
        int reqLevel = getStorageUpgradeRequiredLevel();
        if (cost == -1) return false; // Max level
        return coins >= cost && playerLevel >= reqLevel;
    }

    public boolean purchaseStorageUpgrade() {
        if (!canUpgradeStorage()) return false;
        coins -= getStorageUpgradeCost();
        storageUpgradeLevel++;
        return true;
    }
}
