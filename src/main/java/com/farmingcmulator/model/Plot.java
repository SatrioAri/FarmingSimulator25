package com.farmingcmulator.model;

public class Plot {
    private int number;
    private String cropName;
    private String cropRarity;
    private int waterRemaining;
    private int daysRemaining;
    private boolean planted;
    private int qualityPenalty;

    public Plot(int number) {
        this.number = number;
        this.planted = false;
        this.cropName = "";
        this.cropRarity = "";
        this.waterRemaining = 0;
        this.daysRemaining = 0;
        this.qualityPenalty = 0;
    }
    
    public void plant(String cropName, String cropRarity, int water, int days) {
        this.cropName = cropName;
        this.cropRarity = cropRarity;
        this.waterRemaining = water;
        this.daysRemaining = days;
        this.planted = true;
        this.qualityPenalty = 0;
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
        this.waterRemaining = 0;
        this.daysRemaining = 0;
        this.planted = false;
        this.qualityPenalty = 0;
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
    public int getWaterRemaining() { return waterRemaining; }
    public int getDaysRemaining() { return daysRemaining; }
    public boolean isPlanted() { return planted; }
    
    public String getRarityColor() {
        return Rarity.getColor(cropRarity);
    }
}
