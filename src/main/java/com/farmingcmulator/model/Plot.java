package com.farmingcmulator.model;

public class Plot {
    private int number;
    private String cropName;
    private String cropRarity;
    private int waterRemaining;
    private int daysRemaining;
    private boolean planted;
    
    public Plot(int number) {
        this.number = number;
        this.planted = false;
        this.cropName = "";
        this.cropRarity = "";
        this.waterRemaining = 0;
        this.daysRemaining = 0;
    }
    
    public void plant(String cropName, String cropRarity, int water, int days) {
        this.cropName = cropName;
        this.cropRarity = cropRarity;
        this.waterRemaining = water;
        this.daysRemaining = days;
        this.planted = true;
    }
    
    public void water() {
        if (waterRemaining > 0) {
            waterRemaining--;
        }
    }
    
    public void harvest() {
        this.cropName = "";
        this.cropRarity = "";
        this.waterRemaining = 0;
        this.daysRemaining = 0;
        this.planted = false;
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
