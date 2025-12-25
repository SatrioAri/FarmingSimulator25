package com.farmingcmulator.model;

public class Plot {
    private int number;
    private String cropName;
    private String cropRarity;
    private String cropSeason;
    private int waterRemaining;
    private int daysRemaining;
    private boolean planted;
    private int qualityPenalty;
    private int seasonQualityBonus; // Positive if in-season, negative if off-season
    private boolean fertilized;
    private int fertilizerQualityBonus;
    private int pestDamage;  // Damage from pest invasion (0 if no pest)

    public Plot(int number) {
        this.number = number;
        this.planted = false;
        this.cropName = "";
        this.cropRarity = "";
        this.cropSeason = "";
        this.waterRemaining = 0;
        this.daysRemaining = 0;
        this.qualityPenalty = 0;
        this.seasonQualityBonus = 0;
        this.fertilized = false;
        this.fertilizerQualityBonus = 0;
        this.pestDamage = 0;
    }

    public void plant(String cropName, String cropRarity, String cropSeason, int water, int days, int seasonQualityBonus) {
        this.cropName = cropName;
        this.cropRarity = cropRarity;
        this.cropSeason = cropSeason;
        this.waterRemaining = water;
        this.daysRemaining = days;
        this.planted = true;
        this.qualityPenalty = 0;
        this.seasonQualityBonus = seasonQualityBonus;
        this.fertilized = false;
        this.fertilizerQualityBonus = 0;
        this.pestDamage = 0;
    }

    // Backward compatibility
    public void plant(String cropName, String cropRarity, int water, int days) {
        plant(cropName, cropRarity, "", water, days, 0);
    }
    
    public void water() {
        water(1);
    }

    public void water(int amount) {
        waterRemaining = Math.max(0, waterRemaining - amount);
    }

    public void harvest() {
        this.cropName = "";
        this.cropRarity = "";
        this.cropSeason = "";
        this.waterRemaining = 0;
        this.daysRemaining = 0;
        this.planted = false;
        this.qualityPenalty = 0;
        this.seasonQualityBonus = 0;
        this.fertilized = false;
        this.fertilizerQualityBonus = 0;
        this.pestDamage = 0;
    }

    public void applyFertilizer(int qualityBoost, int daysReduction) {
        this.fertilized = true;
        this.fertilizerQualityBonus = qualityBoost;
        this.daysRemaining = Math.max(0, this.daysRemaining - daysReduction);
    }

    public boolean isFertilized() {
        return fertilized;
    }

    public int getFertilizerQualityBonus() {
        return fertilizerQualityBonus;
    }

    public void addQualityPenalty(int amount) {
        this.qualityPenalty += amount;
    }

    public int getQualityPenalty() {
        return qualityPenalty;
    }
    
    public void decreaseDay() {
        if (daysRemaining > 0) {
            daysRemaining--;
        }
    }
    
    public void increaseWater() {
        waterRemaining++;
    }
    
    public boolean isEmpty() {
        return !planted;
    }
    
    public boolean isReadyToHarvest() {
        return planted && waterRemaining <= 0 && daysRemaining <= 0;
    }
    
    public boolean needsWater() {
        return planted && waterRemaining > 0;
    }
    
    public int getNumber() { return number; }
    public String getCropName() { return cropName; }
    public String getCropRarity() { return cropRarity; }
    public String getCropSeason() { return cropSeason; }
    public int getWaterRemaining() { return waterRemaining; }
    public int getDaysRemaining() { return daysRemaining; }
    public boolean isPlanted() { return planted; }
    public int getSeasonQualityBonus() { return seasonQualityBonus; }

    public String getRarityColor() {
        return Rarity.getColor(cropRarity);
    }

    // Pest invasion methods
    public void applyPestDamage(int damage) {
        this.pestDamage = damage;
        this.qualityPenalty += damage;  // Add to quality penalty
    }

    public int getPestDamage() {
        return pestDamage;
    }

    public boolean wasPestInfested() {
        return pestDamage > 0;
    }

    public void clearPestStatus() {
        this.pestDamage = 0;
    }
}
