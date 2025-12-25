package com.farmingcmulator.util;

import java.util.Random;

public class Randomizer {
    
    private Random random;
    
    public Randomizer() {
        random = new Random();
    }
    
    public int nextInt(int bound) {
        return random.nextInt(bound);
    }
    
    public int commonBox() {
        int roll = random.nextInt(100);
        if (roll < 60) return random.nextInt(17) + 1;        // Common 1-17
        else if (roll < 85) return random.nextInt(12) + 18;  // Uncommon 18-29
        else if (roll < 95) return random.nextInt(11) + 30;  // Rare 30-40
        else if (roll < 99) return random.nextInt(6) + 41;   // Epic 41-46
        else return random.nextInt(2) + 47;                   // Legendary 47-48
    }
    
    public int rareBox() {
        int roll = random.nextInt(100);
        if (roll < 30) return random.nextInt(17) + 1;
        else if (roll < 55) return random.nextInt(12) + 18;
        else if (roll < 80) return random.nextInt(11) + 30;
        else if (roll < 95) return random.nextInt(6) + 41;
        else return random.nextInt(2) + 47;
    }
    
    public int epicBox() {
        int roll = random.nextInt(100);
        if (roll < 15) return random.nextInt(17) + 1;
        else if (roll < 35) return random.nextInt(12) + 18;
        else if (roll < 55) return random.nextInt(11) + 30;
        else if (roll < 85) return random.nextInt(6) + 41;
        else return random.nextInt(2) + 47;
    }
    
    public int[] harvestWithQuality(int basePrice) {
        return harvestWithQuality(basePrice, 0, 0);
    }

    public int[] harvestWithQuality(int basePrice, int penalty, int harvestBonus) {
        // Base quality 0-100, minus penalty for not watering, plus harvest upgrade bonus
        int quality = Math.max(0, Math.min(100, random.nextInt(101) - penalty + harvestBonus));
        int bonus = (int) Math.round(basePrice * quality / 100.0);
        int finalPrice = basePrice + bonus;
        return new int[] { finalPrice, quality };
    }
    
    public static String getQualityTier(int quality) {
        if (quality >= 90) return "PERFECT";
        if (quality >= 75) return "EXCELLENT";
        if (quality >= 50) return "GOOD";
        if (quality >= 25) return "AVERAGE";
        return "POOR";
    }
    
    public static String getQualityColor(int quality) {
        if (quality >= 90) return "#f39c12";
        if (quality >= 75) return "#27ae60";
        if (quality >= 50) return "#3498db";
        if (quality >= 25) return "#7f8c8d";
        return "#e74c3c";
    }
    
    public static String getQualityMessage(int quality) {
        if (quality >= 90) return "A masterpiece of farming!";
        if (quality >= 75) return "Excellent work, farmer!";
        if (quality >= 50) return "A decent harvest!";
        if (quality >= 25) return "Could be better...";
        return "Not your best work...";
    }

    // ==================== MARKET PRICE SYSTEM ====================

    /**
     * Generate a random price multiplier for market fluctuation.
     * Low volatility: 0.9x to 1.1x (+-10% swing)
     * @return multiplier between 0.9 and 1.1
     */
    public double generatePriceMultiplier() {
        return 0.9 + (random.nextDouble() * 0.2);
    }

    /**
     * Generate seasonal bonus for crops matching current season.
     * Adds +5% to +10% bonus for in-season crops.
     * @return bonus between 0.05 and 0.10
     */
    public double generateSeasonalBonus() {
        return 0.05 + (random.nextDouble() * 0.05);
    }
}
