package com.farmingcmulator.model;

public class MarketPrice {
    private String cropName;
    private String cropRarity;
    private double priceMultiplier;  // 0.9 to 1.1 (daily fluctuation)
    private double seasonalBonus;    // +0.05 to +0.10 for in-season crops

    public MarketPrice(String cropName, String cropRarity, double priceMultiplier) {
        this.cropName = cropName;
        this.cropRarity = cropRarity;
        this.priceMultiplier = priceMultiplier;
        this.seasonalBonus = 0.0;
    }

    // Getters
    public String getCropName() {
        return cropName;
    }

    public String getCropRarity() {
        return cropRarity;
    }

    public double getPriceMultiplier() {
        return priceMultiplier;
    }

    public double getSeasonalBonus() {
        return seasonalBonus;
    }

    // Setters
    public void setPriceMultiplier(double multiplier) {
        this.priceMultiplier = multiplier;
    }

    public void setSeasonalBonus(double bonus) {
        this.seasonalBonus = bonus;
    }

    // Combined multiplier (daily + seasonal)
    public double getTotalMultiplier() {
        return priceMultiplier + seasonalBonus;
    }

    // Get effective price based on rarity
    public int getEffectiveBasePrice() {
        Rarity rarity = Rarity.getRarityByName(cropRarity);
        return (int) Math.round(rarity.getSellValue() * getTotalMultiplier());
    }

    // Get percentage display (e.g., "+10%" or "-5%")
    public String getMultiplierDisplay() {
        double percentChange = (getTotalMultiplier() - 1.0) * 100;
        if (percentChange >= 0) {
            return String.format("+%.0f%%", percentChange);
        } else {
            return String.format("%.0f%%", percentChange);
        }
    }

    // Check if this is a "good" price (above 1.0)
    public boolean isGoodPrice() {
        return getTotalMultiplier() > 1.0;
    }

    // Check if this is a "bad" price (below 1.0)
    public boolean isBadPrice() {
        return getTotalMultiplier() < 1.0;
    }
}
