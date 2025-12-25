package com.farmingcmulator.model;

public class StorageItem {
    private String cropName;
    private String cropRarity;
    private int quantity;
    private int totalQuality;  // Sum of all qualities (for averaging)
    private int basePrice;     // Rarity base price
    private int daysInStorage; // Days since last harvest added (resets when adding new)

    public static final int DECAY_INTERVAL = 5;  // Days before quality decays
    public static final int DECAY_PERCENT = 10;  // Quality loss per decay

    public StorageItem(String cropName, String cropRarity, int quality, int basePrice) {
        this.cropName = cropName;
        this.cropRarity = cropRarity;
        this.quantity = 1;
        this.totalQuality = quality;
        this.basePrice = basePrice;
        this.daysInStorage = 0;
    }

    // Getters
    public String getCropName() {
        return cropName;
    }

    public String getCropRarity() {
        return cropRarity;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getTotalQuality() {
        return totalQuality;
    }

    public int getBasePrice() {
        return basePrice;
    }

    // Calculate average quality
    public int getAverageQuality() {
        if (quantity <= 0) return 0;
        return totalQuality / quantity;
    }

    // Add another harvest to this stack (resets decay timer)
    public void addToStack(int quality) {
        this.quantity++;
        this.totalQuality += quality;
        this.daysInStorage = 0;  // Reset decay timer when adding new crops
    }

    // Days tracking
    public int getDaysInStorage() {
        return daysInStorage;
    }

    public void incrementDay() {
        daysInStorage++;
    }

    // Get days until next quality decay
    public int getDaysUntilDecay() {
        return DECAY_INTERVAL - (daysInStorage % DECAY_INTERVAL);
    }

    // Check if decay should happen this day
    public boolean shouldDecay() {
        return daysInStorage > 0 && daysInStorage % DECAY_INTERVAL == 0;
    }

    // Apply quality decay (reduces total quality by percentage)
    public void applyDecay() {
        int decayAmount = (int) Math.round(totalQuality * DECAY_PERCENT / 100.0);
        totalQuality = Math.max(quantity, totalQuality - decayAmount);  // Minimum 1% per item
    }

    // Check if item is about to decay (within 2 days)
    public boolean isAboutToDecay() {
        return getDaysUntilDecay() <= 2;
    }

    // Calculate current value based on market multiplier
    public int getCurrentValue(double marketMultiplier) {
        int avgQuality = getAverageQuality();
        int adjustedBase = (int) Math.round(basePrice * marketMultiplier);
        int qualityBonus = (int) Math.round(adjustedBase * avgQuality / 100.0);
        return (adjustedBase + qualityBonus) * quantity;
    }

    // Calculate value per unit
    public int getValuePerUnit(double marketMultiplier) {
        int avgQuality = getAverageQuality();
        int adjustedBase = (int) Math.round(basePrice * marketMultiplier);
        int qualityBonus = (int) Math.round(adjustedBase * avgQuality / 100.0);
        return adjustedBase + qualityBonus;
    }

    // For display in table
    public String getDisplayName() {
        return cropName;
    }

    public String getDisplayQuality() {
        return getAverageQuality() + "%";
    }

    @Override
    public String toString() {
        return cropName + " (" + cropRarity + ") x" + quantity + " [" + getAverageQuality() + "%]";
    }
}
